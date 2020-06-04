package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;

@Dao
public interface CargoDao {
    @Insert
    void insert(Cargo cargo);

    @Update
    void update(Cargo cargo);

    @Delete
    void delete(Cargo cargo);

    @Query("delete from Cargo")
    void borrarCargo();

    @Query("select * from Cargo")
    LiveData<List<Cargo>> obtenerCargos();
}
