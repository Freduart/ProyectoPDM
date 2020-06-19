package sv.ues.fia.eisi.proyectopdm.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

@Dao
public interface PrimeraRevisionDao {
    @Insert
    void insertPrimeraRevision(PrimeraRevision primeraRevision);

    @Update
    void updatePrimeraRevision(PrimeraRevision primeraRevision);

    @Delete
    void deletePrimeraRevision(PrimeraRevision primeraRevision);

    @Query("delete from PrimeraRevision")
    void deleteAllPrimerasRevisiones();

    @Query("select * from PrimeraRevision")
    LiveData<List<PrimeraRevision>> obtenerPrimerasRevisiones();

    @Query("select * from PrimeraRevision where idPrimerRevision == :primerarevisionid")
    PrimeraRevision obtenerPrimeraRevision(int primerarevisionid);

    @Query("select * from PrimeraRevision where idDetalleEvFK == :id")
    List<PrimeraRevision> obtenerRevisionPorDetalle(int id);

   @Query("Select PrimeraRevision.* from PrimeraRevision " +
           "inner join DetalleEvaluacion on PrimeraRevision.idDetalleEvFK=DetalleEvaluacion.idDetalleEv " +
           "inner join Evaluacion on DetalleEvaluacion.idEvaluacionFK=Evaluacion.idEvaluacion " +
           "inner join Docente on Evaluacion.carnetDocenteFK=Docente.carnetDocente " +
           "where Docente.carnetDocente=:carnet")
    LiveData<List<PrimeraRevision>> obtenerPrimerasRevisionesAsDocente(final String carnet);

   @Query("select * from Docente where idUsuarioFk == :id")
    Docente obtenerDocUsuario(int id);
}
