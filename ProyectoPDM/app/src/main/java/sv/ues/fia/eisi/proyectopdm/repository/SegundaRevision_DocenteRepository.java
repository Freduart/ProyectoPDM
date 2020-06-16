package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.SegundaRevisionDao;
import sv.ues.fia.eisi.proyectopdm.dao.SegundaRevision_DocenteDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision_Docente;

public class SegundaRevision_DocenteRepository {
    private SegundaRevision_DocenteDao segundaRevision_DocenteDao;
    private LiveData<List<SegundaRevision_Docente>> allSegundaRevision_Docente;

    //----------CONSTRUCTOR
    public SegundaRevision_DocenteRepository(Application application) {
        DataBase base = DataBase.getInstance(application);
        segundaRevision_DocenteDao = base.segundaRevision_docenteDao();
        allSegundaRevision_Docente = segundaRevision_DocenteDao.obtenerSegundaRevision_Docente_Todo();
    }

    //----------CLASE PARAMETROS
    public static class ParametrosSR_D {
        int segunda_revision;
        String docente;

        public ParametrosSR_D(int segunda_revision, String docente) {
            this.segunda_revision = segunda_revision;
            this.docente = docente;
        }
    }

    //----------CLASES ASYNC
    private static class InsertarSegundaRevision_DocenteAsyncTask extends AsyncTask<SegundaRevision_Docente, Void, Void> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private InsertarSegundaRevision_DocenteAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected Void doInBackground(SegundaRevision_Docente... segundaRevision_docentes) {
            segundaRevision_docenteDao.insertSR_Docente(segundaRevision_docentes[0]);
            return null;
        }
    }

    private static class ActualizarSegundaRevision_DocenteAsyncTask extends AsyncTask<SegundaRevision_Docente, Void, Void> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private ActualizarSegundaRevision_DocenteAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected Void doInBackground(SegundaRevision_Docente... segundaRevision_docentes) {
            segundaRevision_docenteDao.updateSR_Docente(segundaRevision_docentes[0]);
            return null;
        }
    }

    private static class EliminarSegundaRevision_DocenteAsyncTask extends AsyncTask<SegundaRevision_Docente, Void, Void> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private EliminarSegundaRevision_DocenteAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected Void doInBackground(SegundaRevision_Docente... segundaRevision_docentes) {
            segundaRevision_docenteDao.deleteSR_Docente(segundaRevision_docentes[0]);
            return null;
        }
    }

    private static class EliminarTodasSegundaRevision_DocenteAsyncTask extends AsyncTask<Void, Void, Void> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private EliminarTodasSegundaRevision_DocenteAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            segundaRevision_docenteDao.deleteAllSR_Docente();
            return null;
        }
    }

    private static class ObtenerSegundaRevision_DocenteAsyncTask extends AsyncTask<ParametrosSR_D, Void, SegundaRevision_Docente> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private ObtenerSegundaRevision_DocenteAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected SegundaRevision_Docente doInBackground(ParametrosSR_D... parametrosSR_ds) {
            return segundaRevision_docenteDao.obtenerSegRev_Docente(parametrosSR_ds[0].segunda_revision,parametrosSR_ds[0].docente);
        }
    }

    private static class ObtenerDocentesDesdeSegundaRevisionAsyncTask extends AsyncTask<Integer, Void, List<Docente>> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private ObtenerDocentesDesdeSegundaRevisionAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected List<Docente> doInBackground(Integer... integers) {
            return segundaRevision_docenteDao.getDocentes(integers[0]);
        }
    }

    private static class ObtenerSegundasRevisionesDesdeDocenteAsyncTask extends AsyncTask<String, Void, List<SegundaRevision>> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private ObtenerSegundasRevisionesDesdeDocenteAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected List<SegundaRevision> doInBackground(String... strings) {
            return segundaRevision_docenteDao.getSegundaRevisions(strings[0]);
        }
    }

    private static class ObtenerCargosDeDocentesEnSRAsyncTask extends AsyncTask<String, Void, List<Cargo>> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private ObtenerCargosDeDocentesEnSRAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected List<Cargo> doInBackground(String... strings) {
            return segundaRevision_docenteDao.getCargosDeDocentesEnSR(strings[0]);
        }
    }

    private static class ObtenerRelacionesConDocenteAsyncTask extends AsyncTask<String, Void, List<SegundaRevision_Docente>> {
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;

        private ObtenerRelacionesConDocenteAsyncTask(SegundaRevision_DocenteDao segundaRevision_docenteDao){
            this.segundaRevision_docenteDao=segundaRevision_docenteDao;
        }

        @Override
        protected List<SegundaRevision_Docente> doInBackground(String... strings) {
            return segundaRevision_docenteDao.getSR_DconDoc(strings[0]);
        }
    }

    //----------MÉTODOS ASÍNCRONOS
    public void insertarSegundaRevision_Docente(SegundaRevision_Docente segundaRevision){
        new InsertarSegundaRevision_DocenteAsyncTask(segundaRevision_DocenteDao).execute(segundaRevision);
    }

    public void actualizarSegundaRevision_Docente(SegundaRevision_Docente segundaRevision){
        new ActualizarSegundaRevision_DocenteAsyncTask(segundaRevision_DocenteDao).execute(segundaRevision);
    }

    public void eliminarSegundaRevision_Docente(SegundaRevision_Docente segundaRevision){
        new EliminarSegundaRevision_DocenteAsyncTask(segundaRevision_DocenteDao).execute(segundaRevision);
    }

    public void eliminarTodasRelacionesSR_D(){
        new EliminarTodasSegundaRevision_DocenteAsyncTask(segundaRevision_DocenteDao).execute();
    }

    public SegundaRevision_Docente obtenerRelacionSegundaRevision_Docente(ParametrosSR_D parametrosSR_d) throws InterruptedException, ExecutionException, TimeoutException {
        return new ObtenerSegundaRevision_DocenteAsyncTask(segundaRevision_DocenteDao).execute(parametrosSR_d).get(12, TimeUnit.SECONDS);
    }

    public List<Docente> obtenerDocentesConSR(int id) throws Exception{
        return new ObtenerDocentesDesdeSegundaRevisionAsyncTask(segundaRevision_DocenteDao).execute(id).get(12,TimeUnit.SECONDS);
    }

    public List<SegundaRevision> obtenerSegundasRevisionesConDocente(String carnet) throws Exception{
        return new ObtenerSegundasRevisionesDesdeDocenteAsyncTask(segundaRevision_DocenteDao).execute(carnet).get(12,TimeUnit.SECONDS);
    }

    public LiveData<List<SegundaRevision_Docente>> obtenerTodasRelacionesSR_D() {
        return allSegundaRevision_Docente;
    }

    public List<Cargo> obtenerCargosDeDocentesEnSegundaRevision(String carnet) throws Exception{
        return new ObtenerCargosDeDocentesEnSRAsyncTask(segundaRevision_DocenteDao).execute(carnet).get(12,TimeUnit.SECONDS);
    }

    public LiveData<List<SegundaRevision_Docente>> obtenerRelacionesConSR(int id) {
        return segundaRevision_DocenteDao.obtenerSegundaRevision_DocenteConSR(id);
    }

    public List<SegundaRevision_Docente> obtenerRelacionesUsandoDocente(String carnet) throws Exception{
        return new ObtenerRelacionesConDocenteAsyncTask(segundaRevision_DocenteDao).execute(carnet).get(12,TimeUnit.SECONDS);
    }
}
