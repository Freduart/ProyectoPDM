package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.TipoEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;


public class EditarSolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinario soliExtraActual;
    private Alumno alumnoActual;
    private Evaluacion evaActual;
    private TipoEvaluacion tipoEvaActual;

    private SolicitudExtraordinarioViewModel soliExtraVM;
    private AlumnoViewModel alumnoVM;
    private EvaluacionViewModel evalVM;
    private TipoEvaluacionViewModel tipoEvaVM;

    private EditText idAlumno;
    private EditText idEvaluacion;
    private Spinner tipoSoli;
    private EditText motivoSoli;
    private EditText fechaSoli;
    private CheckBox justiSoli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_solicitud_extraordinario);

            //Título personalizado para Activity
            setTitle("Editar Solicitud");

            //Inicializa elementos del Layout en Activity
            idAlumno = (EditText) findViewById(R.id.editCarnetAlumno);
            idEvaluacion = (EditText) findViewById(R.id.editIdEvaluacion);
            tipoSoli = (Spinner) findViewById(R.id.editTipoSoli);
            motivoSoli = (EditText) findViewById(R.id.editMotivoSoliExtra);
            fechaSoli = (EditText) findViewById(R.id.editFechaSoliExtra);
            justiSoli = (CheckBox) findViewById(R.id.JustiSoliExtra);

            //Spinner Tipo evaluacion
            final ArrayList<String> tipoEvaluacionesNom = new ArrayList<>();
            final ArrayAdapter<String> adaptadorSpinnerTipoEval = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipoEvaluacionesNom);
            adaptadorSpinnerTipoEval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tipoSoli.setAdapter(adaptadorSpinnerTipoEval);
            tipoEvaVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);
            tipoEvaVM.getTodosTiposEvaluaciones().observe(this, new Observer<List<TipoEvaluacion>>() {
                @Override
                public void onChanged(@Nullable List<TipoEvaluacion> tiposEvaluaciones) {
                    for (TipoEvaluacion x : tiposEvaluaciones) {
                        tipoEvaluacionesNom.add(x.getIdTipoEvaluacion()+ " - "+x.getTipoEvaluacion());
                    }
                    adaptadorSpinnerTipoEval.notifyDataSetChanged();
                }
            });

            //Inicializa los ViewModel
            soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);
            alumnoVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
            evalVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            tipoEvaVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);

            //Se extrae el identificador de la solicitud a editar del Intent
            Bundle extras = getIntent().getExtras();
            int idSoliExtra = 0;
            if(extras != null){
                idSoliExtra = extras.getInt(SolicitudExtraordinarioActivity.IDENTIFICADOR_SOLI_EXTRA);
            }

            //Se asignan objetos extraídos del ViewModel
            soliExtraActual = soliExtraVM.getSoliExtra(idSoliExtra);
            evaActual = evalVM.getEval(soliExtraActual.getIdEvaluacion());
            alumnoActual = alumnoVM.getAlumn(soliExtraActual.getCarnetAlumnoFK());
            tipoEvaActual = tipoEvaVM.getTipoEvaluacion(soliExtraActual.getIdEvaluacion());

            //Se asignan los valores correspondientes en elementos del Layout
            idAlumno.setText(alumnoActual.getCarnetAlumno());
            idEvaluacion.setText(String.valueOf(evaActual.getIdEvaluacion()));
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
            //Almacena valores de los datos que se modifiquen
            String carnetAlumno = idAlumno.getText().toString();
            int idEval = Integer.parseInt(idEvaluacion.getText().toString());
            String motivo = motivoSoli.getText().toString();
            String fecha = fechaSoli.getText().toString();
            boolean justi = justiSoli.isChecked();

            //---obtener valor de spinner TIPO EVALUACION
            String tipoEvalAux1 = tipoSoli.getSelectedItem().toString();
            String[] tipoEvalAux2 = tipoEvalAux1.split("-");
            //almacenar TIPO EVAL
            String tipoEvaAux3 = tipoEvalAux2[0].trim();
            int tipoEva = Integer.parseInt(tipoEvaAux3);

            //Se extrae el identificador de la solicitud a editar del Intent
            Bundle extras = getIntent().getExtras();
            int idSoliExtra = 0;
            if(extras != null){
                idSoliExtra = extras.getInt(SolicitudExtraordinarioActivity.IDENTIFICADOR_SOLI_EXTRA);
            }

            //Se verifica que no se seleccione Ordinario, por ser una Solicitud Extraordinaria
            if(tipoEva == 1){
                //Si se selecciona Ordinaria, retorna a la Activity anterior.
                Toast.makeText(EditarSolicitudExtraordinarioActivity.this, "No puede seleccionar tipo Ordinario. Seleccione el tipo de evaluación extraordinaria que desea realizar", Toast.LENGTH_LONG).show();
            } else {
                //Se inicializa de nuevo el ViewModel
                soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);

                //Se crea una instancia de la clase SolicitudExtraordinario para operar el VM
                SolicitudExtraordinario soliAux = soliExtraVM.getSoliExtra(idSoliExtra);
                //Se ingresan los campos
                soliAux.setCarnetAlumnoFK(carnetAlumno);
                soliAux.setIdEvaluacion(idEval);
                soliAux.setTipoSolicitud(tipoEva);
                soliAux.setMotivoSolicitud(motivo);
                soliAux.setFechaSolicitudExtr(fecha);
                soliAux.setJustificacion(justi);

                //Se actualiza la Solicitud
                soliExtraVM.update(soliAux);

                //Mensaje de éxito de la operación. En caso de error, es atrapado y se muestra en el Toast del segmento de catch
                Toast.makeText(EditarSolicitudExtraordinarioActivity.this, "Solicitud Actualizada con éxito", Toast.LENGTH_SHORT).show();

                finish();
            }
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
