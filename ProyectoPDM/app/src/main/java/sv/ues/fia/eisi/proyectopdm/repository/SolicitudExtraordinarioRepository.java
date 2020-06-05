package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.SolicitudExtraordinarioDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;

public class SolicitudExtraordinarioRepository {

    private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
    private LiveData<List<SolicitudExtraordinario>> allSolicitudesExtraordinario;

    public SolicitudExtraordinarioRepository(Application application){
        DataBase dataBase=DataBase.getInstance(application);
        solicitudExtraordinarioDao=dataBase.solicitudExtraordinarioDao();
        allSolicitudesExtraordinario=solicitudExtraordinarioDao.obtenerSolicitudesExtraordinario();
    }

    public void insertar(SolicitudExtraordinario solicitudExtraordinario){
        new InsertarSolicitudExtraordinarioAsyncTask(solicitudExtraordinarioDao).execute(solicitudExtraordinario);
    }

    public void actualizar(SolicitudExtraordinario solicitudExtraordinario){
        new ActualizarSolicitudExtraordinarioAsyncTask(solicitudExtraordinarioDao).execute(solicitudExtraordinario);
    }

    public void eliminar(SolicitudExtraordinario solicitudExtraordinario){
        new BorrarSolicitudExtraordinarioAsyncTask(solicitudExtraordinarioDao).execute(solicitudExtraordinario);
    }

    public void eliminarTodas(){
        new DeleteAllSolicitudesExtraordinarioAsyncTask(solicitudExtraordinarioDao).execute();
    }

    public LiveData<List<SolicitudExtraordinario>> getAllSolicitudesExtraordinario(){
        return allSolicitudesExtraordinario;
    }

    private static class InsertarSolicitudExtraordinarioAsyncTask extends AsyncTask<SolicitudExtraordinario,Void,Void> {
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;

        public InsertarSolicitudExtraordinarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao) {
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Void doInBackground(SolicitudExtraordinario... solicitudesExtraordinario) {
            solicitudExtraordinarioDao.insertSolicitudExtraordinario(solicitudesExtraordinario[0]);
            return null;
        }
    }

    private static class ActualizarSolicitudExtraordinarioAsyncTask extends AsyncTask<SolicitudExtraordinario,Void,Void>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;

        public ActualizarSolicitudExtraordinarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao) {
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Void doInBackground(SolicitudExtraordinario... solicitudesExtraordinario) {
            solicitudExtraordinarioDao.updateSolicitudExtraordinario(solicitudesExtraordinario[0]);
            return null;
        }
    }

    private static class BorrarSolicitudExtraordinarioAsyncTask extends AsyncTask<SolicitudExtraordinario,Void,Void>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;

        public BorrarSolicitudExtraordinarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao) {
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Void doInBackground(SolicitudExtraordinario... solicitudesExtraordinario) {
            solicitudExtraordinarioDao.deleteSolicitudExtraordinario(solicitudesExtraordinario[0]);
            return null;
        }
    }

    private static class DeleteAllSolicitudesExtraordinarioAsyncTask extends AsyncTask<Void,Void,Void>{
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;

        public DeleteAllSolicitudesExtraordinarioAsyncTask(SolicitudExtraordinarioDao solicitudExtraordinarioDao) {
            this.solicitudExtraordinarioDao = solicitudExtraordinarioDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            solicitudExtraordinarioDao.borrarSolicitudesExtraordinario();
            return null;
        }
    }
}
