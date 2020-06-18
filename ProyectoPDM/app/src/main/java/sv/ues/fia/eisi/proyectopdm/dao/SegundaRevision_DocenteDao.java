package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision_Docente;

/*
En esta clase hacemos una insercion a la entidad resultado de la relacion muchos a muchos entre Docente y
SegundaRevision necesitamos relacionar el campo de las llaves compuestas con la llave primaria de la entidad
El primer Query se muestra relacionando el idSegundaRevision de la tabla SegundaRevision con la compuesta
de SegundaRevision_Docente llamada idSegundaRevisionFK luego seleccionamos el campo donde queremos insertar
el dato que seria carnetDocenteFK y se crea la variable en la insercion luego en la lista asignamos esa variable
 */

@Dao
public interface SegundaRevision_DocenteDao {

    @Insert
    void insertSR_Docente(SegundaRevision_Docente segundaRevision_docente);

    @Update
    void updateSR_Docente(SegundaRevision_Docente segundaRevision_docente);

    @Delete
    void deleteSR_Docente(SegundaRevision_Docente segundaRevision_docente);

    @Query("delete from SegundaRevision_Docente")
    void deleteAllSR_Docente();

    @Query("select * from SegundaRevision_Docente")
    LiveData<List<SegundaRevision_Docente>> obtenerSegundaRevision_Docente_Todo();

    @Query("select Docente.* from SegundaRevision " +
            "inner join SegundaRevision_Docente on SegundaRevision.idSegundaRevision=SegundaRevision_Docente.idSegundaRevisionFK "+
            "inner join Docente on Docente.carnetDocente=SegundaRevision_Docente.carnetDocenteFK "+
            "where SegundaRevision_Docente.idSegundaRevisionFK=:id")
    List<Docente> getDocentes(final int id);

    @Query("select SegundaRevision.* from Docente " +
            "inner join SegundaRevision_Docente on Docente.carnetDocente=SegundaRevision_Docente.carnetDocenteFK "+
            "inner join SegundaRevision on SegundaRevision.idSegundaRevision=SegundaRevision_Docente.idSegundaRevisionFK "+
            "where SegundaRevision_Docente.carnetDocenteFK=:id")
    List<SegundaRevision> getSegundaRevisions(final String id);

    @Query("select * from SegundaRevision_Docente where idSegundaRevisionFK == :segundarevisionid and carnetDocenteFK == :docenteid")
    SegundaRevision_Docente obtenerSegRev_Docente(int segundarevisionid, String docenteid);

    @Query("select Cargo.* from Docente " +
            "inner join SegundaRevision_Docente on Docente.carnetDocente=SegundaRevision_Docente.carnetDocenteFK "+
            "inner join SegundaRevision on SegundaRevision.idSegundaRevision=SegundaRevision_Docente.idSegundaRevisionFK "+
            "inner join Cargo on Cargo.idCargo=Docente.idCargoFK "+
            "where SegundaRevision_Docente.carnetDocenteFK=:id")
    List<Cargo> getCargosDeDocentesEnSR(final String id);

    @Query("select SegundaRevision_Docente.* from SegundaRevision_Docente " +
            "inner join SegundaRevision on SegundaRevision.idSegundaRevision=SegundaRevision_Docente.idSegundaRevisionFK " +
            "where SegundaRevision.idSegundaRevision= :segundarevisionid")
    LiveData<List<SegundaRevision_Docente>> obtenerSegundaRevision_DocenteConSR(int segundarevisionid);

    @Query("select SegundaRevision_Docente.* from Docente " +
            "inner join SegundaRevision_Docente on Docente.carnetDocente=SegundaRevision_Docente.carnetDocenteFK "+
            "where Docente.carnetDocente=:id")
    List<SegundaRevision_Docente> getSR_DconDoc(final String id);
}
