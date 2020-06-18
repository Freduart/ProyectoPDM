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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;

public class EditarDocenteActivity extends AppCompatActivity {

    TextInputLayout textCarnetDocente,textNomDocente,textApellidoDocente,textCorreoDocente,textTelefonoDocente;
    Spinner spinnerCargo;
    ArrayList<String> cargo;
    ArrayAdapter<String> cargoAdapter;
    CargoViewModel cargoViewModel;
    DocenteViewModel docenteViewModel;
    Docente docenteActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_docente);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("EDITAR DOCENTE");

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

        Bundle bundle=getIntent().getExtras();
        String carnetDocente="";
        if(bundle!=null){
            carnetDocente=bundle.getString(DocenteActivity.CARNET_DOCENTE);
        }

        docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
        try {
            docenteActual=docenteViewModel.getDocente(carnetDocente);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        textCarnetDocente.getEditText().setText(docenteActual.getCarnetDocente());
        textCarnetDocente.setEnabled(false);
        textNomDocente.getEditText().setText(docenteActual.getNomDocente());
        textApellidoDocente.getEditText().setText(docenteActual.getApellidoDocente());
        textCorreoDocente.getEditText().setText(docenteActual.getCorreoDocente());
        textTelefonoDocente.getEditText().setText(docenteActual.getTelefonoDocente());

        cargoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CargoViewModel.class);
        cargoViewModel.getAllCargos().observe(this, new Observer<List<Cargo>>() {
            @Override
            public void onChanged(List<Cargo> cargos) {
                try {
                    Cargo cg=cargoViewModel.getCargo(docenteActual.getIdCargoFK());
                    for (Cargo c:cargos){
                        cargo.add(c.getIdCargo()+"-"+c.getNomCargo());
                        if(c.getIdCargo()==(cg.getIdCargo())){
                            spinnerCargo.setSelection(cargo.indexOf(c.getIdCargo()+"-"+c.getNomCargo()));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
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
        if(textNomDocente.getEditText().getText().toString().trim().isEmpty()){
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

                Docente docente=new Docente(carnetDocente,idCargo,2,nomDocente,apellidoDocente,correoDocente,telefonoDocente);
                docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
                docenteViewModel.updateDocente(docente);
                Toast.makeText(EditarDocenteActivity.this, "Guardado Exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }catch (Exception e){
                Toast.makeText(EditarDocenteActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
