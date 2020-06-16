package sv.ues.fia.eisi.proyectopdm.db.entity;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "SolicitudExtraordinario")
public class SolicitudExtraordinario {

    @PrimaryKey(autoGenerate = true)
    private int idSolicitud;
    @ForeignKey(
            entity = Alumno.class,
            parentColumns = "carnetAlumno",
            childColumns = "carnetAlumnoFK"
    )@NonNull
    private String carnetAlumnoFK;
    @ForeignKey(
            entity = Evaluacion.class,
            parentColumns = "idEvaluacion",
            childColumns = "idEvaluacionFK"
    )
    private int idEvaluacion;
    @ForeignKey(
            entity = TipoEvaluacion.class,
            parentColumns = "idTipoEvaluacion",
            childColumns = "tipoSolicitudFK"
    )
    private int tipoSolicitud;
    private String motivoSolicitud;
    private String fechaSolicitudExtr;
    private boolean justificacion;

    public SolicitudExtraordinario(@NonNull String carnetAlumnoFK, int idEvaluacion, int tipoSolicitud, String motivoSolicitud, String fechaSolicitudExtr, boolean justificacion) {
        this.carnetAlumnoFK = carnetAlumnoFK;
        this.idEvaluacion = idEvaluacion;
        this.tipoSolicitud = tipoSolicitud;
        this.motivoSolicitud = motivoSolicitud;
        this.fechaSolicitudExtr = fechaSolicitudExtr;
        this.justificacion = justificacion;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    @NonNull
    public String getCarnetAlumnoFK() {
        return carnetAlumnoFK;
    }

    public void setCarnetAlumnoFK(@NonNull String carnetAlumnoFK) {
        this.carnetAlumnoFK = carnetAlumnoFK;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public int getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(int tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getMotivoSolicitud() {
        return motivoSolicitud;
    }

    public void setMotivoSolicitud(String motivoSolicitud) {
        this.motivoSolicitud = motivoSolicitud;
    }

    public String getFechaSolicitudExtr() {
        return fechaSolicitudExtr;
    }

    public void setFechaSolicitudExtr(String fechaSolicitudExtr) {
        this.fechaSolicitudExtr = fechaSolicitudExtr;
    }

    public boolean isJustificacion() {
        return justificacion;
    }

    public void setJustificacion(boolean justificacion){
        this.justificacion = justificacion;
    }
}
