package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "CargaAcademica",
        primaryKeys = {"carnetDocenteFK","idCicloFK"}
)
public class CargaAcademica {

    @ForeignKey(
            entity=Docente.class,
            parentColumns = "carnetDocente",
            childColumns = "carnetDocenteFK"
    )@NonNull
    private String carnetDocenteFK;
    @ForeignKey(
            entity=Ciclo.class,
            parentColumns = "idCiclo",
            childColumns = "idCicloFK"
    )
    private int idCicloFK;

    public CargaAcademica(String carnetDocenteFK, int idCicloFK) {
        this.carnetDocenteFK = carnetDocenteFK;
        this.idCicloFK = idCicloFK;
    }

    public String getCarnetDocenteFK() {
        return carnetDocenteFK;
    }

    public void setCarnetDocenteFK(String carnetDocenteFK) {
        this.carnetDocenteFK = carnetDocenteFK;
    }

    public int getIdCicloFK() {
        return idCicloFK;
    }

    public void setIdCicloFK(int idCicloFK) {
        this.idCicloFK = idCicloFK;
    }
}
