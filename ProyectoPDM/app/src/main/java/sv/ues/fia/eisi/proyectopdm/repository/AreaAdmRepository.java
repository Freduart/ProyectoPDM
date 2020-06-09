package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.AreaAdmDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;

public class AreaAdmRepository {
    //atributos
    private AreaAdmDao areaAdmDao;
    private LiveData<List<AreaAdm>> todasAreasAdm;

    //constructor
    public AreaAdmRepository(Application application) {
        DataBase base = DataBase.getInstance(application);
        areaAdmDao = base.areaAdmDao();
        todasAreasAdm = areaAdmDao.obtenerAreas();
    }

    //insertar asíncrono
    public void insertAreaAdm(AreaAdm areaAdm){
        new AreaAdmRepository.InsertarAreaAdmAsyncTask(areaAdmDao).execute(areaAdm);
    }

    //actualizar asíncrono
    public void actualizarAreaAdm(AreaAdm areaAdm){
        new AreaAdmRepository.actualizarAreaAdmAsyncTask(areaAdmDao).execute(areaAdm);
    }

    public void borrarAreaAdm(AreaAdm areaAdm){
        new AreaAdmRepository.BorrarAreaAdmAsyncTask(areaAdmDao).execute(areaAdm);
    }

    //borrar todas asíncrono
    public void borrarTodasAreasAdm(){
        new AreaAdmRepository.DeleteAllAreaAdmAsyncTask(areaAdmDao).execute();
    }

    //obtener evaluacion asíncrono
    public AreaAdm obtenerArea(Integer integer) throws InterruptedException, ExecutionException, TimeoutException {
        return new AreaAdmRepository.obtenerAreaAdmAsyncTask(areaAdmDao).execute(integer).get(12, TimeUnit.SECONDS);
    }

    //obtener todas
    public LiveData<List<AreaAdm>> obtenerAreasAdm() {
        return todasAreasAdm;
    }



    //Async de insertar
    private static class InsertarAreaAdmAsyncTask extends AsyncTask<AreaAdm, Void, Void> {
        private AreaAdmDao areaAdmDao;

        private InsertarAreaAdmAsyncTask(AreaAdmDao areaAdmDao){
            this.areaAdmDao=areaAdmDao;
        }

        @Override
        protected Void doInBackground(AreaAdm... areasAdm) {
            areaAdmDao.insertAreaAdm(areasAdm[0]);
            return null;
        }
    }

    //async de actualizar
    private static class actualizarAreaAdmAsyncTask extends AsyncTask<AreaAdm, Void, Void>{
        private AreaAdmDao areaAdmDao;

        private actualizarAreaAdmAsyncTask(AreaAdmDao areaAdmDao){
            this.areaAdmDao=areaAdmDao;
        }

        @Override
        protected Void doInBackground(AreaAdm... areasAdm) {
            areaAdmDao.updateAreaAdm(areasAdm[0]);
            return null;
        }
    }

    //async de Borrar
    private static class BorrarAreaAdmAsyncTask extends AsyncTask<AreaAdm, Void, Void>{
        private AreaAdmDao areaAdmDao;

        private BorrarAreaAdmAsyncTask(AreaAdmDao areaAdmDao){
            this.areaAdmDao=areaAdmDao;
        }

        @Override
        protected Void doInBackground(AreaAdm... areasAdm) {
            areaAdmDao.deleteAreaAdm(areasAdm[0]);
            return null;
        }
    }

    //Async de borrar todos
    private static class DeleteAllAreaAdmAsyncTask extends AsyncTask<Void, Void, Void> {
        private AreaAdmDao areaAdmDao;

        private DeleteAllAreaAdmAsyncTask(AreaAdmDao areaAdmDao) {
            this.areaAdmDao = areaAdmDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            areaAdmDao.borrarAreas();
            return null;
        }
    }

    //async obtener evaluacion
    private static class obtenerAreaAdmAsyncTask extends AsyncTask<Integer, Void, AreaAdm>{
        private AreaAdmDao areaAdmDao;

        private obtenerAreaAdmAsyncTask(AreaAdmDao areaAdmDao){
            this.areaAdmDao=areaAdmDao;
        }

        @Override
        protected AreaAdm doInBackground(Integer... areas) {
            return areaAdmDao.obtenerAreaAdm(areas[0]);
        }
    }
}
