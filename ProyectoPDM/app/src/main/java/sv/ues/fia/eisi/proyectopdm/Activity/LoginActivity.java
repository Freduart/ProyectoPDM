package sv.ues.fia.eisi.proyectopdm.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.UsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;

/*
Login sin restriccion de acceso... De momento
 */

public class LoginActivity extends AppCompatActivity {
    public static final String ID_USUARIO = "ID_USUARIO_INGRESADO";
    public static final String USERNAME = "USER_NAME";
    public static final String USER_ROL = "USER_ROL";
    public static final String USER_PASSWORD = "USER_PASSWORD";


    EditText usernom, passuser;
    Button login;
    UsuarioViewModel usuarioViewModel;
    SharedPreferences sharedPreferences;
    Usuario usuarioIngresado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        usernom = (EditText) findViewById(R.id.etEmail);
        passuser = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);

        usuarioViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(UsuarioViewModel.class);

        sharedPreferences=getSharedPreferences("CredencialesUsuario",LoginActivity.MODE_PRIVATE);
        sharedPreferences=getPreferences(LoginActivity.MODE_PRIVATE);
        String usuario=sharedPreferences.getString(USERNAME,"");
        String password=sharedPreferences.getString(USER_PASSWORD,"");
        if(!usuario.equals("") && !password.equals("")){
            final String[] credenciales = {usuario, password};
            try {
                usuarioIngresado = usuarioViewModel.obtenerCredenciales(credenciales);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            if (usuarioIngresado != null) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra(ID_USUARIO, usuarioIngresado.getIdUsuario());
                intent.putExtra(USERNAME, usuarioIngresado.getNombreUsuario());
                intent.putExtra(USER_ROL, usuarioIngresado.getRol());
                startActivity(intent);
                Toast.makeText(this, "Bienvenido!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorectos.", Toast.LENGTH_SHORT).show();
            }
        }else{
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final String usuario = usernom.getText().toString().trim();
                        final String password = passuser.getText().toString().trim();
                        final String[] credenciales = {usuario, password};
                        usuarioViewModel.getAllUsers().observe(LoginActivity.this, new Observer<List<Usuario>>() {
                            @Override
                            public void onChanged(List<Usuario> usuarios) {
                                try {
                                    usuarioIngresado = usuarioViewModel.obtenerCredenciales(credenciales);
                                    if (usuarioIngresado != null) {
                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                        intent.putExtra(ID_USUARIO, usuarioIngresado.getIdUsuario());
                                        intent.putExtra(USERNAME, usuarioIngresado.getNombreUsuario());
                                        intent.putExtra(USER_ROL, usuarioIngresado.getRol());
                                        startActivity(intent);

                                        SharedPreferences sharedPreferences=getPreferences(LoginActivity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString(USERNAME,usuario);
                                        editor.putString(USER_PASSWORD,password);
                                        editor.commit();

                                        Toast.makeText(v.getContext(), "Bienvenido!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(v.getContext(), "Usuario o contraseña incorectos.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage() + " - " + e.getCause(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
