package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.AsignaturaDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;

public class AsignaturaRepository {
    private AsignaturaDao asignaturaDao;
    private LiveData<List<Asignatura>> allAsignaturas;

    public AsignaturaRepository(Application application){
        DataBase dataBase=DataBase.getInstance(application);
        asignaturaDao=dataBase.asignaturaDao();
        allAsignaturas=asignaturaDao.obtenerAsignaturas();
    }

    public void insertar(Asignatura asignatura){
        new InsertarAsignaturaAsyncTask(asignaturaDao).execute(asignatura);
    }

    public void actualizar(Asignatura asignatura){
        new ActualizarAsignaturasAsyncTask(asignaturaDao).execute(asignatura);
    }

    public void eliminar(Asignatura asignatura){
        new BorrarAsignaturaAsyncTask(asignaturaDao).execute(asignatura);
    }

    public void eliminarTodas(){
        new DeleteAllAsignatirasAsyncTask(asignaturaDao).execute();
    }

    public LiveData<List<Asignatura>> getAllAsignaturas(){
        return allAsignaturas;
    }

    public Asignatura obtenerAsignatura(String id){
        return asignaturaDao.obtenerAsignatura(id);
    }

    private static class InsertarAsignaturaAsyncTask extends AsyncTask<Asignatura,Void,Void>{
        private AsignaturaDao asignaturaDao;

        public InsertarAsignaturaAsyncTask(AsignaturaDao asignaturaDao) {
            this.asignaturaDao = asignaturaDao;
        }

        @Override
        protected Void doInBackground(Asignatura... asignaturas) {
            asignaturaDao.insertAsignatura(asignaturas[0]);
            return null;
        }
    }

    private static class ActualizarAsignaturasAsyncTask extends AsyncTask<Asignatura,Void,Void>{
        private AsignaturaDao asignaturaDao;

        public ActualizarAsignaturasAsyncTask(AsignaturaDao asignaturaDao) {
            this.asignaturaDao = asignaturaDao;
        }

        @Override
        protected Void doInBackground(Asignatura... asignaturas) {
            asignaturaDao.updateAsignatura(asignaturas[0]);
            return null;
        }
    }

    private static class BorrarAsignaturaAsyncTask extends AsyncTask<Asignatura,Void,Void>{
        private AsignaturaDao asignaturaDao;

        public BorrarAsignaturaAsyncTask(AsignaturaDao asignaturaDao) {
            this.asignaturaDao = asignaturaDao;
        }

        @Override
        protected Void doInBackground(Asignatura... asignaturas) {
            asignaturaDao.borrarAsignatura(asignaturas[0]);
            return null;
        }
    }

    private static class DeleteAllAsignatirasAsyncTask extends AsyncTask<Void,Void,Void>{
        private AsignaturaDao asignaturaDao;

        public DeleteAllAsignatirasAsyncTask(AsignaturaDao asignaturaDao) {
            this.asignaturaDao = asignaturaDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asignaturaDao.borrarAsignaturas();
            return null;
        }
    }
}
