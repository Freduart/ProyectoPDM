package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.SegundaRevisionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;

public class SegundaRevisionRepository {
    //atributos
    private SegundaRevisionDao segundaRevisionDao;
    private LiveData<List<SegundaRevision>> todasSegundaRevisiones;

    //constructor
    public SegundaRevisionRepository(Application application) {
        DataBase base = DataBase.getInstance(application);
        segundaRevisionDao = base.segundaRevisionDao();
        todasSegundaRevisiones = segundaRevisionDao.obtenerSegundasRevisiones();
    }

    //insertar asíncrono
    public void insertarSegundaRevision(SegundaRevision segundaRevision){
        new SegundaRevisionRepository.InsertarSegundaRevisionAsyncTask(segundaRevisionDao).execute(segundaRevision);
    }

    //actualizar asíncrono
    public void actualizarSegundaRevision(SegundaRevision segundaRevision){
        new SegundaRevisionRepository.actualizarSegundaRevisionAsyncTask(segundaRevisionDao).execute(segundaRevision);
    }

    public void borrarSegundaRevision(SegundaRevision segundaRevision){
        new SegundaRevisionRepository.BorrarSegundaRevisionAsyncTask(segundaRevisionDao).execute(segundaRevision);
    }

    //borrar todas asíncrono
    public void borrarTodasSegundaRevisiones(){
        new SegundaRevisionRepository.DeleteAllSegundaRevisionesAsyncTask(segundaRevisionDao).execute();
    }

    //obtener segundaRevision asíncrono
    public SegundaRevision obtenerSegundaRevision(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return new SegundaRevisionRepository.obtenerSegundaRevisionAsyncTask(segundaRevisionDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    //obtener todas
    public LiveData<List<SegundaRevision>> getTodasSegundaRevisiones() {
        return todasSegundaRevisiones;
    }

    //Async de insertar
    private static class InsertarSegundaRevisionAsyncTask extends AsyncTask<SegundaRevision, Void, Void> {
        private SegundaRevisionDao segundaRevisionDao;

        private InsertarSegundaRevisionAsyncTask(SegundaRevisionDao segundaRevisionDao){
            this.segundaRevisionDao=segundaRevisionDao;
        }

        @Override
        protected Void doInBackground(SegundaRevision... segundaRevisiones) {
            segundaRevisionDao.insertSegundaRevision(segundaRevisiones[0]);
            return null;
        }
    }

    //async de actualizar
    private static class actualizarSegundaRevisionAsyncTask extends AsyncTask<SegundaRevision, Void, Void>{
        private SegundaRevisionDao segundaRevisionDao;

        private actualizarSegundaRevisionAsyncTask(SegundaRevisionDao segundaRevisionDao){
            this.segundaRevisionDao=segundaRevisionDao;
        }

        @Override
        protected Void doInBackground(SegundaRevision... segundaRevisiones) {
            segundaRevisionDao.updateSegundaRevision(segundaRevisiones[0]);
            return null;
        }
    }

    //async de Borrar
    private static class BorrarSegundaRevisionAsyncTask extends AsyncTask<SegundaRevision, Void, Void>{
        private SegundaRevisionDao segundaRevisionDao;

        private BorrarSegundaRevisionAsyncTask(SegundaRevisionDao segundaRevisionDao){
            this.segundaRevisionDao=segundaRevisionDao;
        }

        @Override
        protected Void doInBackground(SegundaRevision... segundaRevisiones) {
            segundaRevisionDao.deleteSegundaRevision(segundaRevisiones[0]);
            return null;
        }
    }

    //Async de borrar todos
    private static class DeleteAllSegundaRevisionesAsyncTask extends AsyncTask<Void, Void, Void> {
        private SegundaRevisionDao segundaRevisionDao;

        private DeleteAllSegundaRevisionesAsyncTask(SegundaRevisionDao segundaRevisionDao) {
            this.segundaRevisionDao = segundaRevisionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            segundaRevisionDao.borrarSegundasRevisiones();
            return null;
        }
    }

    //async obtener segundaRevision
    private static class obtenerSegundaRevisionAsyncTask extends AsyncTask<Integer, Void, SegundaRevision>{
        private SegundaRevisionDao segundaRevisionDao;

        private obtenerSegundaRevisionAsyncTask(SegundaRevisionDao segundaRevisionDao){
            this.segundaRevisionDao=segundaRevisionDao;
        }

        @Override
        protected SegundaRevision doInBackground(Integer... primerasRevisiones) {
            return segundaRevisionDao.obtenerSegundaRevision(primerasRevisiones[0]);
        }
    }

}
