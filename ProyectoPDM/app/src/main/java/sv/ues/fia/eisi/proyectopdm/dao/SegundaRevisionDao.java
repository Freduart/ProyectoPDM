package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;

@Dao
public interface SegundaRevisionDao {
    @Insert
    void insertSegundaRevision(SegundaRevision evaluacion);

    @Update
    void updateSegundaRevision(SegundaRevision evaluacion);

    @Delete
    void deleteSegundaRevision(SegundaRevision evaluacion);

    @Query("delete from SegundaRevision")
    void borrarSegundasRevisiones();

    @Query("select * from SegundaRevision")
    LiveData<List<SegundaRevision>> obtenerSegundasRevisiones();

    @Query("select * from SegundaRevision where idPrimeraRevisionFK == :primerarevisionid")
    SegundaRevision obtenerSegundaRevision(String primerarevisionid);
}