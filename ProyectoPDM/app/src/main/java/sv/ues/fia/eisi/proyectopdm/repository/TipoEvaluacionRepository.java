package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.TipoEvaluacionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

public class TipoEvaluacionRepository {
    //atributos
    private TipoEvaluacionDao tipoEvaluacionDao;
    private LiveData<List<TipoEvaluacion>> todasTipoEvaluaciones;

    //constructor
    public TipoEvaluacionRepository(Application application) {
        DataBase base = DataBase.getInstance(application);
        tipoEvaluacionDao = base.tipoEvaluacionDao();
        todasTipoEvaluaciones = tipoEvaluacionDao.obtenerTipos();
    }

    //insertar asíncrono
    public void insertarTipoEvaluacion(TipoEvaluacion tipoEvaluacion){
        new InsertarTipoEvaluacionAsyncTask(tipoEvaluacionDao).execute(tipoEvaluacion);
    }

    //actualizar asíncrono
    public void actualizarTipoEvaluacion(TipoEvaluacion tipoEvaluacion){
        new actualizarTipoEvaluacionAsyncTask(tipoEvaluacionDao).execute(tipoEvaluacion);
    }

    public void borrarTipoEvaluacion(TipoEvaluacion tipoEvaluacion){
        new BorrarTipoEvaluacionAsyncTask(tipoEvaluacionDao).execute(tipoEvaluacion);
    }

    //borrar todas asíncrono
    public void borrarTodasTipoEvaluaciones(){
        new DeleteAllTipoEvaluacionesAsyncTask(tipoEvaluacionDao).execute();
    }

    //obtener todas
    public LiveData<List<TipoEvaluacion>> getTodosTipoEvaluaciones() {
        return todasTipoEvaluaciones;
    }

    //obtener tipo evaluacion asíncrono
    public TipoEvaluacion obtenerTipoEvaluacion(Integer integer) throws InterruptedException, ExecutionException, TimeoutException {
        return new obtenerTipoEvaluacionAsyncTask(tipoEvaluacionDao).execute(integer).get(12, TimeUnit.SECONDS);
    }

    //Async de insertar
    private static class InsertarTipoEvaluacionAsyncTask extends AsyncTask<TipoEvaluacion, Void, Void>{
        private TipoEvaluacionDao tipoEvaluacionDao;

        private InsertarTipoEvaluacionAsyncTask(TipoEvaluacionDao tipoEvaluacionDao){
            this.tipoEvaluacionDao=tipoEvaluacionDao;
        }

        @Override
        protected Void doInBackground(TipoEvaluacion... tiposEvaluaciones) {
            tipoEvaluacionDao.insertarTipoEv(tiposEvaluaciones[0]);
            return null;
        }
    }

    //async de actualizar
    private static class actualizarTipoEvaluacionAsyncTask extends AsyncTask<TipoEvaluacion, Void, Void>{
        private TipoEvaluacionDao tipoEvaluacionDao;

        private actualizarTipoEvaluacionAsyncTask(TipoEvaluacionDao tipoEvaluacionDao){
            this.tipoEvaluacionDao=tipoEvaluacionDao;
        }

        @Override
        protected Void doInBackground(TipoEvaluacion... tipoEvaluaciones) {
            tipoEvaluacionDao.updateTipoEv(tipoEvaluaciones[0]);
            return null;
        }
    }

    //async de Borrar
    private static class BorrarTipoEvaluacionAsyncTask extends AsyncTask<TipoEvaluacion, Void, Void>{
        private TipoEvaluacionDao tipoEvaluacionDao;

        private BorrarTipoEvaluacionAsyncTask(TipoEvaluacionDao tipoEvaluacionDao){
            this.tipoEvaluacionDao=tipoEvaluacionDao;
        }

        @Override
        protected Void doInBackground(TipoEvaluacion... tipoEvaluaciones) {
            tipoEvaluacionDao.borrarTipoEv(tipoEvaluaciones[0]);
            return null;
        }
    }

    //Async de borrar todos
    private static class DeleteAllTipoEvaluacionesAsyncTask extends AsyncTask<Void, Void, Void> {
        private TipoEvaluacionDao tipoEvaluacionDao;

        private DeleteAllTipoEvaluacionesAsyncTask(TipoEvaluacionDao tipoEvaluacionDao) {
            this.tipoEvaluacionDao = tipoEvaluacionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tipoEvaluacionDao.borrarTipos();
            return null;
        }
    }

    //async obtener tipo evaluacion
    private static class obtenerTipoEvaluacionAsyncTask extends AsyncTask<Integer, Void, TipoEvaluacion>{
        private TipoEvaluacionDao tipoEvaluacionDao;

        private obtenerTipoEvaluacionAsyncTask(TipoEvaluacionDao tipoEvaluacionDao){
            this.tipoEvaluacionDao=tipoEvaluacionDao;
        }

        @Override
        protected TipoEvaluacion doInBackground(Integer... tipos) {
            return tipoEvaluacionDao.obtenerTipoEvaluacion(tipos[0]);
        }
    }
}
