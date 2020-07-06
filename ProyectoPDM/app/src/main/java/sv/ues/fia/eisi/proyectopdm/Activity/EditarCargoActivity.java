package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;

public class EditarCargoActivity extends AppCompatActivity {

    private Cargo cargoActual;
    private AreaAdm areaAdmActual;
    private CargoViewModel cargoViewModel;
    private AreaAdmViewModel areaAdmViewModel;
    private TextView idCargo;
    private Spinner idAreaAdmCargo;
    private EditText nomCargo;
    //Para reconocimiento de voz a texto
    private ImageButton ibMic;
    public static final int REC_CODE_INPUT=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_cargo);

            idCargo = (TextView) findViewById(R.id.textViewIdCargoE);
            idAreaAdmCargo = (Spinner) findViewById(R.id.spEscuelaCargoE);
            nomCargo = (EditText) findViewById(R.id.textViewNomCargoE);

            ibMic = (ImageButton) findViewById(R.id.ibMicCargo);

            ibMic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iniciarEntradaDeVoz();
                }
            });

            //Instancias viewmodels
            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CargoViewModel.class);
            areaAdmViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);
            //obtiene intent de CargoActivity
            Bundle extras = getIntent().getExtras();
            int identCargo = 0;
            if (extras != null) {
                identCargo = extras.getInt(CargoActivity.IDENTIFICADOR_CARGO);
            }
            //Obtiene cargo actual por medio de EXTRA_ID_CARGO
            cargoActual =cargoViewModel.getCargo(identCargo);
            //Obtener idEscuelaCargo actual
            areaAdmActual = areaAdmViewModel.getAreaAdm(cargoActual.getIdAreaAdminFK());

            idCargo.setText(String.valueOf(cargoActual.getIdCargo()));

            //Llenar spinner de escuela
            //Almacena id y nombre de escuela
            final ArrayList<String> areaAdmNom = new ArrayList<>();
            //Adaptador a arreglos para spinner
            final ArrayAdapter<String> adapterSpinnerAreaAdm = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, areaAdmNom);
            //Settea layout de dropdown del spinner
            adapterSpinnerAreaAdm.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            //Settea el adaptador creado en el spinner
            idAreaAdmCargo.setAdapter(adapterSpinnerAreaAdm);

            //Obtiene todas las escuelas en livedata
            areaAdmViewModel.getAreaAdmAll().observe(this, new Observer<List<AreaAdm>>() {
                @Override
                public void onChanged(List<AreaAdm> areaAdms) {
                    try {
                        Cargo c = cargoViewModel.getCargo(cargoActual.getIdCargo());
                        //añade los elementos del livedata a las listas para almacenar id y nombre de escuelas
                        for (AreaAdm e : areaAdms) {
                            areaAdmNom.add(e.getIdDeptarmento() + " - " + e.getNomDepartamento());
                            if(e.getIdDeptarmento()==(c.getIdAreaAdminFK()))
                                idAreaAdmCargo.setSelection(areaAdmNom.indexOf(e.getIdDeptarmento() + " - " + e.getNomDepartamento()));
                        }
                        //Refresca
                        adapterSpinnerAreaAdm.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(EditarCargoActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            nomCargo.setText(cargoActual.getNomCargo());

            setTitle(R.string.EditarCargo);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.getCause(),Toast.LENGTH_LONG).show();
        }
    }


    public void iniciarEntradaDeVoz(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora.");
        try{
            startActivityForResult(intent, REC_CODE_INPUT);
        }catch (ActivityNotFoundException e){

            Toast.makeText(EditarCargoActivity.this, e.getMessage()+" - "+e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REC_CODE_INPUT:{
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    nomCargo.setText(result.get(0));
                }
                break;
            }
        }
    }

    private void actualizarCargo(){
        try {

            int id = cargoActual.getIdCargo();

            //Obtener valor de spinner Escuela
            String escuelaAuxiliar1 = idAreaAdmCargo.getSelectedItem().toString();
            String [] escuelaAuxiliar2 = escuelaAuxiliar1.split("-");
            //almacenar id de Escuela
            String areaAdm = escuelaAuxiliar2[0].trim();

            //Almacenar nombre de cargo
            String nombre = nomCargo.getText().toString();

            if(nombre.trim().isEmpty()){
                Toast.makeText(this, R.string.error_form_incompleto_eval,
                        Toast.LENGTH_SHORT).show();
            }

            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(CargoViewModel.class);
            Cargo aux2 = cargoViewModel.getCargo(id);
            aux2.setNomCargo(nombre);
            aux2.setIdAreaAdminFK(Integer.parseInt(areaAdm));
            //actualizar
            cargoViewModel.update(aux2);


            Toast.makeText(EditarCargoActivity.this, R.string.cargoactualizado, Toast.LENGTH_LONG).show();

            finish();

        }catch (Exception e){
            Toast.makeText(EditarCargoActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
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
                actualizarCargo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
