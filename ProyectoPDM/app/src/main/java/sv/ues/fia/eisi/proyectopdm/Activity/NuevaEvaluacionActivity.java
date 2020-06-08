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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
//import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.TipoEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;


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
    private DocenteViewModel docenteVME;
    private TipoEvaluacionViewModel tipoEvaluacionVM;
    private EvaluacionViewModel evaluacionViewModel;


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

            //LLENAR SPINNERS
            //Spinner asignatura
            //listas para alamacenar nombre e id de asignatura
            final ArrayList<String> asignaturasNom = new ArrayList<>();
            //adaptador a arreglos para spinner, recibe (contexto, layout de spinner por defecto de android, arreglo a mostrar)
            final ArrayAdapter<String> adaptadorSpinnerAsignatura = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,asignaturasNom);
            //settea layout de dropdown del spinner (layout por defecto de android)
            adaptadorSpinnerAsignatura.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //settea el adaptador creado en el spinner
            spinCodigoAsignaturaEvaluacion.setAdapter(adaptadorSpinnerAsignatura);
            //instancia asignatura view model
            asignaturaVME = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);
            //obtener todas las asignaturas en livedata
            asignaturaVME.getAllAsignaturas().observe(this, new Observer<List<Asignatura>>() {
                @Override
                public void onChanged(@Nullable List<Asignatura> asignaturas) {
                    //añade los elementos del livedata a las listas para alamcenar nombre e id de asignatura
                    for (Asignatura x : asignaturas) {
                        asignaturasNom.add(x.getCodigoAsignatura()+" - " + x.getNomasignatura());
                    }
                    //refresca (necesario para mostrar los datos recuperados en el spinner)
                    adaptadorSpinnerAsignatura.notifyDataSetChanged();
                }
            });
            //--fin llenado spinner asignatura

            //Spinner Tipo evaluacion
            final ArrayList<String> tipoEvaluacionesNom = new ArrayList<>();
            final ArrayAdapter<String> adaptadorSpinnerTipoEval = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipoEvaluacionesNom);
            adaptadorSpinnerTipoEval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinTipoEvaluacion.setAdapter(adaptadorSpinnerTipoEval);
            tipoEvaluacionVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);
            tipoEvaluacionVM.getTodosTiposEvaluaciones().observe(this, new Observer<List<TipoEvaluacion>>() {
                @Override
                public void onChanged(@Nullable List<TipoEvaluacion> tiposEvaluaciones) {
                    for (TipoEvaluacion x : tiposEvaluaciones) {
                        tipoEvaluacionesNom.add(x.getIdTipoEvaluacion()+" - " + x.getTipoEvaluacion());
                    }
                    adaptadorSpinnerTipoEval.notifyDataSetChanged();
                }
            });
            //--fin llenado spinner tipo evaluaciones

            //Spinner docentes
            final ArrayList<String> docentesNom = new ArrayList<>();
            final ArrayAdapter<String> adaptadorSpinnerDocentes = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,docentesNom);
            adaptadorSpinnerDocentes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinCarnetDocenteEvaluacion.setAdapter(adaptadorSpinnerDocentes);
            docenteVME = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
            docenteVME.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
                @Override
                public void onChanged(@Nullable List<Docente> docentes) {
                    for (Docente x : docentes) {
                        docentesNom.add(x.getCarnetDocente()+" - " + x.getNomDocente() + " " + x.getApellidoDocente());
                    }
                    adaptadorSpinnerDocentes.notifyDataSetChanged();
                }
            });
            //--fin llenado spinner docentes

            //nomeacuerdoxdddddd
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            setTitle("Nueva evaluación");

        } catch (Exception e){
            Toast.makeText(NuevaEvaluacionActivity.this, e.getMessage()+ " " + e.getCause(), Toast.LENGTH_LONG).show();
        }

    }

    private void guardarEvaluacion() {
        try {
            //---alamcenar NOMBRE evaluacion
            String nombre = editNombreEvaluacion.getText().toString();

            //---obtener valor de spinner ASIGNATURA
            String asignaturaAux1 = spinCodigoAsignaturaEvaluacion.getSelectedItem().toString();
            String[] asignaturaAux2 = asignaturaAux1.split("-");
            //almacenar id de ASIGNATURA
            String asignatura = asignaturaAux2[0].trim();

            //---obtener valor de spinner TIPO EVALUACION
            String tipoEvalAux1 = spinTipoEvaluacion.getSelectedItem().toString();
            String[] tipoEvalAux2 = tipoEvalAux1.split("-");
            //almacenar TIPO EVAL
            String tipoEval = tipoEvalAux2[0].trim();

            //---obtener valor de spinner DOCENTE
            String docenteAux1 = spinCarnetDocenteEvaluacion.getSelectedItem().toString();
            String[] docenteAux2 = docenteAux1.split("-");
            //almacenar DOCENTE
            String docente = docenteAux2[0].trim();

            //---obtener valor de datepicker FECHA INICIO evaluacion
            StringBuilder fechInBuilder = new StringBuilder(10);
            //concatenar
            fechInBuilder.append(dpickFechaInicioEvaluacion.getDayOfMonth()).append("/").append(dpickFechaInicioEvaluacion.getMonth()).append("/").append(dpickFechaInicioEvaluacion.getYear());
            //almacenar FECHA INICIO
            String fechaInicio = fechInBuilder.toString();

            //---obtener valor de datepicker FECHA FIN evaluacion
            StringBuilder fechFinBuilder = new StringBuilder(10);
            //concatenar
            fechFinBuilder.append(dpickFechaFinEvaluacion.getDayOfMonth()).append("/").append(dpickFechaFinEvaluacion.getMonth()).append("/").append(dpickFechaFinEvaluacion.getYear());
            //almacenar FECHA FIN
            String fechaFin = fechFinBuilder.toString();

            //---almacenar DESCRIPCION
            String descripcion = editDescripcionEvaluacion.getText().toString();

            //---almacenar PARTICIPANTES
            String participantes = editNumParticipantesEvaluacion.getText().toString();

            if(nombre.trim().isEmpty() || descripcion.trim().isEmpty() || participantes.trim().isEmpty()){
                Toast.makeText(this,"Por favor, llena todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            //Objeto Evaluación auxiliar construido a partir de los datos almacenados
            Evaluacion aux = new Evaluacion(docente,Integer.parseInt(tipoEval),asignatura,nombre,fechaInicio,fechaFin,descripcion,"000000",Integer.parseInt(participantes));
            //instancia View Model de evaluacion
            evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            //insertar
            evaluacionViewModel.insertEval(aux);
            //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
            Toast.makeText(NuevaEvaluacionActivity.this, "insertado con éxito: " + nombre + "-" + asignatura + "-" + tipoEval + "-" + fechaInicio + "-" + fechaFin + "-" + descripcion + "-" + participantes, Toast.LENGTH_LONG).show();
            //salir de la actividad
            finish();
        } catch (Exception e){
            Toast.makeText(NuevaEvaluacionActivity.this, e.getMessage() + " - " + e.fillInStackTrace().toString(), Toast.LENGTH_LONG).show();
        }
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
