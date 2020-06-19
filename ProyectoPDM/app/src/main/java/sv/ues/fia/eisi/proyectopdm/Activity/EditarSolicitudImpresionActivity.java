package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Adapter.ListaArchivosAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EncargadoImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class EditarSolicitudImpresionActivity extends AppCompatActivity {

    static final int PICK_DOCUMENT_REQUEST = 1;
    //Botones
    TextView textDatosDocente;
    EditText text_detalleImpresiones;
    TextInputLayout text_impresiones,text_anexos;
    RecyclerView recyclerDocumentos;
    ListaArchivosAdapter listaArchivosAdapter;
    Spinner docDirector,encImpres;
    //Variables
    Uri documentUri;
    ArrayList<String> listaDocumentos,listDocDirector,listEncImpres;
    ArrayAdapter<String> adapterDocDirector,adapterEncImpres;
    DocenteViewModel docenteViewModel;
    EncargadoImpresionViewModel encargadoImpresionViewModel;
    SolicitudImpresionViewModel solicitudImpresionViewModel;
    private SolicitudImpresion solicitudImpresionActual,solicitudImpresion;
    private Docente docenteActual,docDirectorActual;
    private EncargadoImpresion encImpresActual;
    boolean documentoModificado;

    ProgressDialog dialog = null;
    public static final String upLoadServerUri="http://dr17010pdm115.000webhostapp.com/procesar.php";
    int serverResponseCode = 0;
    private String rutaArchivoServer="";
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_solicitud_impresion);

        //Titulo de ActionBar
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("EDITAR SOLICITUD");

        //DocenteViewModel
        docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
        //EncargadoImpresionViewModel
        encargadoImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EncargadoImpresionViewModel.class);
        //SolicitudImpresionViewModel
        solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudImpresionViewModel.class);

        //Obteniendo datos del intent
        Bundle bundle=getIntent().getExtras();
        int idImpreison=0;
        if(bundle!=null){
            idImpreison=bundle.getInt(SolicitudImpresionActivity.IDENTIFICADOR_IMPRESION);
        }
        try {
            solicitudImpresionActual=solicitudImpresionViewModel.obtenerSolicitudImpresion(idImpreison);
            docenteActual=docenteViewModel.getDocente(solicitudImpresionActual.getCarnetDocenteFK());
            docDirectorActual=docenteViewModel.getDocente(solicitudImpresionActual.getDocDirector());
            encImpresActual=encargadoImpresionViewModel.ObtenerEncargadoImpresion(solicitudImpresionActual.getIdEncargadoFK());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        //Cajas de texto
        text_detalleImpresiones=(EditText)findViewById(R.id.text_detalleImpresion_editar);
        text_impresiones=(TextInputLayout)findViewById(R.id.text_impresiones_editar);
        text_anexos=(TextInputLayout)findViewById(R.id.text_anexos_editar);
        textDatosDocente=(TextView)findViewById(R.id.textDatosDocente);
        String datosDocente=docenteActual.getCarnetDocente()+"-"+docenteActual.getNomDocente()+" "+docenteActual.getApellidoDocente();
        textDatosDocente.setText(datosDocente);
        listaDocumentos=new ArrayList<>();
        //Valor por defecto para las cajas de texto
        text_impresiones.getEditText().setText(Integer.toString(solicitudImpresionActual.getNumImpresiones()));
        String detallesImpresion=solicitudImpresionActual.getDetalleImpresion();
        String[] detalles=detallesImpresion.split("\n");
        String[] detallesAnexos=detalles[0].split(" ");
        text_anexos.getEditText().setText(detallesAnexos[detallesAnexos.length-1]);
        text_detalleImpresiones.setText(detalles[1]);
        //Spinners
        docDirector=(Spinner)findViewById(R.id.textDocDirectorEditar);
        encImpres=(Spinner)findViewById(R.id.textEncImpresEditar);

        //RecyclerView
        recyclerDocumentos=(RecyclerView)findViewById(R.id.recycler_archivos_ver);
        recyclerDocumentos.setLayoutManager(new LinearLayoutManager(this));
        listaArchivosAdapter=new ListaArchivosAdapter(listaDocumentos);
        recyclerDocumentos.setAdapter(listaArchivosAdapter);
        //Valor por defecto para la listaDocumentos
        listaDocumentos.add(solicitudImpresionActual.getDocumento());
        listaArchivosAdapter.setOnItemClickListener(new ListaArchivosAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, String documento) {
                createCustomDialog(position,documento).show();
            }
        });
        //Inicialización de variables
        try {
            //Spinner DocDirector:
            listDocDirector=new ArrayList<>();
            adapterDocDirector=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listDocDirector);
            adapterDocDirector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            docDirector.setAdapter(adapterDocDirector);
            docenteViewModel.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
                @Override
                public void onChanged(List<Docente> docentes) {
                    try {
                        Docente doc=docenteViewModel.getDocente(solicitudImpresionActual.getDocDirector());
                        for (Docente docente:docentes){
                            if(docente.getIdCargoFK()==1){
                                listDocDirector.add(docente.getCarnetDocente()+"-"+docente.getNomDocente()+" "+docente.getApellidoDocente());
                                if(docente.getCarnetDocente().equals(doc.getCarnetDocente())){
                                    docDirector.setSelection(listDocDirector.indexOf(docente.getCarnetDocente()+"-"+docente.getNomDocente()+" "+docente.getApellidoDocente()));
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                    adapterDocDirector.notifyDataSetChanged();
                }
            });
            //Spinner EncImpres:
            listEncImpres=new ArrayList<>();
            adapterEncImpres=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listEncImpres);
            adapterEncImpres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            encImpres.setAdapter(adapterEncImpres);
            encargadoImpresionViewModel.getAllEncargadoImpresion().observe(this, new Observer<List<EncargadoImpresion>>() {
                @Override
                public void onChanged(List<EncargadoImpresion> encargadoImpresions) {
                    try {
                        EncargadoImpresion enc=encargadoImpresionViewModel.ObtenerEncargadoImpresion(solicitudImpresionActual.getIdEncargadoFK());
                        for(EncargadoImpresion encargadoImpresion:encargadoImpresions){
                            listEncImpres.add(encargadoImpresion.getIdEncargadoImpresion()+"-"+encargadoImpresion.getNomEncargado());
                            if(encargadoImpresion.getIdEncargadoImpresion()==(enc.getIdEncargadoImpresion())){
                                encImpres.setSelection(listEncImpres.indexOf(encargadoImpresion.getIdEncargadoImpresion()+"-"+encargadoImpresion.getNomEncargado()));
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                    adapterEncImpres.notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Ha ocurrido un error: "+e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DOCUMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                documentUri = data.getData();
                try{
                    String path = getPathMethod(this, documentUri);
                    if (path == null) {
                        Toast.makeText(this, "Archivo No Encontrado...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                        listaDocumentos.add(path);
                        //Ponemos a la escucha cada item agregado
                        listaArchivosAdapter.setOnItemClickListener(new ListaArchivosAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position, String documento) {
                                createCustomDialog(position,documento).show();
                            }
                        });
                        listaArchivosAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editar_solicitud_impresion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar:
                if(documentoModificado){
                    dialog = ProgressDialog.show(EditarSolicitudImpresionActivity.this, "", "Subiendo Archivo...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(EditarSolicitudImpresionActivity.this, "Subiendo Archivo.....", Toast.LENGTH_SHORT).show();
                                }
                            });
                            uploadFile(listaDocumentos.get(0));
                            if(result){
                                actualizarSolicitudImpresion();
                                if(result){
                                    finish();
                                }
                            }
                        }
                    }).start();
                }else{
                    actualizarSolicitudImpresion();
                    finish();
                }
                return true;
            case R.id.agregar:
                //Intent para seleccionar un documento...
                String[] mimeTypes =
                        {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                "application/pdf"};
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
                intent.setData(uri);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(Intent.createChooser(intent, "Open Document"),PICK_DOCUMENT_REQUEST);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizarSolicitudImpresion(){
        //Carnet Docente
        String carnetDocente=docenteActual.getCarnetDocente();
        //Docente Director
        String docente=docDirector.getSelectedItem().toString();
        String docente2[]=docente.split("-");
        String carnetDocDirector=docente2[0];
        //Encargado Impresión
        String encargado=encImpres.getSelectedItem().toString();
        String encargado2[]=encargado.split("-");
        String encargadoID=encargado2[0];
        //Impresiones
        String nImpresiones=text_impresiones.getEditText().getText().toString();
        //Variables
        String nHojasAnexas,detallesDeImpresion1,detalleImpresion2,fechaHoy;
        try {
            //Hojas Anexas
            if(!text_anexos.getEditText().getText().toString().trim().isEmpty()){
                nHojasAnexas="0";
            }else{
                nHojasAnexas=text_anexos.getEditText().getText().toString();
            }
            //Detalles de Impresión
            detallesDeImpresion1=text_detalleImpresiones.getText().toString();
            //Fecha del sistema
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fechaHoy=simpleDateFormat.format(calendar.getTime());
            //Documentos
            detalleImpresion2="Hojas Anexas Por Documento: "+nHojasAnexas+"\n"+detallesDeImpresion1;
            solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudImpresionViewModel.class);
            solicitudImpresion=solicitudImpresionViewModel.obtenerSolicitudImpresion(solicitudImpresionActual.getIdImpresion());
            solicitudImpresion.setCarnetDocenteFK(carnetDocente);
            solicitudImpresion.setIdEncargadoFK(Integer.parseInt(encargadoID));
            solicitudImpresion.setDocDirector(carnetDocDirector);
            solicitudImpresion.setNumImpresiones(Integer.parseInt(nImpresiones));
            solicitudImpresion.setDetalleImpresion(detalleImpresion2);
            solicitudImpresion.setEstadoSolicitud("MODIFICADO");
            solicitudImpresion.setResultadoImpresion("");
            solicitudImpresion.setFechaSolicitud(fechaHoy);
            if(documentoModificado){
                solicitudImpresion.setDocumento(rutaArchivoServer);
            }
            solicitudImpresionViewModel.update(solicitudImpresion);
            Toast.makeText(getApplicationContext(), "Guardado Exitosamente", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomDialog(int position, String documento){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Uri uriSeleccionado=Uri.fromFile(new File(documento));
        String nombreDocumento=getFileName(documento);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflar y establecer el layout para el dialogo
        // Pasar nulo como vista principal porque va en el diseño del diálogo
        View v = inflater.inflate(R.layout.dialog_opciones_documento, null);
        Button btnPrevisualizar = (Button)v.findViewById(R.id.btnPrevisualizar);
        Button btnQuitar = (Button)v.findViewById(R.id.btnQuitar);
        TextView textView=(TextView)v.findViewById(R.id.textNomEnc);
        textView.setText(nombreDocumento);
        builder.setView(v);
        alertDialog = builder.create();
        // Add action buttons
        btnPrevisualizar.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("IntentReset")
                    @Override
                    public void onClick(View v) {
                        //Previsualizar
                        String[] mimeTypes =
                                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                        "application/pdf"};
                        Intent intent = new Intent( Intent.ACTION_VIEW );
                        intent.setData(uriSeleccionado);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
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
                        //Quitar
                        listaDocumentos.remove(position);
                        listaArchivosAdapter.notifyDataSetChanged();
                        documentoModificado=true;
                        alertDialog.dismiss();
                    }
                }
        );
        return alertDialog;
    }
    /*Cuando el intent para seleccionar un documento es iniciado, se enlistan todos los proveedores de contenido que existen en el
      dispositivo, a través de este metodo se obtiene la ruta del archivo obtenidos los datos de su proveedor y su key.*/
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

    //A través de este método obtenemos la ruta original del documento consultando al proveedor de contenido correspondiente.
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
    /*Se valida que la ruta corresponda a cualquiera de estos proveedores de contenido y se devuelve el contenedor padre de este*/
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

    //Con este metodo obtenemos el nombre del documento dada la ruta especifica donde este se encuentra.
    private String getFileName(String filePath){
        String[] path=filePath.split("/");
        String name;
        int position=0;
        position=path.length-1;
        name=path[position];
        return name;
    }

    public int uploadFile(String sourceFileUri) {
        String fileName = getFileName(sourceFileUri).replace(" ","_");
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            dialog.dismiss();
            Log.e("uploadFile", "Source File not exist :" +sourceFileUri);
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(EditarSolicitudImpresionActivity.this, "Source File not exist :" +sourceFileUri , Toast.LENGTH_SHORT).show();
                    result=false;
                }
            });
            return 0;
        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("archivo", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=archivo; filename=" + fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed.\n\n" + sourceFileUri;
                            Toast.makeText(EditarSolicitudImpresionActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                            rutaArchivoServer="http://dr17010pdm115.000webhostapp.com/uploads/"+fileName;
                            result=true;
                        }
                    });
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                dialog.dismiss();
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(EditarSolicitudImpresionActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                        result=false;
                    }
                });
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                dialog.dismiss();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(EditarSolicitudImpresionActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                        result=false;
                    }
                });
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;
        } // End else block
    }
}
