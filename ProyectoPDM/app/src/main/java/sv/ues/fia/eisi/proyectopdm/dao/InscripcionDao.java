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
import sv.ues.fia.eisi.proyectopdm.db.entity.Inscripcion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision_Docente;

@Dao
public interface InscripcionDao {

    @Insert
    void insertInscripcion(Inscripcion inscripcion);

    @Update
    void updateInscripcion(Inscripcion inscripcion);

    @Delete
    void deleteInscripcion(Inscripcion inscripcion);

    /*
        En este Query nosotros borramos todos los datos que contenga la tabla Inscripcion
        Para borrar uno en especifico necesitaremos usar el id en el Query como un delete de SQL
     */
    @Query("delete from Inscripcion ")
    void borrarLocales();

    /*
        LiveData tiene ventajas como mostrar los datos siempre actualizados en la app usando ROOM
     */
    @Query("Select * from Inscripcion")
    LiveData<List<Inscripcion>> obtenerInscripciones();

    @Query("Select * from Inscripcion where carnetAlumnoFK == :alumnoid and codigoAsignaturaFK == :asignaturaid")
    Inscripcion obtenerInscripcion(String alumnoid, String asignaturaid);

    @Query("select Asignatura.* from Alumno " +
            "inner join Inscripcion on Alumno.carnetAlumno=Inscripcion.carnetAlumnoFK " +
            "inner join Asignatura on Asignatura.codigoAsignatura=Inscripcion.codigoAsignaturaFK "+
            "where Inscripcion.carnetAlumnoFK=:id")
    List<Asignatura> getAsignaturas(final String id);

    @Query("select Alumno. * from Asignatura "+
            "inner join Inscripcion on Asignatura.codigoAsignatura=Inscripcion.codigoAsignaturaFK "+
            "inner join Alumno on Alumno.carnetAlumno=Inscripcion.carnetAlumnoFK " +
            "where Inscripcion.carnetAlumnoFK=:id")
    List<Alumno> getAlumnos(final String id);

}
