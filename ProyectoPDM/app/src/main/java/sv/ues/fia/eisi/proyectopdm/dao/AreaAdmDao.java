package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;

@Dao
public interface AreaAdmDao {
    @Insert
    void insertAreaAdm(AreaAdm areaAdm);

    @Update
    void updateAreaAdm(AreaAdm areaAdm);

    @Delete
    void deleteAreaAdm(AreaAdm areaAdm);

    @Query("delete from AreaAdm")
    void borrarAreas();

    @Query("select * from AreaAdm")
    LiveData<List<AreaAdm>> obtenerAreas();

    @Query("select * from AreaAdm where idDeptarmento == :areaid")
    AreaAdm obtenerAreaAdm(int areaid);

    @Query("select AreaAdm.* from AreaAdm " +
            "inner join Escuela on Escuela.idEscuela=AreaAdm.idEscuelaFK "+
            "inner join Cargo on Escuela.idEscuela=Cargo.idEscuelaFK "+
            "inner join Docente on Cargo.idCargo=Docente.idCargoFK "+
            "where Docente.carnetDocente=:id")
    LiveData<List<AreaAdm>> obtenerAreasDesdeDocente(String id);
}
