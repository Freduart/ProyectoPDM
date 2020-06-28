package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.OpcionCrud;

@Dao
public interface OpcionCrudDao {
    @Insert
    void insertOpcionCrud(OpcionCrud opcionCrud);

    @Update
    void updateOpcionCrud(OpcionCrud opcionCrud);

    @Delete
    void deleteOpcionCrud(OpcionCrud opcionCrud);

    @Query("delete from OpcionCrud")
    void borrarTodasOpcionesCrud();

    @Query("select * from OpcionCrud")
    LiveData<List<OpcionCrud>> obtenerOpcionesCrud();

    @Query("select * from OpcionCrud where idOpcion==:idOpcionCrud")
    OpcionCrud obtenerOpcionCrud(int idOpcionCrud);
}
