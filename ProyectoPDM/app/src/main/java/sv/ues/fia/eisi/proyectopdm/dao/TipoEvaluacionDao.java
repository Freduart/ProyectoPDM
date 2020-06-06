package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

@Dao
public interface TipoEvaluacionDao {
    @Insert
    void insertarTipoEv(TipoEvaluacion tipoEvaluacion);

    @Update
    void updateTipoEv(TipoEvaluacion tipoEvaluacion);

    @Delete
    void borrarTipoEv(TipoEvaluacion tipoEvaluacion);


    /*
        En este Query nosotros borramos todos los datos que contenga la tabla Alumno
        Para borrar uno en especifico necesitaremos usar el id en el Query como un delete de SQL
     */
    @Query("delete from TipoEvaluacion ")
    void borrarTipos();

    /*
        LiveData tiene ventajas como mostrar los datos siempre actualizados en la app usando ROOM
     */
    @Query("Select * from TipoEvaluacion")
    LiveData<List<TipoEvaluacion>> obtenerTipos();

    @Query("select * from TipoEvaluacion where idTipoEvaluacion == :tipoid")
    TipoEvaluacion obtenerTipoEvaluacion(int tipoid);
}
