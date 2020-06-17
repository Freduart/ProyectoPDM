package sv.ues.fia.eisi.proyectopdm.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;

@Dao
public interface UsuarioDao {
    @Insert
    void insertUser(Usuario usuario);

    @Update
    void updateUser(Usuario usuario);

    @Delete
    void deleteUsuario(Usuario usuario);

    @Query("DELETE FROM Usuario")
    void borrarUsuario();

    @Query("SELECT * FROM Usuario")
    LiveData<List<Usuario>> obtenerUsuarios();

    @Query("SELECT * FROM Usuario WHERE idUsuario == :usuarioid")
    Usuario obtenerUsuario(int usuarioid);

    @Query("SELECT * FROM Usuario WHERE nombreUsuario == :usuarionom AND clave == :usuarioclave")
    Usuario getUser(String usuarionom, String usuarioclave);
}
