package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

@Dao
public interface LocalDao {

    @Insert
    void insertarLocal(Local local);

    @Update
    void updateLocal(Local local);

    @Delete
    void deleteLocal(Local local);


    /*
        En este Query nosotros borramos todos los datos que contenga la tabla Escuela
        Para borrar uno en especifico necesitaremos usar el id en el Query como un delete de SQL
     */
    @Query("delete from Local ")
    void borrarLocales();

    /*
        LiveData tiene ventajas como mostrar los datos siempre actualizados en la app usando ROOM
     */
    @Query("Select * from Local")
    LiveData<List<Local>> obtenerLocales();

    @Query("Select * from Local where idLocal == :localid")
    Local obtenerLocal(String localid);
}
