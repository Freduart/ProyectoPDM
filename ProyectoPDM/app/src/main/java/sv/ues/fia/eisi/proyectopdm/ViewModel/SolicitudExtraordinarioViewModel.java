package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;
import sv.ues.fia.eisi.proyectopdm.repository.SolicitudExtraordinarioRepository;

public class SolicitudExtraordinarioViewModel extends AndroidViewModel {

    private SolicitudExtraordinarioRepository solicitudExtraordinarioRepository;
    private LiveData<List<SolicitudExtraordinario>> allSolicitudesExtraordinario;

    public SolicitudExtraordinarioViewModel(@NonNull Application application){
        super(application);
        solicitudExtraordinarioRepository=new SolicitudExtraordinarioRepository(application);
        allSolicitudesExtraordinario=solicitudExtraordinarioRepository.getAllSolicitudesExtraordinario();
    }

    public void insert(SolicitudExtraordinario solicitudExtraordinario){
        solicitudExtraordinarioRepository.insertar(solicitudExtraordinario);
    }

    public void update(SolicitudExtraordinario solicitudExtraordinario){
        solicitudExtraordinarioRepository.actualizar(solicitudExtraordinario);
    }

    public void delete(SolicitudExtraordinario solicitudExtraordinario){
        solicitudExtraordinarioRepository.eliminar(solicitudExtraordinario);
    }

    public void deleteAllSolicitudesExtraordinario(){
        solicitudExtraordinarioRepository.eliminarTodas();
    }

    public LiveData<List<SolicitudExtraordinario>> getAllSolicitudesExtraordinario(){
        return allSolicitudesExtraordinario;
    }

    public SolicitudExtraordinario getSoliExtra(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return solicitudExtraordinarioRepository.getSoliExtra(id);
    }

    public Evaluacion getEvaluacion(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return solicitudExtraordinarioRepository.getEvaluacion(id);
    }

    public TipoEvaluacion getTipoEval(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return solicitudExtraordinarioRepository.getTipoEvaluacion(id);
    }

    public Alumno getAlumno(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return solicitudExtraordinarioRepository.getAlumno(id);
    }

    public LiveData<List<SolicitudExtraordinario>> obtenerSolicitudesDocente(String carnet) {
        return solicitudExtraordinarioRepository.getSolicitudesDocente(carnet);
    }

    public LiveData<List<SolicitudExtraordinario>> obtenerSolicitudesAlumno(String carnet) {
        return solicitudExtraordinarioRepository.getSolicitudesEstudiante(carnet);
    }

    public Alumno getAlumnConUsuario(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return solicitudExtraordinarioRepository.obtenerAlumnoConUsuario(id);
    }

    public Docente getDocenteConUsuario(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return solicitudExtraordinarioRepository.obtenerDocenteConUsuario(id);
    }
}
