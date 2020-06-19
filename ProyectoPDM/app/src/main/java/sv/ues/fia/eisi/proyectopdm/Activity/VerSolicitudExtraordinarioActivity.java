package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.TipoEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

public class VerSolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinario soliExtraActual;
    private Alumno alumnoActual;
    private Evaluacion evaActual;
    private TipoEvaluacion tipoEvaActual;

    private SolicitudExtraordinarioViewModel soliExtraVM;
    private AlumnoViewModel alumnoVM;
    private EvaluacionViewModel evalVM;
    private TipoEvaluacionViewModel tipoEvaVM;

    private TextView idSoliExtraordinario;
    private TextView idAlumno;
    private TextView idEvaluacion;
    private TextView tipoSoli;
    private TextView motivoSoli;
    private TextView fechaSoli;
    private TextView justiSoli;
    private TextView estadoSoli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_solicitud_extraordinario);

            //Se inicializan elementos del Layout en Activity
            idSoliExtraordinario = (TextView) findViewById(R.id.vSoliExtra_disp_IDSoli);
            idAlumno = (TextView) findViewById(R.id.vSoliExtra_disp_IDAlum);
            idEvaluacion = (TextView) findViewById(R.id.vSoliExtra_disp_IDEval);
            tipoSoli = (TextView) findViewById(R.id.vSoliExtra_disp_tipo);
            motivoSoli = (TextView) findViewById(R.id.vSoliExtra_disp_motivo);
            fechaSoli = (TextView) findViewById(R.id.vSoliExtra_disp_fecha);
            justiSoli = (TextView) findViewById(R.id.vSoliExtra_disp_justi);
            estadoSoli = (TextView) findViewById(R.id.vSoliExtra_disp_estado);

            //Se inicializan los ViewModel
            soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);
            alumnoVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
            evalVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            tipoEvaVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);

            //Se extrae el id de la solicitud a través del Intent
            Bundle extras = getIntent().getExtras();
            int idSoliExtra = 0;
            if(extras != null){
                idSoliExtra = extras.getInt(SolicitudExtraordinarioActivity.IDENTIFICADOR_SOLI_EXTRA);
            }

            //Se asignan objetos extraídos del ViewModel
            soliExtraActual = soliExtraVM.getSoliExtra(idSoliExtra);
            evaActual = evalVM.getEval(soliExtraActual.getIdEvaluacion());
            alumnoActual = alumnoVM.getAlumn(soliExtraActual.getCarnetAlumnoFK());
            tipoEvaActual = tipoEvaVM.getTipoEvaluacion(soliExtraActual.getTipoSolicitud());

            //Se asignan los valores correspondientes en elementos del Layout
            idSoliExtraordinario.setText(String.valueOf(soliExtraActual.getIdSolicitud()));
            idAlumno.setText(alumnoActual.getCarnetAlumno());
            idEvaluacion.setText(evaActual.getIdEvaluacion() + " - " + evaActual.getNomEvaluacion());
            tipoSoli.setText(tipoEvaActual.getIdTipoEvaluacion() + " - " + tipoEvaActual.getTipoEvaluacion());
            motivoSoli.setText(soliExtraActual.getMotivoSolicitud());
            fechaSoli.setText(soliExtraActual.getFechaSolicitudExtr());
            if(soliExtraActual.isJustificacion()==true)
                justiSoli.setText("Justificado");
            else
                justiSoli.setText("No justificado");

            if(soliExtraActual.isEstadoSolicitud()==true)
                estadoSoli.setText("Aprobada");
            else
                estadoSoli.setText("Rechazada");

            //Titulo personalizado para Activity
            setTitle("Detalle de Solicitud Extraordinaria");
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),
                    "Error during request: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
