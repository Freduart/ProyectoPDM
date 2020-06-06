package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;
import sv.ues.fia.eisi.proyectopdm.repository.CicloRepository;

public class CicloViewModel extends AndroidViewModel {

    //Atributos de Clase
    private CicloRepository repository;
    private LiveData<List<Ciclo>> allCiclos;

    //Constructor
    public CicloViewModel(@NonNull Application application){
        super(application);
        repository = new CicloRepository(application);
        allCiclos = repository.getAllCiclos();
    }

    //Acciones CRUD para Ciclo
    public void insertCiclo(Ciclo ciclo){
        repository.insertarCiclo(ciclo);
    }

    public void updateCiclo(Ciclo ciclo){
        repository.actualizarCiclo(ciclo);
    }

    public void deleteCiclo(Ciclo ciclo){
        repository.borrarCiclo(ciclo);
    }

    public void deleteAllCiclos(){
        repository.borrarTodosCiclos();
    }

    public LiveData<List<Ciclo>> getAllCiclos() {
        return allCiclos;
    }

    public Ciclo getCic(int id){
        return repository.getCiclo(id);
    }
}
