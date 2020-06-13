package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;


public class NuevaEditarSegundaRevisionActivity extends AppCompatActivity {/*
    public final String ENTREGA_NOTAS_PLACEHOLDER = getText(R.string.fecha_placeholder_eval).toString();

    private int idPrimera;

    private EditText editIdSegundaRevision;
    private EditText editNotaFinalSegundaRevision;
    private EditText editObservacionesSegundaRevision;
    private DatePicker dpickFechaSolicitudSegundaRevision;
    private TimePicker tpickHoraSegundaRevision;
    private DatePicker dpickFechaSegundaRevision;

    private SegundaRevisionViewModel segundaRevisionViewModel;
    private PrimeraRevisionViewModel primeraRevisionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_editar_segunda_revision);
            //obtener extras del intent
            final Bundle extras = getIntent().getExtras();

            //inicialización de elementos del layout
            editIdSegundaRevision = findViewById(R.id.edit_id_segundarevision);
            editNotaFinalSegundaRevision = findViewById(R.id.edit_notafinal_segundarevision);
            editObservacionesSegundaRevision = findViewById(R.id.edit_observaciones_segundarevision);
            dpickFechaSolicitudSegundaRevision = findViewById(R.id.edit_fecha_solicitud_segundarevision);
            tpickHoraSegundaRevision = findViewById(R.id.edit_hora_segundarevision);
            dpickFechaSegundaRevision = findViewById(R.id.edit_fecha_segundarevision);

            //nomeacuerdoxdddddd
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

            //PARA EDITAR
            //instancia View Model de segundaRevision
            segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);
            SegundaRevision auxiliar;
            int idPrimeraRevision = 0, operacionEv = 0;
            if (extras != null) {
                idPrimeraRevision = extras.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PRIMERA_REVISION);
                operacionEv = extras.getInt(PrimeraRevisionActivity.OPERACION_SEGUNDA_REVISION);
                //verificar extras de intent
                if(operacionEv == PrimeraRevisionActivity.EDITAR_SEGUNDA_REVISION){
                    //obtener segundaRevision auxiliar
                    auxiliar = segundaRevisionViewModel.getSegundaRevision(idPrimeraRevision);
                    idPrimera = idPrimeraRevision;
                    //obtener primeraRevision actual
                    PrimeraRevision primeraRevisionActual = primeraRevisionViewModel.getPrimeraRevision(auxiliar.getIdPrimeraRevisionFK());
                    //settear editTexts con los objetos obtenidos
                    editIdSegundaRevision.setText(auxiliar.getIdSegundaRevision());
                    //convertir a string
                    editNotaFinalSegundaRevision.setText(String.format("%s", auxiliar.getNotaFinalSegundaRev()));
                    editObservacionesSegundaRevision.setText(auxiliar.getObservacionesSegundaRev());
                    //separa en array la fecha obtenida
                    String[] fechaSolicitud = auxiliar.getFechaSolicitudSegRev().split("/");
                    //se ingresa la fecha desde el array
                    dpickFechaSolicitudSegundaRevision.init(Integer.parseInt(fechaSolicitud[2]),Integer.parseInt(fechaSolicitud[1]),Integer.parseInt(fechaSolicitud[0]), null);
                    String[] fechaRevision = auxiliar.getFechaSegundaRev().split("/");
                    dpickFechaSegundaRevision.init(Integer.parseInt(fechaRevision[2]),Integer.parseInt(fechaRevision[1]),Integer.parseInt(fechaRevision[0]), null);
                    String[] horaRevision = auxiliar.getFechaSegundaRev().split(":");
                    tpickHoraSegundaRevision.setCurrentHour(Integer.parseInt(horaRevision[0]));
                    tpickHoraSegundaRevision.setCurrentMinute(Integer.parseInt(horaRevision[1]));
                    setTitle(R.string.titulo_EA_editarsegrev);
                    editNotaFinalSegundaRevision.setVisibility(View.VISIBLE);
                    editObservacionesSegundaRevision.setVisibility(View.VISIBLE);
                }
                else if (operacionEv == PrimeraRevisionActivity.AÑADIR_SEGUNDA_REVISION){
                    editNotaFinalSegundaRevision.setVisibility(View.GONE);
                    editObservacionesSegundaRevision.setVisibility(View.GONE);
                    setTitle(R.string.titulo_EA_nuevasegrev);
                }
            }

        } catch (Exception e){
            Toast.makeText(NuevaEditarSegundaRevisionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void guardarSegundaRevision() {
        try {
            //---alamcenar NOMBRE segundaRevision
            String nombre = editIdSegundaRevision.getText().toString();

            //---obtener PRimeraRevision
            PrimeraRevision primeraRevision = primeraRevisionViewModel.getPrimeraRevision(idPrimera);

            //---obtener valor de datepicker FECHA REVISION segundaRevision
            StringBuilder fechaRevisionBuilder = new StringBuilder(10);
            //concatenar
            fechaRevisionBuilder.append(dpickFechaSegundaRevision.getDayOfMonth()).append("/").append(dpickFechaSegundaRevision.getMonth()).append("/").append(dpickFechaSegundaRevision.getYear());
            //almacenar FECHA REVISION
            String fechaRevision = fechaRevisionBuilder.toString();

            //---obtener valor de datepicker FECHA SOLICITUD segundaRevision
            StringBuilder fechaSolicitudBuilder = new StringBuilder(10);
            //concatenar
            fechaSolicitudBuilder.append(dpickFechaSolicitudSegundaRevision.getDayOfMonth()).append("/").append(dpickFechaSolicitudSegundaRevision.getMonth()).append("/").append(dpickFechaSolicitudSegundaRevision.getYear());
            //almacenar FECHA SOLICITUD
            String fechaSolicitud = fechaSolicitudBuilder.toString();

            //---obtener valor de timepicker HORA segundaRevision
            StringBuilder horaBuilder = new StringBuilder(10);
            //concatenar
            horaBuilder.append(tpickHoraSegundaRevision.getCurrentHour()).append(":").append(tpickHoraSegundaRevision.getCurrentMinute());
            //almacenar HORA
            String hora = horaBuilder.toString();

            if(nombre.trim().isEmpty()){
                Toast.makeText(this,getText(R.string.error_form_incompleto_eval), Toast.LENGTH_LONG).show();
                return;
            }
            //instancia View Model de segundaRevision
            segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);

            //obtener extras del intent
            Bundle extras = getIntent().getExtras();
            int idPrimeraRevision = 0, operacionEv = 0;
            if (extras != null) {
                idPrimeraRevision = extras.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PRIMERA_REVISION);
                operacionEv = extras.getInt(PrimeraRevisionActivity.OPERACION_SEGUNDA_REVISION);
                //verificar extras de intent
                if (operacionEv == PrimeraRevisionActivity.EDITAR_SEGUNDA_REVISION) {

                    //---almacenar NOTA FINAL
                    String notaFinal = editNotaFinalSegundaRevision.getText().toString();

                    //---almacenar OBSERVACIONES
                    String observaciones = editObservacionesSegundaRevision.getText().toString();

                    //Objeto SegRevuación auxiliar construido a partir de los datos almacenados
                    SegundaRevision aux = new SegundaRevision(idPrimeraRevision,fechaRevision,hora,Double.parseDouble(notaFinal),observaciones,fechaSolicitud);

                    //insertar
                    segundaRevisionViewModel.updateSegundaRevision(aux);
                    //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
                    Toast.makeText(NuevaEditarSegundaRevisionActivity.this, getText(R.string.inic_notif_segrev) + nombre + getText(R.string.accion_actualizar_notif_eval), Toast.LENGTH_LONG).show();
                } else if (operacionEv == PrimeraRevisionActivity.AÑADIR_SEGUNDA_REVISION ) {
                    //---almacenar OBSERVACIONES
                    String observaciones = editObservacionesSegundaRevision.getText().toString();

                    //Objeto SegRevuación auxiliar construido a partir de los datos almacenados
                    SegundaRevision aux;
                    if(observaciones.isEmpty())
                        aux = new SegundaRevision(idPrimeraRevision,fechaRevision,hora,fechaSolicitud);
                    else
                        aux = new SegundaRevision(idPrimeraRevision,fechaRevision,hora,observaciones,fechaSolicitud);
                    //insertar
                    segundaRevisionViewModel.insertSegundaRevision(aux);
                    //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
                    Toast.makeText(NuevaEditarSegundaRevisionActivity.this, getText(R.string.inic_notif_segrev) + nombre  + getText(R.string.accion_insertar_notif_eval), Toast.LENGTH_LONG).show();
                }
            }
            //salir de la actividad
            finish();
        } catch (Exception e){
            Toast.makeText(NuevaEditarSegundaRevisionActivity.this, e.getMessage() + " /// " + " /// " + e.fillInStackTrace().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.nueva_segundarevision_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_segundarevision:
                guardarSegundaRevision();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
