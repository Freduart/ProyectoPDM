package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class verAlumnoActivity extends AppCompatActivity {

    //Enlaces con las tablas para poder obtener informacion del alumno y de la carrera que cursa
    private Alumno alumnoActual;
    private Escuela escuelaActual;

    private AlumnoViewModel alumnoViewModel;
    private EscuelaViewModel escuelaViewModel;

    //Enlaces con los textview
    private TextView disp_Carnet;
    private TextView disp_Nombre;
    private TextView disp_Apellido;
    private TextView disp_Carrera;
    private TextView disp_Correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);

            //Enlaces con la pantalla
            setContentView(R.layout.activity_ver_alumno);
            disp_Carnet=(TextView)findViewById(R.id.disp_carnet_alumno);
            disp_Nombre=(TextView)findViewById(R.id.disp_nombre_alumno);
            disp_Apellido=(TextView)findViewById(R.id.disp_apellido_alumno);
            disp_Carrera=(TextView)findViewById(R.id.disp_carrera_alumno);
            disp_Correo=(TextView)findViewById(R.id.disp_correo_alumno);

            //Inicializacion de los ViewModel
            alumnoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
            escuelaViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);

            //Obtener los datos del Intent
            Bundle extra=getIntent().getExtras();
            String carnet="";
            if(extra!=null){
                carnet=extra.getString(AlumnoActivity.IDENTIFICADOR_ALUMNO);
            }

            //Obtenemos el carnet por medio del intent
            alumnoActual=alumnoViewModel.getAlumn(carnet);

            //Obtenemos sus datos relacionados
            //escuelaActual=escuelaViewModel.getEscuela(alumnoActual.getCarrera());

            //Colocar los datos en los TextView
            disp_Carnet.setText(alumnoActual.getCarnetAlumno());
            disp_Nombre.setText(alumnoActual.getNombre());
            disp_Apellido.setText(alumnoActual.getApellido());
            disp_Correo.setText(alumnoActual.getCorreo());

            /*
                Debido a que la entidad Alumno no esta enlazada con la tabla escuela se guarda el id en lugar del
                nombre de la carrera de esa escuela debido a ese problema se compara el id de la escuela y se manda a mostrar segun
                ese id
             */

            if(Integer.parseInt(alumnoActual.getCarrera())==1){
                disp_Carrera.setText("Ingenieria de Sistemas Informaticos");
            }
            if(Integer.parseInt(alumnoActual.getCarrera())==2){
                disp_Carrera.setText("Ingenieria Industrial");
            }
            if(Integer.parseInt(alumnoActual.getCarrera())==3){
                disp_Carrera.setText("Ingenieria Electrica");
            }
            if(Integer.parseInt(alumnoActual.getCarrera())==4){
                disp_Carrera.setText("Ingenieria Civil");
            }
            if(Integer.parseInt(alumnoActual.getCarrera())==5){
                disp_Carrera.setText("Ingenieria Quimica");
            }
            if(Integer.parseInt(alumnoActual.getCarrera())==6){
                disp_Carrera.setText("Ingenieria de Alimentos");
            }
            if(Integer.parseInt(alumnoActual.getCarrera())==7){
                disp_Carrera.setText("Arquitectura");
            }
            if(Integer.parseInt(alumnoActual.getCarrera())==8){
                disp_Carrera.setText("Ingenieria Mecanica");
            }

        }catch (Exception e){
            Toast.makeText(this,  "ERROR AL CONSULTAR ALUMNO " +e, Toast.LENGTH_SHORT).show();
        }
    }

}
