package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class verAlumnoActivity extends AppCompatActivity {

    //Enlaces con las tablas para poder obtener informacion del alumno y de la carrera que cursa
    private Alumno alumnoActual;
    private Escuela escuela;

    private AlumnoViewModel alumnoViewModel;
    private EscuelaViewModel escuelaViewModel;

    //Enlaces con los textview
    private TextView disp_Carnet;
    private TextView disp_Nombre;
    private TextView disp_Apellido;
    private TextView disp_Carrera;
    private TextView disp_Correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);

            //Enlaces con la pantalla
            setContentView(R.layout.activity_ver_alumno);
            disp_Carnet=(TextView)findViewById(R.id.disp_carnet_alumno);
            disp_Nombre=(TextView)findViewById(R.id.disp_nombre_alumno);
            disp_Apellido=(TextView)findViewById(R.id.disp_apellido_alumno);
            disp_Carrera=(TextView)findViewById(R.id.disp_carrera_alumno);
            disp_Correo=(TextView)findViewById(R.id.disp_correo_alumno);

            //Inicializacion de los ViewModel
            alumnoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
            escuelaViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);

            //Obtener los datos del Intent
            Bundle extra=getIntent().getExtras();
            String carnet="";
            if(extra!=null){
                carnet=extra.getString(AlumnoActivity.IDENTIFICADOR_ALUMNO);
            }

            //Obtenemos el carnet por medio del intent
            alumnoActual=alumnoViewModel.getAlumn(carnet);

            //Obtenemos sus datos relacionados
            //Escuela
//
//            //obtener evaluación actual por medio de EXTRA_ID de intent
//            evaluacionActual = evaluacionViewModel.getEval(idEvaluacion);
//            //obtener objetos relacionados
//            tipoEvaluacionActual = tipoEvaluacionViewModel.getTipoEvaluacion(evaluacionActual.getIdTipoEvaluacionFK());
//            docenteActual = docenteViewModel.getDocente(evaluacionActual.getCarnetDocenteFK());
//            asignaturaActual = asignaturaViewModel.obtenerAsignatura(evaluacionActual.getCodigoAsignaturaFK());
//            //convertir participantes en string
//            String partAux = evaluacionActual.getNumParticipantes() + "";
//            //coloca texto en textviews
//            dispNombreEvaluacion.setText(evaluacionActual.getNomEvaluacion());
//            dispDescripcionEvaluacion.setText(evaluacionActual.getDescripcion());
//            dispAsignaturaEvaluacion.setText(asignaturaActual.getCodigoAsignatura() + " - " + asignaturaActual.getNomasignatura());
//            dispDocenteEvaluacion.setText(docenteActual.getCarnetDocente() + " - " + docenteActual.getNomDocente() + " " + docenteActual.getApellidoDocente());
//            dispTipoEvaluacion.setText(tipoEvaluacionActual.getTipoEvaluacion());
//            dispFechaInicioEvaluacion.setText(evaluacionActual.getFechaInicio());
//            dispFechaFinEvaluacion.setText(evaluacionActual.getFechaFin());
//            dispFechaEntregaEvaluacion.setText(evaluacionActual.getFechaEntregaNotas());
//            dispParticipantesEvaluacion.setText(partAux);
//            //título
//            setTitle(R.string.titulo_ver_eval);
//        }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+ " "+e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

}
