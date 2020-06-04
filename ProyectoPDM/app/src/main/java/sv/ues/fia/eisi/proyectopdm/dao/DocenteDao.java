package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;

@Dao
public interface DocenteDao {
    @Insert
    void insertDocente(Docente docente);

    @Update
    void updateDocente(Docente docente);

    @Delete
    void deleteDocente(Docente docente);

    @Query("delete from Docente")
    void borrarDocente();

    @Query("select * from Docente")
    LiveData<List<Docente>> obtenerDocentes();
}
