package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Bitacora;

@Dao
public interface BitacoraDao {

    @Insert
    void insertBitacora(Bitacora bitacora);

    @Update
    void updateBitacora(Bitacora bitacora);

    @Delete
    void deleteBitacora(Bitacora bitacora);

    @Query("select * from Bitacora")
    List<Bitacora> obtenerBitacoras();

    @Query("delete from Bitacora")
    void borrarBitacoras();

}
