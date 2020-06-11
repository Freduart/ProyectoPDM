package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;
@Dao
public interface EncargadoImpresionDao {
    @Insert
    void insertEncargadoImpresion(EncargadoImpresion encargadoImpresion);

    @Update
    void updateEncargadoImpresion(EncargadoImpresion encargadoImpresion);

    @Delete
    void deleteEncargadoImpresion(EncargadoImpresion encargadoImpresion);

    @Query("delete from EncargadoImpresion")
    void borrarEncargadoImpresion();

    @Query("select * from EncargadoImpresion")
    LiveData<List<EncargadoImpresion>> obtenerEncargados();

    @Query("select * from EncargadoImpresion where idEncargadoImpresion == :encargadoid")
    EncargadoImpresion obtenerEncargadoImpresion(int encargadoid);
}
