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
    void insert(Docente docente);

    @Update
    void update(Docente docente);

    @Delete
    void delete(Docente docente);

    @Query("delete from Docente")
    void borrarDocente();

    @Query("select * from Docente")
    LiveData<List<Docente>> obtenerDocentes();}
