package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import sv.ues.fia.eisi.proyectopdm.R;

public class NuevoUsuarioActivity extends AppCompatActivity {

    EditText textNomUsuario,textPasswordUsuario;
    Spinner spinnerRolUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("NUEVO USUARIO");

        textNomUsuario=(EditText)findViewById(R.id.textNomUsuario);
        textPasswordUsuario=(EditText)findViewById(R.id.textPasswordUsuario);
        spinnerRolUsuario=(Spinner)findViewById(R.id.spinnerRolusuario);

        final ArrayList<String> rolesUsuario=new ArrayList<>();
        rolesUsuario.add("1-Docente Director");
        rolesUsuario.add("2-Docente");
        rolesUsuario.add("3-Alumno");
        rolesUsuario.add("4-Encargado Impresión");
        rolesUsuario.add("5-Administrador");

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,rolesUsuario);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRolUsuario.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.agregar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar:
                    guardarUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void guardarUsuario(){
        String rol=spinnerRolUsuario.getSelectedItem().toString();
        String[] id=rol.split("-");
        int idRol=Integer.parseInt(id[0]);
        switch (idRol){
            case 1:
                Intent intent3=new Intent(NuevoUsuarioActivity.this,NuevoDocenteActivity.class);
                startActivity(intent3);
            case 2:
                Intent intent=new Intent(NuevoUsuarioActivity.this,NuevoDocenteActivity.class);
                startActivity(intent);
            case 3:
                Intent intent1=new Intent(NuevoUsuarioActivity.this,AgregarAlumnoActivity.class);
                startActivity(intent1);
            case 4:
                Intent intent2=new Intent(NuevoUsuarioActivity.this,NuevoEncargadoImpresionActivity.class);
                startActivity(intent2);
            case 5:

        }
    }
}