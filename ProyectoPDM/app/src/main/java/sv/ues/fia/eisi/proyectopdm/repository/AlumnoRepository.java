package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.AlumnoDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;


/*
    Las clases Repository se encargan de la conexion con las clases DAO para operaciones de datos
    y las transformara en operaciones de la clase AsyncTask para operaciones de datos
 */

public class AlumnoRepository {
    private AlumnoDao alumnoDao;
    private LiveData<List<Alumno>> allAlumnos;

    public AlumnoRepository(Application application){
        DataBase dataBase = DataBase.getInstance((application));
        alumnoDao = dataBase.alumnoDao();
        allAlumnos =  alumnoDao.obtenerAlumnos();
    }

    /*
        Los metodos AsyncTask se enlazan con los metodos en las clases DAO necesitaremos mandarles
        1 parametros, 1 progreso y el resultado
     */

    public static class InsertarAlumnoAsyncTask extends AsyncTask<Alumno, Void, Void> {
        private AlumnoDao alumnoDao;

        public InsertarAlumnoAsyncTask(AlumnoDao alumnoDao){
            this.alumnoDao = alumnoDao;
        }
        @Override
        protected Void doInBackground(Alumno... alumnos) {
            //Aca enlazamos con el metodo DAO
            alumnoDao.insertarAlumno(alumnos[0]);
            return null;
        }
    }

    public static class ActualizarAlumnoAsyncTask extends AsyncTask<Alumno, Void, Void> {
        private AlumnoDao alumnoDao;
        public ActualizarAlumnoAsyncTask(AlumnoDao alumnoDao){
            this.alumnoDao = alumnoDao;
        }
        @Override
        protected Void doInBackground(Alumno... alumnos) {
            alumnoDao.updateAlumno(alumnos[0]);
            return null;
        }
    }

    public static class BorrarAlumnoAsyncTask extends AsyncTask<Alumno, Void, Void> {
        private AlumnoDao alumnoDao;
        public BorrarAlumnoAsyncTask(AlumnoDao alumnoDao){
            this.alumnoDao = alumnoDao;
        }
        @Override
        protected Void doInBackground(Alumno... alumnos) {
            alumnoDao.deleteAlumno(alumnos[0]);
            return null;
        }
    }

    public static class DeleteAllAlumnosAsyncTask extends AsyncTask<Void, Void, Void> {
        private AlumnoDao alumnoDao;

        public DeleteAllAlumnosAsyncTask(AlumnoDao alumnoDao){
            this.alumnoDao = alumnoDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            alumnoDao.borrarAlumnos();
            return null;
        }
    }

    public void insertar(Alumno alumno){
        new InsertarAlumnoAsyncTask(alumnoDao).execute(alumno);
    }

    /*public void insertarServer(Alumno alumno,Context context){
        new insertAlumnoServerAsynTask(alumno,context).execute();
    }*/

    public void actualizar(Alumno alumno){
        new ActualizarAlumnoAsyncTask(alumnoDao).execute(alumno);
    }

    public void eliminar(Alumno alumno){
        new BorrarAlumnoAsyncTask(alumnoDao).execute(alumno);
    }

    public void eliminarTodos(){
        new DeleteAllAlumnosAsyncTask(alumnoDao).execute();
    }

    //obtener alumno async
    public Alumno obtenerAlumno(String string)throws InterruptedException, ExecutionException, TimeoutException{
        return new obtenerAlumnoAsyncTask(alumnoDao).execute(string).get(12, TimeUnit.SECONDS);
    }

    public LiveData<List<Alumno>> getAllAlumnos() {return allAlumnos;}

    /*
    //async obtener evaluacion
    private static class obtenerEvaluacionAsyncTask extends AsyncTask<Integer, Void, Evaluacion>{
        private EvaluacionDao evaluacionDao;

        private obtenerEvaluacionAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Evaluacion doInBackground(Integer... evaluaciones) {
            return evaluacionDao.obtenerEvaluacion(evaluaciones[0]);
        }
    }*/

    //async obtener alumno
    private static class obtenerAlumnoAsyncTask extends AsyncTask<String, Void, Alumno>{
        private AlumnoDao alumnoDao;
        private obtenerAlumnoAsyncTask(AlumnoDao alumnoDao){
            this.alumnoDao=alumnoDao;
        }

        @Override
        protected Alumno doInBackground(String... alumnos) {
            return alumnoDao.obtenerAlumno(alumnos[0]);
        }
    }
/*
    private static class insertAlumnoServerAsynTask extends AsyncTask<Void,Void,Void>{
        private Alumno alumno;
        private StringRequest stringRequest;
        private String url="http://192.168.1.6/CrudAlumno/alumno_insert.php";
        private Context context;

        public insertAlumnoServerAsynTask(Alumno alumno, Context context) {
            this.alumno = alumno;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            stringRequest=new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("carnetalumno",alumno.getCarnetAlumno());
                    params.put("idusuariofk",String.valueOf(alumno.getIdUsuarioFk()));
                    params.put("nombre",alumno.getNombre());
                    params.put("apellido",alumno.getApellido());
                    params.put("carrera",alumno.getCarrera());
                    params.put("correo",alumno.getCorreo());
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
            return null;
        }
    }*/
}
