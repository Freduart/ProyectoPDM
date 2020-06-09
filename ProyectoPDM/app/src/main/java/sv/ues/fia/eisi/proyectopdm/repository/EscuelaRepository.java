package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.EscuelaDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
/*
    Las clases Repository se encargan de la conexion con las clases DAO para operaciones de datos
    y las transformara en operaciones de la clase AsyncTask para operaciones de datos
 */
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

    public Escuela obtenerEscuela(Integer id) throws InterruptedException, ExecutionException, TimeoutException {
        return new obtenerEscuelaAsyncTask(escuelaDao).execute(id).get(12, TimeUnit.SECONDS);
    }

    public LiveData<List<Escuela>> getAllEscuelas() {
        return allEscuelas;
    }

    /*
        Los metodos AsyncTask se enlazan con los metodos en las clases DAO necesitaremos mandarles
        1 parametros, 1 progreso y el resultado
     */


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
            //Aca enlazamos con el metodo DAO
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

    private static class obtenerEscuelaAsyncTask extends AsyncTask<Integer, Void, Escuela>{
        private  EscuelaDao escuelaDao;

        private obtenerEscuelaAsyncTask(EscuelaDao escuelaDao) {
            this.escuelaDao = escuelaDao;
        }
        @Override
        protected Escuela doInBackground(Integer... escuelas) {
            return escuelaDao.obtenerEscuela(escuelas[0]);
        }
    }
}
