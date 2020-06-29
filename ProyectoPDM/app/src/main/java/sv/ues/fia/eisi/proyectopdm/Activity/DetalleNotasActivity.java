package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;

public class DetalleNotasActivity extends AppCompatActivity {
    public static final String ID_DETALLE = "ID_det_Actual";
    public static final String NOTAMAX = "sv.ues.fia.eisi.proyectopdm.Activity.eval.NOTAMAX";

    private int idActual;
    private PrimeraRevision revisionRespectiva;
    private DetalleEvaluacion detalleEvaluacion;
    private TextView nombreEstudiante;
    private EditText notaEstudiante;
    private Button editarPrimeraRevision;
    private TextView mensajeRevisionExistente;

    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
    private PrimeraRevisionViewModel primeraRevisionViewModel;
    private SegundaRevisionViewModel segundaRevisionViewModel;
    private EvaluacionViewModel evaluacionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_notas);
        nombreEstudiante = findViewById(R.id.titulo_nombre_detalleestudiante);
        notaEstudiante = findViewById(R.id.nota_editar_detalle);
        editarPrimeraRevision = findViewById(R.id.boton_primera_revision_detalle);
        mensajeRevisionExistente = findViewById(R.id.mensaje_revision_existente);
        detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
        evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
        primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
        segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);
        Double notaAux = 0d;
        //obtener extras del intent
        final Bundle extras = getIntent().getExtras();
        if (extras != null){
            idActual = extras.getInt(DetalleEvaluacionActivity.ID_DETALLE_EVAL);
            try {
                detalleEvaluacion = detalleEvaluacionViewModel.getDetalleEvaluacion(idActual);
                notaAux = detalleEvaluacion.getNota();
                if(notaAux < 0)
                    notaEstudiante.setText("");
                else
                    notaEstudiante.setText(String.format("%s", notaAux));
                nombreEstudiante.setText(detalleEvaluacion.getCarnetAlumnoFK());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mensajeRevisionExistente.setVisibility(View.GONE);
        actualizarBotonesRevision();
        //obtener Primera Revision
        setTitle(R.string.display_nota_alumno_evaluacion);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.editar_detalle_evaluacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_detalleeval:
                try {
                    guardarDetalle();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        actualizarBotonesRevision();
    }

    private void guardarDetalle() throws InterruptedException, ExecutionException, TimeoutException {
        DetalleEvaluacion aux = new DetalleEvaluacion(detalleEvaluacion.getIdEvaluacionFK(),detalleEvaluacion.getCarnetAlumnoFK());
        Evaluacion evDeDetalle = evaluacionViewModel.getEval(detalleEvaluacion.getIdEvaluacionFK());

        //VALIDACION NOTA
        String campoNota = notaEstudiante.getText().toString();
        //nota no puede estar vacía
        if(campoNota.trim().isEmpty()){
            Toast.makeText(this,getText(R.string.error_form_incompleto_eval), Toast.LENGTH_LONG).show();
            return;
            //caso contrario, no puede ser mayor a la nota máxima de evaluacion
        } else if(Float.parseFloat(campoNota.trim()) > (float) evDeDetalle.getNotaMaxima()){
            //obtiene nota máxima de evaluacion en string
            String notaMaxAux = String.format("%s",evDeDetalle.getNotaMaxima());
            //mensaje de error, informa al usuario que la nota no puede ser mayor a la nota máxima
            Toast.makeText(this,getText(R.string.notamaxerror) + notaMaxAux, Toast.LENGTH_LONG).show();
            //cancela
            return;
        }

        Double notaAux = Double.parseDouble(campoNota);
        aux.setNota(notaAux);
        aux.setIdDetalleEv(detalleEvaluacion.getIdDetalleEv());
        detalleEvaluacionViewModel.updateDetalleEvaluacion(aux);
        Toast.makeText(DetalleNotasActivity.this, getText(R.string.confirmacion_nota), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 113 && resultCode == RESULT_OK) {
            actualizarBotonesRevision();
        }
    }

    private void actualizarBotonesRevision(){
        try {
            notaEstudiante.setEnabled(true);
            List<PrimeraRevision> listRevision = primeraRevisionViewModel.getRevisionPorDetalle(idActual);
            //si la lista no está vacía
            if(!listRevision.isEmpty()){
                editarPrimeraRevision.setEnabled(true);
                notaEstudiante.setEnabled(false);
                mensajeRevisionExistente.setVisibility(View.VISIBLE);
                revisionRespectiva = listRevision.get(0);
                Evaluacion evDeDetalle = evaluacionViewModel.getEval(detalleEvaluacion.getIdEvaluacionFK());
                //al hacer clic corto en un objeto del recycler
                editarPrimeraRevision.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                        Intent intent = new Intent(DetalleNotasActivity.this, EditarPrimeraRevisionActivity.class);
                        //se mete en un extra del intent, el id
                        intent.putExtra(PrimeraRevisionActivity.IDENTIFICADOR_PR, listRevision.get(0).getIdPrimerRevision());

                        intent.putExtra(NOTAMAX,evDeDetalle.getNotaMaxima());
                        //inicia la activity
                        startActivityForResult(intent,113);
                    }
                });
                double notaDespues;
                if(revisionRespectiva.getNotaDespuesPrimeraRev() != 0.0)
                    notaDespues = revisionRespectiva.getNotaDespuesPrimeraRev();
                else
                    notaDespues = revisionRespectiva.getNotasAntesPrimeraRev();
                notaEstudiante.setText(String.format("%s", notaDespues));
                detalleEvaluacion = detalleEvaluacionViewModel.getDetalleEvaluacion(idActual);
                if(!Double.isNaN(notaDespues)){
                    detalleEvaluacion.setNota(notaDespues);
                    detalleEvaluacionViewModel.updateDetalleEvaluacion(detalleEvaluacion);
                }
                SegundaRevision segundaRevision = segundaRevisionViewModel.getSegundaRevision(revisionRespectiva.getIdPrimerRevision());
                if(segundaRevision != null){
                    notaEstudiante.setText(String.format("%s", segundaRevision.getNotaFinalSegundaRev()));
                    editarPrimeraRevision.setText(R.string.titulo_EA_editarsegrev);
                    editarPrimeraRevision.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                            Intent intent = new Intent(DetalleNotasActivity.this, NuevaEditarSegundaRevisionActivity.class);
                            //se mete en un extra del intent, el id
                            intent.putExtra(EditarPrimeraRevisionActivity.IDENTIFICADOR_PRIMERA_REVISION, segundaRevision.getIdPrimeraRevisionFK());
                            intent.putExtra(EditarPrimeraRevisionActivity.OPERACION_SEGUNDA_REVISION, EditarPrimeraRevisionActivity.EDITAR_SEGUNDA_REVISION);
                            intent.putExtra(NOTAMAX,evDeDetalle.getNotaMaxima());
                            //inicia la activity
                            startActivity(intent);
                        }
                    });
                    if(!Double.isNaN(segundaRevision.getNotaFinalSegundaRev())){
                        detalleEvaluacion.setNota(segundaRevision.getNotaFinalSegundaRev());
                        detalleEvaluacionViewModel.updateDetalleEvaluacion(detalleEvaluacion);
                    }
                }
            } else {
                editarPrimeraRevision.setEnabled(false);
            }
        } catch (Exception e){
            Toast.makeText(DetalleNotasActivity.this, getText(R.string.inic_notif_eval), Toast.LENGTH_LONG).show();
        }

    }
}
