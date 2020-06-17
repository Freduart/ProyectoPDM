package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;
import sv.ues.fia.eisi.proyectopdm.repository.UsuarioRepository;

public class UsuarioViewModel extends AndroidViewModel {

    private UsuarioRepository usuarioRepository;
    private LiveData<List<Usuario>> allUsers;


    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        usuarioRepository = new UsuarioRepository(application);
        allUsers =usuarioRepository.getAllUsers();
    }

    public void insertUser(Usuario usuario) {usuarioRepository.insertarUsuario(usuario);}

    public void updateUser(Usuario usuario) {usuarioRepository.actualizarUsuario(usuario);}

    public void deleteUser(Usuario usuario) {usuarioRepository.eliminarUsuario(usuario);}

    public void deleteAllUser() {usuarioRepository.deleteAllUsers();}

    public LiveData<List<Usuario>> getAllUsers() {return  allUsers;}

    public Usuario obtenerUsuario(int id) throws InterruptedException, ExecutionException, TimeoutException {
        return usuarioRepository.obtenerUsuario(id);
    }

    public Usuario obtenerCredenciales(String[] credenciales) throws InterruptedException, ExecutionException, TimeoutException {
        return usuarioRepository.obtenerUserPass(credenciales);
    }
}
