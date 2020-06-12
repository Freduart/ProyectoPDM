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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.ListaArchivosAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EncargadoImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudImpresionViewModel;
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
    Spinner docDirector,encImpres,carnetDoc;
    //Variables
    Uri documentUri;
    //Uri del documento del recyclerDocumentos
    Uri uriSeleccionado;
    String nombreDocumento;
    int index=0;
    int[] numImpresiones={}, hojasAnexas={};
    String detallesImpresion, acumPath="",carnetDocente;
    ArrayList<String> listaDocumentos,listDocDirector,listEncImpres,listDocentes;
    ArrayAdapter<String> adapterDocDirector,adapterEncImpres,adapterCarnetDocente;
    DocenteViewModel docenteViewModel;
    EncargadoImpresionViewModel encargadoImpresionViewModel;
    SolicitudImpresionViewModel solicitudImpresionViewModel;
    private SolicitudImpresion solicitudImpresion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_solicitud_impresion);
        //Titulo de ActionBar
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("NUEVA SOLICITUD");
        //Cajas de texto
        text_detalleImpresiones=(EditText)findViewById(R.id.text_detalleImpresion);
        text_impresiones=(TextInputLayout)findViewById(R.id.text_impresiones);
        text_anexos=(TextInputLayout)findViewById(R.id.text_anexos);
        listaDocumentos=new ArrayList<>();
        //Spinners
        docDirector=(Spinner)findViewById(R.id.spinnerDocDirector);
        encImpres=(Spinner)findViewById(R.id.spinnerEncImpres);
        carnetDoc=(Spinner)findViewById(R.id.spinnerCarnetDocente);

        //Boton flotante
        FloatingActionButton enviarSolicitud=(FloatingActionButton)findViewById(R.id.fab_enviar_solicitud);
        //RecyclerView
        recyclerDocumentos=(RecyclerView)findViewById(R.id.recycler_archivos);
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
            docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
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
            encargadoImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EncargadoImpresionViewModel.class);
            encargadoImpresionViewModel.getAllEncargadoImpresion().observe(this, new Observer<List<EncargadoImpresion>>() {
                @Override
                public void onChanged(List<EncargadoImpresion> encargadoImpresions) {
                    for(EncargadoImpresion encargadoImpresion:encargadoImpresions){
                        listEncImpres.add(encargadoImpresion.getIdEncargadoImpresion()+"-"+encargadoImpresion.getNomEncargado());
                    }
                    adapterEncImpres.notifyDataSetChanged();
                }
            });
            //Spinner CarnetDocente:
            listDocentes=new ArrayList<>();
            adapterCarnetDocente=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listDocentes);
            adapterCarnetDocente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            carnetDoc.setAdapter(adapterCarnetDocente);
            //DocenteViewModel
            docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
            docenteViewModel.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
                @Override
                public void onChanged(List<Docente> docentes) {
                    for (Docente docente:docentes){
                        listDocentes.add(docente.getCarnetDocente()+"-"+docente.getNomDocente()+" "+docente.getApellidoDocente());
                    }
                    adapterCarnetDocente.notifyDataSetChanged();
                }
            });

        }catch (Exception e){
            Toast.makeText(this, "Ha ocurrido un error: "+e, Toast.LENGTH_SHORT).show();
        }
        //Boton Enviar Solicitud
        enviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nImpresiones=text_impresiones.getEditText().getText().toString();
                if(listaDocumentos.size()==0){
                    Snackbar.make(v, "Debe Añadir Un Documento...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else if((text_impresiones.getEditText().getText().toString().trim().isEmpty())){
                    text_impresiones.setError("Ingrese N° Impreisones");
                }else if(nImpresiones.contains("*")||nImpresiones.contains("#")||nImpresiones.contains("(")||nImpresiones.contains(")")||nImpresiones.contains("/")||nImpresiones.contains("N")||nImpresiones.contains(";")||nImpresiones.contains("+")||nImpresiones.contains("-")||nImpresiones.contains(".")){
                    text_impresiones.setError("Ingrese Los Datos Correctamente.");
                }else{
                    //Carnet Docente
                    String carnetDocente1=carnetDoc.getSelectedItem().toString();
                    String[] carnetDocente2=carnetDocente1.split("-");
                    String carnetDocente=carnetDocente2[0];
                    //Docente Director
                    String docente=docDirector.getSelectedItem().toString();
                    String docente2[]=docente.split("-");
                    String carnetDocDirector=docente2[0];
                    //Encargado Impresión
                    String encargado=encImpres.getSelectedItem().toString();
                    String encargado2[]=encargado.split("-");
                    String encargadoID=encargado2[0];
                    //N° Impresiones
                    nImpresiones=nImpresiones.replace(" ","");
                    String[] splitImpresiones,splitHojasAnexas;
                    splitImpresiones=nImpresiones.split(",");
                    //Variables
                    String nHojasAnexas="0",detallesDeImpresion1,detalleImpresion2,fechaHoy;
                    if(splitImpresiones.length!=listaDocumentos.size()){
                        text_impresiones.setError("Ingrese Los Datos Correctamente.");
                    }else{
                        try {
                            //Hojas Anexas
                            if(!text_anexos.getEditText().getText().toString().trim().isEmpty()){
                                nHojasAnexas=text_anexos.getEditText().getText().toString();
                                if(nHojasAnexas.contains("*")||nHojasAnexas.contains("#")||nHojasAnexas.contains("(")||nHojasAnexas.contains(")")||nHojasAnexas.contains("/")||nHojasAnexas.contains("N")||nHojasAnexas.contains(";")||nHojasAnexas.contains("+")||nHojasAnexas.contains("-")||nHojasAnexas.contains(".")){
                                    text_anexos.setError("Ingrese Los Datos Correctamente.");
                                }else{
                                    nHojasAnexas=nHojasAnexas.replace(" ","");
                                    splitHojasAnexas=nHojasAnexas.split(",");
                                    if(splitHojasAnexas.length!=listaDocumentos.size()){
                                        text_anexos.setError("Ingrese Los Datos Correctamente.");
                                    }else{
                                        //Detalles de Impresión
                                        detallesDeImpresion1=text_detalleImpresiones.getText().toString();
                                        //Fecha del sistema
                                        Calendar calendar=Calendar.getInstance();
                                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        fechaHoy=simpleDateFormat.format(calendar.getTime());
                                        //Documentos
                                        for(int i=0;i<listaDocumentos.size();i++){
                                            detalleImpresion2="Hojas Anexas Por Documento: "+splitHojasAnexas[i]+".\n"+detallesDeImpresion1;
                                            solicitudImpresion=new SolicitudImpresion(carnetDocente,Integer.parseInt(encargadoID),carnetDocDirector,
                                                    Integer.parseInt(splitImpresiones[i]),detalleImpresion2,"En Curso",
                                                    "Nueva",fechaHoy,listaDocumentos.get(i));
                                            solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication())
                                                    .create(SolicitudImpresionViewModel.class);
                                            solicitudImpresionViewModel.insert(solicitudImpresion);
                                        }
                                        Toast.makeText(getApplicationContext(), "Guardado Exitosamente", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
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
                        listaArchivosAdapter.setOnItemClickListener(new ItemClickListenerArchivos() {
                            @Override
                            public void OnItemClick(int position, String documento) {
                                uriSeleccionado=Uri.fromFile(new File(documento));
                                nombreDocumento=getFileName(documento);
                                index=position;
                                createCustomDialog().show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.añadirDocumento:
                //Intent para seleccionar un documento...
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
                intent.setDataAndType(uri, "application/pdf");
                startActivityForResult(Intent.createChooser(intent, "Open Document"),PICK_DOCUMENT_REQUEST);
            case R.id.ajustesServer:
                //ajustes del servidor
            default:
                return super.onOptionsItemSelected(item);
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
                        //Previsualizar
                        Intent intent = new Intent( Intent.ACTION_VIEW );
                        intent.setDataAndType(uriSeleccionado, "application/pdf");
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
                        listaDocumentos.remove(index);
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
}
