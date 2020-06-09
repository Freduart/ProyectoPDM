package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

//import sv.ues.fia.eisi.proyectopdm.Adapter.SpinAdapter;
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


public class NuevaEditarEvaluacionActivity extends AppCompatActivity {
    public static final String ENTREGA_NOTAS_PLACEHOLDER = "Sin Fecha";

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
    private DatePicker dpickFechaEntregaEvaluacion;
    private TextView fechaEntregaEvaluacionLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_evaluacion);
            //obtener extras del intent
            final Bundle extras = getIntent().getExtras();

            //inicialización de elementos del layout
            editNombreEvaluacion = findViewById(R.id.edit_nombre_eval);
            spinTipoEvaluacion = findViewById(R.id.edit_tipoEval);
            spinCarnetDocenteEvaluacion = findViewById(R.id.edit_docente_eval);
            spinCodigoAsignaturaEvaluacion = findViewById(R.id.edit_asignatura_eval);
            dpickFechaInicioEvaluacion = findViewById(R.id.edit_fechInicio_eval);
            dpickFechaFinEvaluacion = findViewById(R.id.edit_fechFin_eval);
            editDescripcionEvaluacion = findViewById(R.id.edit_descripcion_eval);
            editNumParticipantesEvaluacion = findViewById(R.id.edit_numParticipantes_eval);
            dpickFechaEntregaEvaluacion = findViewById(R.id.edit_fechaEntregaNotas);
            fechaEntregaEvaluacionLabel = findViewById(R.id.fechaEntregaNotas);

            //LLENAR SPINNERS
            //Spinner asignatura
            //listas para alamacenar nombre e id de asignatura
            final ArrayList<Asignatura> asignaturasNom = new ArrayList<>();
            //adaptador a arreglos para spinner, recibe (contexto, layout de spinner por defecto de android, arreglo a mostrar)
            final ArrayAdapter<Asignatura> adaptadorSpinnerAsignatura = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,asignaturasNom);
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
                    try {
                        if(extras.getInt(EvaluacionActivity.OPERACION_EVALUACION) == EvaluacionActivity.EDITAR_EVALUACION){
                            Evaluacion ev = evaluacionViewModel.getEval(extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION));
                            Asignatura as = asignaturaVME.obtenerAsignatura(ev.getCodigoAsignaturaFK());
                            //añade los elementos del livedata a las listas para alamcenar nombre e id de asignatura
                            for (Asignatura x : asignaturas) {
                                asignaturasNom.add(x);
                                if(x.getCodigoAsignatura().equals(as.getCodigoAsignatura()))
                                    spinCodigoAsignaturaEvaluacion.setSelection(asignaturasNom.indexOf(x));
                            }
                            //refresca (necesario para mostrar los datos recuperados en el spinner)
                            adaptadorSpinnerAsignatura.notifyDataSetChanged();
                        } else {
                            for (Asignatura x : asignaturas)
                                asignaturasNom.add(x);
                            adaptadorSpinnerAsignatura.notifyDataSetChanged();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });
            //--fin llenado spinner asignatura extras.getInt(EvaluacionActivity.OPERACION_EVALUACION)

            //Spinner Tipo evaluacion
            final ArrayList<TipoEvaluacion> tipoEvaluacionesNom = new ArrayList<>();
            final ArrayAdapter<TipoEvaluacion> adaptadorSpinnerTipoEval = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipoEvaluacionesNom);
            adaptadorSpinnerTipoEval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinTipoEvaluacion.setAdapter(adaptadorSpinnerTipoEval);
            tipoEvaluacionVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);
            tipoEvaluacionVM.getTodosTiposEvaluaciones().observe(this, new Observer<List<TipoEvaluacion>>() {
                @Override
                public void onChanged(@Nullable List<TipoEvaluacion> tiposEvaluaciones) {
                    try {
                        if(extras.getInt(EvaluacionActivity.OPERACION_EVALUACION) == EvaluacionActivity.EDITAR_EVALUACION){
                            Evaluacion ev = evaluacionViewModel.getEval(extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION));
                            TipoEvaluacion as = tipoEvaluacionVM.getTipoEvaluacion(ev.getIdTipoEvaluacionFK());
                            //añade los elementos del livedata a las listas para alamcenar nombre e id de asignatura
                            for (TipoEvaluacion x : tiposEvaluaciones) {
                                tipoEvaluacionesNom.add(x);
                                if(x.getIdTipoEvaluacion()==(as.getIdTipoEvaluacion()))
                                    spinTipoEvaluacion.setSelection(tipoEvaluacionesNom.indexOf(x));
                            }
                            //refresca (necesario para mostrar los datos recuperados en el spinner)
                            adaptadorSpinnerTipoEval.notifyDataSetChanged();
                        } else {
                            for (TipoEvaluacion x : tiposEvaluaciones)
                                tipoEvaluacionesNom.add(x);
                            adaptadorSpinnerTipoEval.notifyDataSetChanged();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });
            //--fin llenado spinner tipo evaluaciones

            //Spinner docentes
            final ArrayList<Docente> docentesNom = new ArrayList<>();
            final ArrayAdapter<Docente> adaptadorSpinnerDocentes = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,docentesNom);
            adaptadorSpinnerDocentes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinCarnetDocenteEvaluacion.setAdapter(adaptadorSpinnerDocentes);
            docenteVME = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
            docenteVME.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
                @Override
                public void onChanged(@Nullable List<Docente> docentes) {
                    try {
                        if(extras.getInt(EvaluacionActivity.OPERACION_EVALUACION) == EvaluacionActivity.EDITAR_EVALUACION){
                            Evaluacion ev = evaluacionViewModel.getEval(extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION));
                            Docente doc = docenteVME.getDocente(ev.getCarnetDocenteFK());
                            //añade los elementos del livedata a las listas para alamcenar nombre e id de asignatura
                            for (Docente x : docentes) {
                                docentesNom.add(x);
                                if(x.getCarnetDocente().equals(doc.getCarnetDocente()))
                                    spinCarnetDocenteEvaluacion.setSelection(docentesNom.indexOf(x));
                            }
                            //refresca (necesario para mostrar los datos recuperados en el spinner)
                            adaptadorSpinnerDocentes.notifyDataSetChanged();
                        } else {
                            for (Docente x : docentes)
                                docentesNom.add(x);
                            adaptadorSpinnerDocentes.notifyDataSetChanged();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });
            //x.getCarnetDocente()+" - " + x.getNomDocente() + " " + x.getApellidoDocente()
            //--fin llenado spinner docentes

            //nomeacuerdoxdddddd
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

            //PARA EDITAR
            //instancia View Model de evaluacion
            evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            Evaluacion auxiliar;
            int idEvaluacion = 0, operacionEv = 0;
            if (extras != null) {
                idEvaluacion = extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION);
                operacionEv = extras.getInt(EvaluacionActivity.OPERACION_EVALUACION);
                //verificar extras de intent
                if(operacionEv == EvaluacionActivity.EDITAR_EVALUACION && idEvaluacion != 0){
                    //obtener evaluacion auxiliar
                    auxiliar = evaluacionViewModel.getEval(idEvaluacion);
                    //obtener docente actual
                    Docente docenteActual = docenteVME.getDocente(auxiliar.getCarnetDocenteFK());
                    //obtener tipo evaluacion actual
                    TipoEvaluacion tipoEvaluacionActual = tipoEvaluacionVM.getTipoEvaluacion(auxiliar.getIdTipoEvaluacionFK());
                    //obtener asignatura actual
                    Asignatura asignaturaActual = asignaturaVME.obtenerAsignatura(auxiliar.getCodigoAsignaturaFK());
                    //settear editTexts con los objetos obtenidos
                    editNombreEvaluacion.setText(auxiliar.getNomEvaluacion());
                    editDescripcionEvaluacion.setText(auxiliar.getDescripcion());
                    editNumParticipantesEvaluacion.setText(auxiliar.getNumParticipantes()+"");
                    //separa en array la fecha obtenida
                    String[] fechaInicioAux = auxiliar.getFechaInicio().split("/");
                    //se ingresa la fecha desde el array
                    dpickFechaInicioEvaluacion.init(Integer.parseInt(fechaInicioAux[2]),Integer.parseInt(fechaInicioAux[1]),Integer.parseInt(fechaInicioAux[0]), null);
                    String[] fechaFinAux = auxiliar.getFechaFin().split("/");
                    dpickFechaFinEvaluacion.init(Integer.parseInt(fechaFinAux[2]),Integer.parseInt(fechaFinAux[1]),Integer.parseInt(fechaFinAux[0]), null);
                    String[] fechaEntregaAux;
                    boolean prueba = auxiliar.getFechaEntregaNotas().equals(ENTREGA_NOTAS_PLACEHOLDER);
                    setTitle(R.string.titulo_EA_editarEval);
                    if(!prueba) {
                        fechaEntregaAux = auxiliar.getFechaEntregaNotas().split("/");
                        dpickFechaEntregaEvaluacion.init(Integer.parseInt(fechaEntregaAux[2]), Integer.parseInt(fechaEntregaAux[1]), Integer.parseInt(fechaEntregaAux[0]), null);
                    }
                    dpickFechaEntregaEvaluacion.setVisibility(View.VISIBLE);
                    fechaEntregaEvaluacionLabel.setVisibility(View.VISIBLE);
                }
                else if (operacionEv == EvaluacionActivity.AÑADIR_EVALUACION && idEvaluacion == 0){
                    dpickFechaEntregaEvaluacion.setVisibility(View.GONE);
                    fechaEntregaEvaluacionLabel.setVisibility(View.GONE);
                    setTitle(R.string.titulo_EA_nuevaEval);
                }
            }

        } catch (Exception e){
            Toast.makeText(NuevaEditarEvaluacionActivity.this, e.getMessage()+ " " + e.getCause() + "\n", Toast.LENGTH_LONG).show();
        }

    }

    private void guardarEvaluacion() {
        try {
            //---alamcenar NOMBRE evaluacion
            String nombre = editNombreEvaluacion.getText().toString();

            //---obtener valor de spinner ASIGNATURA
            Asignatura asignaturaAux1 = (Asignatura) spinCodigoAsignaturaEvaluacion.getSelectedItem();
            //String[] asignaturaAux2 = asignaturaAux1.split("-");
            //almacenar id de ASIGNATURA
            //String asignatura = asignaturaAux2[0].trim();

            //---obtener valor de spinner TIPO EVALUACION
            TipoEvaluacion tipoEvalAux1 = (TipoEvaluacion) spinTipoEvaluacion.getSelectedItem();
            //String[] tipoEvalAux2 = tipoEvalAux1.split("-");
            //almacenar TIPO EVAL
            //String tipoEval = tipoEvalAux2[0].trim();

            //---obtener valor de spinner DOCENTE
            Docente docenteAux1 = (Docente) spinCarnetDocenteEvaluacion.getSelectedItem();
            //String[] docenteAux2 = docenteAux1.split("-");
            //almacenar DOCENTE
            //String docente = docenteAux2[0].trim();

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

            //---obtener valor de datepicker FECHA FIN evaluacion
            StringBuilder fechEntregaNotas = new StringBuilder(10);
            //concatenar
            fechEntregaNotas.append(dpickFechaEntregaEvaluacion.getDayOfMonth()).append("/").append(dpickFechaEntregaEvaluacion.getMonth()).append("/").append(dpickFechaEntregaEvaluacion.getYear());
            //almacenar FECHA FIN
            String fechaEntrega = fechEntregaNotas.toString();

            //---almacenar DESCRIPCION
            String descripcion = editDescripcionEvaluacion.getText().toString();

            //---almacenar PARTICIPANTES
            String participantes = editNumParticipantesEvaluacion.getText().toString();

            if(nombre.trim().isEmpty() || descripcion.trim().isEmpty() || participantes.trim().isEmpty()){
                Toast.makeText(this,"Por favor, llena todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }            //instancia View Model de evaluacion
            evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);

            //obtener extras del intent
            Bundle extras = getIntent().getExtras();
            int idEvaluacion = 0, operacionEv = 0;
            if (extras != null) {
                idEvaluacion = extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION);
                operacionEv = extras.getInt(EvaluacionActivity.OPERACION_EVALUACION);
                //verificar extras de intent
                if (operacionEv == EvaluacionActivity.EDITAR_EVALUACION && idEvaluacion != 0) {
                    //Objeto Evaluación auxiliar construido a partir de los datos almacenados
                    Evaluacion aux = new Evaluacion(docenteAux1.getCarnetDocente(),tipoEvalAux1.getIdTipoEvaluacion(),asignaturaAux1.getCodigoAsignatura(),nombre,fechaInicio,fechaFin,descripcion,fechaEntrega,Integer.parseInt(participantes));
                    aux.setIdEvaluacion(idEvaluacion);
                    //insertar
                    evaluacionViewModel.updateEval(aux);
                    //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
                    Toast.makeText(NuevaEditarEvaluacionActivity.this, "actualizado con éxito: " + nombre + "-" + fechaInicio + "-" + fechaFin + "-" + descripcion + "-" + participantes, Toast.LENGTH_LONG).show();
                } else if (operacionEv == EvaluacionActivity.AÑADIR_EVALUACION && idEvaluacion == 0) {
                    //Objeto Evaluación auxiliar construido a partir de los datos almacenados
                    Evaluacion aux = new Evaluacion(docenteAux1.getCarnetDocente(),tipoEvalAux1.getIdTipoEvaluacion(),asignaturaAux1.getCodigoAsignatura(),nombre,fechaInicio,fechaFin,descripcion,ENTREGA_NOTAS_PLACEHOLDER,Integer.parseInt(participantes));
                    //insertar
                    evaluacionViewModel.insertEval(aux);
                    //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
                    Toast.makeText(NuevaEditarEvaluacionActivity.this, "insertado con éxito: " + nombre  + "-" + fechaInicio + "-" + fechaFin + "-" + descripcion + "-" + participantes, Toast.LENGTH_LONG).show();
                }
            }
            //salir de la actividad
            finish();
        } catch (Exception e){
            Toast.makeText(NuevaEditarEvaluacionActivity.this, e.getMessage() + " /// " + " /// " + e.fillInStackTrace().toString(), Toast.LENGTH_LONG).show();
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