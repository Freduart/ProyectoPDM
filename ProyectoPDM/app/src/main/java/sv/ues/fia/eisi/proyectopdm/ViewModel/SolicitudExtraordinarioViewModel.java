package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.repository.SolicitudExtraordinarioRepository;

public class SolicitudExtraordinarioViewModel extends AndroidViewModel {

    private SolicitudExtraordinarioRepository solicitudExtraordinarioRepository;
    private LiveData<List<SolicitudExtraordinario>> allSolicitudesExtraordinario;

    public SolicitudExtraordinarioViewModel(@NonNull Application application){
        super(application);
        solicitudExtraordinarioRepository=new SolicitudExtraordinarioRepository(application);
        allSolicitudesExtraordinario=solicitudExtraordinarioRepository.getAllSolicitudesExtraordinario();
    }

    public void insert(SolicitudExtraordinario solicitudExtraordinario){
        solicitudExtraordinarioRepository.insertar(solicitudExtraordinario);
    }

    public void update(SolicitudExtraordinario solicitudExtraordinario){
        solicitudExtraordinarioRepository.actualizar(solicitudExtraordinario);
    }

    public void delete(SolicitudExtraordinario solicitudExtraordinario){
        solicitudExtraordinarioRepository.eliminar(solicitudExtraordinario);
    }

    public void deleteAllSolicitudesExtraordinario(){
        solicitudExtraordinarioRepository.eliminarTodas();
    }

    public LiveData<List<SolicitudExtraordinario>> getAllSolicitudesExtraordinario(){
        return allSolicitudesExtraordinario;
    }
}
