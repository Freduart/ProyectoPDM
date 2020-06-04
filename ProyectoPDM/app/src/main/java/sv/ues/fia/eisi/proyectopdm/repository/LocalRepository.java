package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.LocalDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class LocalRepository {
    private LocalDao localDao;
    private LiveData<List<Local>> allLocales;

    public LocalRepository(Application application){
        DataBase dataBase = DataBase.getInstance(application);
        localDao = dataBase.localDao();
        allLocales = localDao.obtenerLocales();
    }

    public void insertar(Local local){
        new InsertarLocalAsyncTask(localDao).execute(local);
    }

    public void actualizar(Local local){
        new actualizarLocalAsyncTask(localDao).execute(local);
    }

    public void borrar(Local local){
        new BorrarLocalAsyncTask(localDao).execute(local);
    }

    public void borrarTodos(){
        new DeleteAllLocalesAsyncTask(localDao).execute();
    }

    public LiveData<List<Local>> getAllLocales() {
        return allLocales;
    }

    private static class InsertarLocalAsyncTask extends AsyncTask<Local, Void, Void>{
        private LocalDao localDao;

        private InsertarLocalAsyncTask(LocalDao localDao){
            this.localDao=localDao;
        }

        @Override
        protected Void doInBackground(Local... locales) {
            localDao.insertarLocal(locales[0]);
            return null;
        }
    }

    private static class actualizarLocalAsyncTask extends AsyncTask<Local, Void, Void>{
        private LocalDao localDao;

        private actualizarLocalAsyncTask(LocalDao localDao){
            this.localDao=localDao;
        }

        @Override
        protected Void doInBackground(Local... locales) {
            localDao.updateLocal(locales[0]);
            return null;
        }
    }

    private static class BorrarLocalAsyncTask extends AsyncTask<Local, Void, Void>{
        private LocalDao localDao;

        private BorrarLocalAsyncTask(LocalDao localDao){
            this.localDao=localDao;
        }

        @Override
        protected Void doInBackground(Local... locales) {
            localDao.deleteLocal(locales[0]);
            return null;
        }
    }

    private static class DeleteAllLocalesAsyncTask extends AsyncTask<Void, Void, Void>{
        private LocalDao localDao;

        private DeleteAllLocalesAsyncTask(LocalDao localDao){
            this.localDao=localDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            localDao.borrarLocales();
            return null;
        }
    }
}
