package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
}
