package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.DetalleEvaluacionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;

public class DetalleEvaluacionRepository {
    private DetalleEvaluacionDao detalleEvaluacionDao;
    private LiveData<List<DetalleEvaluacion>> allDetalleEvaluaciones;

    public DetalleEvaluacionRepository(Application application){
        DataBase base = DataBase.getInstance(application);
        detalleEvaluacionDao = base.detalleEvaluacionDao();
        allDetalleEvaluaciones = detalleEvaluacionDao.obtenerDetallesEvaluaciones();
    }

    public void insertarDetalleEvaluacion(DetalleEvaluacion detalleEvaluacion){
        new DetalleEvaluacionRepository.InsertarDetalleEvaluacionAsyncTask(detalleEvaluacionDao).execute(detalleEvaluacion);
    }

    public void actualizarDetalleEvaluacion(DetalleEvaluacion detalleEvaluacion){
        new DetalleEvaluacionRepository.ActualizarDetalleEvaluacionAsyncTask(detalleEvaluacionDao).execute(detalleEvaluacion);
    }

    public void borrarDetalleEvaluacion(DetalleEvaluacion detalleEvaluacion){
        new DetalleEvaluacionRepository.BorrarDetalleEvaluacionAsyncTask(detalleEvaluacionDao).execute(detalleEvaluacion);
    }

    public void borrarTodosDetalleEvaluaciones(){
        new DetalleEvaluacionRepository.DeleteAllDetalleEvaluacionesAyncTask(detalleEvaluacionDao).execute();
    }

    public DetalleEvaluacion obtenerDetalleEvaluacion(Integer integer) throws InterruptedException, ExecutionException, TimeoutException {
        return new DetalleEvaluacionRepository.obtenerDetalleEvaluacionAsyncTask(detalleEvaluacionDao).execute(integer).get(12, TimeUnit.SECONDS);
    }

    public List<DetalleEvaluacion> obtenerDetallePorAlumno(String string) throws InterruptedException, ExecutionException, TimeoutException {
        return new DetalleEvaluacionRepository.obtenerDetallePorAlumnoAsyncTask(detalleEvaluacionDao).execute(string).get(12, TimeUnit.SECONDS);
    }

    public LiveData<List<DetalleEvaluacion>> getAllDetalleEvaluaciones(){
        return allDetalleEvaluaciones;
    }

    private static class InsertarDetalleEvaluacionAsyncTask extends AsyncTask<DetalleEvaluacion, Void, Void>{
        private DetalleEvaluacionDao detalleEvaluacionDao;
        private InsertarDetalleEvaluacionAsyncTask(DetalleEvaluacionDao detalleEvaluacionDao){
            this.detalleEvaluacionDao=detalleEvaluacionDao;
        }

        @Override
        protected Void doInBackground(DetalleEvaluacion... detalleEvaluaciones) {
            detalleEvaluacionDao.insertDetalleEvaluacion(detalleEvaluaciones[0]);
            return null;
        }
    }

    private static class ActualizarDetalleEvaluacionAsyncTask extends AsyncTask<DetalleEvaluacion, Void, Void>{
        private DetalleEvaluacionDao detalleEvaluacionDao;
        private ActualizarDetalleEvaluacionAsyncTask(DetalleEvaluacionDao detalleEvaluacionDao){
            this.detalleEvaluacionDao=detalleEvaluacionDao;
        }

        @Override
        protected Void doInBackground(DetalleEvaluacion... detalleEvaluaciones) {
            detalleEvaluacionDao.updateDetalleEvaluacion(detalleEvaluaciones[0]);
            return null;
        }
    }

    private static class BorrarDetalleEvaluacionAsyncTask extends AsyncTask<DetalleEvaluacion, Void, Void>{
        private DetalleEvaluacionDao detalleEvaluacionDao;
        private BorrarDetalleEvaluacionAsyncTask(DetalleEvaluacionDao detalleEvaluacionDao){
            this.detalleEvaluacionDao=detalleEvaluacionDao;
        }

        @Override
        protected Void doInBackground(DetalleEvaluacion... detalleEvaluaciones) {
            detalleEvaluacionDao.deleteDetalleEvaluacion(detalleEvaluaciones[0]);
            return null;
        }
    }

    private static class DeleteAllDetalleEvaluacionesAyncTask extends AsyncTask<Void, Void, Void>{
        private DetalleEvaluacionDao detalleEvaluacionDao;

        private DeleteAllDetalleEvaluacionesAyncTask(DetalleEvaluacionDao detalleEvaluacionDao){
            this.detalleEvaluacionDao=detalleEvaluacionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            detalleEvaluacionDao.deleteAllDetallesEvaluaciones();
            return null;
        }
    }

    private static class obtenerDetalleEvaluacionAsyncTask extends AsyncTask<Integer, Void, DetalleEvaluacion>{
        private DetalleEvaluacionDao detalleEvaluacionDao;

        private obtenerDetalleEvaluacionAsyncTask(DetalleEvaluacionDao detalleEvaluacionDao){
            this.detalleEvaluacionDao=detalleEvaluacionDao;
        }
        @Override
        protected DetalleEvaluacion doInBackground(Integer... detalleEvaluaciones) {
            return detalleEvaluacionDao.obtenerDetalleEvaluacion(detalleEvaluaciones[0]);
        }
    }

    private static class obtenerDetallePorAlumnoAsyncTask extends AsyncTask<String, Void, List<DetalleEvaluacion>>{
        private DetalleEvaluacionDao detalleEvaluacionDao;

        private obtenerDetallePorAlumnoAsyncTask(DetalleEvaluacionDao detalleEvaluacionDao){
            this.detalleEvaluacionDao=detalleEvaluacionDao;
        }
        @Override
        protected List<DetalleEvaluacion> doInBackground(String... strings) {
            return detalleEvaluacionDao.obtenerDetallePorEstudiante(strings[0]);
        }
    }

}
