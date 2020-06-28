package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "Escuela")
public class Escuela {

    @PrimaryKey(autoGenerate = true)
    private int idEscuela;
    private String nomEscuela;
    private String carrera;
    @ForeignKey(
            entity = Docente.class,
            parentColumns = "carnetDocente",
            childColumns = "docDirector"
    )
    private String docDirector;

    public Escuela(String nomEscuela,String carrera,String docDirector) {
        this.nomEscuela = nomEscuela;
        this.carrera=carrera;
        this.docDirector=docDirector;
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

    public String getDocDirector() {
        return docDirector;
    }

    public void setDocDirector(String docDirector) {
        this.docDirector = docDirector;
    }

    @NonNull
    @Override
    public String toString() {
        return idEscuela + ". " + nomEscuela;
    }
}