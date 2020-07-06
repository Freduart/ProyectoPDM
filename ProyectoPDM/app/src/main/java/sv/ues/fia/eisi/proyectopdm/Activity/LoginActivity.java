package sv.ues.fia.eisi.proyectopdm.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static final String FECHA_INGRESO = "FECHA_INGRESO";


    EditText usernom, passuser;
    Button login;
    UsuarioViewModel usuarioViewModel;
    Usuario usuarioIngresado;
    String fechaHoy;
    SimpleDateFormat simpleDateFormat;

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
        Calendar calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fechaHoy=simpleDateFormat.format(calendar.getTime());

        PreferenceSingleton.getInstance().Initialize(getApplicationContext());
        String usuario=PreferenceSingleton.getInstance().readPreference(USERNAME);
        String password=PreferenceSingleton.getInstance().readPreference(USER_PASSWORD);
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
               // if(usuarioIngresado.getRol()!=4){
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    intent.putExtra(ID_USUARIO, usuarioIngresado.getIdUsuario());
                    intent.putExtra(USERNAME, usuarioIngresado.getNombreUsuario());
                    intent.putExtra(USER_ROL, usuarioIngresado.getRol());
                    SincronizacionService.shouldContinue = true;
                    startService(new Intent(LoginActivity.this, SincronizacionService.class));
                    startActivity(intent);
                    PreferenceSingleton.getInstance().writePreference(FECHA_INGRESO,fechaHoy);
                    Toast.makeText(this, "Bienvenido!", Toast.LENGTH_SHORT).show();
                    finish();
                /*} else {
                    Intent intent = new Intent(LoginActivity.this, SolicitudImpresionActivity.class);
                    intent.putExtra(ID_USUARIO, usuarioIngresado.getIdUsuario());
                    intent.putExtra(USERNAME, usuarioIngresado.getNombreUsuario());
                    intent.putExtra(USER_ROL, usuarioIngresado.getRol());
                    startActivity(intent);
                    Toast.makeText(this, "Bienvenido!", Toast.LENGTH_SHORT).show();
                    finish();
                }*/
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
                                        //if(usuarioIngresado.getRol()!=4){
                                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                            intent.putExtra(ID_USUARIO, usuarioIngresado.getIdUsuario());
                                            intent.putExtra(USERNAME, usuarioIngresado.getNombreUsuario());
                                            intent.putExtra(USER_ROL, usuarioIngresado.getRol());
                                            SincronizacionService.shouldContinue = true;
                                            startService(new Intent(LoginActivity.this, SincronizacionService.class));
                                            startActivity(intent);
                                        /*} else {
                                            Intent intent = new Intent(LoginActivity.this, SolicitudImpresionActivity.class);
                                            intent.putExtra(ID_USUARIO, usuarioIngresado.getIdUsuario());
                                            intent.putExtra(USERNAME, usuarioIngresado.getNombreUsuario());
                                            intent.putExtra(USER_ROL, usuarioIngresado.getRol());
                                            startActivity(intent);
                                        }*/

                                        PreferenceSingleton.getInstance().writePreference(USERNAME,usuario);
                                        PreferenceSingleton.getInstance().writePreference(USER_PASSWORD,password);
                                        PreferenceSingleton.getInstance().writePreference(FECHA_INGRESO,fechaHoy);
                                        Toast.makeText(v.getContext(), "Bienvenido!", Toast.LENGTH_SHORT).show();
                                        finish();
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
