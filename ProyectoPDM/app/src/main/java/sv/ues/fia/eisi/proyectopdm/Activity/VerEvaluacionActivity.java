package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.TipoEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;
import sv.ues.fia.eisi.proyectopdm.repository.DetalleEvaluacionRepository;

public class VerEvaluacionActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "sv.ues.fia.eisi.proyectopdm.Activity.EXTRA_ID";

    private Evaluacion evaluacionActual;
    private TipoEvaluacion tipoEvaluacionActual;
    private Asignatura asignaturaActual;
    private Docente docenteActual;

    private boolean currentUserAlumno = false;
    private TextView headlineNotaAlumno;
    private TextView notaAlumnoDisplay;
    private Button solicitarRevisionBtn;

    private EvaluacionViewModel evaluacionViewModel;
    private AsignaturaViewModel asignaturaViewModel;
    private TipoEvaluacionViewModel tipoEvaluacionViewModel;
    private DocenteViewModel docenteViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
    private PrimeraRevisionViewModel primeraRevisionViewModel;

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
        headlineNotaAlumno = findViewById(R.id.alumno_eval_nota_headline);
        notaAlumnoDisplay = findViewById(R.id.alumno_eval_nota_disp);
        solicitarRevisionBtn = findViewById(R.id.boton_primera_revision_detalle);
        //inicializar viewmodels
        evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
        docenteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
        tipoEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);
        asignaturaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);
        detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
        primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
        //obtener intent de activity
        Bundle extras = getIntent().getExtras();
        int idEvaluacion = 0;
            if (extras != null) {
                idEvaluacion = extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION);
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
        dispAsignaturaEvaluacion.setText(String.format("%s - %s", asignaturaActual.getCodigoAsignatura(), asignaturaActual.getNomasignatura()));
        dispDocenteEvaluacion.setText(String.format("%s - %s %s", docenteActual.getCarnetDocente(), docenteActual.getNomDocente(), docenteActual.getApellidoDocente()));
        dispTipoEvaluacion.setText(tipoEvaluacionActual.getTipoEvaluacion());
        dispFechaInicioEvaluacion.setText(evaluacionActual.getFechaInicio());
        dispFechaFinEvaluacion.setText(evaluacionActual.getFechaFin());
        dispFechaEntregaEvaluacion.setText(evaluacionActual.getFechaEntregaNotas());
        dispParticipantesEvaluacion.setText(partAux);

        //si es alumno, mostrar nota y solicitud de revisión
        if(currentUserAlumno){
            headlineNotaAlumno.setVisibility(View.VISIBLE);
            notaAlumnoDisplay.setVisibility(View.VISIBLE);
            solicitarRevisionBtn.setVisibility(View.VISIBLE);
            solicitarRevisionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        DetalleEvaluacionRepository.ParametrosDetalles params = new DetalleEvaluacionRepository.ParametrosDetalles(evaluacionActual.getIdEvaluacion(),"CARNET");
                        DetalleEvaluacion detalleEvaluacion = detalleEvaluacionViewModel.getDetalleAlumnoEvaluacion(params);
                        List<PrimeraRevision> primeraRevisions= primeraRevisionViewModel.getRevisionPorDetalle(detalleEvaluacion.getIdDetalleEv());
                        //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                        Intent intent = new Intent(VerEvaluacionActivity.this, NuevaPrimeraRevisionActivity.class);
                        //se mete en un extra del intent, el id
                        intent.putExtra(PrimeraRevisionActivity.IDENTIFICADOR_PR, primeraRevisions.get(0).getIdPrimerRevision());
                        //inicia la activity
                        startActivity(intent);
                    } catch (Exception e){
                        e.fillInStackTrace();
                    }
                }
            });
        } else {
            headlineNotaAlumno.setVisibility(View.GONE);
            notaAlumnoDisplay.setVisibility(View.GONE);
            solicitarRevisionBtn.setVisibility(View.GONE);

        }
        //título
        setTitle(R.string.titulo_ver_eval);
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString() ,Toast.LENGTH_LONG).show();
        }
    }
}
