package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Bitacora")
public class Bitacora {
    @PrimaryKey(autoGenerate = true)
    private int idBitacora;
    @NonNull
    private String idObjeto;
    @NonNull
    private String nomTabla;
    @NonNull
    private String operacion;

    public Bitacora(){}

    public Bitacora(@NonNull String idObjeto, @NonNull String nomTabla, @NonNull String operacion) {
        this.idObjeto = idObjeto;
        this.nomTabla = nomTabla;
        this.operacion = operacion;
    }

    public int getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(int idBitacora) {
        this.idBitacora = idBitacora;
    }

    @NonNull
    public String getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(@NonNull String idObjeto) {
        this.idObjeto = idObjeto;
    }

    @NonNull
    public String getNomTabla() {
        return nomTabla;
    }

    public void setNomTabla(@NonNull String nomTabla) {
        this.nomTabla = nomTabla;
    }

    @NonNull
    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(@NonNull String operacion) {
        this.operacion = operacion;
    }

    public String toString(){
        return idObjeto+" - "+nomTabla+" - "+operacion;
    }
}
