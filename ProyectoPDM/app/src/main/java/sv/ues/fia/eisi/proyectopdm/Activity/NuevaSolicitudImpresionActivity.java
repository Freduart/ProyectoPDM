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
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
import sv.ues.fia.eisi.proyectopdm.Ws.UploadFileAsyncTask;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class NuevaSolicitudImpresionActivity extends AppCompatActivity {

    static final int PICK_DOCUMENT_REQUEST = 1;
    //Botones
    EditText text_detalleImpresiones;
    TextInputLayout text_impresiones,text_anexos;
    RecyclerView recyclerDocumentos;
    ListaArchivosAdapter listaArchivosAdapter;
    Spinner docDirector,encImpres;
    TextView textDocente;
    //Variables
    Uri documentUri;
    ArrayList<String> listaDocumentos,listDocDirector,listEncImpres;
    ArrayAdapter<String> adapterDocDirector,adapterEncImpres,adapterCarnetDocente;
    DocenteViewModel docenteViewModel;
    EncargadoImpresionViewModel encargadoImpresionViewModel;
    SolicitudImpresionViewModel solicitudImpresionViewModel;
    private SolicitudImpresion solicitudImpresion;
    private Docente docente;

    int id_usuario;
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_solicitud_impresion);
        //Titulo de ActionBar
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("NUEVA SOLICITUD");

        docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
        encargadoImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EncargadoImpresionViewModel.class);

        final Bundle extras = getIntent().getExtras();
        //verifica que los extra no estén vacíos
        if(extras != null) {
            //recibe id del usuario desde el extra
            id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
        }
        try {
            docente=docenteViewModel.obtenerDocentePorIdUsuario(id_usuario);
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
        listaDocumentos=new ArrayList<>();
        //Spinners
        docDirector=(Spinner)findViewById(R.id.textDocDirectorEditar);
        encImpres=(Spinner)findViewById(R.id.textEncImpresEditar);
        //TextView
        textDocente=(TextView)findViewById(R.id.textUserDocente);
        String datosDocente=docente.getCarnetDocente()+"-"+docente.getNomDocente()+" "+docente.getApellidoDocente();
        textDocente.setText(datosDocente);

        //Boton flotante
        FloatingActionButton enviarSolicitud=(FloatingActionButton)findViewById(R.id.fab_enviar_solicitud);
        //RecyclerView
        recyclerDocumentos=(RecyclerView)findViewById(R.id.recycler_archivos_ver);
        recyclerDocumentos.setLayoutManager(new LinearLayoutManager(this));
        listaArchivosAdapter=new ListaArchivosAdapter(listaDocumentos);
        recyclerDocumentos.setAdapter(listaArchivosAdapter);
        //Inicialización de variables
        try {
            //Spinner DocDirector:
            listDocDirector=new ArrayList<>();
            adapterDocDirector=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listDocDirector);
            adapterDocDirector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            docDirector.setAdapter(adapterDocDirector);
            //DocenteViewModel
            docenteViewModel.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
                @Override
                public void onChanged(List<Docente> docentes) {
                    for (Docente docente:docentes){
                        if(docente.getIdCargoFK()==1){
                            listDocDirector.add(docente.getCarnetDocente()+"-"+docente.getNomDocente()+" "+docente.getApellidoDocente());
                        }
                    }
                    adapterDocDirector.notifyDataSetChanged();
                }
            });
            //Spinner EncImpres:
            listEncImpres=new ArrayList<>();
            adapterEncImpres=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listEncImpres);
            adapterEncImpres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            encImpres.setAdapter(adapterEncImpres);
            //EncargadoImpresionViewModel
            encargadoImpresionViewModel.getAllEncargadoImpresion().observe(this, new Observer<List<EncargadoImpresion>>() {
                @Override
                public void onChanged(List<EncargadoImpresion> encargadoImpresions) {
                    for(EncargadoImpresion encargadoImpresion:encargadoImpresions){
                        listEncImpres.add(encargadoImpresion.getIdEncargadoImpresion()+"-"+encargadoImpresion.getNomEncargado());
                    }
                    adapterEncImpres.notifyDataSetChanged();
                }
            });

        }catch (Exception e){
            Toast.makeText(this, "Ha ocurrido un error: "+e, Toast.LENGTH_SHORT).show();
        }

        //Boton Enviar Solicitud
        enviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaDocumentos.size()==0){
                    Toast.makeText(NuevaSolicitudImpresionActivity.this, "Debe Añadir Un Documento...", Toast.LENGTH_SHORT).show();
                    result=false;
                }else if((text_impresiones.getEditText().getText().toString().trim().isEmpty())){
                    text_impresiones.setError("Ingrese N° Impreisones");
                    result=false;
                }else{
                    guardarSolicitud();
                    UploadFileAsyncTask uploadFileAsyncTask=new UploadFileAsyncTask(NuevaSolicitudImpresionActivity.this,listaDocumentos.get(0));
                    uploadFileAsyncTask.execute();
                }
            }
        });
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
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.nueva_solicitud_impresion_menu,menu);
        return true;
    }

    @SuppressLint("IntentReset")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.añadirDocumento:
                //Intent para seleccionar un documento...
                if(listaDocumentos.size()<1){
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
                }
            case R.id.ajustesServer:
                //ajustes del servidor
                new CheckInternetAsyncTask(getApplicationContext()).execute();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public AlertDialog createCustomDialog(int position,String documento){
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
                                {"application/pdf","application/msword", // .doc & .docx
                                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                        "application/vnd.ms-excel"};
                        Intent intent = new Intent( Intent.ACTION_VIEW );
                        intent.setData(uriSeleccionado);
                        intent.setType("application/*");
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

    public class CheckInternetAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        private Context context;
        public CheckInternetAsyncTask(Context context) {
            this.context = context;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
            if (isConnected) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    if (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0){
                        return true;
                    }else{
                        return false;
                    }
                } catch (IOException e) {
                    Log.e("TAG", "Error checking internet connection", e);
                    return false;
                }
            } else {
                Log.d("TAG", "No network available!");
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.d("TAG", "result" + result);
            if(!result){
                Toast.makeText(NuevaSolicitudImpresionActivity.this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(NuevaSolicitudImpresionActivity.this, "Si hay conexion a internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void guardarSolicitud(){
        //Carnet Docente
        String carnetDocente=docente.getCarnetDocente();
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
            String archivo1=getFileName(listaDocumentos.get(0)).replace(" ","_");
            String archivo=removeEspecial(archivo1);
            String rutaArchivoServer="http://dr17010pdm115.000webhostapp.com/uploads/"+archivo;
            detalleImpresion2="Hojas Anexas Por Documento: "+nHojasAnexas+"\n"+detallesDeImpresion1;
            solicitudImpresion=new SolicitudImpresion(carnetDocente,Integer.parseInt(encargadoID),carnetDocDirector,
                    Integer.parseInt(nImpresiones),detalleImpresion2,"",
                    "NUEVA",fechaHoy,rutaArchivoServer);
            solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(SolicitudImpresionViewModel.class);
            solicitudImpresionViewModel.insert(solicitudImpresion);
            Toast.makeText(getApplicationContext(), "Guardado Exitosamente", Toast.LENGTH_SHORT).show();
            result=true;
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
            result=false;
        }
    }

    public String removeEspecial(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }
}
