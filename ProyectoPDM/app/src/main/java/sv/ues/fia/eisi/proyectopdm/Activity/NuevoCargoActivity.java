package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class NuevoCargoActivity extends AppCompatActivity {

    private EditText nomCargo;
    private Spinner spinnerIdEscuela;
    private EscuelaViewModel escuelaViewModel;
    private CargoViewModel cargoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nuevo_cargo);

            nomCargo = (EditText) findViewById(R.id.etNomCargo);
            spinnerIdEscuela = (Spinner) findViewById(R.id.spinCodEscuelaFK);

            //Llenar spinner de escuela
            //Almacena id y nombre de escuela
            final ArrayList<String> escuelasNom = new ArrayList<>();
            //Adaptador a arreglos para spinner
            final ArrayAdapter<String> adapterSpinnerEscuela = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, escuelasNom);
            //Settea layout de dropdown del spinner
            adapterSpinnerEscuela.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            //Settea el adaptador creado en el spinner
            spinnerIdEscuela.setAdapter(adapterSpinnerEscuela);
            //instancia Escuela View Model
            escuelaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(EscuelaViewModel.class);
            //Obtiene todas las escuelas en livedata
            escuelaViewModel.getAllEscuelas().observe(this, new Observer<List<Escuela>>() {
                @Override
                public void onChanged(List<Escuela> escuelas) {
                    //añade los elementos del livedata a las listas para almacenar id y nombre de escuelas
                    for (Escuela e : escuelas) {
                        escuelasNom.add(e.getIdEscuela() + " - " + e.getNomEscuela());
                    }
                    //Refresca
                    adapterSpinnerEscuela.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Toast.makeText(NuevoCargoActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarCargo(){
        try {

            //Obtener valor de spinner Escuela
            String escuelaAuxiliar1 = spinnerIdEscuela.getSelectedItem().toString();
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
