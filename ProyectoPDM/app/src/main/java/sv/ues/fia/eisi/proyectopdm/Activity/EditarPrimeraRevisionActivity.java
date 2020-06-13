package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class EditarPrimeraRevisionActivity extends AppCompatActivity {

    private PrimeraRevision primeraRevisionActual;
    private DetalleEvaluacion detalleEvaluacionActual;
    private Local localActual;

    private PrimeraRevisionViewModel primeraRevisionViewModel;
    private LocalViewModel localViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;

    private Spinner spinlocalFK;
    private Spinner spindetalleEFK;
    private DatePicker dpickfechaSoli;
    private Spinner spinestado;
    private EditText notaAntes;
    private EditText notaDespues;
    private EditText observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_primera_revision);
            final Bundle extra = getIntent().getExtras();
            spinlocalFK = (Spinner) findViewById(R.id.editarLocalFK);
            spindetalleEFK = (Spinner) findViewById(R.id.editarDetalleEvaFK);
            dpickfechaSoli = (DatePicker) findViewById(R.id.editarFechaSolicitud);
            spinestado = (Spinner) findViewById(R.id.editarEstadoPR);
            notaAntes = (EditText) findViewById(R.id.editarNotaAntesPR);
            notaDespues = (EditText) findViewById(R.id.editarNotaDespuesPR);
            observaciones = (EditText) findViewById(R.id.editarObservacionesPR);

            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            int idPR= 0;
            if(extra != null){
                idPR = extra.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR);
            }
            primeraRevisionActual = primeraRevisionViewModel.getPrimeraRevision(idPR);

            //Llenar spinner de local
            //Lista para almacenar id de local
            final ArrayList<String> localesId = new ArrayList<>();
            //Adaptador a arreglo para spinner
            final ArrayAdapter<String> adaptadorSpinnerLocal = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localesId);
            //settea layout de dropdown del spinner
            adaptadorSpinnerLocal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //setea el adaptador creado en el spinner
            spinlocalFK.setAdapter(adaptadorSpinnerLocal);
            //Instancia LocalViewModel
            localViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
            //Obtener todos los locales en livedata
            localViewModel.getAllLocales().observe(this, new Observer<List<Local>>() {
                @Override
                public void onChanged(List<Local> locals) {
                    try {
                       PrimeraRevision pr = primeraRevisionViewModel.getPrimeraRevision(extra.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR));
                       Local loc = localViewModel.getLoc(pr.getIdLocalFK());
                       //añade los elementos del livedata a la lista localesId
                       for(Local l : locals){
                           localesId.add(l.getIdLocal());
                           if(l.getIdLocal().equals(loc.getIdLocal())){
                               spinlocalFK.setSelection(localesId.indexOf(l.getIdLocal()));
                           }
                       }
                       //refresca para mostrar los datos recuperados en el spinner
                        adaptadorSpinnerLocal.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(EditarPrimeraRevisionActivity.this, "Local: "+e.getMessage()+ " - "+e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            //Spinner de DetalleEvaluación
            final ArrayList<String> detallesIdNom = new ArrayList<>();
            final ArrayAdapter<String> adaptadorSpinnerDetalle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, detallesIdNom);
            adaptadorSpinnerDetalle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spindetalleEFK.setAdapter(adaptadorSpinnerDetalle);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            detalleEvaluacionViewModel.getAllDetalles().observe(this, new Observer<List<DetalleEvaluacion>>() {
                @Override
                public void onChanged(List<DetalleEvaluacion> detalleEvaluacions) {
                    try {
                        PrimeraRevision pr = primeraRevisionViewModel.getPrimeraRevision(extra.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR));
                        DetalleEvaluacion dete = detalleEvaluacionViewModel.getDetalleEvaluacion(pr.getIdDetalleEvFK());
                        for(DetalleEvaluacion d : detalleEvaluacions){
                            detallesIdNom.add(d.getIdDetalleEv() + " - " +d.getCarnetAlumnoFK());
                            if(d.getIdDetalleEv()== dete.getIdDetalleEv()){
                                spindetalleEFK.setSelection(detallesIdNom.indexOf(d.getIdDetalleEv() + " - " + d.getCarnetAlumnoFK()));
                            }
                        }
                        adaptadorSpinnerDetalle.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(EditarPrimeraRevisionActivity.this, "Detalle: "+e.getMessage()+ " - "+e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            //Datepicker
            String[] fechaAuxiliar = primeraRevisionActual.getFechaSolicitudPrimRev().split("/");
            //se ingresa la fecha desde el array
            dpickfechaSoli.init(Integer.parseInt(fechaAuxiliar[2]),Integer.parseInt(fechaAuxiliar[1]),Integer.parseInt(fechaAuxiliar[0]), null);
            //Spinner de estado
            ArrayAdapter<CharSequence> adapterSpinnerEstado = ArrayAdapter.createFromResource(this, R.array.estado_array, android.R.layout.simple_spinner_item);
            adapterSpinnerEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinestado.setAdapter(adapterSpinnerEstado);
            //spinestado.setSelection();
            notaAntes.setText(String.valueOf(primeraRevisionActual.getNotasAntesPrimeraRev()));
            notaDespues.setText(String.valueOf(primeraRevisionActual.getNotaDespuesPrimeraRev()));
            observaciones.setText(primeraRevisionActual.getObservacionesPrimeraRev());
            //titulo appbar
            setTitle(R.string.EditarPrimera);

        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
        }
    }
    private void actualizarPrimeraRevision(){
        try {
            int id= primeraRevisionActual.getIdPrimerRevision();
            //Obtener valor de spinner de Local
            String local = spinlocalFK.getSelectedItem().toString();
            //Obtener valor de spinner de DetalleEvaluación
            String detalleAuxiliar = spindetalleEFK.getSelectedItem().toString();
            String[] detalleAuxiliar2 = detalleAuxiliar.split("-");
            String detalle = detalleAuxiliar2[0].trim();
            //Obtener el valor de datepicker
            StringBuilder f = new StringBuilder(10);
            //Concatenar
            f.append(dpickfechaSoli.getDayOfMonth()).append("/").append(dpickfechaSoli.getMonth()).append("/").append(dpickfechaSoli.getYear());
            //Almacenar
            String fecha = f.toString();
            //Obtener valor de spinner de estado
            String est = spinestado.getSelectedItem().toString();
            //Almacenar notaAntes
            String notaA = notaAntes.getText().toString();
            String notaD = notaDespues.getText().toString();
            String ob = observaciones.getText().toString();

            if(notaA.trim().isEmpty()||notaD.trim().isEmpty()||ob.trim().isEmpty()){
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_LONG).show();
            }
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            PrimeraRevision p = primeraRevisionViewModel.getPrimeraRevision(id);
            //p = new PrimeraRevision(local, Integer.parseInt(detalle), fecha, Boolean.parseBoolean(est), Double.parseDouble(notaA), Double.parseDouble(notaD), ob);
            p.setIdLocalFK(local);
            p.setIdDetalleEvFK(Integer.parseInt(detalle));
            p.setFechaSolicitudPrimRev(fecha);
            p.setEstadoPrimeraRev(Boolean.parseBoolean(est));
            p.setNotasAntesPrimeraRev(Double.parseDouble(notaA));
            p.setNotaDespuesPrimeraRev(Double.parseDouble(notaD));
            p.setObservacionesPrimeraRev(ob);
            //Actualizar
            primeraRevisionViewModel.updatePrimeraRevision(p);
            Log.d("id", String.valueOf(p.getIdPrimerRevision()));
            Log.d("idLocal", p.getIdLocalFK());
            Log.d("idD", String.valueOf(p.getIdDetalleEvFK()));
            Log.d("fecha", p.getFechaSolicitudPrimRev());
            Log.d("Estado", String.valueOf(p.isEstadoPrimeraRev()));
            Log.d("a", String.valueOf(p.getNotasAntesPrimeraRev()));
            Log.d("d", String.valueOf(p.getNotaDespuesPrimeraRev()));
            Log.d("o", p.getObservacionesPrimeraRev());
            String idp = p.getIdLocalFK();
            Toast.makeText(EditarPrimeraRevisionActivity.this, "Primera revisión: "+ idp + "actualizada con éxito", Toast.LENGTH_LONG);
            finish();
        }catch (Exception e){
            Toast.makeText(EditarPrimeraRevisionActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_LONG).show();
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
        switch (item.getItemId()){
            case R.id.guardar:
                actualizarPrimeraRevision();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
