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
    void borrarTodasInscripciones();

    /*
        LiveData tiene ventajas como mostrar los datos siempre actualizados en la app usando ROOM
     */
    @Query("Select * from Inscripcion")
    LiveData<List<Inscripcion>> obtenerInscripciones();

    //Query para traer los alumnos que cursan esa asignatura
    @Query("select Alumno.* from Asignatura "+
            "inner join Inscripcion on Asignatura.codigoAsignatura=Inscripcion.codigoAsignaturaFK "+
            "inner join Alumno on Alumno.carnetAlumno=Inscripcion.carnetAlumnoFK "+
            "where Inscripcion.codigoAsignaturaFK=:id")
    List<Alumno> getAlumnosAsignatura(final String id);


    //Query para traer las asignaturas que cursa el alumno
    @Query("select Asignatura.* from Alumno "+
            "inner join Inscripcion on Alumno.carnetAlumno=Inscripcion.carnetAlumnoFK "+
            "inner join Asignatura on Asignatura.codigoAsignatura=Inscripcion.codigoAsignaturaFK "+
            "where Inscripcion.carnetAlumnoFK=:id")
    List<Asignatura> getAsignaturasAlumno(final String id);

    @Query("Select * from Inscripcion where carnetAlumnoFK == :alumnoid and codigoAsignaturaFK == :asignaturaid")
    Inscripcion obtenerInscripcion(String alumnoid, String asignaturaid);


    //Query para pasar las asignaturas filtradas segun la carrera que curse el estudiante
    @Query("select * from Asignatura "+
            "inner join AreaAdm on AreaAdm.idDeptarmento=Asignatura.idDepartamentoFK "+
            "inner join Escuela on Escuela.idEscuela=AreaAdm.idEscuelaFK "+
            "where idEscuela=:id")
    List<Escuela>getEscuelasDeAsignaturasEnInscripcion(final int id);


    //Query para devolver las asignaturas del alumno segun su carrera
    @Query("select Inscripcion.* from Alumno "+
            "inner join Inscripcion on Alumno.carnetAlumno=Inscripcion.carnetAlumnoFK "+
            "where Alumno.carrera=:id")
    List<Inscripcion> getInscripcionCONAlumno(final String id);

}
