package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.TipoEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

public class VerEvaluacionActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "sv.ues.fia.eisi.proyectopdm.Activity.EXTRA_ID";

    private Evaluacion evaluacionActual;
    private TipoEvaluacion tipoEvaluacionActual;
    private Asignatura asignaturaActual;
    private Docente docenteActual;

    private EvaluacionViewModel evaluacionViewModel;
    private AsignaturaViewModel asignaturaViewModel;
    private TipoEvaluacionViewModel tipoEvaluacionViewModel;
    private DocenteViewModel docenteViewModel;

    private TextView dispNombreEvaluacion;
    private TextView dispDescripcionEvaluacion;
    private TextView dispTipoEvaluacion;
    private TextView dispAsignaturaEvaluacion;
    private TextView dispDocenteEvaluacion;
    private TextView dispFechaInicioEvaluacion;
    private TextView dispFechaFinEvaluacion;
    private TextView dispFechaEntregaEvaluacion;
    private TextView dispParticipantesEvaluacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evaluacion);
        //asociar textviews de activity con controles del layout
        dispNombreEvaluacion = findViewById(R.id.disp_nombre_ver_evaluacion);
        dispDescripcionEvaluacion = findViewById(R.id.disp_descripcion_ver_evaluacion);
        dispTipoEvaluacion = findViewById(R.id.disp_tipoevaluacion_ver_evaluacion);
        dispAsignaturaEvaluacion = findViewById(R.id.disp_asignatura_ver_evaluacion);
        dispDocenteEvaluacion = findViewById(R.id.disp_docente_ver_evaluacion);
        dispFechaInicioEvaluacion = findViewById(R.id.disp_fechainicio_ver_evaluacion);
        dispFechaFinEvaluacion = findViewById(R.id.disp_fechafin_ver_evaluacion);
        dispFechaEntregaEvaluacion = findViewById(R.id.disp_fechaentrega_ver_evaluacion);
        dispParticipantesEvaluacion = findViewById(R.id.disp_participantes_ver_evaluacion);
        //inicializar viewmodels
        evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
        docenteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
        tipoEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);
        asignaturaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);
        //obtener intent de activity
        Bundle extras = getIntent().getExtras();
        int idEvaluacion = 0;
            if (extras != null) {
                idEvaluacion = extras.getInt("ID_Evaluacion_Actual");
            }
        //obtener evaluación actual por medio de EXTRA_ID de intent
        evaluacionActual = evaluacionViewModel.getEval(idEvaluacion);
        //obtener objetos relacionados
        tipoEvaluacionActual = tipoEvaluacionViewModel.getTipoEvaluacion(evaluacionActual.getIdTipoEvaluacionFK());
        docenteActual = docenteViewModel.getDocente(evaluacionActual.getCarnetDocenteFK());
        asignaturaActual = asignaturaViewModel.obtenerAsignatura(evaluacionActual.getCodigoAsignaturaFK());
        //convertir participantes en string
        String partAux = evaluacionActual.getNumParticipantes() + "";
        //coloca texto en textviews
        dispNombreEvaluacion.setText(evaluacionActual.getNomEvaluacion());
        dispDescripcionEvaluacion.setText(evaluacionActual.getDescripcion());
        dispAsignaturaEvaluacion.setText(asignaturaActual.getCodigoAsignatura() + " - " + asignaturaActual.getNomasignatura());
        dispDocenteEvaluacion.setText(docenteActual.getCarnetDocente() + " - " + docenteActual.getNomDocente() + " " + docenteActual.getApellidoDocente());
        dispTipoEvaluacion.setText(tipoEvaluacionActual.getTipoEvaluacion());
        dispFechaInicioEvaluacion.setText(evaluacionActual.getFechaInicio());
        dispFechaFinEvaluacion.setText(evaluacionActual.getFechaFin());
        dispFechaEntregaEvaluacion.setText(evaluacionActual.getFechaEntregaNotas());
        dispParticipantesEvaluacion.setText(partAux);
        //título
        setTitle("Detalle de evaluación");
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString() ,Toast.LENGTH_LONG).show();
        }
    }
}
