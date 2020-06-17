package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;


public class AgregarAlumnoActivity extends AppCompatActivity {

    //Variables a utilizar
    private EditText et_carnet;
    private EditText et_nombre;
    private EditText et_apellidos;
    private EditText et_correo;
    private Spinner spin_Carrera;
    private AlumnoViewModel alumnoViewModel;
    private EscuelaViewModel escuelaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_agregar_alumno);

            //Enlaces con el layout
            et_carnet=(EditText)findViewById(R.id.et_Carnet);
            et_nombre=(EditText)findViewById(R.id.et_Nombre);
            et_apellidos=(EditText)findViewById(R.id.et_Apellidos);
            et_correo=(EditText)findViewById(R.id.et_Correo);
            spin_Carrera=(Spinner)findViewById(R.id.spn_Carrera);



            //Llenado del Spinner Carreras
            final ArrayList<String>carrerasNom=new ArrayList<>();
            final ArrayAdapter<String>adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,carrerasNom);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Se cambia el adapter del spiner al creado
            spin_Carrera.setAdapter(adapter);
            escuelaViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);

            //Con este observe se llena con los datos deseados en este caso el idEscuela de la tabla Escuela y el nombre de la carrera
            escuelaViewModel.getAllEscuelas().observe(this,new Observer<List<Escuela>>() {
                @Override
                public void onChanged(@Nullable List<Escuela> carreras) {
                    for (Escuela x : carreras) {
                        carrerasNom.add(x.getIdEscuela() + " - "+x.getCarrera());
                    }
                    adapter.notifyDataSetChanged();
                }
            });

        }catch (Exception e){
            Toast.makeText(this, "Error inesperado al crear "+e, Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para agregar Alumno
    private void guardarAlumno(){
        try{
            //Enlaces con cada uno de los editText para almacenar su valor
            String carnet=et_carnet.getText().toString();
            String nombre=et_nombre.getText().toString();
            String apellidos=et_apellidos.getText().toString();
            String correo=et_correo.getText().toString();

            //Enlace con el spinner para almacenar el valor seleccionado
            String carreraSelect=spin_Carrera.getSelectedItem().toString();
            String[] carreraSelect2=carreraSelect.split("-");         //Para omitir el caracter
            String carrera=carreraSelect2[0].trim();                        //Para omitir espacios

            //Validacion de caracteres no vacios
            if(carnet.isEmpty()||nombre.isEmpty()||apellidos.isEmpty()||correo.isEmpty()||carrera.trim().isEmpty()){
                Toast.makeText(this, "Por favor llenar los datos", Toast.LENGTH_SHORT).show();
                return;
            }

            //Objeto auxiliar creado por los datos ingresados
            Alumno alumno=new Alumno(carnet,nombre,apellidos,carrera,correo, 1);

            //Instancia VMAlumno
            alumnoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);

            //Insercion
            alumnoViewModel.insert(alumno);
            Toast.makeText(this, "¡Guardado con exito!", Toast.LENGTH_SHORT).show();
            finish();


        }catch (Exception e){
            Toast.makeText(this, "Ocurrio un error al guardar", Toast.LENGTH_SHORT).show();
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
                guardarAlumno();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
