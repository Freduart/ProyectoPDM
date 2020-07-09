package sv.ues.fia.eisi.proyectopdm.Ws;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sv.ues.fia.eisi.proyectopdm.Activity.SolicitudImpresionActivity;
import sv.ues.fia.eisi.proyectopdm.R;

public class DownloadFileAsyncTask extends AsyncTask<Void,Integer,String> {

    private static final String DIRECTORIO_DOCUMENTOS = "DocumentosImpresion";
    ProgressDialog progressDialog;
    String urlArchivo;
    SolicitudImpresionActivity solicitud;
    String pathArchivo;

    public DownloadFileAsyncTask(String url, SolicitudImpresionActivity solicitud) {
        this.urlArchivo = url;
        this.solicitud=solicitud;
    }

    private String getFileName(String filePath){
        String[] path=filePath.split("/");
        String name;
        int position=0;
        position=path.length-1;
        name=path[position];
        return name;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(solicitud);
        progressDialog.setTitle("Documento");
        progressDialog.setIcon(R.drawable.ic_download);
        progressDialog.setMessage("Descargando Documento...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        //Establecemos la ruta del archivo.
        String path=Environment.getExternalStorageDirectory()+"/"+DIRECTORIO_DOCUMENTOS+"/"+getFileName(urlArchivo);
        try {
            URL url=new URL(urlArchivo);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();

            int fileLength=connection.getContentLength();

            //Creamos una carpeta en el almacenamiento ecterno para guardar los documentos.
            File file=new File(Environment.getExternalStorageDirectory(),DIRECTORIO_DOCUMENTOS);
            //Verificamos si la carpeta ya existe...
            boolean existe=file.exists();
            if(!existe){
                existe=file.mkdirs();
            }
            //Creamos un nuevo archivo con esa ruta.
            File documento=new File(Environment.getExternalStorageDirectory()+"/"+DIRECTORIO_DOCUMENTOS,getFileName(urlArchivo));
            //Verificamos si el archivo no existe para descargarlo.
            if(!documento.exists()){
                final FileOutputStream fileOutputStream=new FileOutputStream(documento);
                final InputStream inputStream= connection.getInputStream();
                byte data[] = new byte[1024];
                long total = 0;
                int count;

                while ((count = inputStream.read(data))!=-1){
                    total+=count;
                    publishProgress((int) (total * 100 / fileLength));
                    fileOutputStream.write(data,0,count);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
            return path;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        solicitud.abrirDocumento(s);
        pathArchivo=s;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }
}