package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.EscuelaDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class EscuelaRepository {
    private EscuelaDao escuelaDao;
    private LiveData<List<Escuela>> allEscuelas;

    public EscuelaRepository(Application application){
        DataBase dataBase=DataBase.getInstance(application);
        escuelaDao=dataBase.escuelaDao();
        allEscuelas=escuelaDao.obtenerEscuelas();
    }

    public void insertar(Escuela escuela){
        new InsertarEscuelaAsyncTask(escuelaDao).execute(escuela);
    }

    public void actualizar(Escuela escuela){
        new actualizarEscuelaAsyncTask(escuelaDao).execute(escuela);
    }

    public void borrar(Escuela escuela){
        new BorrarEscuelaAsyncTask(escuelaDao).execute(escuela);
    }

    public void borrarTodas(){
        new DeleteAllEscuelasAsyncTask(escuelaDao).execute();
    }

    public LiveData<List<Escuela>> getAllEscuelas() {
        return allEscuelas;
    }

    private static class InsertarEscuelaAsyncTask extends AsyncTask<Escuela, Void, Void>{
        private EscuelaDao escuelaDao;

        private InsertarEscuelaAsyncTask(EscuelaDao escuelaDao){
            this.escuelaDao=escuelaDao;
        }

        @Override
        protected Void doInBackground(Escuela... escuelas) {
            escuelaDao.insert(escuelas[0]);
            return null;
        }
    }

    private static class actualizarEscuelaAsyncTask extends AsyncTask<Escuela, Void, Void>{
        private EscuelaDao escuelaDao;

        private actualizarEscuelaAsyncTask(EscuelaDao escuelaDao){
            this.escuelaDao=escuelaDao;
        }

        @Override
        protected Void doInBackground(Escuela... escuelas) {
            escuelaDao.update(escuelas[0]);
            return null;
        }
    }

    private static class BorrarEscuelaAsyncTask extends AsyncTask<Escuela, Void, Void>{
        private EscuelaDao escuelaDao;

        private BorrarEscuelaAsyncTask(EscuelaDao escuelaDao){
            this.escuelaDao=escuelaDao;
        }

        @Override
        protected Void doInBackground(Escuela... escuelas) {
            escuelaDao.delete(escuelas[0]);
            return null;
        }
    }

    private static class DeleteAllEscuelasAsyncTask extends AsyncTask<Void, Void, Void>{
        private EscuelaDao escuelaDao;

        private DeleteAllEscuelasAsyncTask(EscuelaDao escuelaDao){
            this.escuelaDao=escuelaDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            escuelaDao.borrarEscuelas();
            return null;
        }
    }
}
