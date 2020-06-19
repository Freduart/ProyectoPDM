package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.room.Dao;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

@Dao
public interface SolicitudExtraordinarioDao {

    @Insert
    void insertSolicitudExtraordinario(SolicitudExtraordinario solicitudExtraordinario);

    @Update
    void updateSolicitudExtraordinario(SolicitudExtraordinario solicitudExtraordinario);

    @Delete
    void deleteSolicitudExtraordinario(SolicitudExtraordinario solicitudExtraordinario);

    /*
        En este Query nosotros borramos todos los datos que contenga la tabla SolicitudExtraordinario
        Para borrar uno en especifico necesitaremos usar el id en el Query como un delete de SQL
     */

    @Query("delete from SolicitudExtraordinario")
    void borrarSolicitudesExtraordinario();


    /*
        LiveData tiene ventajas como mostrar los datos siempre actualizados en la app usando ROOM
     */
    @Query("Select * from SolicitudExtraordinario")
    LiveData<List<SolicitudExtraordinario>> obtenerSolicitudesExtraordinario();

    @Query("select * from SolicitudExtraordinario where idSolicitud == :solicitudid")
    SolicitudExtraordinario obtenerSolicitudExtraordinario(int solicitudid);

    @Query("select TipoEvaluacion.* from SolicitudExtraordinario " +
            "inner join TipoEvaluacion on SolicitudExtraordinario.tipoSolicitud=TipoEvaluacion.idTipoEvaluacion " +
            "where SolicitudExtraordinario.idSolicitud=:idSolicitud")
    TipoEvaluacion getTipoEvaluacion(final int idSolicitud);

    @Query("select Evaluacion.* from SolicitudExtraordinario " +
            "inner join Evaluacion on SolicitudExtraordinario.idEvaluacion=Evaluacion.idEvaluacion " +
            "where SolicitudExtraordinario.idSolicitud=:idSolicitud")
    Evaluacion getEvaluacion(final int idSolicitud);

    @Query("select Alumno.* from SolicitudExtraordinario " +
            "inner join Alumno on SolicitudExtraordinario.carnetAlumnoFK=Alumno.carnetAlumno " +
            "where SolicitudExtraordinario.idSolicitud=:idSolicitud")
    Alumno getAlumno(final int idSolicitud);

    @Query("select SolicitudExtraordinario.* from SolicitudExtraordinario " +
            "inner join Alumno on SolicitudExtraordinario.carnetAlumnoFK=Alumno.carnetAlumno " +
            "where Alumno.carnetAlumno=:carnet")
    LiveData<List<SolicitudExtraordinario>> obtenerSolicitudesDeEstudiante(final String carnet);

    @Query("select SolicitudExtraordinario.* from SolicitudExtraordinario " +
            "inner join Evaluacion on SolicitudExtraordinario.idEvaluacion=Evaluacion.idEvaluacion " +
            "inner join Docente on Evaluacion.carnetDocenteFK=Docente.carnetDocente " +
            "where Docente.carnetDocente=:carnet")
    LiveData<List<SolicitudExtraordinario>> obtenerSolicitudesParaDocente(final String carnet);

    @Query("select * from Alumno where idUsuarioFk == :id")
    Alumno obtenerAlumnoConUsuario(int id);

    @Query("select * from Docente where idUsuarioFk == :id")
    Docente obtenerDocenteConUsuario(int id);


}
