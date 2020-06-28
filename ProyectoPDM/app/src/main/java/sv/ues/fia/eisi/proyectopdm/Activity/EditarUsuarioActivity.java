package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.OpcionCrudViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.UsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.db.entity.OpcionCrud;
import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;

public class EditarUsuarioActivity extends AppCompatActivity {

    Switch switchAlumno,switchEvaluacion,switchCargo,switchAreaAdm,switchSoliImpres,switchAsignatura,switchPrimRev,switchCiclo,switchLocal,switchSoliExtr,switchDocente,switchEncImpres;
    CheckBox checkEditAlumno,checkCrearAlumno,checkEliminarAlumno,
             checkEditEvaluacion,checkCrearEvaluacion,checkEliminarEvaluacion,
             checkEditCargo,checkCrearCargo,checkEliminarCargo,
             checkEditAreaAdm,checkCrearAreaAdm,checkEliminarAreaAdm,
             checkEditSoliImpres,checkCrearSoliImpres,checkEliminarSoliImpres,
             checkEditAsignatura,checkCrearAsignatura,checkEliminarAsignatura,
             checkEditPrimRev,checkCrearPrimRev,checkEliminarPrimRev,
             checkEditCiclo,checkCrearCiclo,checkEliminarCiclo,
             checkEditLocal,checkCrearLocal,checkEliminarLocal,
             checkEditSoliExtr,checkCrearSoliExtr,checkEliminarSoliExtr,
             checkEditDocente,checkCrearDocente,checkEliminarDocente,
             checkEditEncImpres,checCrearEncImpres,checkEliminarEncImpres;
    private UsuarioViewModel usuarioViewModel;
    private Usuario usuarioActual;
    private AccesoUsuarioViewModel accesoUsuarioViewModel;
    private OpcionCrudViewModel opcionCrudViewModel;
    private ArrayList<String> habilitados;
    private boolean alumnoHabilitado,evaluacionHabilitado,cargoHabilitado,areaAdmHabilitado,soliImpresHabilitado,asignaturaHabilitado,primRevHabilitado,cicloHabilitado,localHabilitado,soliExtrHabilitado,docenteHabilitado,encImpresHabilitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("EDITAR USUARIO");

        habilitados=new ArrayList<>();

        usuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(UsuarioViewModel.class);
        opcionCrudViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(OpcionCrudViewModel.class);
        accesoUsuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AccesoUsuarioViewModel.class);

        Bundle bundle= getIntent().getExtras();
        int idUsuario=0;
        if(bundle!=null){
            idUsuario=bundle.getInt(UsuarioActivity.ID_USUARIO_SELEC);
        }
        try {
            usuarioActual=usuarioViewModel.obtenerUsuario(idUsuario);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        switchAlumno=(Switch)findViewById(R.id.switchAlumnoMenu);
        switchEvaluacion=(Switch)findViewById(R.id.switchEvaluacionMenu);
        switchCargo=(Switch)findViewById(R.id.switchCargoMenu);
        switchAreaAdm=(Switch)findViewById(R.id.switchAreaAdmMenu);
        switchSoliImpres=(Switch)findViewById(R.id.switchSolicitudImpresionMenu);
        switchAsignatura=(Switch)findViewById(R.id.switchAsignaturas);
        switchPrimRev=(Switch)findViewById(R.id.switchPrimeraRevision);
        switchCiclo=(Switch)findViewById(R.id.switchCiclo);
        switchLocal=(Switch)findViewById(R.id.switchLocal);
        switchSoliExtr=(Switch)findViewById(R.id.switchExtraordinario);
        switchDocente=(Switch)findViewById(R.id.switchDocente);
        switchEncImpres=(Switch)findViewById(R.id.switchEncImpres);

        checkEditAlumno=(CheckBox)findViewById(R.id.checkEditarAlumno);
        checkCrearAlumno=(CheckBox)findViewById(R.id.checkCrearAlumno);
        checkEliminarAlumno=(CheckBox)findViewById(R.id.checkBorrarAlumno);
        checkEditEvaluacion=(CheckBox)findViewById(R.id.checkEditarEvaluacion);
        checkCrearEvaluacion=(CheckBox)findViewById(R.id.checkCrearEvaluacion);
        checkEliminarEvaluacion=(CheckBox)findViewById(R.id.checkEliminarEvaluacion);
        checkEditCargo=(CheckBox)findViewById(R.id.checkEditarCargo);
        checkCrearCargo=(CheckBox)findViewById(R.id.checkCrearCargo);
        checkEliminarCargo=(CheckBox)findViewById(R.id.checkEliminarCargo);
        checkEditAreaAdm=(CheckBox)findViewById(R.id.checkEditarAreaAdm);
        checkCrearAreaAdm=(CheckBox)findViewById(R.id.checkCrearAreaAdm);
        checkEliminarAreaAdm=(CheckBox)findViewById(R.id.checkEliminarAreaAdm);
        checkEditSoliImpres=(CheckBox)findViewById(R.id.checkEditarSolicitudImpresion);
        checkCrearSoliImpres=(CheckBox)findViewById(R.id.checkCrearSolicitudImpresion);
        checkEliminarSoliImpres=(CheckBox)findViewById(R.id.checkEliminarSolicitudImpresion);
        checkEditAsignatura=(CheckBox)findViewById(R.id.checkEditarAsignatura);
        checkCrearAsignatura=(CheckBox)findViewById(R.id.checkCrearAsignatura);
        checkEliminarAsignatura=(CheckBox)findViewById(R.id.checkEliminarAsignatura);
        checkEditPrimRev=(CheckBox)findViewById(R.id.checkEditarPrimeraRevision);
        checkCrearPrimRev=(CheckBox)findViewById(R.id.checkCrearPrimeraRevision);
        checkEliminarPrimRev=(CheckBox)findViewById(R.id.checkEliminarPrimeraRevision);
        checkEditCiclo=(CheckBox)findViewById(R.id.checkEditarCiclo);
        checkCrearCiclo=(CheckBox)findViewById(R.id.checkCrearCiclo);
        checkEliminarCiclo=(CheckBox)findViewById(R.id.checkEliminarCiclo);
        checkEditLocal=(CheckBox)findViewById(R.id.checkEditarLocal);
        checkCrearLocal=(CheckBox)findViewById(R.id.checkCrearLocal);
        checkEliminarLocal=(CheckBox)findViewById(R.id.checkEliminarLocal);
        checkEditSoliExtr=(CheckBox)findViewById(R.id.checkEditarExtraordinario);
        checkCrearSoliExtr=(CheckBox)findViewById(R.id.checkCrearExtraordinario);
        checkEliminarSoliExtr=(CheckBox)findViewById(R.id.checkEliminarExtraordinario);
        checkEditDocente=(CheckBox)findViewById(R.id.checkEditarDocente);
        checkCrearDocente=(CheckBox)findViewById(R.id.checkCrearDocente);
        checkEliminarDocente=(CheckBox)findViewById(R.id.checkEliminarDocente);
        checkEditEncImpres=(CheckBox)findViewById(R.id.checkEditarEncImpres);
        checCrearEncImpres=(CheckBox)findViewById(R.id.checkCrearEncImpres);
        checkEliminarEncImpres=(CheckBox)findViewById(R.id.checkEliminarEncImpres);

        switchAlumno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditAlumno.setEnabled(false);
                    checkCrearAlumno.setEnabled(false);
                    checkEliminarAlumno.setEnabled(false);
                }else{
                    checkEditAlumno.setEnabled(true);
                    checkCrearAlumno.setEnabled(true);
                    checkEliminarAlumno.setEnabled(true);
                }
                alumnoHabilitado=isChecked;
            }
        });
        switchEvaluacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditEvaluacion.setEnabled(false);
                    checkCrearEvaluacion.setEnabled(false);
                    checkEliminarEvaluacion.setEnabled(false);
                }else{
                    checkEditEvaluacion.setEnabled(true);
                    checkCrearEvaluacion.setEnabled(true);
                    checkEliminarEvaluacion.setEnabled(true);
                }
                evaluacionHabilitado=isChecked;
            }
        });
        switchCargo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditCargo.setEnabled(false);
                    checkCrearCargo.setEnabled(false);
                    checkEliminarCargo.setEnabled(false);
                }else{
                    checkEditCargo.setEnabled(true);
                    checkCrearCargo.setEnabled(true);
                    checkEliminarCargo.setEnabled(true);
                }
                cargoHabilitado=isChecked;
            }
        });
        switchAreaAdm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditAreaAdm.setEnabled(false);
                    checkCrearAreaAdm.setEnabled(false);
                    checkEliminarAreaAdm.setEnabled(false);
                }else{
                    checkEditAreaAdm.setEnabled(true);
                    checkCrearAreaAdm.setEnabled(true);
                    checkEliminarAreaAdm.setEnabled(true);
                }
                areaAdmHabilitado=isChecked;
            }
        });
        switchSoliImpres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditSoliImpres.setEnabled(false);
                    checkCrearSoliImpres.setEnabled(false);
                    checkEliminarSoliImpres.setEnabled(false);
                }else{
                    checkEditSoliImpres.setEnabled(true);
                    checkCrearSoliImpres.setEnabled(true);
                    checkEliminarSoliImpres.setEnabled(true);
                }
                soliImpresHabilitado=isChecked;
            }
        });
        switchAsignatura.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditAsignatura.setEnabled(false);
                    checkCrearAsignatura.setEnabled(false);
                    checkEliminarAsignatura.setEnabled(false);
                }else{
                    checkEditAsignatura.setEnabled(true);
                    checkCrearAsignatura.setEnabled(true);
                    checkEliminarAsignatura.setEnabled(true);
                }
                asignaturaHabilitado=isChecked;
            }
        });
        switchPrimRev.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditPrimRev.setEnabled(false);
                    checkCrearPrimRev.setEnabled(false);
                    checkEliminarPrimRev.setEnabled(false);
                }else{
                    checkEditPrimRev.setEnabled(true);
                    checkCrearPrimRev.setEnabled(true);
                    checkEliminarPrimRev.setEnabled(true);
                }
                primRevHabilitado=isChecked;
            }
        });
        switchCiclo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditCiclo.setEnabled(false);
                    checkCrearCiclo.setEnabled(false);
                    checkEliminarCiclo.setEnabled(false);
                }else{
                    checkEditCiclo.setEnabled(true);
                    checkCrearCiclo.setEnabled(true);
                    checkEliminarCiclo.setEnabled(true);
                }
                cicloHabilitado=isChecked;
            }
        });
        switchLocal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditLocal.setEnabled(false);
                    checkCrearLocal.setEnabled(false);
                    checkEliminarLocal.setEnabled(false);
                }else{
                    checkEditLocal.setEnabled(true);
                    checkCrearLocal.setEnabled(true);
                    checkEliminarLocal.setEnabled(true);
                }
                localHabilitado=isChecked;
            }
        });
        switchSoliExtr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditSoliExtr.setEnabled(false);
                    checkCrearSoliExtr.setEnabled(false);
                    checkEliminarSoliExtr.setEnabled(false);
                }else{
                    checkEditSoliExtr.setEnabled(true);
                    checkCrearSoliExtr.setEnabled(true);
                    checkEliminarSoliExtr.setEnabled(true);
                }
                soliExtrHabilitado=isChecked;
            }
        });
        switchDocente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditDocente.setEnabled(false);
                    checkCrearDocente.setEnabled(false);
                    checkEliminarDocente.setEnabled(false);
                }else{
                    checkEditDocente.setEnabled(true);
                    checkCrearDocente.setEnabled(true);
                    checkEliminarDocente.setEnabled(true);
                }
                docenteHabilitado=isChecked;
            }
        });
        switchEncImpres.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    checkEditEncImpres.setEnabled(false);
                    checCrearEncImpres.setEnabled(false);
                    checkEliminarEncImpres.setEnabled(false);
                }else{
                    checkEditEncImpres.setEnabled(true);
                    checCrearEncImpres.setEnabled(true);
                    checkEliminarEncImpres.setEnabled(true);
                }
                encImpresHabilitado=isChecked;
            }
        });
        mostrarAccesos();
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
                guardarDatos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mostrarAccesos(){
        try {
            accesoUsuarioViewModel.obtenerAccesosPorNumCrud(usuarioActual.getIdUsuario(),0).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        try {
                            OpcionCrud opcionCrudNew=opcionCrudViewModel.obtenerOpcionCrud(acceso.getIdOpcionFK());
                            switch (opcionCrudNew.getDescOpcion()){
                                case "AlumnoMenu":
                                    switchAlumno.setChecked(true);
                                    break;
                                case "EvaluacionMenu":
                                    switchEvaluacion.setChecked(true);
                                    break;
                                case "CargoMenu":
                                    switchCargo.setChecked(true);
                                    break;
                                case "AreaAdmMenu":
                                    switchAreaAdm.setChecked(true);
                                    break;
                                case "SoliImpresMenu":
                                    switchSoliImpres.setChecked(true);
                                    break;
                                case "AsignaturaMenu":
                                    switchAsignatura.setChecked(true);
                                    break;
                                case "PrimRevMenu":
                                    switchPrimRev.setChecked(true);
                                    break;
                                case "CicloMenu":
                                    switchCiclo.setChecked(true);
                                    break;
                                case "LocalMenu":
                                    switchLocal.setChecked(true);
                                    break;
                                case "SoliExtrMenu":
                                    switchSoliExtr.setChecked(true);
                                    break;
                                case "DocenteMenu":
                                    switchDocente.setChecked(true);
                                    break;
                                case "EncImpresMenu":
                                    switchEncImpres.setChecked(true);
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

            accesoUsuarioViewModel.obtenerAccesosPorNumCrud(usuarioActual.getIdUsuario(),1).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        try {
                            OpcionCrud opcionCrudNew=opcionCrudViewModel.obtenerOpcionCrud(acceso.getIdOpcionFK());
                            switch (opcionCrudNew.getDescOpcion()){
                                case "EditarAlumno":
                                    checkEditAlumno.setChecked(true);
                                    break;
                                case "EditarEvaluacion":
                                    checkEditEvaluacion.setChecked(true);
                                    break;
                                case "EditarCargo":
                                    checkEditCargo.setChecked(true);
                                    break;
                                case "EditarAreaAdm":
                                    checkEditAreaAdm.setChecked(true);
                                    break;
                                case "EditarSoliImpres":
                                    checkEditSoliImpres.setChecked(true);
                                    break;
                                case "EditarAsignatura":
                                    checkEditAsignatura.setChecked(true);
                                    break;
                                case "EditarPrimRev":
                                    checkEditPrimRev.setChecked(true);
                                    break;
                                case "EditarCiclo":
                                    checkEditCiclo.setChecked(true);
                                    break;
                                case "EditarLocal":
                                    checkEditLocal.setChecked(true);
                                    break;
                                case "EditarSoliExtr":
                                    checkEditSoliExtr.setChecked(true);
                                    break;
                                case "EditarDocente":
                                    checkEditDocente.setChecked(true);
                                    break;
                                case "EditarEncImpres":
                                    checkEditEncImpres.setChecked(true);
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

            accesoUsuarioViewModel.obtenerAccesosPorNumCrud(usuarioActual.getIdUsuario(),2).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        try {
                            OpcionCrud opcionCrudNew=opcionCrudViewModel.obtenerOpcionCrud(acceso.getIdOpcionFK());
                            switch(opcionCrudNew.getDescOpcion()){
                                case "CrearAlumno":
                                    checkCrearAlumno.setChecked(true);
                                    break;
                                case "CrearEvaluacion":
                                    checkCrearEvaluacion.setChecked(true);
                                    break;
                                case "CrearCargo":
                                    checkCrearCargo.setChecked(true);
                                    break;
                                case "CrearAreaAdm":
                                    checkCrearAreaAdm.setChecked(true);
                                    break;
                                case "CrearSoliImpres":
                                    checkCrearSoliImpres.setChecked(true);
                                    break;
                                case "CrearAsignatura":
                                    checkCrearAsignatura.setChecked(true);
                                    break;
                                case "CrearPrimRev":
                                    checkCrearPrimRev.setChecked(true);
                                    break;
                                case "CrearCiclo":
                                    checkCrearCiclo.setChecked(true);
                                    break;
                                case "CrearLocal":
                                    checkCrearLocal.setChecked(true);
                                    break;
                                case "CrearSoliExtr":
                                    checkCrearSoliExtr.setChecked(true);
                                    break;
                                case "CrearDocente":
                                    checkCrearDocente.setChecked(true);
                                    break;
                                case "CrearEncImpres":
                                    checCrearEncImpres.setChecked(true);
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

            accesoUsuarioViewModel.obtenerAccesosPorNumCrud(usuarioActual.getIdUsuario(),3).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        try {
                            OpcionCrud opcionCrudNew=opcionCrudViewModel.obtenerOpcionCrud(acceso.getIdOpcionFK());
                            switch (opcionCrudNew.getDescOpcion()){
                                case "EliminarAlumno":
                                    checkEliminarAlumno.setChecked(true);
                                    break;
                                case "EliminarEvaluacion":
                                    checkEliminarEvaluacion.setChecked(true);
                                    break;
                                case "EliminarCargo":
                                    checkEliminarCargo.setChecked(true);
                                    break;
                                case "EliminarAreaAdm":
                                    checkEliminarAreaAdm.setChecked(true);
                                    break;
                                case "EliminarSoliImpres":
                                    checkEliminarSoliImpres.setChecked(true);
                                    break;
                                case "EliminarAsignatura":
                                    checkEliminarAsignatura.setChecked(true);
                                    break;
                                case "EliminarPrimRev":
                                    checkEliminarPrimRev.setChecked(true);
                                    break;
                                case "EliminarCiclo":
                                    checkEliminarCiclo.setChecked(true);
                                    break;
                                case "EliminarLocal":
                                    checkEliminarLocal.setChecked(true);
                                    break;
                                case "EliminarSoliExtr":
                                    checkEliminarSoliExtr.setChecked(true);
                                    break;
                                case "EliminarDocente":
                                    checkEliminarDocente.setChecked(true);
                                    break;
                                case "EliminarEncImpres":
                                    checkEliminarEncImpres.setChecked(true);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void guardarDatos(){
        int idUsuario=usuarioActual.getIdUsuario();
        accesoUsuarioViewModel.eliminarAccesosDeUsuario(idUsuario);

        if(alumnoHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,1));
            if(checkCrearAlumno.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,15));
            }
            if(checkEditAlumno.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,16));
            }
            if(checkEliminarAlumno.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,17));
            }
        }
        if(evaluacionHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),2));
            if(checkCrearEvaluacion.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,18));
            }
            if(checkEditEvaluacion.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,19));
            }
            if(checkEliminarEvaluacion.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,20));
            }
        }
        if(cargoHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),3));
            if(checkCrearCargo.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,21));
            }
            if(checkEditCargo.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,22));
            }
            if(checkEliminarCargo.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,23));
            }
        }
        if(areaAdmHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),4));
            if(checkCrearAreaAdm.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,24));
            }
            if(checkEditAreaAdm.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,25));
            }
            if(checkEliminarAreaAdm.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,26));
            }
        }
        if(soliImpresHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),5));
            if(checkCrearSoliImpres.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,27));
            }
            if(checkEditSoliImpres.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,28));
            }
            if(checkEliminarSoliImpres.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,29));
            }
        }
        if(asignaturaHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),6));
            if(checkCrearAsignatura.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,30));
            }
            if(checkEditAsignatura.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,31));
            }
            if(checkEliminarAsignatura.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,32));
            }
        }
        if(primRevHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),7));
            if(checkCrearPrimRev.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,33));
            }
            if(checkEditPrimRev.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,34));
            }
            if(checkEliminarPrimRev.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,35));
            }
        }
        if(cicloHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),8));
            if(checkCrearCiclo.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,36));
            }
            if(checkEditCiclo.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,37));
            }
            if(checkEliminarCiclo.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,38));
            }
        }
        if(localHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),9));
            if(checkCrearLocal.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,39));
            }
            if(checkEditLocal.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,40));
            }
            if(checkEliminarLocal.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,41));
            }
        }
        if(soliExtrHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),10));
            if(checkCrearSoliExtr.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,42));
            }
            if(checkEditSoliExtr.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,43));
            }
            if(checkEliminarSoliExtr.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,44));
            }
        }
        if(docenteHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),11));
            if(checkCrearDocente.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,45));
            }
            if(checkEditDocente.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,46));
            }
            if(checkEliminarDocente.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,47));
            }
        }
        if(encImpresHabilitado){
            accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(usuarioActual.getIdUsuario(),12));
            if(checCrearEncImpres.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,48));
            }
            if(checkEditEncImpres.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,49));
            }
            if(checkEliminarEncImpres.isChecked()){
                accesoUsuarioViewModel.insertAccesoUsuario(new AccesoUsuario(idUsuario,50));
            }
        }

        Toast.makeText(EditarUsuarioActivity.this,"Guardado Exitosamente",Toast.LENGTH_SHORT).show();

        finish();
    }
}