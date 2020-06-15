package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "EncargadoImpresion")
public class EncargadoImpresion {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int idEncargadoImpresion;
    private String nomEncargado;


    public EncargadoImpresion( String nomEncargado) {
        this.nomEncargado = nomEncargado;
    }

    public int getIdEncargadoImpresion() {
        return idEncargadoImpresion;
    }

    public void setIdEncargadoImpresion(int idEncargadoImpresion) {
        this.idEncargadoImpresion = idEncargadoImpresion;
    }

    public String getNomEncargado() {
        return nomEncargado;
    }

    public void setNomEncargado(String nomEncargado) {
        this.nomEncargado = nomEncargado;
    }
}
