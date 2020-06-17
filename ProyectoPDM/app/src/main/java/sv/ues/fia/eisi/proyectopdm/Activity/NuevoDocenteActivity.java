package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;

public class NuevoDocenteActivity extends AppCompatActivity {

    TextInputLayout textCarnetDocente,textNomDocente,textApellidoDocente,textCorreoDocente,textTelefonoDocente;
    Spinner spinnerCargo;
    ArrayList<String> cargo;
    ArrayAdapter<String> cargoAdapter;
    CargoViewModel cargoViewModel;
    DocenteViewModel docenteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_docente);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("NUEVO DOCENTE");

        textCarnetDocente=(TextInputLayout)findViewById(R.id.textCarnetDocenteVer);
        textNomDocente=(TextInputLayout)findViewById(R.id.textNomDocenteVer);
        textApellidoDocente=(TextInputLayout)findViewById(R.id.textApellidoDocenteVer);
        textCorreoDocente=(TextInputLayout)findViewById(R.id.textNomEncVer);
        textTelefonoDocente=(TextInputLayout)findViewById(R.id.textTelefonoDocenteVer);
        spinnerCargo=(Spinner)findViewById(R.id.textCargosVer);

        cargo=new ArrayList<>();
        cargoAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,cargo);
        cargoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCargo.setAdapter(cargoAdapter);

        cargoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CargoViewModel.class);
        cargoViewModel.getAllCargos().observe(this, new Observer<List<Cargo>>() {
            @Override
            public void onChanged(List<Cargo> cargos) {
                for (Cargo c:cargos){
                    cargo.add(c.getIdCargo()+"-"+c.getNomCargo());
                }
                cargoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.agregar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar:
                guardarDocente();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void guardarDocente(){
        final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";
        final String regexTelefono = "^[+]?[0-9]{10,13}$";
        if(textCarnetDocente.getEditText().getText().toString().trim().isEmpty()){
            textCarnetDocente.setError("Ingrese el Carnet del Docente.");
        }else if(textNomDocente.getEditText().getText().toString().trim().isEmpty()){
            textNomDocente.setError("Ingrese el Nombre del Docente.");
        }else if(textApellidoDocente.getEditText().getText().toString().trim().isEmpty()){
            textApellidoDocente.setError("Ingrese el Apellido del Docente.");
        }else if(textCorreoDocente.getEditText().getText().toString().trim().isEmpty()){
            textCorreoDocente.setError("Ingrese el Correo Electrónico del Docente.");
        }else if(!textCorreoDocente.getEditText().getText().toString().matches(regex)){
            textCorreoDocente.setError("Ingrese un Correo Válido.");
        }else if(textTelefonoDocente.getEditText().getText().toString().trim().isEmpty()){
            textTelefonoDocente.setError("Ingrese el Teléfono del Docente.");
        }else if(!textTelefonoDocente.getEditText().getText().toString().matches(regexTelefono)){
            textTelefonoDocente.setError("Ingrese un Numero Válido.");
        }else{
            try {
                String carnetDocente=textCarnetDocente.getEditText().getText().toString();
                String nomDocente=textNomDocente.getEditText().getText().toString();
                String apellidoDocente=textApellidoDocente.getEditText().getText().toString();
                String cargoDocente=spinnerCargo.getSelectedItem().toString();
                String[] splitCargo=cargoDocente.split("-");
                int idCargo=Integer.parseInt(splitCargo[0]);
                String correoDocente=textCorreoDocente.getEditText().getText().toString();
                String telefonoDocente=textTelefonoDocente.getEditText().getText().toString();

                //Constructor para crear docentes y nalces con los viewModel
                Docente docente=new Docente(carnetDocente,idCargo, 2, nomDocente,apellidoDocente,correoDocente,telefonoDocente);
                docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
                docenteViewModel.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
                    @Override
                    public void onChanged(List<Docente> docentes) {
                        try {
                            Docente docenteAInsertar = docenteViewModel.getDocente(docente.getCarnetDocente());
                            if(docenteAInsertar!=null){
                                Toast.makeText(NuevoDocenteActivity.this, "Error, registro duplicado.", Toast.LENGTH_SHORT).show();
                            }else {
                                docenteViewModel.insertDocente(docente);
                                Toast.makeText(NuevoDocenteActivity.this, "Guardado Exitosamente", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }catch (Exception e){
                            Toast.makeText(NuevoDocenteActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }catch (Exception e){
                Toast.makeText(NuevoDocenteActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
