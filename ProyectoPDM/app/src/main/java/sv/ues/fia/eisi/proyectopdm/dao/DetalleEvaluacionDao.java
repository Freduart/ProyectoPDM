package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;

@Dao
public interface DetalleEvaluacionDao {
    @Insert
    void insertDetalleEvaluacion(DetalleEvaluacion detalleEvaluacion);

    @Update
    void updateDetalleEvaluacion(DetalleEvaluacion detalleEvaluacion);

    @Delete
    void deleteDetalleEvaluacion(DetalleEvaluacion detalleEvaluacion);

    @Query("delete from DetalleEvaluacion")
    void deleteAllDetallesEvaluaciones();

    @Query("select * from DetalleEvaluacion")
    LiveData<List<DetalleEvaluacion>> obtenerDetallesEvaluaciones();

    @Query("select * from DetalleEvaluacion where idDetalleEv == :detalleevaluacionid")
    DetalleEvaluacion obtenerDetalleEvaluacion(int detalleevaluacionid);
}
