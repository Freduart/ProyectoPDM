package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.OpcionCrudDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.OpcionCrud;

public class OpcionCrudRepository {

    private OpcionCrudDao opcionCrudDao;
    private LiveData<List<OpcionCrud>> todasOpciones;

    public OpcionCrudRepository(Application application){
        DataBase base = DataBase.getInstance(application);
        opcionCrudDao=base.opcionCrudDao();
        todasOpciones=opcionCrudDao.obtenerOpcionesCrud();
    }

    public void insertOpcionCrud(OpcionCrud opcionCrud){
        new InsertarOpcionCrudAsyncTask(opcionCrudDao).execute(opcionCrud);
    }

    public void  updateOpcionCrud(OpcionCrud opcionCrud){
        new ActualizarOpcionCrudAsyncTask(opcionCrudDao).execute(opcionCrud);
    }

    public void deleteOpcionCrud(OpcionCrud opcionCrud){
        new EliminarOpcionCrudAsyncTask(opcionCrudDao).execute(opcionCrud);
    }

    public void deleteAllOpcionCrud(){
        new EliminarTodasOpcionesAsyncTask(opcionCrudDao).execute();
    }

    public OpcionCrud obtenerOpcionCrud(Integer idOpcion) throws InterruptedException, ExecutionException, TimeoutException {
        return new ObtenerOpcionCrudAsyncTask(opcionCrudDao).execute(idOpcion).get(12, TimeUnit.SECONDS);
    }

    private static class InsertarOpcionCrudAsyncTask extends AsyncTask<OpcionCrud,Void,Void>{
        private OpcionCrudDao opcionCrudDao;

        public InsertarOpcionCrudAsyncTask(OpcionCrudDao opcionCrudDao) {
            this.opcionCrudDao = opcionCrudDao;
        }

        @Override
        protected Void doInBackground(OpcionCrud... opcionCruds) {
            opcionCrudDao.insertOpcionCrud(opcionCruds[0]);
            return null;
        }
    }

    private static class ActualizarOpcionCrudAsyncTask extends AsyncTask<OpcionCrud,Void,Void>{
        private OpcionCrudDao opcionCrudDao;

        public ActualizarOpcionCrudAsyncTask(OpcionCrudDao opcionCrudDao) {
            this.opcionCrudDao = opcionCrudDao;
        }

        @Override
        protected Void doInBackground(OpcionCrud... opcionCruds) {
            opcionCrudDao.updateOpcionCrud(opcionCruds[0]);
            return null;
        }
    }

    private static class EliminarOpcionCrudAsyncTask extends AsyncTask<OpcionCrud,Void,Void>{
        private OpcionCrudDao opcionCrudDao;

        public EliminarOpcionCrudAsyncTask(OpcionCrudDao opcionCrudDao) {
            this.opcionCrudDao = opcionCrudDao;
        }

        @Override
        protected Void doInBackground(OpcionCrud... opcionCruds) {
            opcionCrudDao.deleteOpcionCrud(opcionCruds[0]);
            return null;
        }
    }

    private static class EliminarTodasOpcionesAsyncTask extends AsyncTask<Void,Void,Void>{
        private OpcionCrudDao opcionCrudDao;

        public EliminarTodasOpcionesAsyncTask(OpcionCrudDao opcionCrudDao) {
            this.opcionCrudDao = opcionCrudDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            opcionCrudDao.borrarTodasOpcionesCrud();
            return null;
        }
    }

    private static class ObtenerOpcionCrudAsyncTask extends AsyncTask<Integer,Void,OpcionCrud>{
        private OpcionCrudDao opcionCrudDao;

        public ObtenerOpcionCrudAsyncTask(OpcionCrudDao opcionCrudDao) {
            this.opcionCrudDao = opcionCrudDao;
        }

        @Override
        protected OpcionCrud doInBackground(Integer... integers) {
            return opcionCrudDao.obtenerOpcionCrud(integers[0]);
        }
    }

    public LiveData<List<OpcionCrud>> getTodasOpciones(){
        return todasOpciones;
    }
}
