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
    void insertCargo(Cargo cargo);

    @Update
    void updateCargo(Cargo cargo);

    @Delete
    void deleteCargo(Cargo cargo);

    @Query("delete from Cargo")
    void borrarCargo();

    @Query("select * from Cargo")
    LiveData<List<Cargo>> obtenerCargos();

    @Query("select * from Cargo where idCargo == :cargoid")
    Cargo obtenerCargo(int cargoid);
}
