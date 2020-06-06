package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.EvaluacionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionRepository {
    //atributos
    private EvaluacionDao evaluacionDao;
    private LiveData<List<Evaluacion>> todasEvaluaciones;

    //constructor
    public EvaluacionRepository(Application application) {
        DataBase base = DataBase.getInstance(application);
        evaluacionDao = base.evaluacionDao();
        todasEvaluaciones = evaluacionDao.obtenerEvaluaciones();
    }

    //insertar asíncrono
    public void insertarEvaluacion(Evaluacion evaluacion){
        new InsertarEvaluacionAsyncTask(evaluacionDao).execute(evaluacion);
    }

    //actualizar asíncrono
    public void actualizarEvaluacion(Evaluacion evaluacion){
        new actualizarEvaluacionAsyncTask(evaluacionDao).execute(evaluacion);
    }

    public void borrarEvaluacion(Evaluacion evaluacion){
        new BorrarEvaluacionAsyncTask(evaluacionDao).execute(evaluacion);
    }

    //borrar todas asíncrono
    public void borrarTodasEvaluaciones(){
        new DeleteAllEvaluacionesAsyncTask(evaluacionDao).execute();
    }

    //obtener todas
    public LiveData<List<Evaluacion>> getTodasEvaluaciones() {
        return todasEvaluaciones;
    }

    //obtener
    public Evaluacion getEvaluacion(int id){
        return evaluacionDao.obtenerEvaluacion(id);
    }

    //Async de insertar
    private static class InsertarEvaluacionAsyncTask extends AsyncTask<Evaluacion, Void, Void>{
        private EvaluacionDao evaluacionDao;

        private InsertarEvaluacionAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Void doInBackground(Evaluacion... evaluaciones) {
            evaluacionDao.insertEvaluacion(evaluaciones[0]);
            return null;
        }
    }

    //async de actualizar
    private static class actualizarEvaluacionAsyncTask extends AsyncTask<Evaluacion, Void, Void>{
        private EvaluacionDao evaluacionDao;

        private actualizarEvaluacionAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Void doInBackground(Evaluacion... evaluaciones) {
            evaluacionDao.updateEvaluacion(evaluaciones[0]);
            return null;
        }
    }

    //async de Borrar
    private static class BorrarEvaluacionAsyncTask extends AsyncTask<Evaluacion, Void, Void>{
        private EvaluacionDao evaluacionDao;

        private BorrarEvaluacionAsyncTask(EvaluacionDao evaluacionDao){
            this.evaluacionDao=evaluacionDao;
        }

        @Override
        protected Void doInBackground(Evaluacion... evaluaciones) {
            evaluacionDao.deleteEvaluacion(evaluaciones[0]);
            return null;
        }
    }

    //Async de borrar todos
    private static class DeleteAllEvaluacionesAsyncTask extends AsyncTask<Void, Void, Void> {
        private EvaluacionDao evaluacionDao;

        private DeleteAllEvaluacionesAsyncTask(EvaluacionDao evaluacionDao) {
            this.evaluacionDao = evaluacionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            evaluacionDao.obtenerEvaluaciones();
            return null;
        }
    }
}
