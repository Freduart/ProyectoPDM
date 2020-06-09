package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Escuela")
public class Escuela {

    @PrimaryKey(autoGenerate = true)
    private int idEscuela;
    private String nomEscuela;
    private String carrera;

    public Escuela(String nomEscuela,String carrera) {
        this.nomEscuela = nomEscuela;
        this.carrera=carrera;
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

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}