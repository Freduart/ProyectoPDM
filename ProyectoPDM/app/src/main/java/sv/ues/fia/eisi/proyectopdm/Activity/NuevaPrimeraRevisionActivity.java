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
    private LocalViewModel localViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
    private EditText codPR;
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
            setContentView(R.layout.activity_nueva_primera_revision);
            codPR = (EditText) findViewById(R.id.editarCodPR);
            spinlocalFK = (Spinner) findViewById(R.id.editarLocalFK);
            spindetalleEFK = (Spinner) findViewById(R.id.editarDetalleEvaFK);
            dpickfechaSoli = (DatePicker) findViewById(R.id.editarFechaSolicitud);
            spinestado = (Spinner) findViewById(R.id.editarEstadoPR);
            notaAntes = (EditText) findViewById(R.id.editarNotaAntesPR);
            notaDespues = (EditText) findViewById(R.id.editarNotaDespuesPR);
            observaciones = (EditText) findViewById(R.id.editarObservacionesPR);


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
                        for (Local l : locales){
                            localesNom.add(l.getIdLocal() + " - " + l.getNombreLocal());
                        }
                        //Refresca
                        adapterSpinnerLocal.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
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
                       for (DetalleEvaluacion d : detalleEvaluaciones){
                           detallesNom.add(d.getIdEvaluacionFK()+ " - " + d.getCarnetAlumnoFK());
                       }
                       adapterSpinnerDetalleE.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });//fin de llenado spinner detalle

            //Llenar spinner de estado
            final String[] arrayEstado = new String[]{"true", "false"};
            final ArrayList<PrimeraRevision> estadoPR = new ArrayList<>();
            spinestado.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayEstado));
            spinestado.setSelection(arrayEstado.toString().indexOf("false"));
        }catch (Exception e){
            Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }


    public void guardarPrimeraRevision(){
        try{
            //Almacenar codigo
            String codigo = codPR.getText().toString();
            //Obtener valor de spinner de Local
            String localAux1= spinlocalFK.getSelectedItem().toString();
            String[] localAux2 = localAux1.split("-");
            String local = localAux2[0].trim();
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
            //Obtener valor de spinner de estado
            String est = spinestado.getSelectedItem().toString();
            String notaAn = notaAntes.getText().toString();
            String notaDes = notaDespues.getText().toString();
            String ob = observaciones.getText().toString();
            if(codigo.trim().isEmpty()||notaAn.trim().isEmpty()||notaDes.isEmpty()||ob.isEmpty()){
                Toast.makeText(this, "Por favor, llena todos los campos.", Toast.LENGTH_LONG).show();
                return;
            }
            PrimeraRevision pr = new PrimeraRevision(codigo, local, Integer.parseInt(detalle), fechaSolicitud, Boolean.parseBoolean(est), Double.parseDouble(notaAn), Double.parseDouble(notaDes), ob);
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            primeraRevisionViewModel.insertPrimeraRevision(pr);

            Toast.makeText(NuevaPrimeraRevisionActivity.this, "Primera revisión: " + pr.getIdPrimerRevision() +" insertada con éxito.", Toast.LENGTH_SHORT).show();
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
