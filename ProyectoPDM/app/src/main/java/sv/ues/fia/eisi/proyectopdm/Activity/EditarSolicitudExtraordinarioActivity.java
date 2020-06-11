package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;


public class EditarSolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinario soliExtraActual;
    private Alumno alumnoActual;
    private Evaluacion evaActual;

    private SolicitudExtraordinarioViewModel soliExtraVM;
    private AlumnoViewModel alumnoVM;
    private EvaluacionViewModel evalVM;

    private EditText idAlumno;
    private EditText idEvaluacion;
    private EditText tipoSoli;
    private EditText motivoSoli;
    private EditText fechaSoli;
    private CheckBox justiSoli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_solicitud_extraordinario);

            setTitle("Editar Solicitud");

            idAlumno = (EditText) findViewById(R.id.editCarnetAlumno);
            idEvaluacion = (EditText) findViewById(R.id.editIdEvaluacion);
            tipoSoli = (EditText) findViewById(R.id.editTipoSoliExtra);
            motivoSoli = (EditText) findViewById(R.id.editMotivoSoliExtra);
            fechaSoli = (EditText) findViewById(R.id.editFechaSoliExtra);
            justiSoli = (CheckBox) findViewById(R.id.JustiSoliExtra);

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

            idAlumno.setText(alumnoActual.getCarnetAlumno());
            idEvaluacion.setText(String.valueOf(evaActual.getIdEvaluacion()));
            tipoSoli.setText(soliExtraActual.getTipoSolicitud());
            motivoSoli.setText(soliExtraActual.getMotivoSolicitud());
            fechaSoli.setText(soliExtraActual.getFechaSolicitudExtr());

            if(soliExtraActual.isJustificacion()){
                justiSoli.isChecked();
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),
                    "Error during request: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarSoliExtra(){
        try {
            String carnetAlumno = idAlumno.getText().toString();
            int idEval = Integer.parseInt(idEvaluacion.getText().toString());
            String tipo = tipoSoli.getText().toString();
            String motivo = motivoSoli.getText().toString();
            String fecha = fechaSoli.getText().toString();
            boolean justi = justiSoli.isChecked();

            SolicitudExtraordinario soliAux = new SolicitudExtraordinario(carnetAlumno, idEval, tipo, motivo, fecha, justi);

            soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);
            soliExtraVM.update(soliAux);

            Toast.makeText(EditarSolicitudExtraordinarioActivity.this, "Solicitud Actualizada con éxito", Toast.LENGTH_SHORT).show();

            finish();
        } catch(Exception e){
            Toast.makeText(EditarSolicitudExtraordinarioActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nueva_soli_extra_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar_soli_extra:
                actualizarSoliExtra();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
