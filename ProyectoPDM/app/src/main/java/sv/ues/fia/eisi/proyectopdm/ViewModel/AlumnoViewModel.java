package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.repository.AlumnoRepository;





public class AlumnoViewModel extends AndroidViewModel {

    private AlumnoRepository repository;
    private LiveData<List<Alumno>> allAlumnos;

    public AlumnoViewModel(@NonNull Application application) {
        super(application);
        repository = new AlumnoRepository(application);
        allAlumnos = repository.getAllAlumnos();
    }


    public void insert(Alumno alumno){
        //Enlace con la clase dao
        repository.insertar(alumno);
    }

    public void update(Alumno alumno){
        repository.actualizar(alumno);
    }

    public void delete(Alumno alumno){
        repository.eliminar(alumno);
    }

    public void deleteAllEscuelas() {
        repository.eliminarTodos();
    }

    public LiveData<List<Alumno>> getAllAlumnos() {
        return allAlumnos;
    }

    public Alumno getAlumn(String carnet) throws InterruptedException, ExecutionException, TimeoutException {
        return repository.obtenerAlumno(carnet);
    }
}
