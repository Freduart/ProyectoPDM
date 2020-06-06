package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;

@Dao
public interface CicloDao {
    @Insert
    void insertCiclo(Ciclo ciclo);

    @Update
    void updateCiclo(Ciclo ciclo);

    @Delete
    void deleteCiclo(Ciclo ciclo);

    @Query("delete from Ciclo")
    void borrarCiclos();

    @Query("select * from Ciclo")
    LiveData<List<Ciclo>> obtenerCiclos();

    @Query("select * from Ciclo where idCiclo == :cicloid")
    Ciclo obtenerCiclo(int cicloid);
}
