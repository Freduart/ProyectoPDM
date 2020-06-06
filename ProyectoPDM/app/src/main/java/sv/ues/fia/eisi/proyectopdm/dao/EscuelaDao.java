package sv.ues.fia.eisi.proyectopdm.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

/*
Aqui se controlan las operaciones sobre las entidades
si presionamos Control+B sobre las funcionalidades (@Insert, @Update y @Delete)
veremos como ROOM da la funcionalidad con las interfaces DAO
*/

@Dao
public interface EscuelaDao {

    @Insert
    void insert(Escuela escuela);

    @Update
    void update(Escuela escuela);

    @Delete
    void delete(Escuela escuela);


    /*
        En este Query nosotros borramos todos los datos que contenga la tabla Escuela
        Para borrar uno en especifico necesitaremos usar el id en el Query como un delete de SQL
     */

    @Query("delete from Escuela ")
    void borrarEscuelas();

    /*
        LiveData tiene ventajas como mostrar los datos siempre actualizados en la app usando ROOM
     */
    @Query("Select * from Escuela")
    LiveData<List<Escuela>> obtenerEscuelas();

    @Query("Select * from Escuela where idEscuela == :escuelaid")
    Escuela obtenerEscuela(int escuelaid);
}
