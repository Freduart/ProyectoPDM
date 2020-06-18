package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;
import sv.ues.fia.eisi.proyectopdm.repository.EvaluacionRepository;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionViewModel extends AndroidViewModel{

    //atributos
    private EvaluacionRepository repo;
    private LiveData<List<Evaluacion>> todasEval;

    //constructor
    public EvaluacionViewModel(@NonNull Application application) {
        super(application);
        repo=new EvaluacionRepository(application);
        todasEval=repo.getTodasEvaluaciones();
    }

    //acciones crud
    public void insertEval(Evaluacion evaluacion){
        repo.insertarEvaluacion(evaluacion);
    }

    public void updateEval(Evaluacion evaluacion){
        repo.actualizarEvaluacion(evaluacion);
    }

    public void deleteEval(Evaluacion evaluacion){
        repo.borrarEvaluacion(evaluacion);
    }

    public void deleteEvalAll(){
        repo.borrarTodasEvaluaciones();
    }

    public LiveData<List<Evaluacion>> getEvalAll() {
        return todasEval;
    }

    public Evaluacion getEval(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return repo.obtenerEvaluacion(id);
    }

    public Docente getDocentesEvaluacion(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return repo.obtenerDocentes(id);
    }

    public Asignatura getAsignaturaEvaluacion(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return repo.obtenerAsignatura(id);
    }

    public TipoEvaluacion getTiposEval(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return repo.obtenerTipos(id);
    }

    public LiveData<List<Evaluacion>> obtenerEvaluacionesDocente(String carnet) {
        return repo.getEvaluacionesDocente(carnet);
    }
    public LiveData<List<Evaluacion>> obtenerEvaluacionesAlumno(String carnet) {
        return repo.getEvaluacionesEstudiante(carnet);
    }

    public Alumno getAlumnConUsuario(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return repo.obtenerAlumnoConUsuario(id);
    }

    public LiveData<List<Alumno>> getAlumnosDesdeAsignatura(String codigo) {
        return repo.getAlumnosDesdeAsignatura(codigo);
    }

    public LiveData<List<Asignatura>> obtenerAsignaturasPorEscuela(int id){
        return repo.obtenerAsignaturasPorEscuela(id);
    }

    public Docente getDocenteConUsuario(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return repo.obtenerDocenteConUsuario(id);
    }

    public Escuela getEscuelaDeDocente(String id) throws InterruptedException, ExecutionException, TimeoutException {
        return repo.obtenerEscuelaDeDocente(id);
    }

    public LiveData<List<Docente>> obtenerDocentesPorEscuela(int id) {
        return repo.obtenerDocentesDeEscuela(id);
    }
}
