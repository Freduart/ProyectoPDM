package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.room.Dao;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;

@Dao
public interface SolicitudExtraordinarioDao {

    @Insert
    void insertSolicitudExtraordinario(SolicitudExtraordinario solicitudExtraordinario);

    @Update
    void updateSolicitudExtraordinario(SolicitudExtraordinario solicitudExtraordinario);

    @Delete
    void deleteSolicitudExtraordinario(SolicitudExtraordinario solicitudExtraordinario);

    /*
        En este Query nosotros borramos todos los datos que contenga la tabla SolicitudExtraordinario
        Para borrar uno en especifico necesitaremos usar el id en el Query como un delete de SQL
     */

    @Query("delete from SolicitudExtraordinario")
    void borrarSolicitudesExtraordinario();


    /*
        LiveData tiene ventajas como mostrar los datos siempre actualizados en la app usando ROOM
     */
    @Query("Select * from SolicitudExtraordinario")
    LiveData<List<SolicitudExtraordinario>> obtenerSolicitudesExtraordinario();
}
