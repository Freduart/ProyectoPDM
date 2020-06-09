package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "SolicitudImpresion")
public class SolicitudImpresion {

    @PrimaryKey(autoGenerate = true)
    private int idImpresion;
    @ForeignKey(
            entity = Docente.class,
            parentColumns = "carnetDocente",
            childColumns = "carnetDocenteFK"
    )@NonNull
    private String carnetDocenteFK;
    @ForeignKey(
            entity = EncargadoImpresion.class,
            parentColumns = "idEncargado",
            childColumns = "idEncargadoFK"
    )
    private int idEncargadoFK;
    @ForeignKey(
            entity = Docente.class,
            parentColumns = "carnetDocente",
            childColumns = "DocDirector"
    )@NonNull
    private String DocDirector;
    private int numImpresiones;
    private String detalleImpresion;
    private String resultadoImpresion;
    private String estadoSolicitud;
    private String fechaSolicitud;
    private String documento;

    public SolicitudImpresion() {
    }

    public SolicitudImpresion(@NonNull String carnetDocenteFK, int idEncargadoFK, @NonNull String docDirector, int numImpresiones, String detalleImpresion, String resultadoImpresion, String estadoSolicitud, String fechaSolicitud, String documento) {
        this.carnetDocenteFK = carnetDocenteFK;
        this.idEncargadoFK = idEncargadoFK;
        DocDirector = docDirector;
        this.numImpresiones = numImpresiones;
        this.detalleImpresion = detalleImpresion;
        this.resultadoImpresion = resultadoImpresion;
        this.estadoSolicitud = estadoSolicitud;
        this.fechaSolicitud = fechaSolicitud;
        this.documento=documento;
    }

    public int getIdImpresion() {
        return idImpresion;
    }

    public void setIdImpresion(int idImpresion) {
        this.idImpresion = idImpresion;
    }

    @NonNull
    public String getCarnetDocenteFK() {
        return carnetDocenteFK;
    }

    public void setCarnetDocenteFK(@NonNull String carnetDocenteFK) {
        this.carnetDocenteFK = carnetDocenteFK;
    }

    public int getIdEncargadoFK() {
        return idEncargadoFK;
    }

    public void setIdEncargadoFK(int idEncargadoFK) {
        this.idEncargadoFK = idEncargadoFK;
    }

    @NonNull
    public String getDocDirector() {
        return DocDirector;
    }

    public void setDocDirector(@NonNull String docDirector) {
        DocDirector = docDirector;
    }

    public int getNumImpresiones() {
        return numImpresiones;
    }

    public void setNumImpresiones(int numImpresiones) {
        this.numImpresiones = numImpresiones;
    }

    public String getDetalleImpresion() {
        return detalleImpresion;
    }

    public void setDetalleImpresion(String detalleImpresion) {
        this.detalleImpresion = detalleImpresion;
    }

    public String getResultadoImpresion() {
        return resultadoImpresion;
    }

    public void setResultadoImpresion(String resultadoImpresion) {
        this.resultadoImpresion = resultadoImpresion;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
