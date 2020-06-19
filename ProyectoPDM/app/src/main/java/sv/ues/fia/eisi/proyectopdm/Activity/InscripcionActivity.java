package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.InscripcionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;

public class InscripcionActivity extends AppCompatActivity {
    //A utilizar
    private TextView carnet;
    private Spinner spn_Asignaturas;

    //ViewModels a utilizar
    private InscripcionViewModel inscripcionViewModel;
    private AlumnoViewModel alumnoViewModel;
    private AsignaturaViewModel asignaturaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inscripcion);

            //Enlaces con el layout
            carnet=(TextView)findViewById(R.id.et_Carnet);
            spn_Asignaturas=(Spinner)findViewById(R.id.spn_Asignatura);

            //Para poder llenar el spiner con las asignaturas
            final ArrayList<String>asignaturasNom=new ArrayList<>();
            final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,asignaturasNom);
            spn_Asignaturas.setAdapter(adapter);

            //Llenado con foreach
            asignaturaViewModel.getAllAsignaturas().observe(this,new Observer<List<Asignatura>>(){

                @Override
                public void onChanged(List<Asignatura> asignaturas) {
                    for(Asignatura x: asignaturas){
                        asignaturasNom.add(x.getCodigoAsignatura()+" - "+x.getNomasignatura());
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Ocurrio un error!\n" +e, Toast.LENGTH_SHORT).show();
        }
    }

    //Para agregar el icono de guardado en el layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.agregar_alumno_menu,menu);
        return true;
    }

    //Funcion de boton guardado en la barra de tareas
    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_alumno:
                guardarAlumno();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
