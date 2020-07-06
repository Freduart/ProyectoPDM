package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

@Dao
public interface SolicitudImpresionDao {

    @Insert
    void insertSolicitudImpresion(SolicitudImpresion solicitudImpresion);

    @Update
    void updateSolicitudImpresion(SolicitudImpresion solicitudImpresion);

    @Delete
    void deleteSolicitudImpreison(SolicitudImpresion solicitudImpresion);


    @Query("delete from SolicitudImpresion")
    void borrarSolicitudesImpresion();

    @Query("select * from SolicitudImpresion")
    LiveData<List<SolicitudImpresion>> obtenerSolicitudesImpresion();

    @Query("select * from SolicitudImpresion where idImpresion == :idSolicitudImpresion")
    SolicitudImpresion obtenerSolicitudImpresion(int idSolicitudImpresion);

    @Query("select * from SolicitudImpresion where estadoSolicitud == :estadoSolicitudImpresion")
    LiveData<List<SolicitudImpresion>> obtenerSolicitudesPorEstado(String estadoSolicitudImpresion);

    @Query("select * from SolicitudImpresion where carnetDocenteFK == :carnetDocente")
    LiveData<List<SolicitudImpresion>> obtenerSolicitudesPorCarnet(String carnetDocente);

    @Query("select * from SolicitudImpresion where DocDirector == :docDirector")
    LiveData<List<SolicitudImpresion>> obtenerSolicitudesPorDirector(String docDirector);

    @Query("delete from SolicitudImpresion where idImpresion=:idImpresion")
    void eliminarSolicitudPorId(String idImpresion);
}
