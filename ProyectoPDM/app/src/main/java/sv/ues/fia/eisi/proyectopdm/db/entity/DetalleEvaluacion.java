package sv.ues.fia.eisi.proyectopdm.db.entity;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "DetalleEvaluacion")
public class DetalleEvaluacion {

    @PrimaryKey(autoGenerate = true)
    private int idDetalleEv;
    @ForeignKey(
            entity = Evaluacion.class,
            parentColumns = "idEvaluacion",
            childColumns = "idEvaluacionFK"
    )
    private int idEvaluacionFK;
    @ForeignKey(
            entity = Alumno.class,
            parentColumns = "carnetAlumno",
            childColumns = "carnetAlumnoFK"
    )
    private String carnetAlumnoFK;
    private double nota;


    public DetalleEvaluacion(int idEvaluacionFK, String carnetAlumnoFK, double nota) {
        this.idEvaluacionFK = idEvaluacionFK;
        this.carnetAlumnoFK = carnetAlumnoFK;
        this.nota = nota;
    }

    @Ignore
    public DetalleEvaluacion(int idEvaluacionFK, String carnetAlumnoFK) {
        this.idEvaluacionFK = idEvaluacionFK;
        this.carnetAlumnoFK = carnetAlumnoFK;
    }

    public int getIdDetalleEv() {
        return idDetalleEv;
    }

    public void setIdDetalleEv(int idDetalleEv) {
        this.idDetalleEv = idDetalleEv;
    }

    public int getIdEvaluacionFK() {
        return idEvaluacionFK;
    }

    public void setIdEvaluacionFK(int idEvaluacionFK) {
        this.idEvaluacionFK = idEvaluacionFK;
    }

    public String getCarnetAlumnoFK() {
        return carnetAlumnoFK;
    }

    public void setCarnetAlumnoFK(String carnetAlumnoFK) {
        this.carnetAlumnoFK = carnetAlumnoFK;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}
