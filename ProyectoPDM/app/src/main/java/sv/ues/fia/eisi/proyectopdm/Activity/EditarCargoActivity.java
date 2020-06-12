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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.repository.CargoRepository;

public class EditarCargoActivity extends AppCompatActivity {

    private Cargo cargoActual;
    private Escuela escuelaActual;
    private CargoViewModel cargoViewModel;
    private EscuelaViewModel escuelaViewModel;
    private TextView idCargo;
    private Spinner idEscuelaCargo;
    private EditText nomCargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_cargo);

            idCargo = (TextView) findViewById(R.id.textViewIdCargoE);
            idEscuelaCargo = (Spinner) findViewById(R.id.spEscuelaCargoE);
            nomCargo = (EditText) findViewById(R.id.textViewNomCargoE);

            //Instancias viewmodels
            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CargoViewModel.class);
            escuelaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);
            //obtiene intent de CargoActivity
            Bundle extras = getIntent().getExtras();
            int identCargo = 0;
            if (extras != null) {
                identCargo = extras.getInt(CargoActivity.IDENTIFICADOR_CARGO);
            }
            //Obtiene cargo actual por medio de EXTRA_ID_CARGO
            cargoActual =cargoViewModel.getCargo(identCargo);
            //Obtener idEscuelaCargo actual
            escuelaActual = escuelaViewModel.getEscuela(cargoActual.getIdEscuelaFK());

            idCargo.setText(String.valueOf(cargoActual.getIdCargo()));

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
            idEscuelaCargo.setAdapter(adapterSpinnerEscuela);
            //instancia Escuela View Model
            escuelaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(EscuelaViewModel.class);
            //Obtiene todas las escuelas en livedata
            escuelaViewModel.getAllEscuelas().observe(this, new Observer<List<Escuela>>() {
                @Override
                public void onChanged(List<Escuela> escuelas) {
                    try {
                        Cargo c = cargoViewModel.getCargo(cargoActual.getIdCargo());
                        //añade los elementos del livedata a las listas para almacenar id y nombre de escuelas
                        for (Escuela e : escuelas) {
                            escuelasNom.add(e.getIdEscuela() + " - " + e.getNomEscuela());
                            if(e.getIdEscuela()==(c.getIdEscuelaFK()))
                                idEscuelaCargo.setSelection(escuelasNom.indexOf(e.getIdEscuela() + " - " + e.getNomEscuela()));
                        }
                        //Refresca
                        adapterSpinnerEscuela.notifyDataSetChanged();
                    }catch (Exception e){

                    }
                }
            });

            nomCargo.setText(cargoActual.getNomCargo());

            setTitle("Editar cargo");
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.getCause(),Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarCargo(){
        try {

            int id = cargoActual.getIdCargo();

            //Obtener valor de spinner Escuela
            String escuelaAuxiliar1 = idEscuelaCargo.getSelectedItem().toString();
            String [] escuelaAuxiliar2 = escuelaAuxiliar1.split("-");
            //almacenar id de Escuela
            String escuela = escuelaAuxiliar2[0].trim();

            //Almacenar nombre de cargo
            String nombre = nomCargo.getText().toString();

            if(nombre.trim().isEmpty()){
                Toast.makeText(this, "Por favor, llena todos los campos.",
                        Toast.LENGTH_SHORT).show();
            }


            //Cargo auxcargo = new Cargo(Integer.parseInt(escuela), nombre);

            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(CargoViewModel.class);
            Cargo aux2 = cargoViewModel.getCargo(id);
            aux2.setNomCargo(nombre);
            aux2.setIdEscuelaFK(Integer.parseInt(escuela));
            //actualizar
            cargoViewModel.update(aux2);


            Toast.makeText(EditarCargoActivity.this, "Cargo " + aux2.getIdCargo()+
                    " actualizado con éxito", Toast.LENGTH_LONG).show();

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
