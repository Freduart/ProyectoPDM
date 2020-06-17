package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.os.ParcelUuid;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.UsuarioDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;

public class UsuarioRepository {
    private UsuarioDao usuarioDao;
    private LiveData<List<Usuario>> allUsers;

    public UsuarioRepository(Application application){
        DataBase dataBase = DataBase.getInstance(application);
        usuarioDao=dataBase.usuarioDao();
        allUsers = usuarioDao.obtenerUsuarios();
    }

    public void insertarUsuario(Usuario usuario){new InsertarUsuarioAsyncTask(usuarioDao).execute(usuario);}

    public void actualizarUsuario(Usuario usuario){new ActualizarUsuarioAsyncTask(usuarioDao).execute(usuario);}

    public void eliminarUsuario(Usuario usuario){new EliminarUsuarioAsyncTask(usuarioDao).execute(usuario);}

    public void deleteAllUsers(){new DeleteAllUsersAsyncTask(usuarioDao).execute();}

    public LiveData<List<Usuario>> getAllUsers() {return  allUsers;}

    public Usuario obtenerUsuario(Integer integer) throws InterruptedException, ExecutionException, TimeoutException {
        return new obtenerUsuarioAsyncTask(usuarioDao).execute(integer).get(12, TimeUnit.SECONDS);
    }


    public static class InsertarUsuarioAsyncTask extends AsyncTask<Usuario, Void, Void>{
        private UsuarioDao usuarioDao;
        public InsertarUsuarioAsyncTask(UsuarioDao usuarioDao) {this.usuarioDao =usuarioDao; }
        @Override
        protected Void doInBackground(Usuario... usuarios) {
            usuarioDao.insertUser(usuarios[0]);
            return null;
        }
    }

    public static class ActualizarUsuarioAsyncTask extends AsyncTask<Usuario, Void, Void>{
        private UsuarioDao usuarioDao;
        public ActualizarUsuarioAsyncTask(UsuarioDao usuarioDao) {this.usuarioDao =usuarioDao;}
            @Override
        protected Void doInBackground(Usuario... usuarios) {
            usuarioDao.updateUser(usuarios[0]);
            return null;
        }
    }

    public static class EliminarUsuarioAsyncTask extends AsyncTask<Usuario, Void, Void>{
        private UsuarioDao usuarioDao;
        public EliminarUsuarioAsyncTask(UsuarioDao usuarioDao) {this.usuarioDao =usuarioDao;}
            @Override
        protected Void doInBackground(Usuario... usuarios) {
            usuarioDao.deleteUsuario(usuarios[0]);
            return null;
        }
    }

    public static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void>{
        private UsuarioDao usuarioDao;
        public DeleteAllUsersAsyncTask(UsuarioDao usuarioDao) {this.usuarioDao =usuarioDao;}
            @Override
        protected Void doInBackground(Void... voids) {
            usuarioDao.borrarUsuario();
            return null;
        }
    }

    private static class obtenerUsuarioAsyncTask extends AsyncTask <Integer, Void, Usuario>{
        private UsuarioDao usuarioDao;
        private obtenerUsuarioAsyncTask(UsuarioDao usuarioDao) {this.usuarioDao = usuarioDao;}
            @Override
        protected Usuario doInBackground(Integer... users) {
            return usuarioDao.obtenerUsuario(users[0]);
        }
    }
}
