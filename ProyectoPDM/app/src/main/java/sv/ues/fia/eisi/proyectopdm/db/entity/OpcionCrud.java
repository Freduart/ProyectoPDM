package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "OpcionCrud")
public class OpcionCrud {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int idOpcion;
    @NonNull
    private String descOpcion;
    @NonNull
    private int numCrud;

    /*
    * 0-Menu
    * 1-Edicion
    * 2-Creacion
    * 3-Eliminacion
    * */

    public OpcionCrud(@NonNull String descOpcion, int numCrud) {
        this.descOpcion = descOpcion;
        this.numCrud = numCrud;
    }

    public int getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(int idOpcion) {
        this.idOpcion = idOpcion;
    }

    @NonNull
    public String getDescOpcion() {
        return descOpcion;
    }

    public void setDescOpcion(@NonNull String descOpcion) {
        this.descOpcion = descOpcion;
    }

    public int getNumCrud() {
        return numCrud;
    }

    public void setNumCrud(int numCrud) {
        this.numCrud = numCrud;
    }
}
