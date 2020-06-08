package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class LoginActivity extends AppCompatActivity {
    EditText email, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        email = (EditText) findViewById(R.id.etEmail);
        pass = (EditText) findViewById(R.id.etPassword);
    }
    public void Login(View view){
        Intent intent = new Intent(this, MainActivity.class);
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage() +" "+e.getCause(), Toast.LENGTH_LONG).show();
        }

    }
}
