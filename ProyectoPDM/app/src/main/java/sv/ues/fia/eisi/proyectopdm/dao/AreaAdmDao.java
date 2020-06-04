package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;

@Dao
public interface AreaAdmDao {
    @Insert
    void insert(AreaAdm areaAdm);

    @Update
    void update(AreaAdm areaAdm);

    @Delete
    void delete(AreaAdm areaAdm);

    @Query("delete from AreaAdm")
    void borrarAreaAdm();

    @Query("select * from AreaAdm")
    LiveData<List<AreaAdm>> obtenerAreaAdm();
}
