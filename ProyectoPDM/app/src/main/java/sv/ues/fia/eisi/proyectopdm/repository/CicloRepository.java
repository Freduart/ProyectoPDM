package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.CicloDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;

public class CicloRepository {
    //Atributos de Clase
    private CicloDao cicloDao;
    private LiveData<List<Ciclo>> allCiclos;

    //Constructor
    public CicloRepository(Application application){
        DataBase dataBase = DataBase.getInstance(application);
        cicloDao = dataBase.cicloDao();
        allCiclos = cicloDao.obtenerCiclos();
    }

    //Insertar de forma asíncrona
    public void insertarCiclo(Ciclo ciclo){
        new InsertarCicloAsyncTask(cicloDao).execute(ciclo);
    }

    //Actualizar de forma asíncrona
    public void actualizarCiclo(Ciclo ciclo){
        new ActualizarCicloAsyncTask(cicloDao).execute(ciclo);
    }

    //Borrar un ciclo de forma asíncrona
    public void borrarCiclo(Ciclo ciclo){
        new BorrarCicloAsyncTask(cicloDao).execute(ciclo);
    }

    //Borrar todos los ciclos de forma asíncrona
    public void borrarTodosCiclos(){
        new DeleteAllCiclosAsyncTask(cicloDao).execute();
    }

    //Obtener todos los ciclos
    public LiveData<List<Ciclo>> getAllCiclos(){
        return allCiclos;
    }

    //Obtener un ciclo
    public Ciclo getCiclo(int id){
        return cicloDao.obtenerCiclo(id);
    }


    //Clase Asíncrona de Insertar
    private static class InsertarCicloAsyncTask extends AsyncTask<Ciclo, Void, Void> {
        private CicloDao cicloDao;

        private InsertarCicloAsyncTask(CicloDao cicloDao){
            this.cicloDao=cicloDao;
        }

        @Override
        protected Void doInBackground(Ciclo... ciclos) {
            cicloDao.insertCiclo(ciclos[0]);
            return null;
        }
    }

    //Clase Asíncrona de Actualizar
    private static class ActualizarCicloAsyncTask extends AsyncTask<Ciclo, Void, Void>{
        private CicloDao cicloDao;

        private ActualizarCicloAsyncTask(CicloDao cicloDao){
            this.cicloDao=cicloDao;
        }

        @Override
        protected Void doInBackground(Ciclo... ciclos) {
            cicloDao.updateCiclo(ciclos[0]);
            return null;
        }
    }

    //Clase Asíncrona de BorrarLocal
    private static class BorrarCicloAsyncTask extends AsyncTask<Ciclo, Void, Void>{
        private CicloDao cicloDao;

        private BorrarCicloAsyncTask(CicloDao cicloDao){
            this.cicloDao=cicloDao;
        }

        @Override
        protected Void doInBackground(Ciclo... ciclos) {
            cicloDao.deleteCiclo(ciclos[0]);
            return null;
        }
    }

    //Clase Asíncrona de Borrar Todos
    private static class DeleteAllCiclosAsyncTask extends AsyncTask<Void, Void, Void>{
        private CicloDao cicloDao;

        private DeleteAllCiclosAsyncTask(CicloDao cicloDao){
            this.cicloDao=cicloDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cicloDao.borrarCiclos();
            return null;
        }
    }
}
