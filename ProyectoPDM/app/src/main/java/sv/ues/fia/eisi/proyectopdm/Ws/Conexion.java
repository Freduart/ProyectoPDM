package sv.ues.fia.eisi.proyectopdm.Ws;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//<Param,Progress,Result>
public class Conexion extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream is = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader reader =  new BufferedReader(new InputStreamReader(is));
                String lineas = "";
                StringBuffer bufferString = new StringBuffer();
                while((lineas=reader.readLine())!=null){
                    bufferString.append(lineas);
                }
                return bufferString.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
