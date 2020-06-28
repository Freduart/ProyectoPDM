package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "AccesoUsuario")
public class AccesoUsuario {
    @PrimaryKey(autoGenerate = true)
    private int idAccesoUsuario;
    @ForeignKey(
            entity = Usuario.class,
            parentColumns = "idUsuario",
            childColumns = "idUsuarioFK"
    )
    private int idUsuarioFK;
    @ForeignKey(
            entity = OpcionCrud.class,
            parentColumns = "idOpcion",
            childColumns = "idOpcionFK"
    )
    private int idOpcionFK;

    public AccesoUsuario(int idUsuarioFK, int idOpcionFK) {
        this.idUsuarioFK = idUsuarioFK;
        this.idOpcionFK = idOpcionFK;
    }

    public int getIdAccesoUsuario() {
        return idAccesoUsuario;
    }

    public void setIdAccesoUsuario(int idAccesoUsuario) {
        this.idAccesoUsuario = idAccesoUsuario;
    }

    public int getIdUsuarioFK() {
        return idUsuarioFK;
    }

    public void setIdUsuarioFK(int idUsuarioFK) {
        this.idUsuarioFK = idUsuarioFK;
    }

    public int getIdOpcionFK() {
        return idOpcionFK;
    }

    public void setIdOpcionFK(int idOpcionFK) {
        this.idOpcionFK = idOpcionFK;
    }
}
