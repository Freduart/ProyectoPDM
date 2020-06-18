package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.InscripcionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.db.entity.Inscripcion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision_Docente;

public class InscripcionRepository {
    private InscripcionDao inscripcionDao;
    private LiveData<List<Inscripcion>> allInscripciones;

    public InscripcionRepository(InscripcionDao inscripcionDao, LiveData<List<Inscripcion>> allInscripciones) {
        this.inscripcionDao = inscripcionDao;
        this.allInscripciones = allInscripciones;
    }

    //Parametros
    public static class ParametrosInscripcion {
        String alumno, asignatura;

        public ParametrosInscripcion(String alumno, String asignatura) {
            this.alumno = alumno;
            this.asignatura = asignatura;
        }
    }

    //Clases async
    private static class InsetarInscripcionAsyncTask extends AsyncTask<Inscripcion,Void, Void>{
        private InscripcionDao inscripcionDao;

        public InsetarInscripcionAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected Void doInBackground(Inscripcion... inscripcions) {
            inscripcionDao.insertInscripcion(inscripcions[0]);
            return null;
        }
    }

    private static class ActualizarInscripcionAsyncTask extends AsyncTask<Inscripcion,Void, Void>{
        private InscripcionDao inscripcionDao;

        public ActualizarInscripcionAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected Void doInBackground(Inscripcion... inscripcions) {
            inscripcionDao.updateInscripcion(inscripcions[0]);
            return null;
        }
    }

    private static class EliminarInscripcionAsyncTask extends AsyncTask<Inscripcion,Void, Void>{
        private InscripcionDao inscripcionDao;

        public EliminarInscripcionAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected Void doInBackground(Inscripcion... inscripcions) {
            inscripcionDao.deleteInscripcion(inscripcions[0]);
            return null;
        }
    }

    private static class EliminarTodasInscripcionesAsyncTask extends AsyncTask<Void,Void, Void>{
        private InscripcionDao inscripcionDao;

        public EliminarTodasInscripcionesAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            inscripcionDao.borrarTodasInscripciones();
            return null;
        }
    }

    private static class ObtenerInscripcionAsyncTask extends AsyncTask<ParametrosInscripcion,Void, Inscripcion> {
        private InscripcionDao inscripcionDao;

        public ObtenerInscripcionAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected Inscripcion doInBackground(ParametrosInscripcion... parametrosInscripcions) {
            return inscripcionDao.obtenerInscripcion(parametrosInscripcions[0].alumno, parametrosInscripcions[0].asignatura);
        }
    }

    private static class ObtenerAlumnosDesdeInscripcionAsyncTask extends AsyncTask<String,Void,List<Alumno>>{
        private InscripcionDao inscripcionDao;

        public ObtenerAlumnosDesdeInscripcionAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected List<Alumno> doInBackground(String... strings) {
            return inscripcionDao.getAlumnosAsignatura(strings[0]);
        }
    }

    private static class ObtenerAsignaturasDesdeInscripcionAsyncTask extends AsyncTask<String, Void, List<Asignatura>>{
        private InscripcionDao inscripcionDao;

        public ObtenerAsignaturasDesdeInscripcionAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected List<Asignatura> doInBackground(String... strings) {
            return inscripcionDao.getAsignaturasAlumno(strings[0]);
        }
    }

    private static class ObtenerEscuelaDeAsignaturaAsyncTask extends AsyncTask<Integer, Void, List<Escuela>>{
        private InscripcionDao inscripcionDao;

        public ObtenerEscuelaDeAsignaturaAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected List<Escuela> doInBackground(Integer... strings) {
            return inscripcionDao.getEscuelasDeAsignaturasEnInscripcion(strings[0]);
        }
    }

    private static class ObenterRelacionesConAlumnoAsyncTask extends AsyncTask<String, Void, List<Inscripcion>>{
        private InscripcionDao inscripcionDao;

        public ObenterRelacionesConAlumnoAsyncTask(InscripcionDao inscripcionDao) {
            this.inscripcionDao = inscripcionDao;
        }

        @Override
        protected List<Inscripcion> doInBackground(String... strings) {
            return inscripcionDao.getInscripcionCONAlumno(strings[0]);
        }
    }

    //Metodos asincronos
    public void insertarInscripcion(Inscripcion inscripcion){
        new InsetarInscripcionAsyncTask(inscripcionDao).execute(inscripcion);
    }

    public void actualizarInscripcion(Inscripcion inscripcion){
        new ActualizarInscripcionAsyncTask(inscripcionDao).execute(inscripcion);
    }

    public void eliminarInscripcion(Inscripcion inscripcion){
        new EliminarInscripcionAsyncTask(inscripcionDao).execute(inscripcion);
    }

    public void EliminarTodasInscripciones(){
        new EliminarTodasInscripcionesAsyncTask(inscripcionDao).execute();
    }

    public Inscripcion obtenerRelacionInscripcion(ParametrosInscripcion parametrosInscripcion) throws InterruptedException, ExecutionException, TimeoutException{

        return new ObtenerInscripcionAsyncTask(inscripcionDao).execute(parametrosInscripcion).get(12, TimeUnit.SECONDS);
    }

    public List<Alumno> obtenerAlumnosConInscripcion(String id)throws Exception{
        return new ObtenerAlumnosDesdeInscripcionAsyncTask(inscripcionDao).execute(id).get(12,TimeUnit.SECONDS);
    }

    public List<Asignatura> obtenerAsignaturasConInscripcion(String id) throws Exception{
        return new ObtenerAsignaturasDesdeInscripcionAsyncTask(inscripcionDao).execute(id).get(12,TimeUnit.SECONDS);
    }

    public LiveData<List<Inscripcion>> obtenerTodasRelacionesInscripcion(){
        return allInscripciones;
    }

    public List<Escuela>obtenerEscuelasDeAsignaturaEnInscripcion(int id) throws Exception{
        return new ObtenerEscuelaDeAsignaturaAsyncTask(inscripcionDao).execute(id).get(12,TimeUnit.SECONDS);
    }

    public List<Inscripcion> obtenerRealcionesUsandoAlumno(String carnet) throws Exception{
        return new ObenterRelacionesConAlumnoAsyncTask(inscripcionDao).execute(carnet).get(12,TimeUnit.SECONDS);
    }
}
