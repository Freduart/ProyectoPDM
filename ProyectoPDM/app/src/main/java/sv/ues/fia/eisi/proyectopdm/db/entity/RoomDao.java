package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {
        Alumno.class, AreaAdm.class,Asignatura.class,CargaAcademica.class,Cargo.class,
        Ciclo.class, CicloAsignatura.class,DetalleEvaluacion.class,Docente.class,
        EncargadoImpresion.class,Escuela.class,Evaluacion.class,Inscripcion.class,
        Local.class,PrimeraRevision.class,SegundaRevision.class,SegundaRevision_Docente.class,
        SolicitudExtraordinario.class,SolicitudImpresion.class,TipoEvaluacion.class,
    }, version = 1)
public abstract class RoomDao extends RoomDatabase {

}
