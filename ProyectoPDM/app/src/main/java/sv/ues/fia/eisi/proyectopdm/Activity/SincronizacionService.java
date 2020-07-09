package sv.ues.fia.eisi.proyectopdm.Activity;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.dao.BitacoraDao;
import sv.ues.fia.eisi.proyectopdm.dao.SolicitudImpresionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Bitacora;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

import static sv.ues.fia.eisi.proyectopdm.Activity.LoginActivity.FECHA_INGRESO;

public class SincronizacionService extends IntentService {

    public static volatile boolean shouldContinue = true;
    public static final int TIME_SINCRONIZE=10000;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private String url="sincronizacion.php?";
    private String fechaHoy;
    private SimpleDateFormat simpleDateFormat;
    private ArrayList<Bitacora> listaBitacoras;
    private Calendar calendar;
    private Timer mTimer=null;
    private SolicitudImpresionDao solicitudImpresionDao;
    private DataBase dataBase;
    private SolicitudImpresion solicitudImpresion;
    private BitacoraDao bitacoraDao;
    private List<Bitacora> bitacora;

    public SincronizacionService() {
        super("SincronizeService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(mTimer!=null){
            mTimer.cancel();
        }else{
            mTimer=new Timer();
        }
        bitacora=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);
        dataBase=DataBase.getInstance(this);
        solicitudImpresionDao=dataBase.solicitudImpresionDao();
        bitacoraDao=dataBase.bitacoraDao();
        System.out.println("Servicio Creado");
        calendar=Calendar.getInstance();
        mTimer.scheduleAtFixedRate(new Sincronizacion(),0,TIME_SINCRONIZE);
    }

    private class Sincronizacion extends TimerTask{
        @Override
        public void run() {
            if (!shouldContinue) {
                stopSelf();
                mTimer.cancel();
                mTimer.purge();
            }

            fechaHoy=PreferenceSingleton.getInstance().readPreference(FECHA_INGRESO);

            String formatFecha=fechaHoy.split(" ")[0],fromatHora=fechaHoy.split(" ")[1];
            String[] fecha=formatFecha.split("-"),hora=fromatHora.split(":");
            String fechayhora="day="+fecha[2]+"&month="+fecha[1]+"&year="+fecha[0]+"&hora="+hora[0]+"&minuto="+hora[1]+"&segundo="+hora[2];
            System.out.print(fechayhora+"\n");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.ipServer)+url+fechayhora, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonArray = response.optJSONArray("resultado");
                    JSONObject jsonObject = null;
                    listaBitacoras=new ArrayList<>();
                    for(int i=0; i<jsonArray.length();i++){
                        Bitacora bitacora=new Bitacora();
                        try {
                            jsonObject = jsonArray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bitacora.setIdObjeto(jsonObject.optString("IDOBJETO"));
                        bitacora.setNomTabla(jsonObject.optString("NOMTABLA"));
                        bitacora.setOperacion(jsonObject.optString("OPERACION"));
                        String fecha_json=jsonObject.optString("FECHAOPERACION");
                        listaBitacoras.add(bitacora);
                    }
                    if(listaBitacoras.size()>0){
                        for(Bitacora bitacora:listaBitacoras){
                            String idObjeto=bitacora.getIdObjeto(),nomTabla=bitacora.getNomTabla();
                            if(!(idObjeto.equalsIgnoreCase("NoData")||nomTabla.equalsIgnoreCase("NoData"))){
                                if(nomTabla.equals("SolicitudImpresion")){
                                    obtenerSolicitud(idObjeto,bitacora.getOperacion());
                                }
                            }
                            System.out.print(bitacora.toString()+"\n");
                        }
                        System.out.println(fechaHoy);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            requestQueue.add(jsonObjectRequest);
            //Para las bitacoras internas...
            try {
                bitacora=new obtenerBitacoraFechaAsyncTask(bitacoraDao).execute().get();
                if(bitacora.size()>0){
                    System.out.print("SI Hay Bitacora! \n");
                    for(Bitacora b:bitacora){
                        System.out.print(b.toString()+"\n");
                        procesarBitacoras(b);
                    }
                    new borrarBitacorasAsyncTask(bitacoraDao).execute();
                }else{
                    System.out.print("NO Hay Bitacora! \n");
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
/*
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            calendar=Calendar.getInstance();
            fechaHoy=simpleDateFormat.format(calendar.getTime());
            PreferenceSingleton.getInstance().writePreference(FECHA_INGRESO,fechaHoy);
            if (!shouldContinue) {
                stopSelf();
                mTimer.cancel();
                mTimer.purge();
            }
        }
    }

    private void obtenerSolicitud(String id,String accion){
        if(accion.equals("DELETE")){
            System.out.print("ES UN DELETE DEL SERVIDOR\n");
            new deleteSolicitudAsyncTask(solicitudImpresionDao).execute(id);
        }
        String url2=getString(R.string.ipServer)+"CrudSolicitudImpresion/soli_impres_retrieve.php?idimpresion="+ id;
        JsonObjectRequest jsonObjectRequest1=new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray1 = response.optJSONArray("solicitud");
                JSONObject jsonObject1;
                try{
                    for(int j=0;j<jsonArray1.length();j++){
                        jsonObject1 = jsonArray1.getJSONObject(j);
                        int idImpresion=Integer.parseInt(jsonObject1.optString("IDIMPRESION"));
                        String carnetDocenteFK=jsonObject1.optString("CARNETDOCENTEFK");
                        int idEncargadoFK=Integer.parseInt(jsonObject1.optString("IDENCARGADOFK"));
                        String docDirector=jsonObject1.optString("DOCDIRECTOR");
                        int numImpresiones=Integer.parseInt(jsonObject1.optString("NUMIMPRESIONES"));
                        String detalleImpresion=jsonObject1.optString("DETALLEIMPRESION");
                        String resultadoImpresion=jsonObject1.optString("RESULTADOIMPRESION");
                        String estadoSolicitud=jsonObject1.optString("ESTADOSOLICITUD");
                        String fechaSolicitud=jsonObject1.optString("FECHASOLICITUD");
                        String documento=jsonObject1.optString("DOCUMENTO");
                        solicitudImpresion=new SolicitudImpresion(carnetDocenteFK,idEncargadoFK,docDirector,numImpresiones,detalleImpresion,resultadoImpresion,estadoSolicitud,fechaSolicitud,documento);
                        if(accion.equals("INSERT")){
                            System.out.print("ES UN INSERT DEL SERVIDOR\n");
                            solicitudImpresion.setIdImpresion(idImpresion);
                            new insertSolicitudAsyncTask(solicitudImpresionDao).execute(solicitudImpresion);
                        }else if(accion.equals("UPDATE")){
                            System.out.print("ES UN UPDATE DEL SERVIDOR\n");
                            solicitudImpresion.setIdImpresion(idImpresion);
                            new updateSolicitudAsyncTask(solicitudImpresionDao).execute(solicitudImpresion);
                        }else{
                            System.out.print("NO HAY OPERACION DEL SERVIDOR\n");
                        }
                        new borrarBitacorasAsyncTask(bitacoraDao).execute();
                        System.out.print(solicitudImpresion.toString()+"\n");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest1);
    }

    private void procesarBitacoras(Bitacora bit){
        Integer id=Integer.parseInt(bit.getIdObjeto());
        try {
            SolicitudImpresion s=new obtenerSolicitudAsyncTask(solicitudImpresionDao).execute(id).get();
            if(bit.getOperacion().equals("INSERT")){
                System.out.print("ES UN INSERT HACIA EL SERVIDOR\n");
                System.out.print(s.toString());
                new insertSolicitudImpresionServer(getString(R.string.ipServer),requestQueue,bit.getIdObjeto()).execute(s);
            }else if(bit.getOperacion().equals("UPDATE")){
                System.out.print("ES UN UPDATE HACIA EL SERVIDOR\n");
                System.out.print(s.toString());
                new actualizarSolicitudServer(getString(R.string.ipServer),bit.getIdObjeto(),requestQueue).execute(s);
            }else if(bit.getOperacion().equals("DELETE")){
                System.out.print("ES UN DELETE HACIA EL SERVIDOR\n");
                new deleteSolicitudServer(getString(R.string.ipServer),bit.getIdObjeto(),requestQueue).execute();
            }else{
                System.out.print("NO HAY OPERACION HACIA EL SERVIDOR\n");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class insertSolicitudAsyncTask extends AsyncTask<SolicitudImpresion,Void,Void>{
        private SolicitudImpresionDao solicitudImpresionDao;

        public insertSolicitudAsyncTask(SolicitudImpresionDao solicitudImpresionDao) {
            this.solicitudImpresionDao = solicitudImpresionDao;
        }

        @Override
        protected Void doInBackground(SolicitudImpresion... solicitudImpresions) {
            solicitudImpresionDao.insertSolicitudImpresion(solicitudImpresions[0]);
            return null;
        }
    }

    private static class updateSolicitudAsyncTask extends AsyncTask<SolicitudImpresion,Void,Void>{
        private SolicitudImpresionDao solicitudImpresionDao;

        public updateSolicitudAsyncTask(SolicitudImpresionDao solicitudImpresionDao) {
            this.solicitudImpresionDao = solicitudImpresionDao;
        }

        @Override
        protected Void doInBackground(SolicitudImpresion... solicitudImpresions) {
            solicitudImpresionDao.updateSolicitudImpresion(solicitudImpresions[0]);
            return null;
        }
    }

    private static class deleteSolicitudAsyncTask extends AsyncTask<String,Void,Void>{
        private SolicitudImpresionDao solicitudImpresionDao;

        public deleteSolicitudAsyncTask(SolicitudImpresionDao solicitudImpresionDao) {
            this.solicitudImpresionDao = solicitudImpresionDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            solicitudImpresionDao.eliminarSolicitudPorId(strings[0]);
            return null;
        }
    }

    private static class obtenerBitacoraFechaAsyncTask extends AsyncTask<Void,Void,List<Bitacora>>{
        private BitacoraDao bitacoraDao;

        public obtenerBitacoraFechaAsyncTask(BitacoraDao bitacoraDao) {
            this.bitacoraDao = bitacoraDao;
        }

        @Override
        protected List<Bitacora> doInBackground(Void... voids) {
            return bitacoraDao.obtenerBitacoras();
        }
    }

    private static class obtenerSolicitudAsyncTask extends AsyncTask<Integer,Void,SolicitudImpresion>{
        private SolicitudImpresionDao solicitudImpresionDao;

        public obtenerSolicitudAsyncTask(SolicitudImpresionDao solicitudImpresionDao) {
            this.solicitudImpresionDao = solicitudImpresionDao;
        }

        @Override
        protected SolicitudImpresion doInBackground(Integer... integers) {
            return solicitudImpresionDao.obtenerSolicitudImpresion(integers[0]);
        }
    }

    private static class insertSolicitudImpresionServer extends AsyncTask<SolicitudImpresion,Void,Void>{
        StringRequest stringRequest;
        String url,idObjeto;
        RequestQueue requestQueue;

        public insertSolicitudImpresionServer(String url,RequestQueue requestQueue,String idObjeto) {
            this.url = url;
            this.requestQueue = requestQueue;
            this.idObjeto=idObjeto;
        }

        @Override
        protected Void doInBackground(SolicitudImpresion... solicitudImpresions) {
            System.out.print("INICIANDO INSERCION\n");
            stringRequest=new StringRequest(Request.Method.POST, url + "CrudSolicitudImpresion/soli_impres_insert.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.print(response+"\n");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.print(error.toString()+"\n");
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("idimpresion",idObjeto);
                    params.put("carnetDocente",solicitudImpresions[0].getCarnetDocenteFK());
                    params.put("docDirector",solicitudImpresions[0].getDocDirector());
                    params.put("numImpresiones",String.valueOf(solicitudImpresions[0].getNumImpresiones()));
                    params.put("detalleImpresion",solicitudImpresions[0].getDetalleImpresion());
                    params.put("estadoSolicitud",solicitudImpresions[0].getEstadoSolicitud());
                    params.put("fechaSolicitudImp",solicitudImpresions[0].getFechaSolicitud());
                    params.put("idEncargado",String.valueOf(solicitudImpresions[0].getIdEncargadoFK()));
                    params.put("documento",solicitudImpresions[0].getDocumento());
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            return null;
        }
    }

    private static class actualizarSolicitudServer extends AsyncTask<SolicitudImpresion,Void,Void>{
        StringRequest stringRequest;
        String url,idObjeto;
        RequestQueue requestQueue;

        public actualizarSolicitudServer(String url, String idObjeto, RequestQueue requestQueue) {
            this.url = url;
            this.idObjeto = idObjeto;
            this.requestQueue = requestQueue;
        }

        @Override
        protected Void doInBackground(SolicitudImpresion... solicitudImpresions) {
            System.out.print("INICIANDO ACTUALIZACION\n");
            stringRequest=new StringRequest(Request.Method.POST, url + "CrudSolicitudImpresion/soli_impres_update.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.print(response+"\n");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.print(error.toString()+"\n");
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("idImpresion",idObjeto);
                    params.put("carnetDocente",solicitudImpresions[0].getCarnetDocenteFK());
                    params.put("docDirector",solicitudImpresions[0].getDocDirector());
                    params.put("numImpresiones",String.valueOf(solicitudImpresions[0].getNumImpresiones()));
                    params.put("detalleImpresion",solicitudImpresions[0].getDetalleImpresion());
                    params.put("resultadoImpresion",solicitudImpresions[0].getResultadoImpresion());
                    params.put("estadoSolicitud",solicitudImpresions[0].getEstadoSolicitud());
                    params.put("fechaSolicitudImp",solicitudImpresions[0].getFechaSolicitud());
                    params.put("idEncargado",String.valueOf(solicitudImpresions[0].getIdEncargadoFK()));
                    params.put("documento",solicitudImpresions[0].getDocumento());
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            return null;
        }
    }

    private static class deleteSolicitudServer extends AsyncTask<Void,Void,Void>{
        StringRequest stringRequest;
        String url,idObjeto;
        RequestQueue requestQueue;

        public deleteSolicitudServer(String url, String idObjeto, RequestQueue requestQueue) {
            this.url = url;
            this.idObjeto = idObjeto;
            this.requestQueue = requestQueue;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.print("INICIANDO ELIMINACION\n");
            stringRequest=new StringRequest(Request.Method.POST, url + "CrudSolicitudImpresion/soli_impres_delete.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.print(response+"\n");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.print(error.toString()+"\n");
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("idimpresion",idObjeto);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            return null;
        }
    }

    private static class borrarBitacorasAsyncTask extends AsyncTask<Void,Void,Void>{
        private BitacoraDao bitacoraDao;

        public borrarBitacorasAsyncTask(BitacoraDao bitacoraDao) {
            this.bitacoraDao = bitacoraDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.print("ELIMINANDO BITACORAS\n");
            bitacoraDao.borrarBitacoras();
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Servicio Destruido");
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}
