package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.repository.CargoRepository;

public class CargoViewModel extends AndroidViewModel {

    private CargoRepository cargoRepository;
    private LiveData<List<Cargo>> allCargos;

    public CargoViewModel(@NonNull Application application) {
        super(application);
        cargoRepository=new CargoRepository(application);
        allCargos=cargoRepository.getAllCargos();
    }

    public void insert(Cargo cargo){
        cargoRepository.insertar(cargo);
    }

    public void update(Cargo cargo){
        cargoRepository.actualizar(cargo);
    }

    public void delete(Cargo cargo){
        cargoRepository.eliminar(cargo);
    }

    public void deleteAll(){
        cargoRepository.eliminarTodas();
    }

    public LiveData<List<Cargo>> getAllCargos(){
        return allCargos;
    }

    public Cargo getCargo(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return cargoRepository.obtenerCargo(id);
    }
}
