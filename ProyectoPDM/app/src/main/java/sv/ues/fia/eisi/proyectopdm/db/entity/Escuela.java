package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Escuela")
public class Escuela {

    @PrimaryKey(autoGenerate = true)
    private int idEscuela;
    private String nomEscuela;

    public Escuela(int idEscuela, String nomEscuela) {
        this.idEscuela = idEscuela;
        this.nomEscuela = nomEscuela;
    }

    public int getIdEscuela() {
        return idEscuela;
    }

    public void setIdEscuela(int idEscuela) {
        this.idEscuela = idEscuela;
    }

    public String getNomEscuela() {
        return nomEscuela;
    }

    public void setNomEscuela(String nomEscuela) {
        this.nomEscuela = nomEscuela;
    }
}