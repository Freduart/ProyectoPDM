package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.OpcionCrud;
import sv.ues.fia.eisi.proyectopdm.repository.OpcionCrudRepository;

public class OpcionCrudViewModel extends AndroidViewModel {

    private OpcionCrudRepository opcionCrudRepository;
    private LiveData<List<OpcionCrud>> todasOpciones;

    public OpcionCrudViewModel(@NonNull Application application) {
        super(application);
        opcionCrudRepository=new OpcionCrudRepository(application);
        todasOpciones=opcionCrudRepository.getTodasOpciones();
    }

    public void insertOpcionCrud(OpcionCrud opcionCrud){
        opcionCrudRepository.insertOpcionCrud(opcionCrud);
    }

    public void updateOpcionCrud(OpcionCrud opcionCrud){
        opcionCrudRepository.updateOpcionCrud(opcionCrud);
    }

    public void deleteOpcionCrud(OpcionCrud opcionCrud){
        opcionCrudRepository.deleteOpcionCrud(opcionCrud);
    }

    public void deleteAllOpciones(){
        opcionCrudRepository.deleteAllOpcionCrud();
    }

    public LiveData<List<OpcionCrud>> getTodasOpciones(){
        return todasOpciones;
    }

    public OpcionCrud obtenerOpcionCrud(int idOpcion) throws InterruptedException, ExecutionException, TimeoutException {
        return opcionCrudRepository.obtenerOpcionCrud(idOpcion);
    }

}
