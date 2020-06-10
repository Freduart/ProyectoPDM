package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.PrimeraRevisionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class PrimeraRevisionRepository {

    private PrimeraRevisionDao primeraRevisionDao;
    private LiveData<List<PrimeraRevision>> allPrimerasRevisiones;

    public PrimeraRevisionRepository(Application application){
        DataBase base = DataBase.getInstance(application);
        primeraRevisionDao = base.primeraRevisionDao();
        allPrimerasRevisiones = primeraRevisionDao.obtenerPrimerasRevisiones();
    }

    public void insertarPrimeraRevision(PrimeraRevision primeraRevision){
        new PrimeraRevisionRepository.InsertarPrimeraRevisionAsyncTask(primeraRevisionDao).execute(primeraRevision);
    }

    public void actualizarPrimeraRevision(PrimeraRevision primeraRevision){
        new PrimeraRevisionRepository.ActualizarPrimeraRevisionAsyncTask(primeraRevisionDao).equals(primeraRevision);
    }

    public void borrarPrimeraRevision(PrimeraRevision primeraRevision){
        new PrimeraRevisionRepository.BorrarPrimeraRevisionAsyncTask(primeraRevisionDao).execute(primeraRevision);
    }

    public void borrarTodasPrimerasRevisiones(){
        new PrimeraRevisionRepository.DeleteAllPrimeraRevisionesAsyncTask(primeraRevisionDao).execute();
    }

    public PrimeraRevision obtenerPrimeraRevision(String string) throws InterruptedException, ExecutionException, TimeoutException {
        return new PrimeraRevisionRepository.obtenerPrimeraRevisionAsyncTask(primeraRevisionDao).execute(string).get(12, TimeUnit.SECONDS);
    }

    public LiveData<List<PrimeraRevision>> getAllPrimerasRevisiones(){
        return allPrimerasRevisiones;
    }

    private static class InsertarPrimeraRevisionAsyncTask extends AsyncTask<PrimeraRevision, Void, Void>{
        private PrimeraRevisionDao primeraRevisionDao;

        private InsertarPrimeraRevisionAsyncTask(PrimeraRevisionDao primeraRevisionDao){
            this.primeraRevisionDao=primeraRevisionDao;
        }
        @Override
        protected Void doInBackground(PrimeraRevision... primeraRevisiones) {
            primeraRevisionDao.insertPrimeraRevision(primeraRevisiones[0]);
            return null;
        }
    }

    private static class ActualizarPrimeraRevisionAsyncTask extends AsyncTask<PrimeraRevision, Void, Void>{
        private PrimeraRevisionDao primeraRevisionDao;

        private ActualizarPrimeraRevisionAsyncTask(PrimeraRevisionDao primeraRevisionDao){
            this.primeraRevisionDao=primeraRevisionDao;
        }
        @Override
        protected Void doInBackground(PrimeraRevision... primeraRevisiones) {
            primeraRevisionDao.updatePrimeraRevision(primeraRevisiones[0]);
            return null;
        }
    }

    private static class BorrarPrimeraRevisionAsyncTask extends AsyncTask<PrimeraRevision, Void, Void>{
        private PrimeraRevisionDao primeraRevisionDao;

        private BorrarPrimeraRevisionAsyncTask(PrimeraRevisionDao primeraRevisionDao){
            this.primeraRevisionDao=primeraRevisionDao;
        }
        @Override
        protected Void doInBackground(PrimeraRevision... primeraRevisiones) {
            primeraRevisionDao.deletePrimeraRevision(primeraRevisiones[0]);
            return null;
        }
    }

    private static class  DeleteAllPrimeraRevisionesAsyncTask extends AsyncTask<Void, Void, Void>{
        private PrimeraRevisionDao primeraRevisionDao;

        private DeleteAllPrimeraRevisionesAsyncTask(PrimeraRevisionDao primeraRevisionDao){
            this.primeraRevisionDao=primeraRevisionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            primeraRevisionDao.deleteAllPrimerasRevisiones();
            return null;
        }
    }

    private static class obtenerPrimeraRevisionAsyncTask extends AsyncTask <String, Void, PrimeraRevision>{
        private PrimeraRevisionDao primeraRevisionDao;

        private obtenerPrimeraRevisionAsyncTask(PrimeraRevisionDao primeraRevisionDao){
            this.primeraRevisionDao=primeraRevisionDao;
        }

        @Override
        protected PrimeraRevision doInBackground(String... primeraRevisiones) {
            return primeraRevisionDao.obtenerPrimeraRevision(primeraRevisiones[0]);
        }
    }
}