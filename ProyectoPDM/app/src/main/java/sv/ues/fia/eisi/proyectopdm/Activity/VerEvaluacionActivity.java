package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.resources.TextAppearance;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

public class VerEvaluacionActivity extends AppCompatActivity {
    public static final String ID_EVAL = "sv.ues.fia.eisi.proyectopdm.Activity.EXTRA_ID";
    public static final String NOTAMAX = "sv.ues.fia.eisi.proyectopdm.Activity.eval.NOTAMAX";

    private Evaluacion evaluacionActual;
    private TipoEvaluacion tipoEvaluacionActual;
    private Asignatura asignaturaActual;
    private Docente docenteActual;
    private DetalleEvaluacion detalleEvaluacion;

    private int id_usuario, rol_usuario;
    private int notamax;
    private Boolean entregaNotas = true;

    private boolean currentUserAlumno = false;
    private TextView headlineNotaAlumno;
    private TextView notaAlumnoDisplay;
    private Button solicitarRevisionBtn;
    private Button alumnosDeEvaluacion;

    private EvaluacionViewModel evaluacionViewModel;
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
    private TextView noseharealizado;

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
            alumnosDeEvaluacion = findViewById(R.id.agregar_alumnos_evaluacion);
            noseharealizado = findViewById(R.id.text_no_se_ha_realizado_eval);
            noseharealizado.setVisibility(View.GONE);
            //inicializar viewmodels
            evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            //obtener intent de activity
            Bundle extras = getIntent().getExtras();
            int idEvaluacion = 0;
            if (extras != null) {
                idEvaluacion = extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION);
                //recibe id del usuario desde el extra
                id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
                //recibe rol del usuario desde el extra
                rol_usuario = extras.getInt(LoginActivity.USER_ROL);
                currentUserAlumno= rol_usuario == 3;
            }
            //obtener evaluación actual por medio de EXTRA_ID de intent
            evaluacionActual = evaluacionViewModel.getEval(idEvaluacion);
            //obtener objetos relacionados
            tipoEvaluacionActual = evaluacionViewModel.getTiposEval(idEvaluacion);
            docenteActual = evaluacionViewModel.getDocentesEvaluacion(idEvaluacion);
            asignaturaActual = evaluacionViewModel.getAsignaturaEvaluacion(idEvaluacion);
            //convertir participantes en string
            String partAux = evaluacionActual.getNumParticipantes() + "";
            //coloca texto en textviews
            dispNombreEvaluacion.setText(evaluacionActual.getNomEvaluacion());
            dispDescripcionEvaluacion.setText(evaluacionActual.getDescripcion());
            dispAsignaturaEvaluacion.setText(String.format("%s - %s", asignaturaActual.getCodigoAsignatura(), asignaturaActual.getNomasignatura()));
            dispDocenteEvaluacion.setText(String.format("%s - %s %s", docenteActual.getCarnetDocente(), docenteActual.getNomDocente(), docenteActual.getApellidoDocente()));
            dispTipoEvaluacion.setText(tipoEvaluacionActual.getTipoEvaluacion());
            notamax = evaluacionActual.getNotaMaxima();
            //separa, añade 1 a mes e imprime fecha inicio
            String[] fechaAux = evaluacionActual.getFechaInicio().split("/");
            int mesAux = Integer.parseInt(fechaAux[1]) + 1;
            String fechaAuxSalida = String.format("%s/%s/%s", fechaAux[0],mesAux,fechaAux[2]);
            dispFechaInicioEvaluacion.setText(fechaAuxSalida);

            //separa, añade 1 a mes e imprime fecha fin
            fechaAux = new String[3]; mesAux = 0; fechaAuxSalida = "";
            fechaAux = evaluacionActual.getFechaFin().split("/");
            mesAux = Integer.parseInt(fechaAux[1]) + 1;
            fechaAuxSalida = String.format("%s/%s/%s", fechaAux[0],mesAux,fechaAux[2]);
            dispFechaFinEvaluacion.setText(fechaAuxSalida);

            if(!evaluacionActual.getFechaEntregaNotas().equals(getText(R.string.fecha_placeholder_eval).toString())){
                //separa, añade 1 a mes e imprime fecha entrega notas
                fechaAux = new String[3]; mesAux = 0; fechaAuxSalida = "";
                fechaAux = evaluacionActual.getFechaEntregaNotas().split("/");
                mesAux = Integer.parseInt(fechaAux[1]) + 1;
                fechaAuxSalida = String.format("%s/%s/%s", fechaAux[0],mesAux,fechaAux[2]);
                dispFechaEntregaEvaluacion.setText(fechaAuxSalida);
            } else {
                dispFechaEntregaEvaluacion.setText(getText(R.string.fecha_noenc_eval));
            }
            dispParticipantesEvaluacion.setText(partAux);

            //si es alumno, mostrar nota y solicitud de revisión
            if(currentUserAlumno){
                EvaluacionViewModel evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
                detalleEvaluacion = detalleEvaluacionViewModel.getDetalleAlumnoEvaluacion(evaluacionActual.getIdEvaluacion(),evaluacionViewModel.getAlumnConUsuario(id_usuario).getCarnetAlumno());
                //PrimeraRevisionViewModel
                //primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
                headlineNotaAlumno.setVisibility(View.VISIBLE);
                notaAlumnoDisplay.setVisibility(View.VISIBLE);
                solicitarRevisionBtn.setVisibility(View.VISIBLE);
                if(detalleEvaluacion==null){
                    entregaNotas = false;
                    solicitarRevisionBtn.setEnabled(false);
                    noseharealizado.setVisibility(View.VISIBLE);
                } else {
                    if(!evaluacionActual.getFechaEntregaNotas().equals(getText(R.string.fecha_placeholder_eval).toString())) {
                        entregaNotas = true;
                        notaAlumnoDisplay.setText(String.format("%s", detalleEvaluacion.getNota()));
                        solicitarRevisionBtn.setEnabled(true);
                        noseharealizado.setVisibility(View.GONE);
                        List<PrimeraRevision> primeraRevisionList = primeraRevisionViewModel.getRevisionPorDetalle(detalleEvaluacion.getIdDetalleEv());
                        if (primeraRevisionList.size() != 0) {
                            notaAlumnoDisplay.setText(String.format("%s", primeraRevisionList.get(0).getNotaDespuesPrimeraRev()));
                            solicitarRevisionBtn.setEnabled(false);
                            noseharealizado.setVisibility(View.VISIBLE);
                            noseharealizado.setText(getText(R.string.sehasolicitado).toString());
                            SegundaRevisionViewModel segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);
                            SegundaRevision segundaRevisionAux = segundaRevisionViewModel.getSegundaRevision(primeraRevisionList.get(0).getIdPrimerRevision());
                            if (segundaRevisionAux != null) {
                                Double notaSegundaRevision = segundaRevisionAux.getNotaFinalSegundaRev();

                                if (!String.format("%s", notaSegundaRevision).trim().equals(""))
                                    notaAlumnoDisplay.setText(String.format("%s", notaSegundaRevision));
                                else {
                                    notaAlumnoDisplay.setText("-");
                                    noseharealizado.setText(getText(R.string.enrevision));
                                }
                            }
                        }
                    } else {
                        entregaNotas = false;
                        solicitarRevisionBtn.setEnabled(false);
                        noseharealizado.setVisibility(View.VISIBLE);
                        noseharealizado.setText(getText(R.string.sinentreganota));
                    }
                }
                alumnosDeEvaluacion.setVisibility(View.GONE);
                solicitarRevisionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            //List<PrimeraRevision> primeraRevisions= primeraRevisionViewModel.getRevisionPorDetalle(detalleEvaluacion.getIdDetalleEv());
                            //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                            Intent intent = new Intent(VerEvaluacionActivity.this, NuevaPrimeraRevisionActivity.class);
                            //se mete en un extra del intent, el id
                            //intent.putExtra(PrimeraRevisionActivity.IDENTIFICADOR_PR, primeraRevisions.get(0).getIdPrimerRevision());
                            intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                            intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                            intent.putExtra(ID_EVAL, evaluacionActual.getIdEvaluacion());
                            intent.putExtra(NOTAMAX,notamax);
                            //inicia la activity
                            startActivity(intent);
                        } catch (Exception e){
                            Toast.makeText(VerEvaluacionActivity.super.getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                headlineNotaAlumno.setVisibility(View.VISIBLE);
                headlineNotaAlumno.setText(getText(R.string.valornota).toString());
                notaAlumnoDisplay.setVisibility(View.VISIBLE);
                notaAlumnoDisplay.setText(String.format("%s",evaluacionActual.getNotaMaxima()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    notaAlumnoDisplay.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    notaAlumnoDisplay.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                    notaAlumnoDisplay.setPaddingRelative(notaAlumnoDisplay.getPaddingStart(),notaAlumnoDisplay.getPaddingTop(),notaAlumnoDisplay.getPaddingEnd(),40);
                }
                solicitarRevisionBtn.setVisibility(View.GONE);
                alumnosDeEvaluacion.setVisibility(View.VISIBLE);
                //al hacer clic en el boton
                alumnosDeEvaluacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                        Intent intent = new Intent(VerEvaluacionActivity.this, DetalleEvaluacionActivity.class);
                        //se mete en un extra del intent, el id
                        intent.putExtra(EvaluacionActivity.IDENTIFICADOR_EVALUACION, evaluacionActual.getIdEvaluacion());
                        //inicia la activity
                        startActivity(intent);
                    }
                });

            }
            //título
            setTitle(R.string.titulo_ver_eval);
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString() ,Toast.LENGTH_LONG).show();
            Log.d("error en OnCreate", e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ver_evaluacion_menu, menu);
        MenuItem botonEstad = menu.findItem(R.id.abrir_estadisticas);
        if(entregaNotas){
            botonEstad.setEnabled(true);
            botonEstad.setIcon(R.drawable.ic_chart);
        }
        else {
            botonEstad.setEnabled(false);
            botonEstad.setIcon(R.drawable.ic_chart_deac);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.abrir_estadisticas:
                Intent intent = new Intent(VerEvaluacionActivity.this, EvaluacionGraficasActivity.class);
                intent.putExtra(ID_EVAL, evaluacionActual.getIdEvaluacion());
                intent.putExtra(LoginActivity.USER_ROL,rol_usuario);
                intent.putExtra(LoginActivity.ID_USUARIO,id_usuario);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (currentUserAlumno) {
                if (detalleEvaluacion == null) {
                    solicitarRevisionBtn.setEnabled(false);
                    noseharealizado.setVisibility(View.VISIBLE);
                } else {
                    if (!evaluacionActual.getFechaEntregaNotas().equals(getText(R.string.fecha_placeholder_eval).toString())) {
                        entregaNotas = true;
                        notaAlumnoDisplay.setText(String.format("%s", detalleEvaluacion.getNota()));
                        solicitarRevisionBtn.setEnabled(true);
                        noseharealizado.setVisibility(View.GONE);
                        List<PrimeraRevision> primeraRevisionList = primeraRevisionViewModel.getRevisionPorDetalle(detalleEvaluacion.getIdDetalleEv());
                        if (primeraRevisionList.size() != 0) {
                            notaAlumnoDisplay.setText(String.format("%s", primeraRevisionList.get(0).getNotaDespuesPrimeraRev()));
                            solicitarRevisionBtn.setEnabled(false);
                            noseharealizado.setVisibility(View.VISIBLE);
                            noseharealizado.setText(getText(R.string.sehasolicitado).toString());
                            SegundaRevisionViewModel segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);
                            SegundaRevision segundaRevisionAux = segundaRevisionViewModel.getSegundaRevision(primeraRevisionList.get(0).getIdPrimerRevision());
                            if (segundaRevisionAux != null) {
                                Double notaSegundaRevision = segundaRevisionAux.getNotaFinalSegundaRev();

                                if (!String.format("%s", notaSegundaRevision).trim().equals(""))
                                    notaAlumnoDisplay.setText(String.format("%s", notaSegundaRevision));
                                else {
                                    notaAlumnoDisplay.setText("-");
                                    noseharealizado.setText(getText(R.string.enrevision));
                                }
                            }
                        }
                    } else {
                        entregaNotas = false;
                        solicitarRevisionBtn.setEnabled(false);
                        noseharealizado.setVisibility(View.VISIBLE);
                        noseharealizado.setText(getText(R.string.sinentreganota));
                    }
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}
