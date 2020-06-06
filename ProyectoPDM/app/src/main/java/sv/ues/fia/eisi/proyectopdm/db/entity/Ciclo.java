package sv.ues.fia.eisi.proyectopdm.db.entity;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Ciclo")
public class Ciclo {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int idCiclo;
    private String fechaDesde;
    private String fechaHasta;
    private int nomCiclo;

    public Ciclo(int idCiclo, String fechaDesde, String fechaHasta, int nomCiclo) {
        this.idCiclo = idCiclo;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.nomCiclo = nomCiclo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public int getNomCiclo() {
        return nomCiclo;
    }

    public void setNomCiclo(int nomCiclo) {
        this.nomCiclo = nomCiclo;
    }
}
