package sv.ues.fia.eisi.proyectopdm.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.DataBase;
import sv.ues.fia.eisi.proyectopdm.dao.InscripcionDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Inscripcion;

public class InscripcionRepository {
    private InscripcionDao inscripcionDao;
    private LiveData<List<Inscripcion>> todasInscripciones;

    public InscripcionRepository(Application application) {
        DataBase base=DataBase.getInstance(application);
        inscripcionDao=base.inscripcionDao();
        todasInscripciones=inscripcionDao.obtenerInscripciones();
    }

}
