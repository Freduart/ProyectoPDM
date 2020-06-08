package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.DocenteDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;

public class DocenteRepository {
    //atributos
    private DocenteDao docenteDao;
    private LiveData<List<Docente>> todosDocentes;

    //constructor
    public DocenteRepository(Application application) {
        DataBase base = DataBase.getInstance(application);
        docenteDao = base.docenteDao();
        todosDocentes = docenteDao.obtenerDocentes();
    }

    //insertar asíncrono
    public void insertarDocente(Docente docente){
        new InsertarDocenteAsyncTask(docenteDao).execute(docente);
    }

    //actualizar asíncrono
    public void actualizarDocente(Docente docente){
        new actualizarDocenteAsyncTask(docenteDao).execute(docente);
    }

    public void borrarDocente(Docente docente){
        new BorrarDocenteAsyncTask(docenteDao).execute(docente);
    }

    //borrar todas asíncrono
    public void borrarTodasDocentees(){
        new DeleteAllDocentesAsyncTask(docenteDao).execute();
    }

    //obtener todas
    public LiveData<List<Docente>> getTodosDocentes() {
        return todosDocentes;
    }

    //obtener
    public Docente getDocente(String id){
        return docenteDao.obtenerDocente(id);
    }

    //Async de insertar
    private static class InsertarDocenteAsyncTask extends AsyncTask<Docente, Void, Void>{
        private DocenteDao docenteDao;

        private InsertarDocenteAsyncTask(DocenteDao docenteDao){
            this.docenteDao=docenteDao;
        }

        @Override
        protected Void doInBackground(Docente... docentees) {
            docenteDao.insertDocente(docentees[0]);
            return null;
        }
    }

    //async de actualizar
    private static class actualizarDocenteAsyncTask extends AsyncTask<Docente, Void, Void> {
        private DocenteDao docenteDao;

        private actualizarDocenteAsyncTask(DocenteDao docenteDao){
            this.docenteDao=docenteDao;
        }

        @Override
        protected Void doInBackground(Docente... docentees) {
            docenteDao.updateDocente(docentees[0]);
            return null;
        }
    }

    //async de Borrar
    private static class BorrarDocenteAsyncTask extends AsyncTask<Docente, Void, Void>{
        private DocenteDao docenteDao;

        private BorrarDocenteAsyncTask(DocenteDao docenteDao){
            this.docenteDao=docenteDao;
        }

        @Override
        protected Void doInBackground(Docente... docentees) {
            docenteDao.deleteDocente(docentees[0]);
            return null;
        }
    }

    //Async de borrar todos
    private static class DeleteAllDocentesAsyncTask extends AsyncTask<Void, Void, Void> {
        private DocenteDao docenteDao;

        private DeleteAllDocentesAsyncTask(DocenteDao docenteDao) {
            this.docenteDao = docenteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            docenteDao.borrarDocentes();
            return null;
        }
    }
}
