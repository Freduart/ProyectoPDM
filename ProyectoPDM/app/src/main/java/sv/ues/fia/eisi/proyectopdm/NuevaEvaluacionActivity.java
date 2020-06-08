package sv.ues.fia.eisi.proyectopdm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
//import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;


public class NuevaEvaluacionActivity extends AppCompatActivity {
    private EditText editNombreEvaluacion;
    private Spinner spinTipoEvaluacion;
    private Spinner spinCarnetDocenteEvaluacion;
    private Spinner spinCodigoAsignaturaEvaluacion;
    private DatePicker dpickFechaInicioEvaluacion;
    private DatePicker dpickFechaFinEvaluacion;
    private EditText editDescripcionEvaluacion;
    private EditText editNumParticipantesEvaluacion;
    private AsignaturaViewModel asignaturaVME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_evaluacion);
        //inicialización de elementos del layout
        editNombreEvaluacion = findViewById(R.id.edit_nombre_eval);
        spinTipoEvaluacion = findViewById(R.id.edit_tipoEval);
        spinCarnetDocenteEvaluacion = findViewById(R.id.edit_docente_eval);
        spinCodigoAsignaturaEvaluacion = findViewById(R.id.edit_asignatura_eval);
        dpickFechaInicioEvaluacion = findViewById(R.id.edit_fechInicio_eval);
        dpickFechaFinEvaluacion = findViewById(R.id.edit_fechFin_eval);
        editDescripcionEvaluacion = findViewById(R.id.edit_descripcion_eval);
        editNumParticipantesEvaluacion = findViewById(R.id.edit_numParticipantes_eval);
        //listas para alamacenar nombre e id de asignatura
        final ArrayList<String> asignaturasNom = new ArrayList<>();
        final ArrayList<String> asignaturasId = new ArrayList<>();
        //adaptador a arreglos para spinner, recibe (contexto, layout de spinner por defecto de android, arreglo a mostrar)
        final ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,asignaturasNom);
        //settea layout de dropdown del spinner (layout por defecto de android)
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //settea el adaptador creado en el spinner
        spinCodigoAsignaturaEvaluacion.setAdapter(adaptadorSpinner);
            //instancia asignatura view model
            asignaturaVME = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);
            //obtener todas las asignaturas en livedata
            asignaturaVME.getAllAsignaturas().observe(this, new Observer<List<Asignatura>>() {
                @Override
                public void onChanged(@Nullable List<Asignatura> asignaturas) {
                    //añade los elementos del livedata a las listas para alamcenar nombre e id de asignatura
                    for (Asignatura x : asignaturas) {
                        asignaturasNom.add(x.getNomasignatura());
                        asignaturasId.add(x.getCodigoAsignatura());
                    }
                    //refresca (necesario para mostrar los datos recuperados en el spinner)
                    adaptadorSpinner.notifyDataSetChanged();
                }
            });
        //nomeacuerdoxdddddd
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Evaluaciones");

        } catch (Exception e){
            Toast.makeText(NuevaEvaluacionActivity.this, e.getMessage()+ " " + e.getCause(), Toast.LENGTH_LONG).show();
        }

    }

    private void guardarEvaluacion(){
        String nombre = editNombreEvaluacion.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.nueva_evaluacion_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_evaluacion:
                guardarEvaluacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
