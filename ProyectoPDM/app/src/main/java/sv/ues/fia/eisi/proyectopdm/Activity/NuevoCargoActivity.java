package sv.ues.fia.eisi.proyectopdm.Activity;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import java.util.Locale;

=======
>>>>>>> parent of c53d5a2... Revert "SincronizacioService"
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class NuevoCargoActivity extends AppCompatActivity {

    private EditText nomCargo;
    private Spinner spinnerIdAreaAdm;
    private EscuelaViewModel escuelaViewModel;
    private AreaAdmViewModel areaAdmViewModel;
    private CargoViewModel cargoViewModel;

    //Para reconocimiento de voz a texto
    private ImageButton ibMic;
    public static final int REC_CODE_INPUT=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nuevo_cargo);

            nomCargo = (EditText) findViewById(R.id.etNomCargo);
            spinnerIdAreaAdm = (Spinner) findViewById(R.id.spinCodEscuelaFK);

            ibMic = (ImageButton) findViewById(R.id.ibMicCargo);

            ibMic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iniciarEntradaDeVoz();
                }
            });

            areaAdmViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);

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
            spinnerIdAreaAdm.setAdapter(adapterSpinnerAreaAdm);
            //instancia Escuela View Model
            escuelaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(EscuelaViewModel.class);
            //Obtiene todas las areas administrativas en livedata
            areaAdmViewModel.getAreaAdmAll().observe(this, new Observer<List<AreaAdm>>() {
                @Override
                public void onChanged(List<AreaAdm> areaAdms) {
                    //añade los elementos del livedata a las listas para almacenar id y nombre de escuelas
                    for (AreaAdm e : areaAdms) {
                        areaAdmNom.add(e.getIdDeptarmento() + " - " + e.getNomDepartamento());
                    }
                    //Refresca
                    adapterSpinnerAreaAdm.notifyDataSetChanged();
                }
            });
            escuelaViewModel.getAllEscuelas().observe(this, new Observer<List<Escuela>>() {
                @Override
                public void onChanged(List<Escuela> escuelas) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(NuevoCargoActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
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

            Toast.makeText(NuevoCargoActivity.this, e.getMessage()+" - "+e.getCause(), Toast.LENGTH_LONG).show();
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

    private void guardarCargo(){
        try {

            //Obtener valor de spinner Escuela
            String escuelaAuxiliar1 = spinnerIdAreaAdm.getSelectedItem().toString();
            String [] escuelaAuxiliar2 = escuelaAuxiliar1.split("-");
            //almacenar id de Escuela
            String escuela = escuelaAuxiliar2[0].trim();

            //Almacenar nombre de cargo
            String nombre = nomCargo.getText().toString();

            if(nombre.trim().isEmpty()){
                Toast.makeText(this, "Por favor, escribe el nombre del cargo.",
                        Toast.LENGTH_SHORT).show();
            }

            Cargo auxcargo = new Cargo(Integer.parseInt(escuela), nombre);

            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(CargoViewModel.class);
            //insertar
            cargoViewModel.insert(auxcargo);

            Toast.makeText(NuevoCargoActivity.this, "Cargo " + auxcargo.getNomCargo() +
                    " insertado con éxito", Toast.LENGTH_SHORT).show();

            finish();

        }catch (Exception e){
            Toast.makeText(NuevoCargoActivity.this, e.getMessage() + " " +
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
                guardarCargo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
