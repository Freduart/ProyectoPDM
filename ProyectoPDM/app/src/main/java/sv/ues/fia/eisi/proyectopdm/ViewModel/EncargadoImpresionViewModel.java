package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;
import sv.ues.fia.eisi.proyectopdm.repository.EncargadoImpresionRepository;

public class EncargadoImpresionViewModel extends AndroidViewModel {
    private EncargadoImpresionRepository encargadoImpresionRepository;
    private LiveData<List<EncargadoImpresion>> allEncargadoImpresion;

    public EncargadoImpresionViewModel(@NonNull Application application) {
        super(application);
        encargadoImpresionRepository=new EncargadoImpresionRepository(application);
        allEncargadoImpresion=encargadoImpresionRepository.getAllEncargadoImpresion();
    }

    public void insert(EncargadoImpresion encargadoImpresion){
        encargadoImpresionRepository.insertar(encargadoImpresion);
    }

    public void update(EncargadoImpresion encargadoImpresion){
        encargadoImpresionRepository.actualizar(encargadoImpresion);
    }

    public void delete(EncargadoImpresion encargadoImpresion) {
        encargadoImpresionRepository.eliminar(encargadoImpresion);
    }

    public void deleteAllEncargadoImpresion(){
        encargadoImpresionRepository.eliminarTodos();
    }

    public LiveData<List<EncargadoImpresion>> getAllEncargadoImpresion() {
        return allEncargadoImpresion;
    }

    public EncargadoImpresion ObtenerEncargadoImpresion(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return encargadoImpresionRepository.ObtenerEncargadoImpresion(id);
    }

}
