package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Evaluacion")
public class Evaluacion {

    @PrimaryKey(autoGenerate = true)
    private int idEvaluacion;
    @ForeignKey(
            entity = Docente.class,
            parentColumns = "carnetDocente",
            childColumns = "carnetDocenteFK"
    )
    private String carnetDocenteFK;
    @ForeignKey(
            entity = TipoEvaluacion.class,
            parentColumns = "idTipoEvaluacion",
            childColumns = "idTipoEvaluacionFK"
    )
    private int idTipoEvaluacionFK;
    @ForeignKey(
            entity = Asignatura.class,
            parentColumns = "codigoAsignatura",
            childColumns = "codigoAsignaturaFK"
    )
    private String codigoAsignaturaFK;
    private String nomEvaluacion;
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;
    private String fechaEntregaNotas;
    private int numParticipantes;


    public Evaluacion(String carnetDocenteFK, int idTipoEvaluacionFK, String codigoAsignaturaFK, String nomEvaluacion, String fechaInicio, String fechaFin, String descripcion, String fechaEntregaNotas, int numParticipantes) {
        this.carnetDocenteFK = carnetDocenteFK;
        this.idTipoEvaluacionFK = idTipoEvaluacionFK;
        this.codigoAsignaturaFK = codigoAsignaturaFK;
        this.nomEvaluacion = nomEvaluacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.fechaEntregaNotas = fechaEntregaNotas;
        this.numParticipantes=numParticipantes;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public String getCarnetDocenteFK() {
        return carnetDocenteFK;
    }

    public void setCarnetDocenteFK(String carnetDocenteFK) {
        this.carnetDocenteFK = carnetDocenteFK;
    }

    public int getIdTipoEvaluacionFK() {
        return idTipoEvaluacionFK;
    }

    public void setIdTipoEvaluacionFK(int idTipoEvaluacionFK) {
        this.idTipoEvaluacionFK = idTipoEvaluacionFK;
    }

    public String getCodigoAsignaturaFK() {
        return codigoAsignaturaFK;
    }

    public void setCodigoAsignaturaFK(String codigoAsignatura) {
        this.codigoAsignaturaFK = codigoAsignatura;
    }

    public String getNomEvaluacion() {
        return nomEvaluacion;
    }

    public void setNomEvaluacion(String nomEvaluacion) {
        this.nomEvaluacion = nomEvaluacion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaEntregaNotas() {
        return fechaEntregaNotas;
    }

    public void setFechaEntregaNotas(String fechaEntregaNotas) {
        this.fechaEntregaNotas = fechaEntregaNotas;
    }

    public int getNumParticipantes() {
        return numParticipantes;
    }

    public void setNumParticipantes(int numParticipantes) {
        this.numParticipantes = numParticipantes;
    }
}
