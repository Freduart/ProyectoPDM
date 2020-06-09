package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.repository.EscuelaRepository;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;


/*
    AndroidViewModel es una subclase de ViewModel, aca enlazamos la vista con el Modelo
 */
public class EscuelaViewModel extends AndroidViewModel {

    private EscuelaRepository repository;
    private LiveData<List<Escuela>> allEscuelas;

    public EscuelaViewModel(@NonNull Application application) {
        super(application);
        repository=new EscuelaRepository(application);
        allEscuelas=repository.getAllEscuelas();
    }


    public void insert(Escuela escuela){
        //Enlazamos con la clase dao
        repository.insertar(escuela);
    }

    public void update(Escuela escuela){
        repository.actualizar(escuela);
    }

    public void delete(Escuela escuela){
        repository.borrar(escuela);
    }

    public void deleteAllEscuelas(){
        repository.borrarTodas();
    }

    public LiveData<List<Escuela>> getAllEscuelas(){
        return allEscuelas;
    }

    public Escuela getEscuela(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return repository.obtenerEscuela(id);
    }
}
