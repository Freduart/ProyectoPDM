package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.db.entity.Inscripcion;
import sv.ues.fia.eisi.proyectopdm.repository.InscripcionRepository;

public class InscripcionViewModel extends AndroidViewModel {
    private InscripcionRepository repository;
    private LiveData<List<Inscripcion>> allInscriptions;

    public InscripcionViewModel(@NonNull Application application) {
        super(application);
        repository=new InscripcionRepository(application);
        allInscriptions=repository.obtenerTodasRelacionesInscripcion();
    }


    public void insertarInscripcion(Inscripcion inscripcion){
        repository.insertarInscripcion(inscripcion);
    }

    public void actualizarInscripcion(Inscripcion inscripcion){
        repository.actualizarInscripcion(inscripcion);
    }

    public void eliminarInscripcion(Inscripcion inscripcion){
        repository.eliminarInscripcion(inscripcion);
    }

    public void eliminarTodasRelacionesInscripcion(){
        repository.EliminarTodasInscripciones();
    }

    public LiveData<List<Inscripcion>> obtenerTodasRelacionesInscripcion(){
        return allInscriptions;
    }

    public List<Alumno> obtenerAlumnosUsandoAsignatura(String idInscripcion)throws Exception{
        return repository.obtenerAlumnosConInscripcion(idInscripcion);
    }

    public List<Asignatura> obtenerAsignaturasUsandoAlumno(String idInscripcion)throws Exception{
        return repository.obtenerAsignaturasConInscripcion(idInscripcion);
    }

    public Inscripcion obtenerInscripcion(String idInscripcion, String carnet) throws Exception{
        return repository.obtenerRelacionInscripcion(new InscripcionRepository.ParametrosInscripcion(idInscripcion,carnet));
    }

    public List<Escuela> obtenerEscuelasDeAsignaturaEnInscripcion(int id) throws Exception{
        return repository.obtenerEscuelasDeAsignaturaEnInscripcion(id);
    }

    public List<Inscripcion> obtenerRelacionesUsandoAlumno(String carnet) throws Exception{
        return repository.obtenerRealcionesUsandoAlumno(carnet);
    }
}
