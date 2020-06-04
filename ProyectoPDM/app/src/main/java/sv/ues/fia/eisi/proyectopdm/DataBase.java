package sv.ues.fia.eisi.proyectopdm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import sv.ues.fia.eisi.proyectopdm.dao.EscuelaDao;
import sv.ues.fia.eisi.proyectopdm.dao.LocalDao;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.CargaAcademica;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;
import sv.ues.fia.eisi.proyectopdm.db.entity.CicloAsignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Inscripcion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision_Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;


/*
    En esta clase es donde creamos cada una de las entity's que usaremos y definir la version de la base
    como en SQLiteOpenHelper
 */
@Database(entities = {
        //Aca se definen todas las entidades que vamos a utlizar junto con la version vigente de la BD
        Alumno.class, AreaAdm.class, Asignatura.class, CargaAcademica.class, Cargo.class,
        Ciclo.class, CicloAsignatura.class, DetalleEvaluacion.class, Docente.class,
        EncargadoImpresion.class, Escuela.class, Evaluacion.class, Inscripcion.class,
        Local.class, PrimeraRevision.class, SegundaRevision.class, SegundaRevision_Docente.class,
        SolicitudExtraordinario.class, SolicitudImpresion.class, TipoEvaluacion.class,
    }, version = 2)
public abstract class DataBase extends RoomDatabase {

    private static DataBase instance;

    //Este atributo es para poder acceder a la clase Dao
    public abstract EscuelaDao escuelaDao();
    public abstract LocalDao localDao();

    /*
        synchronized garantiza el patron singleton para que solo haya una instancia de una clase
        es util para cuando todos los usuarios esten usando la misma instancia
     */
    public static synchronized DataBase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class,"PDMDataBase").fallbackToDestructiveMigration().addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    /*
        En este metodo particularmente solo hemos creado la base de datos en onCreate y se asigna
        cada uno de los valores conel metodo PoblarBDAsyncTask a la instancia
     */
    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PoblarDBAsyncTask(instance).execute();
        }
    };


    //Aca llenamos la base de datos con los objetos dao y las operaciones asignadas
    private static class PoblarDBAsyncTask extends AsyncTask<Void, Void, Void>{
        private EscuelaDao escuelaDao;
        private LocalDao localDao;

        private PoblarDBAsyncTask(DataBase db){
            escuelaDao=db.escuelaDao();
            localDao=db.localDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            escuelaDao.insert(new Escuela(1,"Escuela de Ingeniería de Sistemas Informáticos"));
            escuelaDao.insert(new Escuela(2,"Escuela de Ingeniería Industrial"));
            escuelaDao.insert(new Escuela(3,"Escuela de Ingeniería Eléctrica"));
            escuelaDao.insert(new Escuela(4,"Escuela de Ingeniería Civil"));
            escuelaDao.insert(new Escuela(5,"Escuela de Ingeniería Química y Alimentos"));
            escuelaDao.insert(new Escuela(6,"Escuela de Arquitectura"));
            escuelaDao.insert(new Escuela(7,"Escuela de Ingeniería Mecánica"));
            localDao.insertarLocal(new Local("LComp1","Laboratorio 1","Escuela de Ingeniería Industrial", 13.711282d, -89.200222d));
            localDao.insertarLocal(new Local("BIB301","Salón 1 de la biblioteca","Biblioteca de Ingeniería y Arquitectura", 13.720434d, -89.202106d));
            localDao.insertarLocal(new Local("D11","Aula D11", "Edificio D", 13.72077d,-89.200545));
            localDao.insertarLocal(new Local("F2","Laboratorio UCB F2", "Unidad de Ciencias Básicas",13.720003d,-89.200787d));

            return null;
        }
    }
}
