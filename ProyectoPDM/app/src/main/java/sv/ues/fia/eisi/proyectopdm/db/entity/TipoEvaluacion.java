package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "TipoEvaluacion")
public class TipoEvaluacion {

    @PrimaryKey(autoGenerate = true)
    private int idTipoEvaluacion;
    private String tipoEvaluacion;

    public TipoEvaluacion(int idTipoEvaluacion, String tipoEvaluacion) {
        this.idTipoEvaluacion = idTipoEvaluacion;
        this.tipoEvaluacion = tipoEvaluacion;

    }

    public int getIdTipoEvaluacion() {
        return idTipoEvaluacion;
    }

    public void setIdTipoEvaluacion(int idTipoEvaluacion) {
        this.idTipoEvaluacion = idTipoEvaluacion;
    }

    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    @NonNull
    @Override
    public String toString() {
        return idTipoEvaluacion + ". " + tipoEvaluacion;
    }
}
