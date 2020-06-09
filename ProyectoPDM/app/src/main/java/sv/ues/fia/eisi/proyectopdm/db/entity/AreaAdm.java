package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "AreaAdm")
public class AreaAdm {

    @PrimaryKey(autoGenerate = true)
    private int idDeptarmento;
    @ForeignKey(
            entity = Escuela.class,
            parentColumns = "idEscuela",
            childColumns = "idEscuelaFK"
    )
    private int idEscuelaFK;
    private String nomDepartamento;


    public AreaAdm(int idEscuelaFK, String nomDepartamento) {
        this.idEscuelaFK = idEscuelaFK;
        this.nomDepartamento = nomDepartamento;
    }

    public int getIdDeptarmento() {
        return idDeptarmento;
    }

    public void setIdDeptarmento(int idDeptarmento) {
        this.idDeptarmento = idDeptarmento;
    }

    public int getIdEscuelaFK() {
        return idEscuelaFK;
    }

    public void setIdEscuelaFK(int idEscuelaFK) {
        this.idEscuelaFK = idEscuelaFK;
    }

    public String getNomDepartamento() {
        return nomDepartamento;
    }

    public void setNomDepartamento(String nomDepartamento) {
        this.nomDepartamento = nomDepartamento;
    }
}
