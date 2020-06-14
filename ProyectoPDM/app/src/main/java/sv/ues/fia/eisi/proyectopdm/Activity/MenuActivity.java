package sv.ues.fia.eisi.proyectopdm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

/*
menu principal de la aplicaicon en este tendra acceso el usuario asginado mas adelante
 */

public class MenuActivity extends AppCompatActivity {
    ImageView imageView1, imageView2, imageView3, imageView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        imageView1=(ImageView)findViewById(R.id.btnMateria);
        imageView2=(ImageView)findViewById(R.id.btnsolicitud);
        imageView3=(ImageView)findViewById(R.id.btn3);
        imageView4=(ImageView)findViewById(R.id.btn4);
    }

    /*
    Redireccion segun boton cada boton tendra un redireccionamiento diferente es conveniente hacerlo en
    un solo metodo pero tambien puede ser practico hacerlo en diferentes metodos, el siguiente metodo sera de redireccionamiento
    a la lista de alumnos
    */
    //Redirect de btnAlumno_menu
    public void alumnoRedirect(View view){
        Intent intent=new Intent(this, AlumnoActivity.class);
        startActivity(intent);
    }

    //Redirect de btnEditable1
    public void evaluacionRedirect(View view){
        Intent intent=new Intent(this, EvaluacionActivity.class);
        startActivity(intent);
    }

    //Redirect de btnCargo_menu
    public void cargoRedirect(View view){
        Intent intent = new Intent(this, CargoActivity.class);
        startActivity(intent);
    }

    //Redirect de btnEditable3
    public void areaAdmRedirect(View view){
        Intent intent = new Intent(this, AreaAdmActivity.class);
        startActivity(intent);

    }

    //Redirect de SolicitudImpresion
    public void solicitudImpresionRedirect(View view){
        Intent intent = new Intent(this, SolicitudImpresionActivity.class);
        startActivity(intent);
    }

    //Redirect de Asignaturas
    public void asignaturaRedirect(View view){
        Intent intent = new Intent(this, AsignaturaActivity.class);
        startActivity(intent);
    }

    //Redirect de PR
    public  void primerarevisionRedirect(View view){
        Intent intent = new Intent(this, PrimeraRevisionActivity.class);
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(MenuActivity.this, e.getMessage() + " - " +e.getCause(), Toast.LENGTH_LONG).show();
        }

    }

    public void cicloRedirect(View view){
        Intent intent = new Intent(this, CicloActivity.class);
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(MenuActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    public void localRedirect(View view){
        Intent intent = new Intent(this, LocalActivity.class);
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

}
