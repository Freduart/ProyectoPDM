package sv.ues.fia.eisi.proyectopdm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import sv.ues.fia.eisi.proyectopdm.dao.AlumnoDao;
import sv.ues.fia.eisi.proyectopdm.dao.AreaAdmDao;
import sv.ues.fia.eisi.proyectopdm.dao.AsignaturaDao;
import sv.ues.fia.eisi.proyectopdm.dao.CargoDao;
import sv.ues.fia.eisi.proyectopdm.dao.CicloDao;
import sv.ues.fia.eisi.proyectopdm.dao.DetalleEvaluacionDao;
import sv.ues.fia.eisi.proyectopdm.dao.DocenteDao;
import sv.ues.fia.eisi.proyectopdm.dao.EncargadoImpresionDao;
import sv.ues.fia.eisi.proyectopdm.dao.EscuelaDao;
import sv.ues.fia.eisi.proyectopdm.dao.InscripcionDao;
import sv.ues.fia.eisi.proyectopdm.dao.LocalDao;
import sv.ues.fia.eisi.proyectopdm.dao.PrimeraRevisionDao;
import sv.ues.fia.eisi.proyectopdm.dao.SegundaRevisionDao;
import sv.ues.fia.eisi.proyectopdm.dao.SegundaRevision_DocenteDao;
import sv.ues.fia.eisi.proyectopdm.dao.EvaluacionDao;
import sv.ues.fia.eisi.proyectopdm.dao.SolicitudExtraordinarioDao;
import sv.ues.fia.eisi.proyectopdm.dao.SolicitudImpresionDao;
import sv.ues.fia.eisi.proyectopdm.dao.TipoEvaluacionDao;
import sv.ues.fia.eisi.proyectopdm.dao.UsuarioDao;
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
import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;


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
        SolicitudExtraordinario.class, SolicitudImpresion.class, TipoEvaluacion.class, Usuario.class,
    }, version = 7)
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
    public abstract SolicitudImpresionDao solicitudImpresionDao();
    public abstract SegundaRevisionDao segundaRevisionDao();
    public abstract DetalleEvaluacionDao detalleEvaluacionDao();
    public abstract PrimeraRevisionDao primeraRevisionDao();
    public abstract SegundaRevision_DocenteDao segundaRevision_docenteDao();
    public abstract UsuarioDao usuarioDao();
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
            try {
                super.onCreate(db);
                db.execSQL("create trigger eliminar_solicitudImpr before delete on Docente " +
                        "begin " +
                        "delete from SolicitudImpresion where SolicitudImpresion.carnetDocenteFK=old.carnetDocente; " +
                        "end");
                db.execSQL("create trigger eliminar_asignatura before delete on AreaAdm " +
                        "begin " +
                        "delete from Asignatura where Asignatura.idDepartamentoFK=old.idDeptarmento; " +
                        "end");
                db.execSQL("create trigger eliminar_evaluacion1 before delete on Asignatura " +
                        "begin " +
                        "delete from Evaluacion where Evaluacion.codigoAsignaturaFK=old.codigoAsignatura; " +
                        "end");
                db.execSQL("create trigger eliminar_evaluacion2 before delete on Docente " +
                        "begin " +
                        "delete from Evaluacion where Evaluacion.carnetDocenteFK=old.carnetDocente; " +
                        "end");
                db.execSQL("create trigger eliminar_detalle before delete on Evaluacion \n" +
                        " begin \n" +
                        " delete from DetalleEvaluacion where DetalleEvaluacion.idEvaluacionFK=old.idEvaluacion; \n" +
                        " end");
                db.execSQL("create trigger eliminar_pr before delete on DetalleEvaluacion " +
                        "begin " +
                        "delete from PrimeraRevision where PrimeraRevision.idDetalleEvFK=old.idDetalleEv; " +
                        "end");
                db.execSQL("create trigger eliminar_sr before delete on PrimeraRevision " +
                        "begin " +
                        "delete from SegundaRevision where SegundaRevision.idPrimeraRevisionFK=old.idPrimerRevision; " +
                        "end");
                db.execSQL("create trigger eliminar_rev_doc1 before delete on Docente " +
                        "begin " +
                        "delete from SegundaRevision_Docente where SegundaRevision_Docente.carnetDocenteFK=old.carnetDocente; " +
                        "end");

                db.execSQL("create trigger eliminar_rev_doc2 before delete on SegundaRevision " +
                        "begin " +
                        "delete from SegundaRevision_Docente where SegundaRevision_Docente.idSegundaRevisionFK=old.idSegundaRevision; " +
                        "end");
                db.execSQL("create trigger eliminar_alumno before delete on Alumno " +
                        "begin " +
                        "delete from DetalleEvaluacion where DetalleEvaluacion.carnetAlumnoFK=old.carnetAlumno; " +
                        "end");
                new PoblarDBAsyncTask(instance).execute();
            } catch (Exception e) {
                Log.d("equisde", e.getMessage() + "\n");
                e.fillInStackTrace();
            }
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
        private SolicitudImpresionDao solicitudImpresionDao;
        private SegundaRevisionDao segundaRevisionDao;
        private DetalleEvaluacionDao detalleEvaluacionDao;
        private PrimeraRevisionDao primeraRevisionDao;
        private SegundaRevision_DocenteDao segundaRevision_docenteDao;
        private UsuarioDao usuarioDao;

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
            solicitudImpresionDao=db.solicitudImpresionDao();
            segundaRevisionDao=db.segundaRevisionDao();
            detalleEvaluacionDao = db.detalleEvaluacionDao();
            primeraRevisionDao = db.primeraRevisionDao();
            segundaRevision_docenteDao = db.segundaRevision_docenteDao();
            usuarioDao = db.usuarioDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            //Usuarios con rol de director
            usuarioDao.insertUser(new Usuario("DOCEISI1", "chicas", 1));
            usuarioDao.insertUser(new Usuario("DOCEIQA1", "torres", 1));
            usuarioDao.insertUser(new Usuario("DOCEICI1", "nuevodir", 1));
            //Usuarios con rol de docente
            usuarioDao.insertUser(new Usuario("DOCEISI2", "gozalez", 2));
            usuarioDao.insertUser(new Usuario("DOCEISI3", "carballo", 2));
            usuarioDao.insertUser(new Usuario("DOCEIQA2", "gamero", 2));
            usuarioDao.insertUser(new Usuario("DOCEIQA3", "nuevodoc", 2));
            //Usuarios con rol de estudiante
            usuarioDao.insertUser(new Usuario("PP15001", "rubper", 3));
            usuarioDao.insertUser(new Usuario("DR17010", "efrain", 3));
            usuarioDao.insertUser(new Usuario("BC14026", "arelyb", 3));
            usuarioDao.insertUser(new Usuario("MM16045", "fredyrol", 3));
            usuarioDao.insertUser(new Usuario("MG17030", "jairois", 3));
            usuarioDao.insertUser(new Usuario("MC16022", "julioc", 3));
            //Usuario con rol de encargado de impresiones
            usuarioDao.insertUser(new Usuario("EURFIA1","eliseo", 4));
            usuarioDao.insertUser(new Usuario("EURFIA2", "nuevoei", 4));
            //Usuario administrador
            usuarioDao.insertUser(new Usuario("Administrador", "administrador", 5));

            usuarioDao.insertUser(new Usuario("VR17035", "nuevoal", 3));

            //Escuelas
            escuelaDao.insert(new Escuela("Escuela de Ingeniería de Sistemas Informáticos","Ingeniería de Sistemas Informáticos"));
            escuelaDao.insert(new Escuela("Escuela de Ingeniería Industrial","Ingeniería Industrial"));
            escuelaDao.insert(new Escuela("Escuela de Ingeniería Eléctrica","Ingeniería Electrica"));
            escuelaDao.insert(new Escuela("Escuela de Ingeniería Civil","Ingeniería Civil"));
            escuelaDao.insert(new Escuela("Escuela de Ingeniería Química","Ingeniería Quimica"));
            escuelaDao.insert(new Escuela("Escuela de Ingeniería de Alimentos","Ingeniería de Alimentos"));
            escuelaDao.insert(new Escuela("Escuela de Arquitectura","Arquitectura"));
            escuelaDao.insert(new Escuela("Escuela de Ingeniería Mecánica","Ingeniería Mecánica"));
            //Areas administrativas por escuelas
            areaAdmDao.insertAreaAdm(new AreaAdm(1, "Dpto. de Comunicaciones y CC.de la Computación"));
            areaAdmDao.insertAreaAdm(new AreaAdm(1,  "Dpto. de Programación y Manejo de datos"));
            areaAdmDao.insertAreaAdm(new AreaAdm(1, "Dpto. de Desarrollo de Sistemas"));
            areaAdmDao.insertAreaAdm(new AreaAdm(1, "Dpto. de Administración"));
            areaAdmDao.insertAreaAdm(new AreaAdm(5, "General"));
            areaAdmDao.insertAreaAdm(new AreaAdm(5, "Dpto. de CC. Básicas de la Ingeniería Quíimica"));
            areaAdmDao.insertAreaAdm(new AreaAdm(5, "Dpto. de Ciencias de la Ingeniería Química"));
            //Asignaturas por area administrativa(Departamentos)
            asignaturaDao.insertAsignatura(new Asignatura("DSI115", 3, "Diseño de Sistemas I"));
            asignaturaDao.insertAsignatura(new Asignatura("SGG115", 3, "Sistemas de Información Geográficos"));
            asignaturaDao.insertAsignatura(new Asignatura("PDM115", 2, "Programación para Dispositivos Móviles"));
            asignaturaDao.insertAsignatura(new Asignatura("MIP115", 2, "Microprogramación"));
            asignaturaDao.insertAsignatura(new Asignatura("TAD115", 4, "Teoría Administrativa"));
            asignaturaDao.insertAsignatura(new Asignatura("FQR215", 6, "Fisicoquímica II"));
            asignaturaDao.insertAsignatura(new Asignatura("OPU215", 7, "Operaciones Unitarias II"));
            //Alumnos
            alumnoDao.insertarAlumno(new Alumno("MM16045", "Fredy Rolando", "Martínez Méndez", "1","fredymartinezues@gmail.com",11));
            alumnoDao.insertarAlumno(new Alumno("BC14026", "Vilma Arely", "Bárcenas Cruz", "1","vabcgv@outlook.com", 10));
            alumnoDao.insertarAlumno(new Alumno("PP15001", "Rubén Alejandro", "Pérez Pineda", "1","rubper@gmail.com", 8));
            alumnoDao.insertarAlumno(new Alumno("DR17010", "José Efraín", "Díaz Rivas", "1","efra.00@gmail.com", 9));
            alumnoDao.insertarAlumno(new Alumno("MG17030", "Jairo Isaac", "Montoya Galdámez", "1","jairomontoya.raices@gmail.com", 12));
            alumnoDao.insertarAlumno(new Alumno("MC16022", "Julio Antonio", "Merino Corcio", "5","corcio@gmail.com", 13));
            //Inscripción
            inscripcionDao.insertInscripcion(new Inscripcion("MM16045", "DSI115", 2, 1, 2));
            inscripcionDao.insertInscripcion(new Inscripcion("MM16045", "PDM115", 3, 1, 3));
            inscripcionDao.insertInscripcion(new Inscripcion("MG17030","PDM115", 2, 1, 2));
            inscripcionDao.insertInscripcion(new Inscripcion("MG17030", "MIP115", 1, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("DR17010", "DSI115", 1,2, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("DR17010", "TAD115", 1, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("PP15001", "DSI115", 2, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("PP15001", "TAD115", 2, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("BC14026", "PDM115", 2, 1, 1));
            inscripcionDao.insertInscripcion(new Inscripcion("MC16022", "FQR215", 1, 1, 1));
            //Ciclo
            cicloDao.insertCiclo(new Ciclo("08-08-19", "10-12-19", 6));
            cicloDao.insertCiclo(new Ciclo("17-02-2020", "20-06-2020", 7));
            //Tipo de evaluación
            tipoEvaluacionDao.insertarTipoEv(new TipoEvaluacion("Ordinario"));
            tipoEvaluacionDao.insertarTipoEv(new TipoEvaluacion("Repetido"));
            tipoEvaluacionDao.insertarTipoEv(new TipoEvaluacion("Diferido"));
            //Evaluación
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI3",1,"DSI115","Parcial de prueba","11/11/2000","12/11/2005","descripción de parcial de prueba","Sin Fecha",2));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI3",1,"DSI115","Tarea de prueba","11/11/2000","11/11/2000","segunda prueba de descripción","Sin Fecha",12));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI3",1,"DSI115","Actividad de prueba","11/11/2000","10/11/2002","tercera prueba de descripción esta vez mucho más larga más de una línea","Sin Fecha",2));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI3",1,"DSI115","Control de lectura","11/11/2000","11/11/2000","cuarta prueba de descripción","Sin Fecha",32));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI2",1,"PDM115","Ensayo de prueba","11/11/2000","10/10/2010","prueba corta","Sin Fecha",52));
            evaluacionDao.insertEvaluacion(new Evaluacion("DOCEISI2",1,"PDM115","Parcial de unidad","11/11/2000","11/11/2000","prueba de distintas longitudes de descripción","Sin Fecha",102));
            //Cargo
            cargoDao.insertCargo(new Cargo(1, "Director de la EISI"));
            cargoDao.insertCargo(new Cargo(2, "Jefe de Dpto. de Comunicaciones y CC. de la Computación"));
            cargoDao.insertCargo(new Cargo(3, "Jefe de Dpto. de Programación y Manejo de datos"));
            cargoDao.insertCargo(new Cargo(4, "Jefe de Dpto. de Desarrollo de Sistemas"));
            cargoDao.insertCargo(new Cargo(5, "Jefa de Dpto. de Administración"));
            cargoDao.insertCargo(new Cargo(6, "Docente de la EISI"));
            cargoDao.insertCargo(new Cargo(7, "Directora de la EIQA"));
            cargoDao.insertCargo(new Cargo(8, "Jefa de Dpto. de Ingeniería de Alimentos"));
            cargoDao.insertCargo(new Cargo(8, "Jefa de Dpto. de CC. Básicas de la Ingeniería Química"));
            cargoDao.insertCargo(new Cargo(8, "Jefe de Dpto de Ciencias de la Ingeniería Química"));
            cargoDao.insertCargo(new Cargo(8, "Docente de la EIQA"));
            //Docente
            docenteDao.insertDocente(new Docente("DOCEISI1", 1, 1, "Rudy Wilfredo", "Chicas", "chicas@ues.edu.sv", "78923456"));
            docenteDao.insertDocente(new Docente("DOCEIQA1", 7, 2,"Tania", "Torres Rivera", "torres@ues.edu.sv", "64589879"));
            docenteDao.insertDocente(new Docente("DOCEISI2", 6, 4,"Cesar Augusto", "González", "gonzalez@ues.edu.sv", "68923457"));
            docenteDao.insertDocente(new Docente("DOCEISI3", 4, 5,"Elmer Arturo", "Carballo Ruiz", "carballo@ues.edu.sv", "68793456"));
            docenteDao.insertDocente(new Docente("DOCEIQA2", 9, 6,"Eugenia Salvadora", "Gamero de Ayala", "gamero@ues.edu.sv", "65789034"));
            //Local
            localDao.insertarLocal(new Local("LComp1","Laboratorio 1","Escuela de Ingeniería de Sistemas Informáticos", 13.711282d, -89.200222d));
            localDao.insertarLocal(new Local("LComp4", "Laboratorio 2", "Escuela de Ingeniería de Sistemas Informáticos", 13.711282d, -89.200222d));
            localDao.insertarLocal(new Local("BIB301","Salón 1 de la biblioteca","Biblioteca de Ingeniería y Arquitectura", 13.720434d, -89.202106d));
            localDao.insertarLocal(new Local("D11","Aula D11", "Edificio D", 13.72077d,-89.200545d));
            localDao.insertarLocal(new Local("F2","Laboratorio UCB F2", "Unidad de Ciencias Básicas",13.720003d,-89.200787d));
            //Detalle de evaluación
            detalleEvaluacionDao.insertDetalleEvaluacion(new DetalleEvaluacion(1, "MM16045", 7.9f));
            detalleEvaluacionDao.insertDetalleEvaluacion(new DetalleEvaluacion(2,"DR17010", 8f));
            detalleEvaluacionDao.insertDetalleEvaluacion(new DetalleEvaluacion(3,"PP15001", 8f));
            detalleEvaluacionDao.insertDetalleEvaluacion(new DetalleEvaluacion(4,"DR17010", 8f));
            detalleEvaluacionDao.insertDetalleEvaluacion(new DetalleEvaluacion(5, "BC14026", 7.5f));
            //Primera revisión
            primeraRevisionDao.insertPrimeraRevision(new PrimeraRevision("LComp1", 1, "7/06/2020", true, 7f, 9f, "Ejercicio 1"));
            primeraRevisionDao.insertPrimeraRevision(new PrimeraRevision("D11", 2, "9/06/2020", true, 6f, 8f, "Ejercicio 2"));
            primeraRevisionDao.insertPrimeraRevision(new PrimeraRevision("LComp4", 3, "9/07/2020", true, 6f, 8f, "Pregunta 2"));
            //Encargado de impresión
            encargadoImpresionDao.insertEncargadoImpresion(new EncargadoImpresion( "Pedro Eliseo Peñate", 14));
            //Segunda revisión
            segundaRevisionDao.insertSegundaRevision(new SegundaRevision(1, "9/06/2020", "12:22:00",10,"", "8/06/2020"));
            segundaRevisionDao.insertSegundaRevision(new SegundaRevision(2, "9/06/2020", "12:22:00", "8/06/2020"));
            //Solicitud de extraordinario
            solicitudExtraordinarioDao.insertSolicitudExtraordinario(new SolicitudExtraordinario("PP15001", 1, 3, "Enfermedad", "16-06-2020", true));
            return null;
        }
    }
}
