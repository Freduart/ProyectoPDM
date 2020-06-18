package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.repository.AreaAdmRepository;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;

public class AreaAdmViewModel extends AndroidViewModel{

    //atributos
    private AreaAdmRepository repo;
    private LiveData<List<AreaAdm>> todasEval;

    //constructor
    public AreaAdmViewModel(@NonNull Application application) {
        super(application);
        repo=new AreaAdmRepository(application);
        todasEval=repo.obtenerAreasAdm();
    }

    //acciones crud
    public void insertAreaAdm(AreaAdm areaAdm){
        repo.insertAreaAdm(areaAdm);
    }

    public void updateAreaAdm(AreaAdm areaAdm){
        repo.actualizarAreaAdm(areaAdm);
    }

    public void deleteAreaAdm(AreaAdm areaAdm){
        repo.borrarAreaAdm(areaAdm);
    }

    public void deleteAreaAdmAll(){
        repo.borrarTodasAreasAdm();
    }

    public LiveData<List<AreaAdm>> getAreaAdmAll() {
        return todasEval;
    }

    public LiveData<List<AreaAdm>> getAreaAdmDocentes(String id) {
        return repo.obtenerAreasAdmDoc(id);
    }

    public AreaAdm getAreaAdm(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return repo.obtenerArea(id);
    }
}
