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
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class NuevaAsignaturaActivity extends AppCompatActivity {

    private AsignaturaViewModel asignaturaViewModel;
    private EditText editCodAsignatura;
    private Spinner spinnerIdAreaAdm;
    private EditText editNomAsignatura;
    private AreaAdmViewModel areaAdmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_asignatura);
            editCodAsignatura = (EditText) findViewById(R.id.etCodAsignatura);
            spinnerIdAreaAdm = (Spinner) findViewById(R.id.spinIdAreaAdm);
            editNomAsignatura = (EditText) findViewById(R.id.etNomAsignatura);

            //Llenar spinner de departamento escuela
            //Almacena id y nombre de escuela
            final ArrayList<String> escuelasNom = new ArrayList<>();
            //Adaptador a arreglos para spinner
            final ArrayAdapter<String> adapterSpinnerEscuela = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, escuelasNom);
            //Settea layout de dropdown del spinner
            adapterSpinnerEscuela.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Settea el adaptador creado en el spinner
            spinnerIdAreaAdm.setAdapter(adapterSpinnerEscuela);
            //instancia escuela view model
            areaAdmViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);
            //Obtiene todas las esceulas en livedata
            areaAdmViewModel.getAreaAdmAll().observe(this, new Observer<List<AreaAdm>>() {
                @Override
                public void onChanged(List<AreaAdm> areasAdms) {
                    //añade los elementos del livedata a las listas para almacenar id y nombre de escuela
                    for (AreaAdm areaAdm : areasAdms) {
                        escuelasNom.add(areaAdm.getIdDeptarmento() + " - " + areaAdm.getNomDepartamento());
                    }
                    //Refresca
                    adapterSpinnerEscuela.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            Toast.makeText(NuevaAsignaturaActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarAsignatura() {
        try {
            //Almacenar código de asignatura
            String codigo = editCodAsignatura.getText().toString();
            //Obtener valor de spinner Escuela
            String escuelaAuxiliar1 = spinnerIdAreaAdm.getSelectedItem().toString();
            String [] escuelaAuxiliar2 = escuelaAuxiliar1.split("-");
            //almacenar id de Escuela
            String escuela = escuelaAuxiliar2[0].trim();

            //Almacenar nombre de asignatura
            String nombre = editNomAsignatura.getText().toString();

            if(codigo.trim().isEmpty() || nombre.trim().isEmpty()){
                Toast.makeText(this, "Por favor, llena todos los campos.", Toast.LENGTH_SHORT).show();
            }

            Asignatura auxasignatura = new Asignatura(codigo, Integer.parseInt(escuela), nombre);

            asignaturaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);
            //insrtar
            asignaturaViewModel.insert(auxasignatura);

            Toast.makeText(NuevaAsignaturaActivity.this, "Asignatura " + auxasignatura.getCodigoAsignatura() + " insertada con éxito.", Toast.LENGTH_SHORT).show();

            finish();

        }catch (Exception e){
            Toast.makeText(NuevaAsignaturaActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nueva_asignatura_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar_asignatura:
                guardarAsignatura();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
