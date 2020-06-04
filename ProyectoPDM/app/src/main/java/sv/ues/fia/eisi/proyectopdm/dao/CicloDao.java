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
    void insert(Ciclo ciclo);

    @Update
    void update(Ciclo ciclo);

    @Delete
    void delete(Ciclo ciclo);

    @Query("delete from Ciclo")
    void borrarCiclo();

    @Query("select * from Ciclo")
    LiveData<List<Ciclo>> obtenerCiclos();
}
