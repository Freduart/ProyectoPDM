package sv.ues.fia.eisi.proyectopdm.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import sv.ues.fia.eisi.proyectopdm.R;

import static sv.ues.fia.eisi.proyectopdm.Activity.LoginActivity.USERNAME;
import static sv.ues.fia.eisi.proyectopdm.Activity.LoginActivity.USER_PASSWORD;

/*
menu principal de la aplicaicon en este tendra acceso el usuario asginado mas adelante
 */

public class MenuActivity extends AppCompatActivity {
    //ImageView imageView1, imageView2, imageView3, imageView4;
    private int id_usuario;
    private int rol_usuario;
    private CardView alumno, evaluacion, cargos, areaadm, solicitudimpresion, asignaturas, primerasrevisiones, ciclo, local, extraordinaria, docente, encargado, inscripcion;
    static final int READ_STORAGE_PERMISSION = 2, WRITE_STORAGE_PERMISSION = 3, INTERNET = 4, NETWORK_STATE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);

            alumno = findViewById(R.id.cardMateria);
            evaluacion = findViewById(R.id.cardEvaluacion);
            cargos = findViewById(R.id.cardViewCargo);
            areaadm = findViewById(R.id.card4);
            solicitudimpresion = findViewById(R.id.cardSolicitudesImpresion);
            asignaturas = findViewById(R.id.cardAsignatura);
            primerasrevisiones = findViewById(R.id.cardPrimeraRevision);
            ciclo = findViewById(R.id.cardCiclo);
            local = findViewById(R.id.cardLocal);
            extraordinaria = findViewById(R.id.cardSolicitudExtraordinario);
            docente = findViewById(R.id.cardDocente);
            encargado = findViewById(R.id.cardEncImpres);
            inscripcion = findViewById(R.id.cardInscripcion);

            solicitarPermisos();

            //crear un bundle para recibir los extra del intent
            final Bundle extras = getIntent().getExtras();
            //verifica que los extra no estén vacíos
            if (extras != null) {
                //recibe id del usuario desde el extra
                id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
                //recibe rol del usuario desde el extra
                rol_usuario = extras.getInt(LoginActivity.USER_ROL);
                switch (rol_usuario) {
                    //en caso de que el rol sea director
                    case 1:
                        alumno.setVisibility(View.GONE); //ocultar acceso a alumno
                        evaluacion.setVisibility(View.VISIBLE); //permitir acceso a evaluacion
                        cargos.setVisibility(View.GONE); //ocultar acceso a cargos
                        areaadm.setVisibility(View.VISIBLE); //ocultar acceso a areaadm
                        solicitudimpresion.setVisibility(View.VISIBLE); //permitir acceso a sol impr
                        asignaturas.setVisibility(View.VISIBLE); //ocultar acceso a asignaturas
                        primerasrevisiones.setVisibility(View.VISIBLE); //permitir acceso a pr
                        ciclo.setVisibility(View.GONE); // ocultar acceso a ciclo
                        local.setVisibility(View.GONE); //ocultar acceso a local
                        extraordinaria.setVisibility(View.VISIBLE); //permitir acceso a sol extr.
                        docente.setVisibility(View.GONE); //ocultar acceso a docentes
                        encargado.setVisibility(View.GONE); //ocultar acceso a encargados impresion
                        inscripcion.setVisibility(View.GONE);
                        break;
                    //caso en que el rol sea docente
                    case 2:
                        alumno.setVisibility(View.GONE); //ocultar acceso a alumno
                        evaluacion.setVisibility(View.VISIBLE); //permitir acceso a evaluacion
                        cargos.setVisibility(View.GONE); //ocultar acceso a cargos
                        areaadm.setVisibility(View.GONE); //ocultar acceso a areaadm
                        solicitudimpresion.setVisibility(View.VISIBLE); //permitir acceso a sol impr
                        asignaturas.setVisibility(View.GONE); //ocultar acceso a asignaturas
                        primerasrevisiones.setVisibility(View.VISIBLE); //permitir acceso a pr
                        ciclo.setVisibility(View.GONE); // ocultar acceso a ciclo
                        local.setVisibility(View.GONE); //ocultar acceso a local
                        extraordinaria.setVisibility(View.VISIBLE); //permitir acceso a sol extr.
                        docente.setVisibility(View.GONE); //ocultar acceso a docentes
                        encargado.setVisibility(View.GONE); //ocultar acceso a encargados impresion
                        inscripcion.setVisibility(View.GONE);
                        break;
                    //en caso de el usuario sea alumno
                    case 3:
                        alumno.setVisibility(View.GONE); //permitir acceso a alumno
                        evaluacion.setVisibility(View.VISIBLE); //permitir acceso a evaluacion
                        cargos.setVisibility(View.GONE); //ocultar acceso a cargos
                        areaadm.setVisibility(View.GONE); //ocultar acceso a areaadm
                        solicitudimpresion.setVisibility(View.GONE);
                        asignaturas.setVisibility(View.GONE);
                        primerasrevisiones.setVisibility(View.GONE);
                        ciclo.setVisibility(View.GONE);
                        local.setVisibility(View.GONE);
                        extraordinaria.setVisibility(View.VISIBLE);
                        docente.setVisibility(View.GONE);
                        encargado.setVisibility(View.GONE);
                        inscripcion.setVisibility(View.VISIBLE);
                        ;
                        break;
                    //en caso de que el usuario sea encargado de impresion
                    case 4:
                        alumno.setVisibility(View.GONE); //ocultar acceso a alumno
                        evaluacion.setVisibility(View.GONE); //permitir acceso a evaluacion
                        cargos.setVisibility(View.GONE); //ocultar acceso a cargos
                        areaadm.setVisibility(View.GONE); //ocultar acceso a areaadm
                        solicitudimpresion.setVisibility(View.VISIBLE); //permitir acceso a sol impr
                        asignaturas.setVisibility(View.GONE); //ocultar acceso a asignaturas
                        primerasrevisiones.setVisibility(View.GONE); //permitir acceso a pr
                        ciclo.setVisibility(View.GONE); // ocultar acceso a ciclo
                        local.setVisibility(View.GONE); //ocultar acceso a local
                        extraordinaria.setVisibility(View.GONE); //permitir acceso a sol extr.
                        docente.setVisibility(View.GONE); //ocultar acceso a docentes
                        encargado.setVisibility(View.GONE); //ocultar acceso a encargados impresion
                        inscripcion.setVisibility(View.GONE);
                        break;
                    //en caso de que el usuario sea el administrador
                    case 5:
                        alumno.setVisibility(View.VISIBLE); //permitir acceso a alumno
                        evaluacion.setVisibility(View.VISIBLE); //permitir acceso a evaluacion
                        cargos.setVisibility(View.VISIBLE); //ocultar acceso a cargos
                        areaadm.setVisibility(View.VISIBLE); //ocultar acceso a areaadm
                        solicitudimpresion.setVisibility(View.VISIBLE);
                        asignaturas.setVisibility(View.VISIBLE);
                        primerasrevisiones.setVisibility(View.VISIBLE);
                        ciclo.setVisibility(View.VISIBLE);
                        local.setVisibility(View.VISIBLE);
                        extraordinaria.setVisibility(View.VISIBLE);
                        docente.setVisibility(View.VISIBLE);
                        encargado.setVisibility(View.VISIBLE);
                        inscripcion.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "Error al mostrar el menu "+e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
    public void alumnoRedirect(View view) {
        cambiarActividad(AlumnoActivity.class);
    }

    //Redirect de btnEditable1
    public void evaluacionRedirect(View view) {
        cambiarActividad(EvaluacionActivity.class);
    }

    //Redirect de btnCargo_menu
    public void cargoRedirect(View view) {
        cambiarActividad(CargoActivity.class);
    }

    //Redirect de btnEditable3
    public void areaAdmRedirect(View view) {
        cambiarActividad(AreaAdmActivity.class);

    }

    //Redirect de SolicitudImpresion
    public void solicitudImpresionRedirect(View view) {
        cambiarActividad(SolicitudImpresionActivity.class);
    }

    //Redirect de Asignaturas
    public void asignaturaRedirect(View view) {
        cambiarActividad(AsignaturaActivity.class);
    }

    //Redirect de PR
    public void primerarevisionRedirect(View view) {
        cambiarActividad(PrimeraRevisionActivity.class);
    }

    public void cicloRedirect(View view) {
        cambiarActividad(CicloActivity.class);
    }

    public void localRedirect(View view) {
        cambiarActividad(LocalActivity.class);
    }

    public void soliExtraRedirect(View view) {
        cambiarActividad(SolicitudExtraordinarioActivity.class);
    }

    public void docenteRedirect(View view) {
        cambiarActividad(DocenteActivity.class);
    }

    public void EncImpresRedirect(View view) {
        cambiarActividad(EncargadoImpresionActivity.class);
    }

    public void inscripcionRedirect(View view) {
        cambiarActividad(InscripcionActivity.class);
    }

    public AlertDialog customAlertDialog() {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.opciones_usuario, null);
        Button btnCerrar = v.findViewById(R.id.btnCerrarSesion);
        builder.setView(v);
        alertDialog = builder.create();

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceSingleton.getInstance().writePreference(USERNAME, "");
                PreferenceSingleton.getInstance().writePreference(USER_PASSWORD, "");
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Hasta Pronto!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        return alertDialog;
    }

    //método de cambio de actividad
    private void cambiarActividad(Class clase) {
        //validacion
        try {
            //crea intent (de la clase en parámetro)
            Intent intent = new Intent(this, clase);
            //coloca datos en extras de intent (identificadorDeExtra,valor)
            intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
            intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
            //inicia actividad
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MenuActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                    solicitudimpresion.setVisibility(View.VISIBLE);
                } else {
                    solicitudimpresion.setVisibility(View.GONE);
                }
                return;
            }
            case WRITE_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    solicitudimpresion.setVisibility(View.VISIBLE);
                } else {
                    solicitudimpresion.setVisibility(View.GONE);
                }
            }
            case INTERNET: {
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    solicitudimpresion.setVisibility(View.VISIBLE);
                } else {
                    solicitudimpresion.setVisibility(View.GONE);
                }
            }
            case NETWORK_STATE: {
                if (grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    solicitudimpresion.setVisibility(View.VISIBLE);
                } else {
                    solicitudimpresion.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
