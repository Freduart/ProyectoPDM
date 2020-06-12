package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;

public class EditarAsignaturaActivity extends AppCompatActivity {

    private Asignatura asignaturaActual;
    private AreaAdm areaAdmActual;
    private AsignaturaViewModel asignaturaViewModel;
    private Spinner spinnerIdAreaAdm;
    private EditText editNomAsignatura;
    private AreaAdmViewModel areaAdmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_asignatura);
            spinnerIdAreaAdm = (Spinner) findViewById(R.id.spinIdAreaAdm);
            editNomAsignatura = (EditText) findViewById(R.id.etNomAsignatura);

            //Instancias de ViewModel
            asignaturaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);
            areaAdmViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);
            //Obtiene el intent de AsignautaActivity
            Bundle extra = getIntent().getExtras();
            String codAS = " ";
            if (extra != null) {
                codAS = extra.getString(AsignaturaActivity.IDENTIFICADOR_AS);
            }
            //Obtiene asignatura actual por medio de IDENTIFICADOR_AS
            asignaturaActual = asignaturaViewModel.obtenerAsignatura(codAS);
            areaAdmActual=areaAdmViewModel.getAreaAdm(asignaturaActual.getIdDepartamentoFK());
            editNomAsignatura.setText(asignaturaActual.getNomasignatura());
            //Llenar spinner de departamento escuela
            //Almacena id y nombre de departamento
            final ArrayList<String> areasNom = new ArrayList<>();
            //Adaptador a arreglos para spinner
            final ArrayAdapter<String> adapterSpinnerEscuela = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areasNom);
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
                    try {
                        Asignatura as = asignaturaViewModel.obtenerAsignatura(asignaturaActual.getCodigoAsignatura());
                        //añade los elementos del livedata a las listas para almacenar id y nombre de escuela
                        for (AreaAdm areaAdm : areasAdms) {
                            areasNom.add(areaAdm.getIdDeptarmento() + " - " + areaAdm.getNomDepartamento());
                            if(areaAdm.getIdDeptarmento()==(as.getIdDepartamentoFK()))
                                spinnerIdAreaAdm.setSelection(areasNom.indexOf(areaAdm.getIdDeptarmento() + " - " + areaAdm.getNomDepartamento()));
                        }
                        //Refresca
                        adapterSpinnerEscuela.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(EditarAsignaturaActivity.this, e.getMessage() + " - " +e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.getCause(),Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarAsignatura(){
        try {
            String id = asignaturaActual.getCodigoAsignatura();
            //Obtener el valor de spinner de areaAdm
            String area1 = spinnerIdAreaAdm.getSelectedItem().toString();
            String[] area2= area1.split("-");
            //Almacenar id area
            String area = area2[0].trim();

            //Almacenar nombre de asignatura
            String nombre = editNomAsignatura.getText().toString();

            if(nombre.trim().isEmpty()){
                Toast.makeText(this, "Por favor, ingresa el nombre de asignatura.",
                        Toast.LENGTH_SHORT).show();
            }
            asignaturaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);
            Asignatura a = asignaturaViewModel.obtenerAsignatura(id);
            a.setNomasignatura(nombre);
            a.setIdDepartamentoFK(Integer.parseInt(area));
            asignaturaViewModel.update(a);
            Toast.makeText(EditarAsignaturaActivity.this, "Asignatura "+a.getCodigoAsignatura() + " actualizada con éxito.", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Toast.makeText(EditarAsignaturaActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
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
                actualizarAsignatura();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
