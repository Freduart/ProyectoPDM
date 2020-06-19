package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;
import sv.ues.fia.eisi.proyectopdm.repository.SolicitudImpresionRepository;

public class SolicitudImpresionViewModel extends AndroidViewModel {

    private SolicitudImpresionRepository solicitudImpresionRepository;
    private LiveData<List<SolicitudImpresion>> allSolicitudesImpresion;

    public SolicitudImpresionViewModel(@NonNull Application application) {
        super(application);
        solicitudImpresionRepository=new SolicitudImpresionRepository(application);
        allSolicitudesImpresion=solicitudImpresionRepository.getAllSolicitudesImpresion();
    }

    public void insert(SolicitudImpresion solicitudImpresion){
        solicitudImpresionRepository.insertar(solicitudImpresion);
    }

    public void update(SolicitudImpresion solicitudImpresion){
        solicitudImpresionRepository.actualizar(solicitudImpresion);
    }

    public void  delete(SolicitudImpresion solicitudImpresion){
        solicitudImpresionRepository.eliminar(solicitudImpresion);
    }

    public void deleteAllSolicitudesImpresion(){
        solicitudImpresionRepository.eliminarTodas();
    }

    public LiveData<List<SolicitudImpresion>> getAllSolicitudesImpresion(){
        return allSolicitudesImpresion;
    }

    public SolicitudImpresion obtenerSolicitudImpresion(int id) throws InterruptedException, ExecutionException, TimeoutException{
        return solicitudImpresionRepository.obtenerSolicitudImpresion(id);
    }

    public LiveData<List<SolicitudImpresion>> obtenerSolicitudPorEstado(String estado) throws InterruptedException, ExecutionException, TimeoutException{
        return solicitudImpresionRepository.obtenerSolicitudPorEstado(estado);
    }

    public LiveData<List<SolicitudImpresion>> obtenerSolicitudesPorCarnet(String carnetDocente) throws InterruptedException, ExecutionException, TimeoutException{
        return solicitudImpresionRepository.ontenerSolicitudesPorCarnet(carnetDocente);
    }

    public LiveData<List<SolicitudImpresion>> obtenerSolicitudesPorDirector(String docDirector) throws InterruptedException, ExecutionException, TimeoutException{
        return solicitudImpresionRepository.obtenerSolicitudesPorDirector(docDirector);
    }
}
