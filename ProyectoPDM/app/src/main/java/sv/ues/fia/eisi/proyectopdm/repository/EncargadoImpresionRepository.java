package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.EncargadoImpresionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;

public class EncargadoImpresionRepository {
    private EncargadoImpresionDao encargadoImpresionDao;
    private LiveData<List<EncargadoImpresion>> allEncargadoImpresion;

    public EncargadoImpresionRepository(Application application){
        DataBase dataBase=DataBase.getInstance(application);
        encargadoImpresionDao=dataBase.encargadoImpresionDao();
        allEncargadoImpresion=encargadoImpresionDao.obtenerEncargados();
    }

    public void insertar(EncargadoImpresion encargadoImpresion){
        new InsertarEncargadoImpresionAsyncTask(encargadoImpresionDao).execute(encargadoImpresion);
    }

    public void actualizar(EncargadoImpresion encargadoImpresion){
        new ActualizarEncargadoImpresionAsyncTask(encargadoImpresionDao).execute(encargadoImpresion);
    }

    public void eliminar(EncargadoImpresion encargadoImpresion){
        new BorrarEncargadoImpresionAsyncTask(encargadoImpresionDao).execute(encargadoImpresion);
    }

    public void eliminarTodos(){
        new BorrarTodosEncargadoImpresionAsyncTask(encargadoImpresionDao).execute();
    }

    public LiveData<List<EncargadoImpresion>> getAllEncargadoImpresion(){
        return allEncargadoImpresion;
    }

    public EncargadoImpresion ObtenerEncargadoImpresion(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return new ObtenerEncargadoImpresionAsyncTask(encargadoImpresionDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    private static class InsertarEncargadoImpresionAsyncTask extends AsyncTask<EncargadoImpresion,Void,Void>{
        private EncargadoImpresionDao encargadoImpresionDao;

        public InsertarEncargadoImpresionAsyncTask(EncargadoImpresionDao encargadoImpresionDao) {
            this.encargadoImpresionDao = encargadoImpresionDao;
        }

        @Override
        protected Void doInBackground(EncargadoImpresion... encargadoImpresions) {
            encargadoImpresionDao.insertEncargadoImpresion(encargadoImpresions[0]);
            return null;
        }
    }
    private static class ActualizarEncargadoImpresionAsyncTask extends AsyncTask<EncargadoImpresion,Void,Void>{
        private EncargadoImpresionDao encargadoImpresionDao;

        public ActualizarEncargadoImpresionAsyncTask(EncargadoImpresionDao encargadoImpresionDao) {
            this.encargadoImpresionDao = encargadoImpresionDao;
        }

        @Override
        protected Void doInBackground(EncargadoImpresion... encargadoImpresions) {
            encargadoImpresionDao.updateEncargadoImpresion(encargadoImpresions[0]);
            return null;
        }
    }
    private static class BorrarEncargadoImpresionAsyncTask extends AsyncTask<EncargadoImpresion,Void,Void>{
        private EncargadoImpresionDao encargadoImpresionDao;

        public BorrarEncargadoImpresionAsyncTask(EncargadoImpresionDao encargadoImpresionDao) {
            this.encargadoImpresionDao = encargadoImpresionDao;
        }

        @Override
        protected Void doInBackground(EncargadoImpresion... encargadoImpresions) {
            encargadoImpresionDao.deleteEncargadoImpresion(encargadoImpresions[0]);
            return null;
        }
    }
    private static class BorrarTodosEncargadoImpresionAsyncTask extends AsyncTask<Void,Void,Void>{
        private EncargadoImpresionDao encargadoImpresionDao;

        public BorrarTodosEncargadoImpresionAsyncTask(EncargadoImpresionDao encargadoImpresionDao) {
            this.encargadoImpresionDao = encargadoImpresionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            encargadoImpresionDao.borrarEncargadoImpresion();
            return null;
        }
    }
    private static class ObtenerEncargadoImpresionAsyncTask extends AsyncTask<Integer,Void,EncargadoImpresion>{
        private EncargadoImpresionDao encargadoImpresionDao;

        public ObtenerEncargadoImpresionAsyncTask(EncargadoImpresionDao encargadoImpresionDao) {
            this.encargadoImpresionDao = encargadoImpresionDao;
        }

        @Override
        protected EncargadoImpresion doInBackground(Integer... integers) {
            return encargadoImpresionDao.obtenerEncargadoImpresion(integers[0]);
        }
    }

}
