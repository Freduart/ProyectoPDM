package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "CicloAsignatura",
        primaryKeys = {"codigoAsignaturaFK","idCicloFK"}
)
public class CicloAsignatura {

    @ForeignKey(
            entity = Asignatura.class,
            parentColumns = "codigoAsignatura",
            childColumns = "codigoAsignaturaFK"
    )@NonNull
    private String codigoAsignaturaFK;
    @ForeignKey(
            entity = Ciclo.class,
            parentColumns = "idCiclo",
            childColumns = "idCicloFK"
    )
    private int idCicloFK;
    private String TipoCiclo;

    public CicloAsignatura() {
    }

    public CicloAsignatura(String codigoAsignaturaFK, int idCicloFK, String tipoCiclo) {
        this.codigoAsignaturaFK = codigoAsignaturaFK;
        this.idCicloFK = idCicloFK;
        TipoCiclo = tipoCiclo;
    }

    public String getCodigoAsignaturaFK() {
        return codigoAsignaturaFK;
    }

    public void setCodigoAsignaturaFK(String codigoAsignaturaFK) {
        this.codigoAsignaturaFK = codigoAsignaturaFK;
    }

    public int getIdCicloFK() {
        return idCicloFK;
    }

    public void setIdCicloFK(int idCicloFK) {
        this.idCicloFK = idCicloFK;
    }

    public String getTipoCiclo() {
        return TipoCiclo;
    }

    public void setTipoCiclo(String tipoCiclo) {
        TipoCiclo = tipoCiclo;
    }
}
