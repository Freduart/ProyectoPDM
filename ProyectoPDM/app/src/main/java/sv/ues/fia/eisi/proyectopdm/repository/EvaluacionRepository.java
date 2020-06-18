package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Adapter.EvaluacionAdapter;
import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.EvaluacionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

public class EvaluacionRepository {
    //atributos
    private EvaluacionDao evaluacionDao;
    private LiveData<List<Evaluacion>> todasEvaluaciones;

    //constructor
    public EvaluacionRepository(Application application) {
        DataBase base = DataBase.getInstance(application);
        evaluacionDao = base.evaluacionDao();
        todasEvaluaciones = evaluacionDao.obtenerEvaluaciones();
    }

    //insertar asíncrono
    public void insertarEvaluacion(Evaluacion evaluacion){
        new InsertarEvaluacionAsyncTask(evaluacionDao).execute(evaluacion);
    }

    //actualizar asíncrono
    public void actualizarEvaluacion(Evaluacion evaluacion){
        new actualizarEvaluacionAsyncTask(evaluacionDao).execute(evaluacion);
    }

    public void borrarEvaluacion(Evaluacion evaluacion){
        new BorrarEvaluacionAsyncTask(evaluacionDao).execute(evaluacion);
    }

    //borrar todas asíncrono
    public void borrarTodasEvaluaciones(){
        new DeleteAllEvaluacionesAsyncTask(evaluacionDao).execute();
    }

    //obtener evaluacion asíncrono
    public Evaluacion obtenerEvaluacion(Integer integer) throws InterruptedException, ExecutionException, TimeoutException {
        return new obtenerEvaluacionAsyncTask(evaluacionDao).execute(integer).get(12, TimeUnit.SECONDS);
    }

    //obtener todas
    public LiveData<List<Evaluacion>> getTodasEvaluaciones() {
        return todasEvaluaciones;
    }

    public LiveData<List<Evaluacion>> getEvaluacionesEstudiante(String carnet) {
        return evaluacionDao.obtenerEvaluacionesDeEstudiante(carnet);
    }

    public LiveData<List<Evaluacion>> getEvaluacionesDocente(String carnet) {
        return evaluacionDao.obtenerEvaluacionesDeDocente(carnet);
    }

    //obtener asignatura usando el id de escuela
    public LiveData<List<Asignatura>> obtenerAsignaturasPorEscuela(int id){
        return evaluacionDao.obtenerAsignaturaPorEscuela(id);
    }

    //obtener docentes asíncrono
    public Docente obtenerDocentes(Integer id) throws InterruptedException, ExecutionException, TimeoutException {
        return new obtenerDocenteAsyncTask(evaluacionDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    //obtener tipos asíncrono
    public TipoEvaluacion obtenerTipos(Integer id) throws InterruptedException, ExecutionException, TimeoutException {
        return new obtenerTiposAsyncTask(evaluacionDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    //obtener asignatura asíncrono
    public Asignatura obtenerAsignatura(Integer id) throws InterruptedException, ExecutionException, TimeoutException {
        return new obtenerAsignaturaAsyncTask(evaluacionDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    public LiveData<List<Alumno>> getAlumnosDesdeAsignatura(String codigo) {return evaluacionDao.obtenerAlumnosDesdeAsignatura(codigo); }

    //obtener docente asíncrono con id de usuario
    public Docente obtenerDocenteConUsuario(int id) throws InterruptedException, ExecutionException, TimeoutException {
        //se puede ajustar el timeout para cancelar la recuperación del dato, el primer parametro indica la cantidad, el segundo la unidad
        return new obtenerDocenteConUsuarioAsyncTask(evaluacionDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    //obtener escuela de docente seleccionado
    public Escuela obtenerEscuelaDeDocente(String id) throws InterruptedException, ExecutionException, TimeoutException {
        //se puede ajustar el timeout para cancelar la recuperación del dato, el primer parametro indica la cantidad, el segundo la unidad
        return new obtenerEscuelaDeDocenteAsyncTask(evaluacionDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    //obtener docentes usando id de escuela
    public LiveData<List<Docente>> obtenerDocentesDeEscuela(int id) {
        return evaluacionDao.obtenerDocentePorEscuela(id);
    }

    //obtener alumno async
    public Alumno obtenerAlumnoConUsuario(int string)throws InterruptedException, ExecutionException, TimeoutException{
        return new obtenerAlumnoConUsuarioAsyncTask(evaluacionDao).execute(string).get(12, TimeUnit.SECONDS);
    }

    //Async de insertar
    private static class InsertarEvaluacionAsyncTask extends AsyncTask<Evaluacion, Void, Void>{
        private EvaluacionDao evaluacionDao;

        private InsertarEvaluacionAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Void doInBackground(Evaluacion... evaluaciones) {
            evaluacionDao.insertEvaluacion(evaluaciones[0]);
            return null;
        }
    }

    //async de actualizar
    private static class actualizarEvaluacionAsyncTask extends AsyncTask<Evaluacion, Void, Void>{
        private EvaluacionDao evaluacionDao;

        private actualizarEvaluacionAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Void doInBackground(Evaluacion... evaluaciones) {
            evaluacionDao.updateEvaluacion(evaluaciones[0]);
            return null;
        }
    }

    //async de Borrar
    private static class BorrarEvaluacionAsyncTask extends AsyncTask<Evaluacion, Void, Void>{
        private EvaluacionDao evaluacionDao;

        private BorrarEvaluacionAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Void doInBackground(Evaluacion... evaluaciones) {
            evaluacionDao.deleteEvaluacion(evaluaciones[0]);
            return null;
        }
    }

    //Async de borrar todos
    private static class DeleteAllEvaluacionesAsyncTask extends AsyncTask<Void, Void, Void> {
        private EvaluacionDao evaluacionDao;

        private DeleteAllEvaluacionesAsyncTask(EvaluacionDao evaluacionDao) {
            this.evaluacionDao = evaluacionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            evaluacionDao.borrarEvaluaciones();
            return null;
        }
    }

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
    }

    //async obtener Asignatura
    private static class obtenerAsignaturaAsyncTask extends AsyncTask<Integer, Void, Asignatura>{
        private EvaluacionDao evaluacionDao;

        private obtenerAsignaturaAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Asignatura doInBackground(Integer... docentes) {
            return evaluacionDao.getAsignaturas(docentes[0]);
        }
    }

    //async obtener Tipo de eval
    private static class obtenerTiposAsyncTask extends AsyncTask<Integer, Void, TipoEvaluacion>{
        private EvaluacionDao evaluacionDao;

        private obtenerTiposAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected TipoEvaluacion doInBackground(Integer... tipos) {
            return evaluacionDao.getTipoEvaluacion(tipos[0]);
        }
    }

    //async obtener docente
    private static class obtenerDocenteAsyncTask extends AsyncTask<Integer, Void, Docente>{
        private EvaluacionDao evaluacionDao;

        private obtenerDocenteAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Docente doInBackground(Integer... carnets) {
            return evaluacionDao.getDocente(carnets[0]);
        }
    }

    //async obtener alumno
    private static class obtenerAlumnoConUsuarioAsyncTask extends AsyncTask<Integer, Void, Alumno>{
        private EvaluacionDao alumnoDao;
        private obtenerAlumnoConUsuarioAsyncTask(EvaluacionDao alumnoDao){
            this.alumnoDao=alumnoDao;
        }

        @Override
        protected Alumno doInBackground(Integer... id) {
            return alumnoDao.obtenerAlumnoConUsuario(id[0]);
        }
    }

    //async obtener docente usando el id de usuario
    private static class obtenerDocenteConUsuarioAsyncTask extends AsyncTask<Integer, Void, Docente>{
        private EvaluacionDao docenteDao;

        private obtenerDocenteConUsuarioAsyncTask(EvaluacionDao docenteDao){
            this.docenteDao=docenteDao;
        }

        @Override
        protected Docente doInBackground(Integer... id) {
            return docenteDao.obtenerDocenteConUsuario(id[0]);
        }
    }


    //async obtener escuela usando el id de docente
    private static class obtenerEscuelaDeDocenteAsyncTask extends AsyncTask<String, Void, Escuela>{
        private EvaluacionDao docenteDao;

        private obtenerEscuelaDeDocenteAsyncTask(EvaluacionDao docenteDao){
            this.docenteDao=docenteDao;
        }

        @Override
        protected Escuela doInBackground(String... id) {
            return docenteDao.obtenerEscuelaDeDocente(id[0]);
        }
    }
}
