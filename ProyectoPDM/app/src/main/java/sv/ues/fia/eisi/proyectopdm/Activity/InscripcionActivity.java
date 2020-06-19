package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.InscripcionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Inscripcion;

public class InscripcionActivity extends AppCompatActivity {
    //A utilizar
    private TextView carnet;
    private Spinner spn_Asignaturas;
    //private EditText glab;
    private EditText glab;
    private EditText gteo;
    private EditText gdisc;

    //ViewModels a utilizar
    private InscripcionViewModel inscripcionViewModel;
    private AlumnoViewModel alumnoViewModel;
    private AsignaturaViewModel asignaturaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inscripcion);

            //Modificar el titulo del layout
            setTitle(R.string.AppBarNameInscripcion);

            //Enlaces con el layout
            carnet=(TextView)findViewById(R.id.et_Carnet);
            spn_Asignaturas=(Spinner)findViewById(R.id.spn_Asignatura);
            glab=(EditText) findViewById(R.id.glabnumber);
            gteo=(EditText) findViewById(R.id.gteonumber);
            glab=(EditText) findViewById(R.id.gdiscnumber);

            //Para poder llenar el spiner con las asignaturas
            final ArrayList<String>asignaturasNom=new ArrayList<>();
            final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,asignaturasNom);

            //settea layout de dropdown del spinner (layout por defecto de android)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_Asignaturas.setAdapter(adapter);
            //Instancia con la clase ViewModel
            asignaturaViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);

            //Llenado con foreach y obtencion de datos
            asignaturaViewModel.getAllAsignaturas().observe(this,new Observer<List<Asignatura>>(){

                @Override
                public void onChanged(List<Asignatura> asignaturas) {
                    for(Asignatura x: asignaturas){
                        asignaturasNom.add(x.getCodigoAsignatura()+" - "+x.getNomasignatura());
                    }
                    adapter.notifyDataSetChanged();
                }
            });

            Bundle extra=getIntent().getExtras();
            String carnet="";
            if(extra!=null){
                carnet=extra.getString(AlumnoActivity.IDENTIFICADOR_ALUMNO);
            }
        }catch (Exception e){
            Toast.makeText(this, "Ocurrio un error!\n" +e, Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarInscripcion(){
        try{
            String carnetActual=carnet.getText().toString();
            int glabActual=Integer.parseInt(glab.getText().toString());
            int gdiscActual=Integer.parseInt(gdisc.getText().toString());
            int gteoActual=Integer.parseInt(gteo.getText().toString());

            //Obtenemos el dato seleccionado
            String materiaSelect=spn_Asignaturas.getSelectedItem().toString();
            String[]materiaSelect2=materiaSelect.split("-");
            String asignatura=materiaSelect2[0].trim();

            if(asignatura.trim().isEmpty()||String.valueOf(glabActual).isEmpty()||String.valueOf(gdiscActual).isEmpty()||String.valueOf(gteoActual).isEmpty()){
                Toast.makeText(this, "Por favor seleccionar una asignatura", Toast.LENGTH_SHORT).show();
                return;
            }

            Inscripcion inscripcion=new Inscripcion(carnetActual,asignatura,glabActual,gdiscActual,gteoActual);

            inscripcionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(InscripcionViewModel.class);
            inscripcionViewModel.obtenerTodasRelacionesInscripcion().observe(this, new Observer<List<Inscripcion>>() {
                @Override
                public void onChanged(List<Inscripcion> inscripcions) {
                    try{
                        Inscripcion inscripcionACrear=inscripcionViewModel.obtenerInscripcion(inscripcion.getCarnetAlumnoFK(),inscripcion.getCodigoAsignaturaFK());
                        if(inscripcionACrear!=null){
                            Toast.makeText(InscripcionActivity.this, "Error, inscripcion ya realizada", Toast.LENGTH_SHORT).show();
                        }else{
                            inscripcionViewModel.insertarInscripcion(inscripcion);
                            Toast.makeText(InscripcionActivity.this, "¡Guardado con exito!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }catch (Exception e){
                        Toast.makeText(InscripcionActivity.this, "ERROR AL TRATAR DE GUARDAR", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Ocurrio un error inesperado\n" +e, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_alumno:
                guardarInscripcion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
