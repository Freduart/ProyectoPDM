package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;

@Dao
public interface AccesoUsuarioDao {
    @Insert
    void insertAccesoUsuario(AccesoUsuario accesoUsuario);

    @Update
    void updateAccesoUsuario(AccesoUsuario accesoUsuario);

    @Delete
    void deleteAccesoUsuario(AccesoUsuario accesoUsuario);

    @Query("delete from AccesoUsuario")
    void eliminarTodosAccesos();

    @Query("select * from AccesoUsuario")
    LiveData<List<AccesoUsuario>> obtenerAccesos();

    @Query("select * from AccesoUsuario where idUsuarioFK==:idUsuario")
    LiveData<List<AccesoUsuario>> obtenerAccesosPorUsuario(int idUsuario);

    @Query("select Accesousuario.* from AccesoUsuario "+
            "inner join OpcionCrud on AccesoUsuario.idOpcionFK==OpcionCrud.idOpcion AND AccesoUsuario.idUsuarioFK==:idUsuario AND numCrud==:numCrud")
    LiveData<List<AccesoUsuario>> obtenerAccesosPorNumCrud(int idUsuario,int numCrud);

    @Query("delete from AccesoUsuario where idUsuarioFK==:idUsuario")
    void eliminarAccesosDeUsuario(int idUsuario);
}
