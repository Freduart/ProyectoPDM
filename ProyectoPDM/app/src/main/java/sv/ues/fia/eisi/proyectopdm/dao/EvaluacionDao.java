package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

@Dao
public interface EvaluacionDao {
    @Insert
    void insertEvaluacion(Evaluacion evaluacion);

    @Update
    void updateEvaluacion(Evaluacion evaluacion);

    @Delete
    void deleteEvaluacion(Evaluacion evaluacion);

    @Query("delete from Evaluacion")
    void borrarEvaluaciones();

    @Query("select * from Evaluacion")
    LiveData<List<Evaluacion>> obtenerEvaluaciones();

    @Query("select * from Evaluacion where idEvaluacion == :evaluacionid")
    Evaluacion obtenerEvaluacion(int evaluacionid);
}