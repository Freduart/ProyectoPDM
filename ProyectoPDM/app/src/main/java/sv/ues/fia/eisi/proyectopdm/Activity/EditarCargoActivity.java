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
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.repository.CargoRepository;

public class EditarCargoActivity extends AppCompatActivity {

    private Cargo cargoActual;
    private AreaAdm areaAdmActual;
    private CargoViewModel cargoViewModel;
    private AreaAdmViewModel areaAdmViewModel;
    private TextView idCargo;
    private Spinner idAreaAdmCargo;
    private EditText nomCargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_cargo);

            idCargo = (TextView) findViewById(R.id.textViewIdCargoE);
            idAreaAdmCargo = (Spinner) findViewById(R.id.spEscuelaCargoE);
            nomCargo = (EditText) findViewById(R.id.textViewNomCargoE);

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
