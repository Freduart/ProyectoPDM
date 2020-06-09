package sv.ues.fia.eisi.proyectopdm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import sv.ues.fia.eisi.proyectopdm.R;

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

    //Redirect de btnEditable2
    public void Redirect2(View view){

    }

    //Redirect de btnEditable3
    public void Redirect3(View view){

    }
}
