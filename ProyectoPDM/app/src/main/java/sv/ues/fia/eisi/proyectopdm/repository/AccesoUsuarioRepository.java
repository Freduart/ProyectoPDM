package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.AccesoUsuarioDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;

public class AccesoUsuarioRepository {
    private AccesoUsuarioDao accesoUsuarioDao;
    private LiveData<List<AccesoUsuario>> todosAccesos;

    public AccesoUsuarioRepository(Application application){
        DataBase dataBase=DataBase.getInstance(application);
        accesoUsuarioDao=dataBase.accesoUsuarioDao();
        todosAccesos=accesoUsuarioDao.obtenerAccesos();
    }

    public void insertarAccesoUsuairo(AccesoUsuario accesoUsuario){
        new InsertarAccesoUsuarioAsyncTask(accesoUsuarioDao).execute(accesoUsuario);
    }

    public void actualizarAccesoUsuario(AccesoUsuario accesoUsuario){
        new ActualizarAccesoUsuarioAsyncTask(accesoUsuarioDao).execute(accesoUsuario);
    }

    public void eliminarAccesoUsuario(AccesoUsuario accesoUsuario){
        new EliminarAccesoUsuarioAsyncTask(accesoUsuarioDao).execute(accesoUsuario);
    }

    public void eliminarTodosAccesos(){
        new BorrarTodosAccesosAsyncTask(accesoUsuarioDao).execute();
    }

    public LiveData<List<AccesoUsuario>> obtenerAccesosusuario(Integer idUsuario) throws InterruptedException, ExecutionException, TimeoutException {
        return new ObtenerAccesosUsuarioAsyncTask(accesoUsuarioDao).execute(idUsuario).get(12, TimeUnit.SECONDS);
    }

    public LiveData<List<AccesoUsuario>> obtenerAccesosPorNumCrud(int idUsuario,int numCrud) throws InterruptedException, ExecutionException, TimeoutException {
        return new ObtenerAccesosPorNumCrudAsyncTask(accesoUsuarioDao,idUsuario,numCrud).execute().get(12, TimeUnit.SECONDS);
    }

    public void eliminarAccesosDeUsuario(Integer idUsuario){
        new EliminarAccesosDeUsuarioAsyncTask(accesoUsuarioDao).execute(idUsuario);
    }

    public LiveData<List<AccesoUsuario>> getTodosAccesos(){
        return todosAccesos;
    }

    private static class InsertarAccesoUsuarioAsyncTask extends AsyncTask<AccesoUsuario,Void,Void>{
        private AccesoUsuarioDao accesoUsuarioDao;

        public InsertarAccesoUsuarioAsyncTask(AccesoUsuarioDao accesoUsuarioDao) {
            this.accesoUsuarioDao = accesoUsuarioDao;
        }

        @Override
        protected Void doInBackground(AccesoUsuario... accesoUsuarios) {
            accesoUsuarioDao.insertAccesoUsuario(accesoUsuarios[0]);
            return null;
        }
    }

    private static class ActualizarAccesoUsuarioAsyncTask extends AsyncTask<AccesoUsuario,Void,Void>{
        private AccesoUsuarioDao accesoUsuarioDao;

        public ActualizarAccesoUsuarioAsyncTask(AccesoUsuarioDao accesoUsuarioDao) {
            this.accesoUsuarioDao = accesoUsuarioDao;
        }

        @Override
        protected Void doInBackground(AccesoUsuario... accesoUsuarios) {
            accesoUsuarioDao.updateAccesoUsuario(accesoUsuarios[0]);
            return null;
        }
    }

    private static class EliminarAccesoUsuarioAsyncTask extends AsyncTask<AccesoUsuario,Void,Void>{
        private AccesoUsuarioDao accesoUsuarioDao;

        public EliminarAccesoUsuarioAsyncTask(AccesoUsuarioDao accesoUsuarioDao) {
            this.accesoUsuarioDao = accesoUsuarioDao;
        }

        @Override
        protected Void doInBackground(AccesoUsuario... accesoUsuarios) {
            accesoUsuarioDao.deleteAccesoUsuario(accesoUsuarios[0]);
            return null;
        }
    }

    private static class BorrarTodosAccesosAsyncTask extends AsyncTask<Void,Void,Void>{
        private AccesoUsuarioDao accesoUsuarioDao;

        public BorrarTodosAccesosAsyncTask(AccesoUsuarioDao accesoUsuarioDao) {
            this.accesoUsuarioDao = accesoUsuarioDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            accesoUsuarioDao.eliminarTodosAccesos();
            return null;
        }
    }

    private static class ObtenerAccesosUsuarioAsyncTask extends AsyncTask<Integer,Void,LiveData<List<AccesoUsuario>>>{
        private AccesoUsuarioDao accesoUsuarioDao;

        public ObtenerAccesosUsuarioAsyncTask(AccesoUsuarioDao accesoUsuarioDao) {
            this.accesoUsuarioDao = accesoUsuarioDao;
        }

        @Override
        protected LiveData<List<AccesoUsuario>> doInBackground(Integer... integers) {
            return accesoUsuarioDao.obtenerAccesosPorUsuario(integers[0]);
        }
    }

    private static class ObtenerAccesosPorNumCrudAsyncTask extends AsyncTask<Void,Void,LiveData<List<AccesoUsuario>>>{
        private AccesoUsuarioDao accesoUsuarioDao;
        private int idUsuario;
        private int numCrud;

        public ObtenerAccesosPorNumCrudAsyncTask(AccesoUsuarioDao accesoUsuarioDao, int idUsuario, int numCrud) {
            this.accesoUsuarioDao = accesoUsuarioDao;
            this.idUsuario = idUsuario;
            this.numCrud = numCrud;
        }

        @Override
        protected LiveData<List<AccesoUsuario>> doInBackground(Void... voids) {
            return accesoUsuarioDao.obtenerAccesosPorNumCrud(idUsuario,numCrud);
        }
    }

    private static class EliminarAccesosDeUsuarioAsyncTask extends AsyncTask<Integer,Void,Void>{
        private AccesoUsuarioDao accesoUsuarioDao;

        public EliminarAccesosDeUsuarioAsyncTask(AccesoUsuarioDao accesoUsuarioDao) {
            this.accesoUsuarioDao = accesoUsuarioDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            accesoUsuarioDao.eliminarAccesosDeUsuario(integers[0]);
            return null;
        }
    }
}
