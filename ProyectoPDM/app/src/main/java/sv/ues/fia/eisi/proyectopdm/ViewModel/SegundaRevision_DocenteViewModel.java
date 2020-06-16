package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision_Docente;
import sv.ues.fia.eisi.proyectopdm.repository.SegundaRevision_DocenteRepository;

public class SegundaRevision_DocenteViewModel extends AndroidViewModel {
    private SegundaRevision_DocenteRepository repositorio;
    private LiveData<List<SegundaRevision_Docente>> allSR_D;

    public SegundaRevision_DocenteViewModel(@NonNull Application application) {
        super(application);
        repositorio = new SegundaRevision_DocenteRepository(application);
        allSR_D = repositorio.obtenerTodasRelacionesSR_D();
    }

    public void insertarSegundaRevision_Docente(SegundaRevision_Docente segundaRevision_docente){
        repositorio.insertarSegundaRevision_Docente(segundaRevision_docente);
    }

    public void actualizarSegundaRevision_Docente(SegundaRevision_Docente segundaRevision_docente){
        repositorio.actualizarSegundaRevision_Docente(segundaRevision_docente);
    }

    public void eliminarSegundaRevision_Docente(SegundaRevision_Docente segundaRevision_docente){
        repositorio.eliminarSegundaRevision_Docente(segundaRevision_docente);
    }

    public void eliminarTodasRelacionesSegundaRevision_Docente(){
        repositorio.eliminarTodasRelacionesSR_D();
    }

    public LiveData<List<SegundaRevision_Docente>> obtenerTodasRelacionesSegundaRevision_Docente(){
        return allSR_D;
    }

    public List<Docente> obtenerDocentesUsandoSegundaRevision(int idSR) throws Exception{
        return repositorio.obtenerDocentesConSR(idSR);
    }

    public List<SegundaRevision> obtenerSegundasRevisionesUsandoDocente(String carnet) throws Exception{
        return repositorio.obtenerSegundasRevisionesConDocente(carnet);
    }

    public SegundaRevision_Docente obtenerSegundaRevision_Docente(int idSR, String carnet) throws  Exception{
        return repositorio.obtenerRelacionSegundaRevision_Docente(new SegundaRevision_DocenteRepository.ParametrosSR_D(idSR,carnet));
    }

    public List<Cargo> obtenerCargosDeDocentesEnSegundaRevision(String carnet) throws Exception{
        return repositorio.obtenerCargosDeDocentesEnSegundaRevision(carnet);
    }

    public LiveData<List<SegundaRevision_Docente>> obtenerRelacionesConSR(int id){
        return repositorio.obtenerRelacionesConSR(id);
    }

    public List<SegundaRevision_Docente> obtenerRelacionesUsandoDocente(String carnet) throws Exception{
        return repositorio.obtenerRelacionesUsandoDocente(carnet);
    }
}
