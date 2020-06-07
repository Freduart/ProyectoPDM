package sv.ues.fia.eisi.proyectopdm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.ListaArchivosAdapter;

import static java.security.AccessController.getContext;

public class NuevaSolicitudImpresionActivity extends AppCompatActivity {

    static final int PICK_DOCUMENT_REQUEST = 1;
    //Botones
    Button btnAñadirDocumento,btnEnviar,btnCancelar;
    EditText text_detalleImpresiones;
    TextInputLayout text_impresiones,text_anexos;
    //Variables
    private int contFiles=0;
    private Uri documentUri;
    //Uri del documento del recyclerDocumentos
    Uri uri;
    String nombreDocumento;
    int[] numImpresiones={}, hojasAnexas={};
    String detallesImpresion, acumPath="";
    String[] uploadFilePath={};
    ArrayList<String> listaDocumentos;
    RecyclerView recyclerDocumentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_solicitud_impresion);
        btnAñadirDocumento=(Button)findViewById(R.id.btnAñadirDocumento);
        text_detalleImpresiones=(EditText)findViewById(R.id.text_detalleImpresion);
        text_impresiones=(TextInputLayout)findViewById(R.id.text_impresiones);
        text_anexos=(TextInputLayout)findViewById(R.id.text_anexos);
        //RecyclerView
        recyclerDocumentos=(RecyclerView)findViewById(R.id.recycler_archivos);
        recyclerDocumentos.setLayoutManager(new LinearLayoutManager(this));
        //Boton AÑADIR
        btnAñadirDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
                intent.setDataAndType(uri, "application/pdf");
                startActivityForResult(Intent.createChooser(intent, "Open Document"),PICK_DOCUMENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DOCUMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                documentUri = data.getData();
                String path = getPathMethod(this, documentUri);
                if (path == null) {
                    Toast.makeText(this, "Archivo No Encontrado...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                    //Actualizamos la lista de documentos con el Adapter.
                    listaDocumentos.add(path);
                    ListaArchivosAdapter listaArchivosAdapter=new ListaArchivosAdapter(listaDocumentos);
                    //Ponemos a la escucha cada item agregado
                    listaArchivosAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Obtenemos la ruta del documento seleccionado del recyclerDocumentos
                            uri=Uri.fromFile(new File(listaDocumentos.get(recyclerDocumentos.getChildAdapterPosition(v))));
                            nombreDocumento=getFileName(listaDocumentos.get(recyclerDocumentos.getChildAdapterPosition(v)));
                            createCustomDialog().show();
                        }
                    });
                    recyclerDocumentos.setAdapter(listaArchivosAdapter);
                    uploadFilePath[contFiles] = path;
                    contFiles+=1;
                }
            }
        }
    }

    public AlertDialog createCustomDialog(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.dialog_opciones_documento, null);
        //builder.setView(inflater.inflate(R.layout.dialog_signin, null))
        Button btnPrevisualizar = (Button)v.findViewById(R.id.btnPrevisualizar);
        Button btnQuitar = (Button)v.findViewById(R.id.btnQuitar);
        TextView textView=(TextView)v.findViewById(R.id.textTitulo);
        textView.setText(nombreDocumento);
        builder.setView(v);
        alertDialog = builder.create();
        // Add action buttons
        btnPrevisualizar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Aceptar
                        Intent intent = new Intent( Intent.ACTION_VIEW );
                        intent.setDataAndType(uri, "application/pdf");
                        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                }
        );
        btnQuitar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                }
        );
        return alertDialog;
    }

    public static String getPathMethod(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // DocumentProvider
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        //Almacenamiento interno
                        return "/storage/emulated/0/"+split[1];
                        //return Environment.getExternalStorageDirectory()+"/"+split[1];
                    }else{
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    String id = DocumentsContract.getDocumentId(uri),path="",result="";
                    Cursor cursor = null;
                    try {
                        cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String fileName = cursor.getString(0);
                            path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                            if (!TextUtils.isEmpty(path)) {
                                return path;
                            }
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                    }
                }
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            if (isGooglePhotosUri(uri)) {
                String contentPath = getContentFromSegments(uri.getPathSegments());
                if (contentPath != "") {
                    return getPathMethod(context, Uri.parse(contentPath));
                } else {
                    return null;
                }
            }

            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            }
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }
    private static String getContentFromSegments(List<String> segments) {
        String contentPath = "";
        for (String item : segments) {
            if (item.startsWith("content://")) {
                contentPath = item;
                break;
            }
        }
        return contentPath;
    }
    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }
    private String getFileName(String filePath){
        String[] path=filePath.split("/");
        String name;
        int position=0;
        position=path.length-1;
        name=path[position];
        return name;
    }
}
