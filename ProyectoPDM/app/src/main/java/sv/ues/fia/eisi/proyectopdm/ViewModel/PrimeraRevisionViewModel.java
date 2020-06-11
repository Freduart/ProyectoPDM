package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.repository.PrimeraRevisionRepository;

public class PrimeraRevisionViewModel extends AndroidViewModel {

    private PrimeraRevisionRepository primeraRevisionRepository;
    private LiveData<List<PrimeraRevision>> allPrimerasRevisiones;

    public PrimeraRevisionViewModel(@NonNull Application application) {
        super(application);
        primeraRevisionRepository = new PrimeraRevisionRepository(application);
        allPrimerasRevisiones = primeraRevisionRepository.getAllPrimerasRevisiones();
    }

    public void insertPrimeraRevision(PrimeraRevision primeraRevision){
        primeraRevisionRepository.insertarPrimeraRevision(primeraRevision);
    }

    public void updatePrimeraRevision(PrimeraRevision primeraRevision){
        primeraRevisionRepository.actualizarPrimeraRevision(primeraRevision);
    }

    public void deletePrimeraRevision(PrimeraRevision primeraRevision){
        primeraRevisionRepository.borrarPrimeraRevision(primeraRevision);
    }

    public void deleteAllPrimeraRevision() {primeraRevisionRepository.borrarTodasPrimerasRevisiones(); }

    public LiveData<List<PrimeraRevision>> getAllPrimerasRevisiones() { return allPrimerasRevisiones; }

    public PrimeraRevision getPrimeraRevision(String id) throws InterruptedException, ExecutionException, TimeoutException {
        return primeraRevisionRepository.obtenerPrimeraRevision(id);
    }
}
