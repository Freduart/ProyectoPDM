package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
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
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class NuevaPrimeraRevisionActivity extends AppCompatActivity {

    private PrimeraRevisionViewModel primeraRevisionViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
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

            NOTA_PH_PR = getText(R.string.nota_place_holder_PR).toString();
            ESTADO_PH_PR = getText(R.string.estado_placeholder_PR).toString();
            LOCAL_PH_PR = getText(R.string.local_placeholder_PR).toString();
            spindetalleEFK = (Spinner) findViewById(R.id.editarDetalleEvaFK);
            dpickfechaSoli = (DatePicker) findViewById(R.id.editarFechaSolicitud);
            notaAntes = (EditText) findViewById(R.id.editarNotaAntesPR);
            observaciones = (EditText) findViewById(R.id.editarObservacionesPR);



            //Spinner de detalle
            final ArrayList<String> detallesNom = new ArrayList<>();
            final ArrayAdapter<String> adapterSpinnerDetalleE = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, detallesNom);
            spindetalleEFK.setAdapter(adapterSpinnerDetalleE);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            detalleEvaluacionViewModel.getAllDetalles().observe(this, new Observer<List<DetalleEvaluacion>>() {
                @Override
                public void onChanged(List<DetalleEvaluacion> detalleEvaluaciones) {
                    try {
                       for (DetalleEvaluacion d : detalleEvaluaciones){
                           detallesNom.add(d.getIdEvaluacionFK()+ " - " + d.getCarnetAlumnoFK());
                       }
                       adapterSpinnerDetalleE.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });//fin de llenado spinner detalle

        }catch (Exception e){
            Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }


    public void guardarPrimeraRevision(){
        try{
            //Obtener valor de spinner de Detalle de evaluación
            String detalleAux1 = spindetalleEFK.getSelectedItem().toString();
            String[] detalleAux2= detalleAux1.split("-");
            String detalle = detalleAux2[0].trim();
            //Obtener el valor del datepicker
            StringBuilder fecha = new StringBuilder(10);
            //concatenar
            fecha.append(dpickfechaSoli.getDayOfMonth()).append("/").append(dpickfechaSoli.getMonth()).append("/").append(dpickfechaSoli.getYear());
            //Almacenar fecha de solicitud
            String fechaSolicitud = fecha.toString();
            String notaAn = notaAntes.getText().toString();
            String ob = observaciones.getText().toString();
            if(notaAn.trim().isEmpty()||ob.isEmpty()){
                Toast.makeText(this, "Por favor, llena todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            PrimeraRevision pr = new PrimeraRevision(LOCAL_PH_PR, Integer.parseInt(detalle), fechaSolicitud, Boolean.parseBoolean(ESTADO_PH_PR), Double.parseDouble(notaAn), Double.parseDouble(NOTA_PH_PR), ob);
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            primeraRevisionViewModel.insertPrimeraRevision(pr);

            Toast.makeText(NuevaPrimeraRevisionActivity.this, "Primera revisión insertada con éxito.", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
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
        switch (item.getItemId()){
            case R.id.guardar:
                guardarPrimeraRevision();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
