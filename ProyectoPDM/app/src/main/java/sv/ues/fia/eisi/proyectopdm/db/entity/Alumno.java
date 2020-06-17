package sv.ues.fia.eisi.proyectopdm.db.entity;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "Alumno")
public class Alumno {

    @PrimaryKey
    @NonNull
    private String carnetAlumno;
    private String nombre;
    private String apellido;
    private String carrera;
    private String correo;
    @ForeignKey(
            entity = Usuario.class,
            parentColumns = "idUsuario",
            childColumns = "idUsuarioFk"
    )@NonNull
    private int idUsuarioFk;


    public Alumno(@NonNull String carnetAlumno, String nombre, String apellido, String carrera, String correo, int idUsuarioFk) {
        this.carnetAlumno = carnetAlumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carrera = carrera;
        this.correo=correo;
        this.idUsuarioFk = idUsuarioFk;
    }

    @NonNull
    public String getCarnetAlumno() {
        return carnetAlumno;
    }

    public void setCarnetAlumno(@NonNull String carnetAlumno) {
        this.carnetAlumno = carnetAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @NonNull
    public int getIdUsuarioFk() {
        return idUsuarioFk;
    }

    public void setIdUsuarioFk(@NonNull int idUsuarioFk) {
        this.idUsuarioFk = idUsuarioFk;
    }

    @NonNull
    @Override
    public String toString() {
        return getCarnetAlumno() + " - " + getNombre() + " " + getApellido();
    }
}
