package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;
import sv.ues.fia.eisi.proyectopdm.repository.TipoEvaluacionRepository;

public class TipoEvaluacionViewModel extends AndroidViewModel {
    //atributos
    private TipoEvaluacionRepository repo;
    private LiveData<List<TipoEvaluacion>> todosTipoEvaluacions;

    //constructor
    public TipoEvaluacionViewModel(@NonNull Application application) {
        super(application);
        repo=new TipoEvaluacionRepository(application);
        todosTipoEvaluacions=repo.getTodosTipoEvaluaciones();
    }

    //acciones crud
    public void insertTipoEvaluacion(TipoEvaluacion tipoEvaluacion){
        repo.insertarTipoEvaluacion(tipoEvaluacion);
    }

    public void updateTipoEvaluacion(TipoEvaluacion tipoEvaluacion){
        repo.actualizarTipoEvaluacion(tipoEvaluacion);
    }

    public void deleteTipoEvaluacion(TipoEvaluacion tipoEvaluacion){
        repo.borrarTipoEvaluacion(tipoEvaluacion);
    }

    public void deleteTodosTiposEvaluaciones(){
        repo.borrarTodasTipoEvaluaciones();
    }

    public LiveData<List<TipoEvaluacion>> getTodosTiposEvaluaciones() {
        return todosTipoEvaluacions;
    }

    public TipoEvaluacion getTipoEvaluacion(int id){
        return repo.getTipoEvaluacion(id);
    }
}
