package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

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

    @Query("select DetalleEvaluacion.* from DetalleEvaluacion " +
            "inner join Alumno on DetalleEvaluacion.carnetAlumnoFK=Alumno.carnetAlumno " +
            "inner join Usuario on Alumno.idUsuarioFk=Usuario.idUsuario " +
            "where Usuario.idUsuario=:id")
    LiveData<List<DetalleEvaluacion>> obtenerDetallesEvaluacionesSegunUsuario(int id);

    @Query("select DetalleEvaluacion.* from Evaluacion " +
            "inner join DetalleEvaluacion on Evaluacion.idEvaluacion=DetalleEvaluacion.idEvaluacionFK " +
            "inner join Alumno on DetalleEvaluacion.carnetAlumnoFK=Alumno.carnetAlumno " +
            "inner join Usuario on Alumno.idUsuarioFk=Usuario.idUsuario " +
            "where Usuario.idUsuario=:idUsuario and Evaluacion.idEvaluacion= :idEvaluacion")
    LiveData<List<DetalleEvaluacion>> obtenerDetallesEvaluacionesPorAlumno(int idUsuario, int idEvaluacion);

    @Query("select * from DetalleEvaluacion where idDetalleEv == :detalleevaluacionid")
    DetalleEvaluacion obtenerDetalleEvaluacion(int detalleevaluacionid);

    @Query("select * from DetalleEvaluacion where carnetAlumnoFK == :carnetalumno")
    List<DetalleEvaluacion> obtenerDetallePorEstudiante(String carnetalumno);

    @Query("select * from DetalleEvaluacion where carnetAlumnoFK == :carnetalumno and idEvaluacionFK == :idEval")
    DetalleEvaluacion obtenerDetalleEstudianteEvaluacion(String carnetalumno, int idEval);

    @Query("select DetalleEvaluacion.* from Evaluacion " +
            "inner join DetalleEvaluacion on Evaluacion.idEvaluacion=DetalleEvaluacion.idEvaluacionFK "+
            "inner join Alumno on DetalleEvaluacion.carnetAlumnoFK=Alumno.carnetAlumno " +
            "where Evaluacion.idEvaluacion=:idEval")
    List<DetalleEvaluacion> obtenerNotasDeEvaluacion(final int idEval);

    @Query("select Alumno.* from Evaluacion " +
            "inner join DetalleEvaluacion on Evaluacion.idEvaluacion=DetalleEvaluacion.idEvaluacionFK "+
            "inner join Alumno on DetalleEvaluacion.carnetAlumnoFK=Alumno.carnetAlumno " +
            "where Evaluacion.idEvaluacion=:idEval")
    List<Alumno> obtenerEstudiantesEnEvaluacion(final int idEval);
}
