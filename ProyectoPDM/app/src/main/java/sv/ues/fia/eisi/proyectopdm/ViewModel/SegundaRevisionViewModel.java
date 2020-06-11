package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.repository.SegundaRevisionRepository;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;

public class SegundaRevisionViewModel extends AndroidViewModel{

    //atributos
    private SegundaRevisionRepository repo;
    private LiveData<List<SegundaRevision>> todasEval;

    //constructor
    public SegundaRevisionViewModel(@NonNull Application application) {
        super(application);
        repo=new SegundaRevisionRepository(application);
        todasEval=repo.getTodasSegundaRevisiones();
    }

    //acciones crud
    public void insertSegundaRevision(SegundaRevision segundaRevision){
        repo.insertarSegundaRevision(segundaRevision);
    }

    public void updateSegundaRevision(SegundaRevision segundaRevision){
        repo.actualizarSegundaRevision(segundaRevision);
    }

    public void deleteSegundaRevision(SegundaRevision segundaRevision){
        repo.borrarSegundaRevision(segundaRevision);
    }

    public void deleteSegundaRevisionAll(){
        repo.borrarTodasSegundaRevisiones();
    }

    public LiveData<List<SegundaRevision>> getSegundaRevisionAll() {
        return todasEval;
    }

    public SegundaRevision getSegundaRevision(String id) throws InterruptedException, ExecutionException, TimeoutException {
        return repo.obtenerSegundaRevision(id);
    }
}