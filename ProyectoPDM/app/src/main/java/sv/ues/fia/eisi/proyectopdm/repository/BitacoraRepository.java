package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.BitacoraDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Bitacora;

public class BitacoraRepository {

    private BitacoraDao bitacoraDao;

    public BitacoraRepository(Application application){
        DataBase base = DataBase.getInstance(application);
        bitacoraDao = base.bitacoraDao();
    }

    public void insertarBitacora(Bitacora bitacora){
        new InsertBitacoraAsyncTask(bitacoraDao).execute(bitacora);
    }

    public List<Bitacora> obtenerBitacoraFecha() throws InterruptedException, ExecutionException, TimeoutException {
        return new ObtenerBitacoraPorFechaAsyncTask(bitacoraDao).execute().get(12, TimeUnit.SECONDS);
    }

    private static class InsertBitacoraAsyncTask extends AsyncTask<Bitacora,Void,Void>{
        private BitacoraDao bitacoraDao;

        public InsertBitacoraAsyncTask(BitacoraDao bitacoraDao) {
            this.bitacoraDao = bitacoraDao;
        }

        @Override
        protected Void doInBackground(Bitacora... bitacoras) {
            bitacoraDao.insertBitacora(bitacoras[0]);
            return null;
        }
    }

    private static class ObtenerBitacoraPorFechaAsyncTask extends AsyncTask<Void,Void,List<Bitacora>>{
        private BitacoraDao bitacoraDao;

        public ObtenerBitacoraPorFechaAsyncTask(BitacoraDao bitacoraDao) {
            this.bitacoraDao = bitacoraDao;
        }

        @Override
        protected List<Bitacora> doInBackground(Void... voids) {
            return bitacoraDao.obtenerBitacoras();
        }
    }

}
