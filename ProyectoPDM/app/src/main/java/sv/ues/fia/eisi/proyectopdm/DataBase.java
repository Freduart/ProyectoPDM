package sv.ues.fia.eisi.proyectopdm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import sv.ues.fia.eisi.proyectopdm.dao.AlumnoDao;
import sv.ues.fia.eisi.proyectopdm.dao.AreaAdmDao;
import sv.ues.fia.eisi.proyectopdm.dao.AsignaturaDao;
import sv.ues.fia.eisi.proyectopdm.dao.CargoDao;
import sv.ues.fia.eisi.proyectopdm.dao.CicloDao;
import sv.ues.fia.eisi.proyectopdm.dao.DocenteDao;
import sv.ues.fia.eisi.proyectopdm.dao.EncargadoImpresionDao;
import sv.ues.fia.eisi.proyectopdm.dao.EscuelaDao;
import sv.ues.fia.eisi.proyectopdm.dao.InscripcionDao;
import sv.ues.fia.eisi.proyectopdm.dao.LocalDao;
import sv.ues.fia.eisi.proyectopdm.dao.SegundaRevision_DocenteDao;
import sv.ues.fia.eisi.proyectopdm.dao.EvaluacionDao;
import sv.ues.fia.eisi.proyectopdm.dao.SolicitudExtraordinarioDao;
import sv.ues.fia.eisi.proyectopdm.dao.TipoEvaluacionDao;
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
    }, version = 3)
public abstract class DataBase extends RoomDatabase {

    private static DataBase instance;

    //Este atributo es para poder acceder a la clase Dao
    public abstract AlumnoDao alumnoDao();
    public abstract AreaAdmDao areaAdmDao();
    public abstract AsignaturaDao asignaturaDao();
    public abstract CargoDao cargoDao();
    public abstract CicloDao cicloDao();
    public abstract DocenteDao docenteDao();
    public abstract EncargadoImpresionDao encargadoImpresionDao();
    public abstract EscuelaDao escuelaDao();
    public abstract InscripcionDao inscripcionDao();
    public abstract LocalDao localDao();
    public abstract TipoEvaluacionDao tipoEvaluacionDao();
    public abstract SolicitudExtraordinarioDao solicitudExtraordinarioDao();
    public abstract EvaluacionDao evaluacionDao();
    /*
        synchronized garantiza el patron singleton para que solo haya una instancia de una clase
        es util para cuando todos los usuarios esten usando la misma instancia
     */
    public static synchronized DataBase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class,"PDMDataBaseVer2").fallbackToDestructiveMigration().addCallback(roomCallback)
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
        private AlumnoDao alumnoDao;
        private AreaAdmDao areaAdmDao;
        private AsignaturaDao asignaturaDao;
        private CargoDao cargoDao;
        private CicloDao cicloDao;
        private DocenteDao docenteDao;
        private EncargadoImpresionDao encargadoImpresionDao;
        private EscuelaDao escuelaDao;
        private InscripcionDao inscripcionDao;
        private LocalDao localDao;
        private TipoEvaluacionDao tipoEvaluacionDao;
        private SolicitudExtraordinarioDao solicitudExtraordinarioDao;
        private EvaluacionDao evaluacionDao;

        private PoblarDBAsyncTask(DataBase db){
            escuelaDao=db.escuelaDao();
            areaAdmDao=db.areaAdmDao();
            asignaturaDao=db.asignaturaDao();
            alumnoDao=db.alumnoDao();
            inscripcionDao=db.inscripcionDao();
            cicloDao=db.cicloDao();
            tipoEvaluacionDao=db.tipoEvaluacionDao();
            cargoDao=db.cargoDao();
            docenteDao=db.docenteDao();
            localDao=db.localDao();
            encargadoImpresionDao=db.encargadoImpresionDao();
            solicitudExtraordinarioDao=db.solicitudExtraordinarioDao();
            evaluacionDao=db.evaluacionDao();
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
            areaAdmDao.insertAreaAdm(new AreaAdm(1, 1, "Dpto de Comunicaciones y CC.de la Computación"));
            areaAdmDao.insertAreaAdm(new AreaAdm(2, 1, "Dpto. de Programación y Manejo de datos"));
            areaAdmDao.insertAreaAdm(new AreaAdm(3, 1, "Dpto. de Desarrollo de Sistemas"));
            areaAdmDao.insertAreaAdm(new AreaAdm(4, 1, "Dpto. de Administración"));
            areaAdmDao.insertAreaAdm(new AreaAdm(5, 5, "General"));
            areaAdmDao.insertAreaAdm(new AreaAdm(6, 5, "Dpto. de CC. Básicas de la Ingeniería Quíimica"));
            areaAdmDao.insertAreaAdm(new AreaAdm(7, 5, "Dpto. de Ciencias de la Ingeniería Química"));
            asignaturaDao.insertAsignatura(new Asignatura("DSI115", 3, "Diseño de Sistemas I"));
            asignaturaDao.insertAsignatura(new Asignatura("SGG115", 3, "Sistemas de Información Geográficos"));
            asignaturaDao.insertAsignatura(new Asignatura("PDM115", 2, "Programación para Dispositivos Móviles"));
            asignaturaDao.insertAsignatura(new Asignatura("MIP115", 2, "Microprogramación"));
            asignaturaDao.insertAsignatura(new Asignatura("TAD115", 4, "Teoría Administrativa"));
            asignaturaDao.insertAsignatura(new Asignatura("FQR215", 6, "Fisicoquímica II"));
            asignaturaDao.insertAsignatura(new Asignatura("OPU215", 7, "Operaciones Unitarias II"));
            alumnoDao.insertarAlumno(new Alumno("MM16045", "Fredy Rolando", "Martínez Méndez", "Ingeniería de Sistemas Informáticos","fredymartinezues@gmail.com"));
            alumnoDao.insertarAlumno(new Alumno("BC14026", "Vilma Arely", "Bárcenas Cruz", "Ingeniería de Sistemas Informáticos","vabcgv@outlook.com"));
            alumnoDao.insertarAlumno(new Alumno("PP15001", "Rubén Alejandro", "Pérez Pineda", "Ingeniería de Sistemas Informéticos","rubper@gmail.com"));
            alumnoDao.insertarAlumno(new Alumno("DR17010", "José Efraín", "Díaz Rivas", "Ingeniería de Sistemas Informáticos","efra.00@gmail.com"));
            alumnoDao.insertarAlumno(new Alumno("MG17030", "Jairo Isaac", "Montoya Galdámez", "Ingeniería de Sistemas Informáticos","jairomontoya.raices@gmail.com"));
            alumnoDao.insertarAlumno(new Alumno("MC16022", "Julio Antonio", "Merino Corcio", "Ingeniería Química","prueba@gmail.com"));
            inscripcionDao.insertInscripcion(new Inscripcion("MM16045", "TAD115", 2, 1, 2));
            inscripcionDao.insertInscripcion(new Inscripcion("MM16045", "PDM115", 3, 1, 3));
            inscripcionDao.insertInscripcion(new Inscripcion("MG17030","PDM115", 2, 1, 2));
            inscripcionDao.insertInscripcion(new Inscripcion("MG17030", "MIP115", 1, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("DR17010", "SGG115", 1,2, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("DR17010", "TAD115", 1, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("PP15001", "DS115", 2, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("PP15001", "TAD115", 2, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("BC14026", "PDM115", 2, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("MC16022", "FQR215", 1, 1, 1));
            cicloDao.insertCiclo(new Ciclo(6, "08-08-19", "10-12-19", 6));
            cicloDao.insertCiclo(new Ciclo(7, "17-02-2020", "20-06-2020", 7));
            tipoEvaluacionDao.insertarTipoEv(new TipoEvaluacion(1, "Ordinario"));
            tipoEvaluacionDao.insertarTipoEv(new TipoEvaluacion(2, "Repetido"));
            tipoEvaluacionDao.insertarTipoEv(new TipoEvaluacion(3, "Diferido"));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI1",1,"DS115","asd","111111","111111","asdfaDSASFD","111111",2));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI1",1,"DS115","asd","111111","111111","asdfaDSASFD","111111",2));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI1",1,"DS115","asd","111111","111111","asdfaDSASFD","111111",2));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI1",1,"DS115","asd","111111","111111","asdfaDSASFD","111111",2));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI1",1,"DS115","asd","111111","111111","asdfaDSASFD","111111",2));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI1",1,"DS115","asd","111111","111111","asdfaDSASFD","111111",2));
            cargoDao.insertCargo(new Cargo(1, 1, "Director"));
            cargoDao.insertCargo(new Cargo(2, 1, "Jefe de Dpto. de Comunicaciones y CC. de la Computación"));
            cargoDao.insertCargo(new Cargo(3, 1, "Jefe de Dpto. de Programación y Manejo de datos"));
            cargoDao.insertCargo(new Cargo(4, 1, "Jefe de Dpto. de Desarrollo de Sistemas"));
            cargoDao.insertCargo(new Cargo(5, 1, "Jefa de Dpto. de Administración"));
            cargoDao.insertCargo(new Cargo(6, 1, "Docente EISI"));
            cargoDao.insertCargo(new Cargo(7, 5, "Directora"));
            cargoDao.insertCargo(new Cargo(8, 5, "Jefa de Dpto. De Ingeniería de Alimentos"));
            cargoDao.insertCargo(new Cargo(9, 5, "Jefa de Dpto. de CC. Básicas de la Ingeniería Química"));
            cargoDao.insertCargo(new Cargo(10, 5, "Jefe de Dpto de Ciencias de la Ingeniería Química"));
            cargoDao.insertCargo(new Cargo(11, 5, "DOCENTE EIQA"));
            docenteDao.insertDocente(new Docente("DOCEISI1", 1, "Rudy Wilfredo", "Chicas", "chicas@ues.edu.sv", "78923456"));
            docenteDao.insertDocente(new Docente("DOCEISI2", 2, "Luis", "Escobar Brizuela", "brizuela@ues.edu.sv", "74589012"));
            docenteDao.insertDocente(new Docente("DOCEISI3", 3, "Bladimir", "Díaz Campos", "diaz@ues.edu.sv", "63256113"));
            docenteDao.insertDocente(new Docente("DOCEISI4", 4, "Elmer Arturo", "Carballo Ruiz", "carballo@ues.edu.sv", "68793456"));
            docenteDao.insertDocente(new Docente("DOCEISI5", 5, "Carmeline", "Góchez de Peñate", "gochez@ues.edu.sv", "78945701"));
            docenteDao.insertDocente(new Docente("DOCEISI6", 6, "Cesar Augusto", "González", "gonzalez@ues.edu.sv", "68923457"));
            docenteDao.insertDocente(new Docente("DOCEISI7", 6, "Jorge Enrique", "Iraheta Tobías", "iraheta@ues.edu.sv", "63145679"));
            docenteDao.insertDocente(new Docente("DOCISI8", 6, "José Mauricio", "Bonilla", "bonilla@ues.edu.sv", "68947800"));
            docenteDao.insertDocente(new Docente("DOCEISI9", 6, "Edgar William", "Castellanos Sanchez", "castellanos@ues.edu.sv", "75674678"));
            docenteDao.insertDocente(new Docente("DOCEIQA1", 7, "Tania", "Torres Rivera", "torres@ues.edu.sv", "64589879"));
            docenteDao.insertDocente(new Docente("DOCEIQA2", 8, "Ana Isabel", "Pereira de Ruíz", "pereira@ues.edu.sv", "76564578"));
            docenteDao.insertDocente(new Docente("DOCEIQA3", 9, "Eugenia Salvadora", "Gamero de Ayala", "gamero@ues.edu.sv", "65789034"));
            docenteDao.insertDocente(new Docente("DOCEIQA4", 10, "Miguel Francisco", "Arévalo Martínez", "arevalo@ues.edu.sv", "67365333"));
            docenteDao.insertDocente(new Docente("DOCEIQA5", 11, "Delmy del Carmen", "Rico Peña", "rico@ues.edu.sv", "67887790"));
            localDao.insertarLocal(new Local("LComp1","Laboratorio 1","Escuela de Ingeniería de Sistemas Informáticos", 13.711282d, -89.200222d));
            localDao.insertarLocal(new Local("LComp4", "Laboratorio 2", "Escuela de Ingeniería de Sistemas Informáticos", 13.711282d, -89.200222d));
            localDao.insertarLocal(new Local("BIB301","Salón 1 de la biblioteca","Biblioteca de Ingeniería y Arquitectura", 13.720434d, -89.202106d));
            localDao.insertarLocal(new Local("D11","Aula D11", "Edificio D", 13.72077d,-89.200545));
            localDao.insertarLocal(new Local("F2","Laboratorio UCB F2", "Unidad de Ciencias Básicas",13.720003d,-89.200787d));
            encargadoImpresionDao.insertEncargadoImpresion(new EncargadoImpresion(1, "Pedro Eliseo Peñate"));
            return null;
        }
    }
}
