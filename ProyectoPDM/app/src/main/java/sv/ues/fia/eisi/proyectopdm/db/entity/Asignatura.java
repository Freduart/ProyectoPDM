package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Asignatura")
public class Asignatura {

    @PrimaryKey
    @NonNull
    private String codigoAsignatura;
    @ForeignKey(
            entity = AreaAdm.class,
            parentColumns = "idDepartamento",
            childColumns = "idDepartamentoFK"
    )
    private int idDepartamentoFK;
    private String nomasignatura;


    public Asignatura(@NonNull String codigoAsignatura, int idDepartamentoFK, String nomasignatura) {
        this.codigoAsignatura = codigoAsignatura;
        this.idDepartamentoFK = idDepartamentoFK;
        this.nomasignatura = nomasignatura;
    }

    @NonNull
    public String getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(@NonNull String codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    public int getIdDepartamentoFK() {
        return idDepartamentoFK;
    }

    public void setIdDepartamentoFK(int idDepartamentoFK) {
        this.idDepartamentoFK = idDepartamentoFK;
    }

    public String getNomasignatura() {
        return nomasignatura;
    }

    public void setNomasignatura(String nomasignatura) {
        this.nomasignatura = nomasignatura;
    }
}
