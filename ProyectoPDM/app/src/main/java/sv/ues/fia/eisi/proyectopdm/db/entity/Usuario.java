package sv.ues.fia.eisi.proyectopdm.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Usuario")
public class Usuario implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int idUsuario;

    private String nombreUsuario;
    private String clave;
    private int rol;
    /*
    * 1 = Director
    * 2 = Docente
    * 3 = Alumno
    * 4 = Encargado Impresiones
    * 5 = Administrador
    * */

    public Usuario(String nombreUsuario, String clave, int rol){
        this.nombreUsuario= nombreUsuario;
        this.clave=clave;
        this.rol=rol;
    }

    @NonNull
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(@NonNull int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", clave='" + clave + '\'' +
                ", rol=" + rol +
                '}';
    }
}
