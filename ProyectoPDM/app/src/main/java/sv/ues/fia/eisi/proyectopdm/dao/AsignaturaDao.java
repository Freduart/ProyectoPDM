package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;

@Dao
public interface AsignaturaDao {

    @Insert
    void insertAsignatura(Asignatura asignatura);

    @Update
    void updateAsignatura(Asignatura asignatura);

    @Delete
    void borrarAsignatura(Asignatura asignatura);

    /*
        En este Query nosotros borramos todos los datos que contenga la tabla Asignatura
        Para borrar uno en especifico necesitaremos usar el id en el Query como un delete de SQL
     */
    @Query("delete from Asignatura ")
    void borrarAsignaturas();

    /*
        LiveData tiene ventajas como mostrar los datos siempre actualizados en la app usando ROOM
     */
    @Query("Select * from Asignatura")
    LiveData<List<Asignatura>> obtenerAsignaturas();

    @Query("select * from Asignatura where codigoAsignatura == :asignaturaid")
    Asignatura obtenerAsignatura(String asignaturaid);
}
