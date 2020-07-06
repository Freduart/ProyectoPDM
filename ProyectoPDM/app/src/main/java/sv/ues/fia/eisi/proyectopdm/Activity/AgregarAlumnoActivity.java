package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    //Para reconocimiento de voz a texto
    private ImageButton ibMic;
    public static final int REC_CODE_INPUT=100;

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

            ibMic = (ImageButton) findViewById(R.id.micst);

            ibMic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iniciarEntradaDeVoz();
                }
            });

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


    public void iniciarEntradaDeVoz(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora.");
        try{
            startActivityForResult(intent, REC_CODE_INPUT);
        }catch (ActivityNotFoundException e){

            Toast.makeText(AgregarAlumnoActivity.this, e.getMessage()+" - "+e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REC_CODE_INPUT:{
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // nomCargo.setText(result.get(0));
                    int idEditText = AgregarAlumnoActivity.this.getCurrentFocus().getId();
                    switch (idEditText){
                        case R.id.et_Carnet :
                            et_carnet.setText(result.get(0).toUpperCase().replace(" ", ""));
                            break;
                        case R.id.et_Nombre:
                            et_nombre.setText(result.get(0));
                            break;
                        case R.id.et_Apellidos:
                            et_apellidos.setText(result.get(0));
                            break;
                        case R.id.et_Correo:
                            et_correo.setText(result.get(0).replace("arroba", "@").replace(" ", ""));
                    }
                }
                break;
            }
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

            //Codigo Arely linea 107
            //Instancia VMAlumno
            alumnoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
            alumnoViewModel.getAllAlumnos().observe(this, new Observer<List<Alumno>>() {
                @Override
                public void onChanged(List<Alumno> alumnos) {
                    try {
                        Alumno alumnoAInsertar = alumnoViewModel.getAlumn(alumno.getCarnetAlumno());
                        if(alumnoAInsertar!=null){
                            Toast.makeText(AgregarAlumnoActivity.this, "Error, registro duplicado.", Toast.LENGTH_SHORT).show();
                        }else {
                            //Insercion
                            alumnoViewModel.insert(alumno);
                            Toast.makeText(AgregarAlumnoActivity.this, "¡Guardado con exito!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }catch (Exception e){
                        Toast.makeText(AgregarAlumnoActivity.this, e.getMessage() + " - "+e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
                //Fin del codigo de Arely
            });
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
