package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

/*
Metodo para editar alumno en base al carnet del estudiante y mostrando los datos correspondientes en su pantalla
 */
public class NuevaEditarAlumnoActivity extends AppCompatActivity {
    //Enlace con los items del layout
    private TextView et_carnet;
    private EditText et_nombre;
    private EditText et_apellidos;
    private EditText et_correo;
    private Spinner spn_Carrera;
    private AlumnoViewModel alumnoViewModel;
    private EscuelaViewModel escuelaViewModel;
    private Alumno alumno_Actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_alumno);

            //Enlace de los componentes de pantalla con los del activity
            et_carnet = (TextView) findViewById(R.id.et_Carnet_editar);
            et_nombre = (EditText) findViewById(R.id.et_Nombre_editar);
            et_apellidos = (EditText) findViewById(R.id.et_Apellidos_editar);
            et_correo=(EditText) findViewById(R.id.et_Correo_editar);
            spn_Carrera = (Spinner) findViewById(R.id.spn_Carrera_editar);
            et_carnet.setEnabled(true);

            final ArrayList<String>carrerasNom=new ArrayList<>();
            final ArrayAdapter<String>adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,carrerasNom);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Se cambia el adapter del spiner al creado
            spn_Carrera.setAdapter(adapter);
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

            alumnoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);

            //Obtenemos los datos de la pantalla anterior
            Bundle extra = getIntent().getExtras();
            String carnet="";
            if(extra!=null){
                carnet=extra.getString(AlumnoActivity.IDENTIFICADOR_ALUMNO);
            }

            //Enlazamos los datos en cada uno de los EditText
            alumno_Actual=alumnoViewModel.getAlumn(carnet);
            et_carnet.setText(alumno_Actual.getCarnetAlumno());
            et_nombre.setText(alumno_Actual.getNombre());
            et_apellidos.setText(alumno_Actual.getApellido());
            et_correo.setText(alumno_Actual.getCorreo());

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para agregar Alumno
    private void guardarAlumno() {
        try {
            //Enlaces con cada uno de los editText para almacenar su valor
            String carnet = et_carnet.getText().toString();
            String nombre = et_nombre.getText().toString();
            String apellidos = et_apellidos.getText().toString();
            String correo = et_correo.getText().toString();

            //Enlace con el spinner para almacenar el valor seleccionado
            //Escuela carreraSelect = (Escuela) spn_Carrera.getSelectedItem();

            //Validacion de caracteres no vacios
            if (carnet.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Por favor llenar los datos", Toast.LENGTH_SHORT).show();
                return;
            }

            alumnoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);

            //Valores a editar con su casteo
            Alumno alumno=alumnoViewModel.getAlumn(carnet);
            alumno.setNombre(nombre);
            alumno.setApellido(apellidos);
            alumno.setCorreo(correo);
            alumno.setIdUsuarioFk(3);
            //alumno.setCarrera(String.valueOf(carreraSelect));
            alumnoViewModel.update(alumno);

            Toast.makeText(this, "El alummno: " + carnet + " a sido actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish();

            /*Codigo Arely
            alumnoViewModel.getAllAlumnos().observe(this, new Observer<List<Alumno>>() {
                @Override
                public void onChanged(List<Alumno> alumnos) {
                    try {
                        Alumno alumnoAActualizar = alumnoViewModel.getAlumn(alumno.getCarnetAlumno());
                        if(alumnoAActualizar!=null){
                            Toast.makeText(NuevaEditarAlumnoActivity.this, "Error, registro duplicado.", Toast.LENGTH_SHORT).show();
                        }else {
                            //Actualizamos el alumno
                            alumnoViewModel.update(alumno);
                            //Mensaje de confirmacion
                            Toast.makeText(NuevaEditarAlumnoActivity.this, "El alummno: " + carnet + " a sido actualizado correctamente", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }catch (Exception e){
                        Toast.makeText(NuevaEditarAlumnoActivity.this, e.getMessage() + " - "+e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            //Fin del codigo de Arely*/
        } catch (Exception e) {
            Toast.makeText(this, "Ocurrio un error al guardar " +e, Toast.LENGTH_SHORT).show();
        }
    }

    //Para agregar el icono de guardado en el layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.agregar_alumno_menu, menu);
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
