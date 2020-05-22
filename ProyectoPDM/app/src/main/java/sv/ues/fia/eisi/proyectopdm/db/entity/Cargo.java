package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Cargo")
public class Cargo {

    @PrimaryKey(autoGenerate = true)
    private int idCargo;
    @ForeignKey(
            entity = Escuela.class,
            parentColumns = "idEscuela",
            childColumns ="idEscuelaFK"
    )
    private int idEscuelaFK;
    private String nomCargo;


    public Cargo(int idCargo, int idEscuelaFK, String nomCargo) {
        this.idCargo = idCargo;
        this.idEscuelaFK = idEscuelaFK;
        this.nomCargo = nomCargo;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public int getIdEscuelaFK() {
        return idEscuelaFK;
    }

    public void setIdEscuelaFK(int idEscuelaFK) {
        this.idEscuelaFK = idEscuelaFK;
    }

    public String getNomCargo() {
        return nomCargo;
    }

    public void setNomCargo(String nomCargo) {
        this.nomCargo = nomCargo;
    }
}
