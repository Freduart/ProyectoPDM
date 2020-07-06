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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.OpcionCrudViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.db.entity.OpcionCrud;

import static sv.ues.fia.eisi.proyectopdm.Activity.LoginActivity.USERNAME;
import static sv.ues.fia.eisi.proyectopdm.Activity.LoginActivity.USER_PASSWORD;

/*
menu principal de la aplicaicon en este tendra acceso el usuario asginado mas adelante
 */

public class MenuActivity extends AppCompatActivity {
    //ImageView imageView1, imageView2, imageView3, imageView4;
    private int id_usuario;
    private int rol_usuario;
    private CardView alumno, evaluacion, cargos, areaadm, solicitudimpresion, asignaturas, primerasrevisiones, ciclo, local, extraordinaria, docente, encargado, inscripcion,usuario;
    private AccesoUsuarioViewModel accesoUsuarioViewModel;
    private OpcionCrudViewModel opcionCrudViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        accesoUsuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AccesoUsuarioViewModel.class);
        opcionCrudViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(OpcionCrudViewModel.class);

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
            usuario = findViewById(R.id.cardUsuario);

            alumno.setVisibility(View.GONE);
            evaluacion.setVisibility(View.GONE);
            cargos.setVisibility(View.GONE);
            areaadm.setVisibility(View.GONE);
            solicitudimpresion.setVisibility(View.GONE);
            asignaturas.setVisibility(View.GONE);
            primerasrevisiones.setVisibility(View.GONE);
            ciclo.setVisibility(View.GONE);
            local.setVisibility(View.GONE);
            extraordinaria.setVisibility(View.GONE);
            docente.setVisibility(View.GONE);
            encargado.setVisibility(View.GONE);
            inscripcion.setVisibility(View.GONE);
            usuario.setVisibility(View.GONE);

            //crear un bundle para recibir los extra del intent
            final Bundle extras = getIntent().getExtras();
            //verifica que los extra no estén vacíos
            if (extras != null) {
                //recibe id del usuario desde el extra
                id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
                //recibe rol del usuario desde el extra
                rol_usuario = extras.getInt(LoginActivity.USER_ROL);

                accesoUsuarioViewModel.obtenerAccesosPorNumCrud(id_usuario,0).observe(this, new Observer<List<AccesoUsuario>>() {
                    @Override
                    public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                        for(AccesoUsuario acceso:accesoUsuarios){
                            try {
                                OpcionCrud opcionCrudNew=opcionCrudViewModel.obtenerOpcionCrud(acceso.getIdOpcionFK());
                                //opcionCrud.add(opcionCrudNew);
                                switch (opcionCrudNew.getDescOpcion()){
                                    case "AlumnoMenu":
                                        alumno.setVisibility(View.VISIBLE);
                                        break;
                                    case "EvaluacionMenu":
                                        evaluacion.setVisibility(View.VISIBLE);
                                        break;
                                    case "CargoMenu":
                                        cargos.setVisibility(View.VISIBLE);
                                        break;
                                    case "AreaAdmMenu":
                                        areaadm.setVisibility(View.VISIBLE);
                                        break;
                                    case "SoliImpresMenu":
                                        solicitudimpresion.setVisibility(View.VISIBLE);
                                        break;
                                    case "AsignaturaMenu":
                                        asignaturas.setVisibility(View.VISIBLE);
                                        break;
                                    case "PrimRevMenu":
                                        primerasrevisiones.setVisibility(View.VISIBLE);
                                        break;
                                    case "CicloMenu":
                                        ciclo.setVisibility(View.VISIBLE);
                                        break;
                                    case "LocalMenu":
                                        local.setVisibility(View.VISIBLE);
                                        break;
                                    case "SoliExtrMenu":
                                        extraordinaria.setVisibility(View.VISIBLE);
                                        break;
                                    case "DocenteMenu":
                                        docente.setVisibility(View.VISIBLE);
                                        break;
                                    case "EncImpresMenu":
                                        encargado.setVisibility(View.VISIBLE);
                                        break;
                                    case "UsuarioMenu":
                                        usuario.setVisibility(View.VISIBLE);
                                        break;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
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

    public void usuarioRedirect(View view){
        cambiarActividad(UsuarioActivity.class);
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
                alertDialog.dismiss();
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
