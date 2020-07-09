package sv.ues.fia.eisi.proyectopdm.Ws;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sv.ues.fia.eisi.proyectopdm.R;

public class UploadFileAsyncTask extends AsyncTask<Void,Integer,Void> {

    public static final String upLoadServerUri="http://dr17010pdm115.000webhostapp.com/procesar.php";
    int serverResponseCode;
    //Context context;
    ProgressDialog progressDialog;
    String sourceFileUri;
    Activity activity;

    public UploadFileAsyncTask(Activity activity, String sourceFileUri) {
        //this.context = context;
        this.sourceFileUri = sourceFileUri;
        this.activity=activity;
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
        progressDialog=new ProgressDialog(activity);
        progressDialog.setTitle("Documento");
        progressDialog.setIcon(R.drawable.ic_upload);
        progressDialog.setMessage("Subiendo Archivos, Espere!");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
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

    @Override
    protected Void doInBackground(Void... voids) {
        String fileName1 = getFileName(sourceFileUri).replace(" ","_");
        String fileName = removeEspecial(fileName1);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize,fileLegth;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            progressDialog.dismiss();
            System.out.print("Source File not exist :" +sourceFileUri);
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
                //fileLegth = Math.min(bytesAvailable, maxBufferSize);
                //fileLegth=(int)sourceFile.length();
                fileLegth=fileInputStream.available();
                buffer = new byte[bufferSize];
                int total=0;
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    total+=bytesRead;
                    dos.write(buffer, 0, bufferSize);
                    publishProgress(total*100/fileLegth);
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
                    String msg = "File Upload Completed.\n\n" + sourceFileUri;
                    System.out.print(msg);
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                progressDialog.dismiss();
                ex.printStackTrace();
                System.out.print("MalformedURLException");
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
                System.out.print("Got Exception : see logcat ");
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
        } // End else block
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        activity.finish();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }
}