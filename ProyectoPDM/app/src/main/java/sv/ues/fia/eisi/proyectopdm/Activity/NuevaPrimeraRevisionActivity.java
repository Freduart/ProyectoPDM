package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class NuevaPrimeraRevisionActivity extends AppCompatActivity {

    private PrimeraRevisionViewModel primeraRevisionViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
    private List<DetalleEvaluacion> detalleEvaluacionPorAlumno;
    private EvaluacionViewModel evaluacionViewModel;
    private Evaluacion evaluacionPr;
    public String FECHA_ENTREGA;
    public String LOCAL_PH_PR;
    public String NOTA_PH_PR;
    public String ESTADO_PH_PR;
    private Spinner spindetalleEFK;
    private DatePicker dpickfechaSoli;
    private EditText notaAntes;
    private EditText observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_primera_revision);
            final Bundle extraidAlUser = getIntent().getExtras();
            final Bundle idEvaExtra = getIntent().getExtras();
            NOTA_PH_PR = getText(R.string.nota_place_holder_PR).toString();
            ESTADO_PH_PR = getText(R.string.estado_placeholder_PR).toString();
            LOCAL_PH_PR = getText(R.string.local_placeholder_PR).toString();
            spindetalleEFK = (Spinner) findViewById(R.id.editarDetalleEvaFK);
            dpickfechaSoli = (DatePicker) findViewById(R.id.editarFechaSolicitud);
            notaAntes = (EditText) findViewById(R.id.editarNotaAntesPR);
            observaciones = (EditText) findViewById(R.id.editarObservacionesPR);
            FECHA_ENTREGA = getText(R.string.fecha_placeholder_eval).toString();

            //Spinner de detalle por alumno loggueado
            final ArrayList<String> detallesNom = new ArrayList<>();
            final ArrayAdapter<String> adapterSpinnerDetalleE = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, detallesNom);
            spindetalleEFK.setAdapter(adapterSpinnerDetalleE);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            int idAlUser = 0;
            if (extraidAlUser != null) {
                idAlUser = extraidAlUser.getInt(LoginActivity.ID_USUARIO);
            }
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            int idEvaPr = 0;
            if (idEvaExtra != null) {
                idEvaPr = idEvaExtra.getInt(VerEvaluacionActivity.ID_EVAL);
            }
            evaluacionPr = primeraRevisionViewModel.obtenerEvaPR(idEvaPr);

            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            detalleEvaluacionViewModel.getDetallePorUsuarioYEvaluacion(idAlUser, idEvaPr).observe(this, new Observer<List<DetalleEvaluacion>>() {
                @Override
                public void onChanged(List<DetalleEvaluacion> detalleEvaluacions) {
                    try {
                        for (DetalleEvaluacion d : detalleEvaluacions) {
                            detalleEvaluacionPorAlumno = detalleEvaluacionViewModel.getDetallePorAlumno(d.getCarnetAlumnoFK());
                            if (!detalleEvaluacionPorAlumno.isEmpty()) {
                                detallesNom.add(d.getIdDetalleEv() + " - " + d.getIdEvaluacionFK() + " / " + d.getCarnetAlumnoFK());
                            }
                            adapterSpinnerDetalleE.notifyDataSetChanged();
                        }
                    } catch (Exception e) {

                    }
                }
            });//fin de llenado spinner detalle por alumno

        } catch (Exception e) {
            Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    public void guardarPrimeraRevision() {
        try {
            //Obtener valor de spinner de Detalle de evaluación
            String detalleAux1 = spindetalleEFK.getSelectedItem().toString();
            String[] detalleAux2 = detalleAux1.split("-");
            String detalle = detalleAux2[0].trim();
            //Obtener el valor del datepicker
            StringBuilder fecha = new StringBuilder(10);
            //concatenar
            fecha.append(dpickfechaSoli.getDayOfMonth()).append("/").append(dpickfechaSoli.getMonth() + 1).append("/").append(dpickfechaSoli.getYear());
            //Almacenar fecha de solicitud
            String fechaSolicitud = fecha.toString();
            String notaAn = notaAntes.getText().toString();
            String ob = observaciones.getText().toString();
            if (notaAn.trim().isEmpty() || ob.isEmpty()) {
                Toast.makeText(this, R.string.error_form_incompleto_eval, Toast.LENGTH_LONG).show();
                return;
            }

            String[] fechaAuxiliar = evaluacionPr.getFechaEntregaNotas().split("/");
            int dia = Integer.parseInt(fechaAuxiliar[0]);
            int mes = Integer.parseInt(fechaAuxiliar[1]);
            int anio = Integer.parseInt(fechaAuxiliar[2]);
            int diaActual = dpickfechaSoli.getDayOfMonth();
            int mesActual = dpickfechaSoli.getMonth();
            int anioActual = dpickfechaSoli.getYear();
            if (evaluacionPr.getFechaEntregaNotas().equals(FECHA_ENTREGA)) {
                Toast.makeText(NuevaPrimeraRevisionActivity.this, R.string.peridohabilantes, Toast.LENGTH_LONG).show();
            } else if (((diaActual > dia) && (diaActual <= (dia + 5))) && (mesActual == mes) && (anioActual == anio)) {
                PrimeraRevision pr = new PrimeraRevision(LOCAL_PH_PR, Integer.parseInt(detalle), fechaSolicitud, Boolean.parseBoolean(ESTADO_PH_PR), Double.parseDouble(notaAn), Double.parseDouble(NOTA_PH_PR), ob);
                primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
                primeraRevisionViewModel.insertPrimeraRevision(pr);
                Toast.makeText(NuevaPrimeraRevisionActivity.this, R.string.prinsertada, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(NuevaPrimeraRevisionActivity.this, R.string.periodohabildespues, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.agregar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar:
                guardarPrimeraRevision();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
