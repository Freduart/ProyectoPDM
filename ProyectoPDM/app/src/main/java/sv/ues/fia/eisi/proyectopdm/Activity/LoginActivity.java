package sv.ues.fia.eisi.proyectopdm.Activity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.UsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;

/*
Login sin restriccion de acceso... De momento
 */

//Se necesita implementar GoogleApiClient.onConnectionFailedListener para poder usar el sing in de google
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static final String ID_USUARIO = "ID_USUARIO_INGRESADO";
    public static final String USERNAME = "USER_NAME";
    public static final String USER_ROL = "USER_ROL";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    static final int READ_STORAGE_PERMISSION = 2, WRITE_STORAGE_PERMISSION = 3, INTERNET = 4, NETWORK_STATE = 5;

    //Codigo de Fredy
    //Variables a utilizar para el sing In
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int SING_IN=1;


    EditText usernom, passuser;
    Button login;
    UsuarioViewModel usuarioViewModel;
    Usuario usuarioIngresado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        solicitarPermisos();
        usernom = (EditText) findViewById(R.id.etEmail);
        passuser = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);

        usuarioViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(UsuarioViewModel.class);

        //Codigo de Fredy para el login con google
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this )
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        signInButton=findViewById(R.id.SingInGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(loginIntent, SING_IN);
            }
        });

        //Fin del codigo de Fredy para el login con google


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
                    startActivity(intent);
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

    //Metodos para solicitar permisos de lectura, escritura y accceso a internet
    private void solicitarPermisos() {
        int readStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int internet = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int networkState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (readStorage != PackageManager.PERMISSION_GRANTED && writeStorage != PackageManager.PERMISSION_GRANTED &&
                internet != PackageManager.PERMISSION_GRANTED && networkState != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION);
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION);
                requestPermissions(new String[]{Manifest.permission.INTERNET}, INTERNET);
                requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, NETWORK_STATE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    login.setEnabled(true);
                } else {
                    login.setEnabled(false);
                }
                return;
            }
            case WRITE_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    login.setEnabled(true);
                } else {
                    login.setEnabled(false);
                }
            }
            case INTERNET: {
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    login.setEnabled(true);
                } else {
                    login.setEnabled(false);
                }
            }
            case NETWORK_STATE: {
                if (grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    login.setEnabled(true);
                } else {
                    login.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //Codigo de Fredy para el logueo con google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SING_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                startActivity(new Intent(LoginActivity.this,Perfil_Activity.class));
                finish();
            }else{
                Toast.makeText(this, "Error al loguearse", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
