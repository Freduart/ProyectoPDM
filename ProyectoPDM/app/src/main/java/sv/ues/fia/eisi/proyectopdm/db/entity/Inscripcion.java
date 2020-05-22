package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Inscripcion",
        primaryKeys = {"carnetAlumnoFK","codigoAsignaturaFK"}
)
public class Inscripcion {


    @ForeignKey(
            entity = Alumno.class,
            parentColumns = "carnetAlumno",
            childColumns = "carnetAlumnoFK"
    )@NonNull
    private String carnetAlumnoFK;
    @ForeignKey(entity = Asignatura.class,
            parentColumns = "codigoAsignatura",
            childColumns = "codigoAsignaturaFK"
    )@NonNull
    private String codigoAsignaturaFK;
    private int glaboratorio;
    private int gteorico;
    private int gdiscusion;

    public Inscripcion(String carnetAlumnoFK, String codigoAsignaturaFK, int glaboratorio, int gteorico, int gdiscusion) {
        this.carnetAlumnoFK = carnetAlumnoFK;
        this.codigoAsignaturaFK = codigoAsignaturaFK;
        this.glaboratorio = glaboratorio;
        this.gteorico = gteorico;
        this.gdiscusion = gdiscusion;
    }

    public String getCarnetAlumnoFK() {
        return carnetAlumnoFK;
    }

    public void setCarnetAlumnoFK(String carnetAlumnoFK) {
        this.carnetAlumnoFK = carnetAlumnoFK;
    }

    public String getCodigoAsignaturaFK() {
        return codigoAsignaturaFK;
    }

    public void setCodigoAsignaturaFK(String codigoAsignaturaFK) {
        this.codigoAsignaturaFK = codigoAsignaturaFK;
    }

    public int getGlaboratorio() {
        return glaboratorio;
    }

    public void setGlaboratorio(int glaboratorio) {
        this.glaboratorio = glaboratorio;
    }

    public int getGteorico() {
        return gteorico;
    }

    public void setGteorico(int gteorico) {
        this.gteorico = gteorico;
    }

    public int getGdiscusion() {
        return gdiscusion;
    }

    public void setGdiscusion(int gdiscusion) {
        this.gdiscusion = gdiscusion;
    }
}
