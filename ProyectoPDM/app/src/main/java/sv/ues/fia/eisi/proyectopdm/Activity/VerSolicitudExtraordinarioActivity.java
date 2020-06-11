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
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;

public class VerSolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinario soliExtraActual;
    private Alumno alumnoActual;
    private Evaluacion evaActual;

    private SolicitudExtraordinarioViewModel soliExtraVM;
    private AlumnoViewModel alumnoVM;
    private EvaluacionViewModel evalVM;

    private TextView idSoliExtraordinario;
    private TextView idAlumno;
    private TextView idEvaluacion;
    private TextView tipoSoli;
    private TextView motivoSoli;
    private TextView fechaSoli;
    private TextView justiSoli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_solicitud_extraordinario);

            idSoliExtraordinario = (TextView) findViewById(R.id.vSoliExtra_disp_IDSoli);
            idAlumno = (TextView) findViewById(R.id.vSoliExtra_disp_IDAlum);
            idEvaluacion = (TextView) findViewById(R.id.vSoliExtra_disp_IDEval);
            tipoSoli = (TextView) findViewById(R.id.vSoliExtra_disp_tipo);
            motivoSoli = (TextView) findViewById(R.id.vSoliExtra_disp_motivo);
            fechaSoli = (TextView) findViewById(R.id.vSoliExtra_disp_fecha);
            justiSoli = (TextView) findViewById(R.id.vSoliExtra_disp_justi);

            soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);
            alumnoVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
            evalVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);

            Bundle extras = getIntent().getExtras();
            int idSoliExtra = 0;
            if(extras != null){
                idSoliExtra = extras.getInt("ID Solicitud Extraordinaria Actual");
            }

            soliExtraActual = soliExtraVM.getSoliExtra(idSoliExtra);
            evaActual = evalVM.getEval(soliExtraActual.getIdEvaluacion());
            alumnoActual = alumnoVM.getAlumn(soliExtraActual.getCarnetAlumnoFK());

            idSoliExtraordinario.setText(String.valueOf(soliExtraActual.getIdSolicitud()));
            idAlumno.setText(String.valueOf(alumnoActual.getCarnetAlumno()));
            idEvaluacion.setText(String.valueOf(evaActual.getIdEvaluacion() + " - " + evaActual.getNomEvaluacion()));
            tipoSoli.setText(soliExtraActual.getTipoSolicitud());
            motivoSoli.setText(soliExtraActual.getMotivoSolicitud());
            fechaSoli.setText(soliExtraActual.getFechaSolicitudExtr());
            justiSoli.setText(String.valueOf(soliExtraActual.isJustificacion()));

            setTitle("Detalle de Solicitud Extraordinaria");
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),
                    "Error during request: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
