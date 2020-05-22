package sv.ues.fia.eisi.proyectopdm.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "SegundaRevision_Docente",
        primaryKeys = {"carnetDocenteFK","idSegundaRevisionFK"}
)
public class SegundaRevision_Docente {

    @ForeignKey(
            entity = Docente.class,
            parentColumns = "carnetDocente",
            childColumns = "carnetDocenteFK"
    )@NonNull
    private String carnetDocenteFK;
    @ForeignKey(
            entity = SegundaRevision.class,
            parentColumns = "idSegundaRevision",
            childColumns = "idSegundaRevisionFK"
    )@NonNull
    private String idSegundaRevisionFK;


    public SegundaRevision_Docente(String carnetDocenteFK, String idSegundaRevisionFK) {
        this.carnetDocenteFK = carnetDocenteFK;
        this.idSegundaRevisionFK = idSegundaRevisionFK;
    }

    public String getCarnetDocenteFK() {
        return carnetDocenteFK;
    }

    public void setCarnetDocenteFK(String carnetDocenteFK) {
        this.carnetDocenteFK = carnetDocenteFK;
    }

    public String getIdSegundaRevisionFK() {
        return idSegundaRevisionFK;
    }

    public void setIdSegundaRevisionFK(String idSegundaRevisionFK) {
        this.idSegundaRevisionFK = idSegundaRevisionFK;
    }

}
