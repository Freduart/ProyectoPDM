package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

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

    @Query("select Asignatura.* from Evaluacion " +
            "inner join Asignatura on Evaluacion.codigoAsignaturaFK=Asignatura.codigoAsignatura "+
            "where Evaluacion.idEvaluacion=:idEvaluacion")
    Asignatura getAsignaturas(final int idEvaluacion);

    @Query("select TipoEvaluacion.* from Evaluacion " +
            "inner join TipoEvaluacion on Evaluacion.idTipoEvaluacionFK=TipoEvaluacion.idTipoEvaluacion "+
            "where Evaluacion.idEvaluacion=:idEvaluacion")
    TipoEvaluacion getTipoEvaluacion(final int idEvaluacion);

    @Query("select Docente.* from Evaluacion " +
            "inner join Docente on Evaluacion.carnetDocenteFK=Docente.carnetDocente "+
            "where Evaluacion.idEvaluacion=:idEvaluacion")
    Docente getDocente(final int idEvaluacion);
}