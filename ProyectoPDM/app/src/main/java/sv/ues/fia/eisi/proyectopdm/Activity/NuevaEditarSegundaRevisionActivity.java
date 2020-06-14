package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;


public class NuevaEditarSegundaRevisionActivity extends AppCompatActivity {

    private int idPrimera;

    private SegundaRevision segundaRevisionActual;

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
            editNotaFinalSegundaRevision = findViewById(R.id.edit_notafinal_segundarevision);
            editObservacionesSegundaRevision = findViewById(R.id.edit_observaciones_segundarevision);
            dpickFechaSolicitudSegundaRevision = findViewById(R.id.edit_fecha_solicitud_segundarevision);
            tpickHoraSegundaRevision = findViewById(R.id.edit_hora_segundarevision);
            dpickFechaSegundaRevision = findViewById(R.id.edit_fecha_segundarevision);

            //nomeacuerdoxdddddd
            Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

            //PARA EDITAR
            //instancia View Model de segundaRevision
            segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);
            SegundaRevision auxiliar;
            int idPrimeraRevision = 0, operacionEv = 0;
            if (extras != null) {
                idPrimeraRevision = extras.getInt(EditarPrimeraRevisionActivity.IDENTIFICADOR_PRIMERA_REVISION);
                operacionEv = extras.getInt(EditarPrimeraRevisionActivity.OPERACION_SEGUNDA_REVISION);
                //verificar extras de intent
                if(operacionEv == EditarPrimeraRevisionActivity.EDITAR_SEGUNDA_REVISION){
                    //obtener segundaRevision auxiliar
                    auxiliar = segundaRevisionViewModel.getSegundaRevision(idPrimeraRevision);
                    //asigna id de primera revision a atributo de clase
                    idPrimera = idPrimeraRevision;
                    segundaRevisionActual = auxiliar;
                    //colocar nota obtenida en edittext
                    editNotaFinalSegundaRevision.setText(String.format(Locale.US, "%.2f", auxiliar.getNotaFinalSegundaRev()));
                    //colocar observación obtenida en edittext
                    editObservacionesSegundaRevision.setText(auxiliar.getObservacionesSegundaRev());
                    //separa en array la fecha obtenida
                    String[] fechaSolicitud = auxiliar.getFechaSolicitudSegRev().split("/");
                    //se ingresa la fecha desde el array
                    dpickFechaSolicitudSegundaRevision.init(Integer.parseInt(fechaSolicitud[2]),Integer.parseInt(fechaSolicitud[1]),Integer.parseInt(fechaSolicitud[0]), null);
                    //separa la fecha de revision en un array
                    String[] fechaRevision = auxiliar.getFechaSegundaRev().split("/");
                    //se coloca la fecha en el dpick desde array
                    dpickFechaSegundaRevision.init(Integer.parseInt(fechaRevision[2]),Integer.parseInt(fechaRevision[1]),Integer.parseInt(fechaRevision[0]), null);
                    //se separa la hora de revision en un array
                    String[] horaRevision = auxiliar.getFechaSegundaRev().split(":");
                    //se coloca la hora desde el array
                    tpickHoraSegundaRevision.setCurrentHour(Integer.parseInt(horaRevision[0]));
                    //se colocan los minutos
                    tpickHoraSegundaRevision.setCurrentMinute(Integer.parseInt(horaRevision[1]));
                    //título de activity
                    setTitle(R.string.titulo_EA_editarsegrev);
                    //se activan los campos nota y observaciones
                    editNotaFinalSegundaRevision.setVisibility(View.VISIBLE);
                    editObservacionesSegundaRevision.setVisibility(View.VISIBLE);
                }
                else if (operacionEv == EditarPrimeraRevisionActivity.AÑADIR_SEGUNDA_REVISION){
                    //se desactivan los campos nota y observaciones
                    editNotaFinalSegundaRevision.setVisibility(View.GONE);
                    editObservacionesSegundaRevision.setVisibility(View.GONE);
                    //título de activity
                    setTitle(R.string.titulo_EA_nuevasegrev);
                }
            }

        } catch (Exception e){
            Toast.makeText(NuevaEditarSegundaRevisionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void guardarSegundaRevision() {
        try {
            //almacenar FECHA REVISION
            String fechaRevision = String.format("%s/%s/%s", dpickFechaSegundaRevision.getDayOfMonth(), dpickFechaSegundaRevision.getMonth(), dpickFechaSegundaRevision.getYear());
            //almacenar FECHA SOLICITUD
            String fechaSolicitud = String.format("%s/%s/%s",dpickFechaSolicitudSegundaRevision.getDayOfMonth(), dpickFechaSolicitudSegundaRevision.getMonth(), dpickFechaSolicitudSegundaRevision.getYear());
            //almacenar HORA
            String hora = String.format("%s:%s",tpickHoraSegundaRevision.getCurrentHour(), tpickHoraSegundaRevision.getCurrentMinute());

            //obtener extras del intent
            Bundle extras = getIntent().getExtras();
            int operacionEv = 0;
            if (extras != null) {
                operacionEv = extras.getInt(EditarPrimeraRevisionActivity.OPERACION_SEGUNDA_REVISION);
                //verificar extras de intent
                if (operacionEv == EditarPrimeraRevisionActivity.EDITAR_SEGUNDA_REVISION) {
                    //---almacenar NOTA FINAL
                    String notaFinal = editNotaFinalSegundaRevision.getText().toString();

                    //---almacenar OBSERVACIONES
                    String observaciones = editObservacionesSegundaRevision.getText().toString().trim();

                    //Objeto SegRevuación auxiliar construido a partir de los datos almacenados
                    SegundaRevision aux = new SegundaRevision(idPrimera,fechaRevision,hora,Double.parseDouble(notaFinal),observaciones,fechaSolicitud);
                    aux.setIdSegundaRevision(segundaRevisionActual.getIdSegundaRevision());
                    //insertar
                    segundaRevisionViewModel.updateSegundaRevision(aux);
                    //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
                    Toast.makeText(NuevaEditarSegundaRevisionActivity.this, String.format("%s%s%s",getText(R.string.inic_notif_segrev), String.format("%d",aux.getIdSegundaRevision()), getText(R.string.accion_actualizar_notif_eval)), Toast.LENGTH_LONG).show();
                } else if (operacionEv == EditarPrimeraRevisionActivity.AÑADIR_SEGUNDA_REVISION ) {

                    //Objeto SegRevuación auxiliar construido a partir de los datos almacenados
                    SegundaRevision aux;
                    aux = new SegundaRevision(idPrimera,fechaRevision,hora,fechaSolicitud);
                    //insertar
                    segundaRevisionViewModel.insertSegundaRevision(aux);
                    //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
                    Toast.makeText(NuevaEditarSegundaRevisionActivity.this,String.format("%s%s", getText(R.string.inic_notif_segrev) , getText(R.string.accion_insertar_notif_eval)), Toast.LENGTH_LONG).show();
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
    }
}
