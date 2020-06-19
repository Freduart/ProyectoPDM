package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.SolicitudExtraordinarioDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

public class SolicitudExtraordinarioRepository {

    private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
    private LiveData<List<SolicitudExtraordinario>> allSolicitudesExtraordinario;

    public SolicitudExtraordinarioRepository(Application application){
        DataBase dataBase=DataBase.getInstance(application);
        solicitudExtraordinarioDao=dataBase.solicitudExtraordinarioDao();
        allSolicitudesExtraordinario=solicitudExtraordinarioDao.obtenerSolicitudesExtraordinario();
    }

    public void insertar(SolicitudExtraordinario solicitudExtraordinario){
        new InsertarSolicitudExtraordinarioAsyncTask(solicitudExtraordinarioDao).execute(solicitudExtraordinario);
    }

    public void actualizar(SolicitudExtraordinario solicitudExtraordinario){
        new ActualizarSolicitudExtraordinarioAsyncTask(solicitudExtraordinarioDao).execute(solicitudExtraordinario);
    }

    public void eliminar(SolicitudExtraordinario solicitudExtraordinario){
        new BorrarSolicitudExtraordinarioAsyncTask(solicitudExtraordinarioDao).execute(solicitudExtraordinario);
    }

    public void eliminarTodas(){
        new DeleteAllSolicitudesExtraordinarioAsyncTask(solicitudExtraordinarioDao).execute();
    }

    public LiveData<List<SolicitudExtraordinario>> getAllSolicitudesExtraordinario(){
        return allSolicitudesExtraordinario;
    }

    public LiveData<List<SolicitudExtraordinario>> getSolicitudesEstudiante(String carnet){
        return solicitudExtraordinarioDao.obtenerSolicitudesDeEstudiante(carnet);
    }

    public LiveData<List<SolicitudExtraordinario>> getSolicitudesDocente(String carnet){
        return solicitudExtraordinarioDao.obtenerSolicitudesParaDocente(carnet);
    }

    public SolicitudExtraordinario getSoliExtra(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return new getSoliExtra(solicitudExtraordinarioDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    public TipoEvaluacion getTipoEvaluacion(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return new getTipoEvalAsyncTask(solicitudExtraordinarioDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    public Evaluacion getEvaluacion(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return new getEvaluacionAsyncTask(solicitudExtraordinarioDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    public Alumno getAlumno(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return new getAlumnoAsyncTask(solicitudExtraordinarioDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    public Alumno obtenerAlumnoConUsuario(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return new obtenerAlumnoUsuarioAsyncTask(solicitudExtraordinarioDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    public Docente obtenerDocenteConUsuario(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return new obtenerDocenteUsuarioAsyncTask(solicitudExtraordinarioDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    private static class InsertarSolicitudExtraordinarioAsyncTask extends AsyncTask<SolicitudExtraordinario,Void,Void> {
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;

        public InsertarSolicitudExtraordinarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao) {
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Void doInBackground(SolicitudExtraordinario... solicitudesExtraordinario) {
            solicitudExtraordinarioDao.insertSolicitudExtraordinario(solicitudesExtraordinario[0]);
            return null;
        }
    }

    private static class ActualizarSolicitudExtraordinarioAsyncTask extends AsyncTask<SolicitudExtraordinario,Void,Void>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;

        public ActualizarSolicitudExtraordinarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao) {
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Void doInBackground(SolicitudExtraordinario... solicitudesExtraordinario) {
            solicitudExtraordinarioDao.updateSolicitudExtraordinario(solicitudesExtraordinario[0]);
            return null;
        }
    }

    private static class BorrarSolicitudExtraordinarioAsyncTask extends AsyncTask<SolicitudExtraordinario,Void,Void>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;

        public BorrarSolicitudExtraordinarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao) {
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Void doInBackground(SolicitudExtraordinario... solicitudesExtraordinario) {
            solicitudExtraordinarioDao.deleteSolicitudExtraordinario(solicitudesExtraordinario[0]);
            return null;
        }
    }

    private static class DeleteAllSolicitudesExtraordinarioAsyncTask extends AsyncTask<Void,Void,Void>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;

        public DeleteAllSolicitudesExtraordinarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao) {
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            solicitudExtraordinarioDao.borrarSolicitudesExtraordinario();
            return null;
        }
    }

    //Clase Asíncrona de Obtener Solicitud
    private static class getSoliExtra extends AsyncTask<Integer, Void, SolicitudExtraordinario>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
        private getSoliExtra(SolicitudExtraordinarioDao solicitudExtraordinarioDao){
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected SolicitudExtraordinario doInBackground(Integer... solicitudesExtra) {
            return solicitudExtraordinarioDao.obtenerSolicitudExtraordinario(solicitudesExtra[0]);
        }
    }

    private static class getTipoEvalAsyncTask extends AsyncTask<Integer, Void, TipoEvaluacion>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
        private getTipoEvalAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao){
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected TipoEvaluacion doInBackground(Integer... solicitudesExtra){
            return solicitudExtraordinarioDao.getTipoEvaluacion(solicitudesExtra[0]);
        }
    }

    private static class getEvaluacionAsyncTask extends AsyncTask<Integer, Void, Evaluacion>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
        private getEvaluacionAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao){
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Evaluacion doInBackground(Integer... solicitudesExtra){
            return solicitudExtraordinarioDao.getEvaluacion(solicitudesExtra[0]);
        }
    }

    private static class getAlumnoAsyncTask extends AsyncTask<Integer, Void, Alumno>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
        private  getAlumnoAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao){
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Alumno doInBackground(Integer... solicitudesExtra){
            return solicitudExtraordinarioDao.getAlumno(solicitudesExtra[0]);
        }
    }

    private static class obtenerAlumnoUsuarioAsyncTask extends AsyncTask<Integer, Void, Alumno>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
        private obtenerAlumnoUsuarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao){
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Alumno doInBackground(Integer... solicitudesExtra){
            return solicitudExtraordinarioDao.obtenerAlumnoConUsuario(solicitudesExtra[0]);
        }
    }

    private static class obtenerDocenteUsuarioAsyncTask extends AsyncTask<Integer, Void, Docente>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
        private  obtenerDocenteUsuarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao){
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Docente doInBackground(Integer... solicitudesExtra){
            return solicitudExtraordinarioDao.obtenerDocenteConUsuario(solicitudesExtra[0]);
        }
    }
}
