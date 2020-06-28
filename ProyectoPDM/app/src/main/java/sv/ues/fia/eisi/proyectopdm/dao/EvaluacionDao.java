package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
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

    @Query("select * from Evaluacion")
    List<Evaluacion> obtenerEvaluacionesAsync();

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

    @Query("select Evaluacion.* from Evaluacion " +
            "inner join Asignatura on Evaluacion.codigoAsignaturaFK=Asignatura.codigoAsignatura "+
            "inner join Inscripcion on Asignatura.codigoAsignatura=Inscripcion.codigoAsignaturaFK "+
            "inner join Alumno on Inscripcion.carnetAlumnoFK=Alumno.carnetAlumno "+
            "where Inscripcion.carnetAlumnoFK=:carnet")
    LiveData<List<Evaluacion>> obtenerEvaluacionesDeEstudiante(final String carnet);

    @Query("select Evaluacion.* from Evaluacion  " +
            "inner join Docente on Evaluacion.carnetDocenteFK=Docente.carnetDocente "+
            "where Docente.carnetDocente=:carnet")
    LiveData<List<Evaluacion>> obtenerEvaluacionesDeDocente(final String carnet);

    @Query("select * from Alumno where idUsuarioFk == :id")
    Alumno obtenerAlumnoConUsuario(int id);

    //obtener alumnos desde Asignatura
    @Query("select Alumno.* from Alumno  " +
            "inner join Inscripcion on Inscripcion.carnetAlumnoFK=Alumno.carnetAlumno "+
            "inner join Asignatura on Inscripcion.codigoAsignaturaFK=Asignatura.codigoAsignatura "+
            "where Asignatura.codigoAsignatura=:codigo")
    LiveData<List<Alumno>> obtenerAlumnosDesdeAsignatura(final String codigo);

    @Query("select * from Docente where idUsuarioFk == :id")
    Docente obtenerDocenteConUsuario(int id);

    //recupera los docentes usando el id de escuela
    @Query("select Docente.* from Docente " +
            "inner join Cargo on Docente.idCargoFK=Cargo.idCargo "+
            "inner join AreaAdm on Cargo.idAreaAdminFK=AreaAdm.idDeptarmento "+
            "inner join Escuela on AreaAdm.idEscuelaFK=Escuela.idEscuela "+
            "where Escuela.idEscuela=:id")
    LiveData<List<Docente>> obtenerDocentePorEscuela(int id);

    //recupera la escuela del docente selecionado
    @Query("select Escuela.* from Docente " +
            "inner join Cargo on Docente.idCargoFK=Cargo.idCargo "+
            "inner join AreaAdm on Cargo.idAreaAdminFK=AreaAdm.idDeptarmento "+
            "inner join Escuela on AreaAdm.idEscuelaFK=Escuela.idEscuela "+
            "where Docente.carnetDocente=:id")
    Escuela obtenerEscuelaDeDocente(String id);

    //recupera las asignaturas usando el id de escuela
    @Query("select Asignatura.* from Asignatura " +
            "inner join AreaAdm on Asignatura.idDepartamentoFK=AreaAdm.idDeptarmento "+
            "inner join Escuela on AreaAdm.idEscuelaFK=Escuela.idEscuela "+
            "where Escuela.idEscuela=:id")
    LiveData<List<Asignatura>> obtenerAsignaturaPorEscuela(int id);
}