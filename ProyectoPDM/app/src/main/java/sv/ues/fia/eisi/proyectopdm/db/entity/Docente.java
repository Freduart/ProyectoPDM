package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Docente")
public class Docente {

    @PrimaryKey
    @NonNull
    private String carnetDocente;
    @ForeignKey(
            entity = Cargo.class,
            parentColumns = "idCargo",
            childColumns = "idCargoFK"
    )
    private int idCargoFK;
    private String nomDocente;
    private String apellidoDocente;
    private String correoDocente;
    private String telefonoDocente;


    public Docente(@NonNull String carnetDocente, int idCargoFK, String nomDocente, String apellidoDocente, String correoDocente, String telefonoDocente) {
        this.carnetDocente = carnetDocente;
        this.idCargoFK = idCargoFK;
        this.nomDocente = nomDocente;
        this.apellidoDocente = apellidoDocente;
        this.correoDocente = correoDocente;
        this.telefonoDocente = telefonoDocente;
    }

    @NonNull
    public String getCarnetDocente() {
        return carnetDocente;
    }

    public void setCarnetDocente(@NonNull String carnetDocente) {
        this.carnetDocente = carnetDocente;
    }

    public int getIdCargoFK() {
        return idCargoFK;
    }

    public void setIdCargoFK(int idCargoFK) {
        this.idCargoFK = idCargoFK;
    }

    public String getNomDocente() {
        return nomDocente;
    }

    public void setNomDocente(String nomDocente) {
        this.nomDocente = nomDocente;
    }

    public String getApellidoDocente() {
        return apellidoDocente;
    }

    public void setApellidoDocente(String apellidoDocente) {
        this.apellidoDocente = apellidoDocente;
    }

    public String getCorreoDocente() {
        return correoDocente;
    }

    public void setCorreoDocente(String correoDocente) {
        this.correoDocente = correoDocente;
    }

    public String getTelefonoDocente() {
        return telefonoDocente;
    }

    public void setTelefonoDocente(String telefonoDocente) {
        this.telefonoDocente = telefonoDocente;
    }
}
