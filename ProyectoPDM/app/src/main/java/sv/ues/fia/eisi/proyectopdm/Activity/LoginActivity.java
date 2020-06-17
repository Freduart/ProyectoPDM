package sv.ues.fia.eisi.proyectopdm.Activity;

import android.content.Intent;
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


    EditText usernom, passuser;
    Button login;
    UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        usernom = (EditText) findViewById(R.id.etEmail);
        passuser = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String usuario = usernom.getText().toString().trim();
                    final String password = passuser.getText().toString().trim();
                    final String[] credenciales = {usuario, password};
                    usuarioViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(UsuarioViewModel.class);
                    usuarioViewModel.getAllUsers().observe(LoginActivity.this, new Observer<List<Usuario>>() {
                        @Override
                        public void onChanged(List<Usuario> usuarios) {
                            try {
                                Usuario usuarioIngresado = usuarioViewModel.obtenerCredenciales(credenciales);
                                if (usuarioIngresado != null) {
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    intent.putExtra(ID_USUARIO, usuarioIngresado.getIdUsuario());
                                    intent.putExtra(USERNAME, usuarioIngresado.getNombreUsuario());
                                    intent.putExtra(USER_ROL, usuarioIngresado.getRol());
                                    startActivity(intent);
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
