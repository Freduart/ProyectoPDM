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

            spinlocalFK = (Spinner) findViewById(R.id.editarLocalFK);
            spindetalleEFK = (Spinner) findViewById(R.id.editarDetalleEvaFK);
            dpickfechaSoli = (DatePicker) findViewById(R.id.editarFechaSolicitud);
            spinestado = (Spinner) findViewById(R.id.editarEstadoPR);
            notaAntes = (EditText) findViewById(R.id.editarNotaAntesPR);
            notaDespues = (EditText) findViewById(R.id.editarNotaDespuesPR);
            observaciones = (EditText) findViewById(R.id.editarObservacionesPR);

            //Instancias de ViewModel
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            localViewModel = new  ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);

            //Obtiene intent de PrimeraRevisionActivity
            Bundle extras = getIntent().getExtras();
            String idPR = " ";
            if(extras != null){
                idPR = extras.getString(PrimeraRevisionActivity.IDENTIFICADOR_PR);
            }
            //Obtiene pr actual
            primeraRevisionActual = primeraRevisionViewModel.getPrimeraRevision(idPR);
            //Obtiene local actual
            localActual = localViewModel.getLoc(primeraRevisionActual.getIdLocalFK());
            //Obtiene detalle actual
            detalleEvaluacionActual = detalleEvaluacionViewModel.getDetalleEvaluacion(primeraRevisionActual.getIdDetalleEvFK());

            //Llenar Spinnerd
            //Spinner Local
            //lista para almacenar id y ubicacion de local
            final ArrayList<String> localesNom = new ArrayList<>();
            //adaptador a arreglos para spinner
            final ArrayAdapter<String> adapterSpinnerLocal = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localesNom);
            //settea layout de dropdown del spinner
            adapterSpinnerLocal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //settea el adaptador creado en el spinner
            spinlocalFK.setAdapter(adapterSpinnerLocal);
            //Instancia ViewModel
            localViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
            //Obtener todas las pr en LiveData
            localViewModel.getAllLocales().observe(this, new Observer<List<Local>>() {
                @Override
                public void onChanged(List<Local> locales) {
                    try {
                        PrimeraRevision pr = primeraRevisionViewModel.getPrimeraRevision(primeraRevisionActual.getIdPrimerRevision());
                        //añade los elementos del live data a las listas para almacenar id y nombre de local
                        for(Local l : locales){
                            localesNom.add(l.getIdLocal());
                            if(l.getIdLocal().equals(pr.getIdLocalFK()))
                                spinlocalFK.setSelection(localesNom.indexOf(l.getIdLocal()));
                        }
                        //Refresca
                        adapterSpinnerLocal.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(EditarPrimeraRevisionActivity.this, e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }); //fin de llenado de spinner de local

            //Spinner de detalle
            final ArrayList<String> detallesNom = new ArrayList<>();
            final ArrayAdapter<String> adapterSpinnerDetalleE = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, detallesNom);
            adapterSpinnerLocal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spindetalleEFK.setAdapter(adapterSpinnerDetalleE);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            detalleEvaluacionViewModel.getAllDetalles().observe(this, new Observer<List<DetalleEvaluacion>>() {
                @Override
                public void onChanged(List<DetalleEvaluacion> detalleEvaluaciones) {
                    try {
                        PrimeraRevision pr = primeraRevisionViewModel.getPrimeraRevision(primeraRevisionActual.getIdPrimerRevision());
                        for (DetalleEvaluacion d : detalleEvaluaciones){
                            detallesNom.add(d.getIdDetalleEv()+" - "+ d.getCarnetAlumnoFK());
                            if(d.getIdDetalleEv()==pr.getIdDetalleEvFK())
                                spindetalleEFK.setSelection(detalleEvaluaciones.indexOf(d.getIdDetalleEv()+ " - "+ d.getCarnetAlumnoFK()));
                        }
                        adapterSpinnerDetalleE.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(EditarPrimeraRevisionActivity.this, e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });//fin de llenado spinner detalle

            //datepicker

            String[] fechaSolAux = primeraRevisionActual.getFechaSolicitudPrimRev().split("/");
            dpickfechaSoli.init(Integer.parseInt(fechaSolAux[2]), Integer.parseInt(fechaSolAux[1]), Integer.parseInt(fechaSolAux[0]), null);
            //Llenar spinner de estado
            final Boolean[] arrayEstado = new Boolean[]{true, false};
            final ArrayList<PrimeraRevision> estadoPR = new ArrayList<>();
            spinestado.setAdapter(new ArrayAdapter<Boolean>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayEstado));

            notaAntes.setText(String.valueOf(primeraRevisionActual.getNotasAntesPrimeraRev()));
            notaDespues.setText(String.valueOf(primeraRevisionActual.getNotaDespuesPrimeraRev()));
            observaciones.setText(primeraRevisionActual.getObservacionesPrimeraRev());

            //titulo appbar
            setTitle(R.string.AppBarNamePrimerRevision);

        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
        }
    }
    private void actualizarPrimeraRevision(){
        try {

            String id = primeraRevisionActual.getIdPrimerRevision();

            //Almacenar valor de spinner local
            String locAux = spinlocalFK.getSelectedItem().toString();
            //Obtener valor de spinner Detalle
            String detalleAuxiliar1=spindetalleEFK.getSelectedItem().toString();
            String[] detalleAuxiliar2=detalleAuxiliar1.split("-");
            //Almacenar id de detalle
            String detalle = detalleAuxiliar2[0].trim();
            //Obtener valor de spinner estado
            String est = spinestado.getSelectedItem().toString();
            //Obtener valor de datepicker
            StringBuilder fecha = new StringBuilder(10);
            //Concatenar
            fecha.append(dpickfechaSoli.getDayOfMonth()).append("/").append(dpickfechaSoli.getMonth()).append("/").append(dpickfechaSoli.getYear());
            //Almacenar fecha
            String fechaSolic = fecha.toString();
            //Almacenar notaAntes y notaDespués
            String notaA = String.valueOf(notaAntes.getText());
            String notaD = String.valueOf(notaDespues.getText());
            String observ = observaciones.getText().toString();

            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);

            PrimeraRevision p = primeraRevisionViewModel.getPrimeraRevision(id);
            p.setIdPrimerRevision(primeraRevisionViewModel.getPrimeraRevision(id).toString());
            p.setIdLocalFK(locAux);
            p.setIdDetalleEvFK(Integer.parseInt(detalle));
            p.setFechaSolicitudPrimRev(fechaSolic);
            p.setEstadoPrimeraRev(Boolean.parseBoolean(est));
            p.setNotasAntesPrimeraRev(Double.parseDouble(notaA));
            p.setNotaDespuesPrimeraRev(Double.parseDouble(notaD));
            p.setObservacionesPrimeraRev(observ);

            primeraRevisionViewModel.updatePrimeraRevision(p);
            Toast.makeText(EditarPrimeraRevisionActivity.this, "Primera revisión "+ p.getIdPrimerRevision() + " actualizada con éxito", Toast.LENGTH_LONG).show();
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
