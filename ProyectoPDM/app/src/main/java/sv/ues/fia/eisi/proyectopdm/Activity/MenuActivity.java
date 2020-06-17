package sv.ues.fia.eisi.proyectopdm.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

import static sv.ues.fia.eisi.proyectopdm.Activity.LoginActivity.USERNAME;
import static sv.ues.fia.eisi.proyectopdm.Activity.LoginActivity.USER_PASSWORD;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.usuario:
                customAlertDialog().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void soliExtraRedirect(View view){
        Intent intent = new Intent(this, SolicitudExtraordinarioActivity.class);
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    public void docenteRedirect(View view){
        Intent intent=new Intent(this,DocenteActivity.class);
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    public void EncImpresRedirect(View view){
        Intent intent=new Intent(this,EncargadoImpresionActivity.class);
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    public AlertDialog customAlertDialog(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.opciones_usuario, null);
        Button btnCerrar=(Button)v.findViewById(R.id.btnCerrarSesion);
        builder.setView(v);
        alertDialog = builder.create();

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceSingleton.getInstance().writePreference(USERNAME,"");
                PreferenceSingleton.getInstance().writePreference(USER_PASSWORD,"");
                Intent intent=new Intent(MenuActivity.this,LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Hasta Pronto!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        return alertDialog;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
