package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.repository.AccesoUsuarioRepository;

public class AccesoUsuarioViewModel extends AndroidViewModel {

    private AccesoUsuarioRepository accesoUsuarioRepository;
    private LiveData<List<AccesoUsuario>> todosAccesos;

    public AccesoUsuarioViewModel(@NonNull Application application) {
        super(application);
        accesoUsuarioRepository=new AccesoUsuarioRepository(application);
        todosAccesos=accesoUsuarioRepository.getTodosAccesos();
    }

    public void insertAccesoUsuario(AccesoUsuario accesoUsuario){
        accesoUsuarioRepository.insertarAccesoUsuairo(accesoUsuario);
    }

    public void actualizarAccesoUsuario(AccesoUsuario accesoUsuario){
        accesoUsuarioRepository.actualizarAccesoUsuario(accesoUsuario);
    }

    public void eliminarAccesoUsuario(AccesoUsuario accesoUsuario){
        accesoUsuarioRepository.eliminarAccesoUsuario(accesoUsuario);
    }

    public void eliminarTodosAccesos(){
        accesoUsuarioRepository.eliminarTodosAccesos();
    }

    public LiveData<List<AccesoUsuario>> getTodosAccesos(){
        return todosAccesos;
    }

    public LiveData<List<AccesoUsuario>> obtenerAccesosPorUsuario(int idUsuario) throws InterruptedException, ExecutionException, TimeoutException {
        return accesoUsuarioRepository.obtenerAccesosusuario(idUsuario);
    }

    public LiveData<List<AccesoUsuario>> obtenerAccesosPorNumCrud(int idUsuario,int numCrud) throws InterruptedException, ExecutionException, TimeoutException {
        return accesoUsuarioRepository.obtenerAccesosPorNumCrud(idUsuario,numCrud);
    }

    public void eliminarAccesosDeUsuario(int idUsuario){
        accesoUsuarioRepository.eliminarAccesosDeUsuario(idUsuario);
    }
}
