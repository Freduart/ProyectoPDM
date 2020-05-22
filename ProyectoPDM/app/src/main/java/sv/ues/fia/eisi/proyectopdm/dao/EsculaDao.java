package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

@Dao
public interface EsculaDao {
    @Insert
    void insert(Escuela escuela);

    @Update
    void update(Escuela escuela);

    @Delete
    void delete(Escuela escuela);

    @Query("delete from Escuela")
    void deleteAllEscuelas();

    @Query("select * from Escuela order by  desc")
    List<Escuela> getAllEscuelas();
}
