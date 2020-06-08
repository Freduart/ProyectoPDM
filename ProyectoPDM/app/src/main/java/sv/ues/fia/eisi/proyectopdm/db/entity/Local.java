package sv.ues.fia.eisi.proyectopdm.db.entity;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Local")
public class Local {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String idLocal;
    private String nombreLocal;
    private String ubicacion;
    private double latitud;
    private double longitud;

    public Local(@NonNull String idLocal, String nombreLocal, String ubicacion, double latitud, double longitud) {
        this.idLocal = idLocal;
        this.nombreLocal = nombreLocal;
        this.ubicacion = ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
