package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "EncargadoImpresion")
public class EncargadoImpresion {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int idEncargadoImpresion;
    private String nomEncargado;
    @ForeignKey(
            entity = Usuario.class,
            parentColumns = "idUsuario",
            childColumns = "idUsuarioFk"
    )
    @NonNull
    private int idUsuarioFk;


    public EncargadoImpresion( String nomEncargado, @NonNull int idUsuarioFk) {
        this.nomEncargado = nomEncargado;
        this.idUsuarioFk = idUsuarioFk;
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

    @NonNull
    public int getIdUsuarioFk() {
        return idUsuarioFk;
    }

    public void setIdUsuarioFk(@NonNull int idUsuarioFk) {
        this.idUsuarioFk = idUsuarioFk;
    }
}
