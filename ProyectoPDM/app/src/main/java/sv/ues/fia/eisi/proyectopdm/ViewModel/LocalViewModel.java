package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Local;
import sv.ues.fia.eisi.proyectopdm.repository.LocalRepository;

public class LocalViewModel extends AndroidViewModel {

    private LocalRepository repository;
    private LiveData<List<Local>> allLocales;

    public LocalViewModel(@NonNull Application application) {
        super(application);
        repository =  new LocalRepository(application);
        allLocales = repository.getAllLocales();
    }

    public void insert(Local local){
        //Enlazamos con la clase dao
        repository.insertar(local);
    }

    public void update(Local local){
        repository.actualizar(local);
    }

    public void delete(Local local){
        repository.borrar(local);
    }

    public void deleteAllLocales(){
        repository.borrarTodos();
    }

    public LiveData<List<Local>> getAllLocales() {
        return allLocales;
    }
}
