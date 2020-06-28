package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.EvaluacionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.Ws.ControladorEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionActivity extends AppCompatActivity {
    public static final int AÑADIR_EVALUACION = 1;
    public static final int EDITAR_EVALUACION = 2;
    public static final String OPERACION_EVALUACION = "Operacion_AE_Evaluacion";
    public static final String IDENTIFICADOR_EVALUACION = "ID_Evaluacion_Actual";

    private EvaluacionViewModel EvaluacionVM;
    private String identificador;
    private int id_usuario, rol_usuario;
    private boolean crearEvaluacion,editarEvaluacion,eliminarEvaluacion;
    private AccesoUsuarioViewModel accesoUsuarioViewModel;

    private String url_retrieve = "https://eisi.fia.ues.edu.sv/eisi02/PP15001/ws_evaluacion_retrieve.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_evaluacion);

            //inicializa viewmodel de evaluacion
            EvaluacionVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            accesoUsuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AccesoUsuarioViewModel.class);

            //obtiene id de usuario y su rol
            //crear un bundle para recibir los extra del intent
            final Bundle extras = getIntent().getExtras();
            //verifica que los extra no estén vacíos
            if(extras != null){
                //recibe id del usuario desde el extra
                id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
                //recibe rol del usuario desde el extra
                rol_usuario = extras.getInt(LoginActivity.USER_ROL);
            }
            accesoUsuarioViewModel.obtenerAccesosPorUsuario(id_usuario).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        if(acceso.getIdOpcionFK()==18){
                            crearEvaluacion=true;
                        }
                        if(acceso.getIdOpcionFK()==19){
                            editarEvaluacion=true;
                        }
                        if(acceso.getIdOpcionFK()==20){
                            eliminarEvaluacion=true;
                        }
                    }
                }
            });

            //--nueva evaluacion
            //inicializa boton flotante de acción
            FloatingActionButton botonNuevaEvaluacion = findViewById(R.id.add_eval_button);
            //al hacer un clic corto
            botonNuevaEvaluacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!crearEvaluacion){
                        Toast.makeText(EvaluacionActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                    }else{
                        //intent hacia nueva evaluacion activity
                        Intent intent = new Intent(EvaluacionActivity.this, NuevaEditarEvaluacionActivity.class);
                        //añadir extra que definirá si añade o edita
                        intent.putExtra(OPERACION_EVALUACION, AÑADIR_EVALUACION);
                        intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                        intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                        //iniciar activity
                        startActivity(intent);
                    }
                }
            });


            //--lista de evaluaciones
            //inicializa recycler view
            RecyclerView EvalRecycler = findViewById(R.id.recycler_eval_view);
            //set layout manager
            EvalRecycler.setLayoutManager(new LinearLayoutManager(this));
            //recycler tiene tamaño fijo = true
            EvalRecycler.setHasFixedSize(true);
            //adaptador para recycler
            final EvaluacionAdapter adaptador = new EvaluacionAdapter();
            //linkea adaptador en el recycler
            EvalRecycler.setAdapter(adaptador);
/*
            //----Para actualización con el server
            //instanciar controlador
            ControladorEvaluacion controladorEvaluacion = new ControladorEvaluacion();
            //obtener todas las evaluaciones locales
            List<Evaluacion> evaluacionesActuales = EvaluacionVM.obtenerEvaluacionesTodasAsync();
            //obtener todas las evaluaciones en servidor
            List<Evaluacion> evaluacionesSincronizadas = controladorEvaluacion.obtenerEvaluacionesGet(url_retrieve,this);
            //recorrer las evaluaciones locales
            for(Evaluacion eActualLocal : evaluacionesActuales)
                //recorrer las evaluaciones en servidor
                for(Evaluacion eActualSinc : evaluacionesSincronizadas)
                    //si hay coincidencia
                    if(eActualLocal.getIdEvaluacion() == eActualSinc.getIdEvaluacion()){
                        //actualizar el record local con los datos del servidor
                        EvaluacionVM.updateEval(eActualSinc);
                        eActualLocal=eActualSinc;
                    }
*/
            switch(rol_usuario){
                case 1:
                case 2:
                    //obtiene todas las evaluaciones en un livedata
                    EvaluacionVM.obtenerEvaluacionesDocente(EvaluacionVM.getDocenteConUsuario(id_usuario).getCarnetDocente()).observe(this, new Observer<List<Evaluacion>>() {
                        @Override
                        public void onChanged(List<Evaluacion> evaluacions) {
                            //mete las evaluaciones en el adaptador
                            adaptador.setEvaluaciones(evaluacions);
                        }
                    });
                    break;
                //obtiene todas las evaluaciones en un livedata
                case 3:
                    botonNuevaEvaluacion.setVisibility(View.GONE);
                    //obtiene todas las evaluaciones en un livedata
                    EvaluacionVM.obtenerEvaluacionesAlumno(EvaluacionVM.getAlumnConUsuario(id_usuario).getCarnetAlumno()).observe(this, new Observer<List<Evaluacion>>() {
                        @Override
                        public void onChanged(List<Evaluacion> evaluacions) {
                            //mete las evaluaciones en el adaptador
                            adaptador.setEvaluaciones(evaluacions);
                        }
                    });
                    break;
                default:
                    //obtiene todas las evaluaciones en un livedata
                    EvaluacionVM.getEvalAll().observe(this, new Observer<List<Evaluacion>>() {
                        @Override
                        public void onChanged(List<Evaluacion> evaluacions) {
                            //mete las evaluaciones en el adaptador
                            adaptador.setEvaluaciones(evaluacions);
                        }
                    });
                    break;
            }

            //--consultar evaluacion
            //al hacer clic corto en un objeto del recycler
            adaptador.setOnItemClickListener(new EvaluacionAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Evaluacion evaluacion) {
                    //guardar id de evaluacion que se tocó
                    int id = evaluacion.getIdEvaluacion();
                    //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                    Intent intent = new Intent(EvaluacionActivity.this, VerEvaluacionActivity.class);
                    //se mete en un extra del intent, el id
                    intent.putExtra(IDENTIFICADOR_EVALUACION, id);
                    intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                    intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                    //inicia la activity
                    startActivity(intent);
                }
            });

            //si no es estudiante
            if(rol_usuario!=3)
                //--menu edición - eliminar evaluacion
                adaptador.setOnLongClickListener(new EvaluacionAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(Evaluacion evaluacion) {
                        //guardar id de evaluacion que se tocó
                        int id = evaluacion.getIdEvaluacion();
                        createCustomDialog(evaluacion).show();
                    }
                });

            //título de la pantalla
            setTitle(R.string.evaluacion);
        } catch (Exception e){
            Toast.makeText(this,e.getMessage() ,Toast.LENGTH_LONG).show();
        }
    }

    //Para cuando se selecciona un cargo
    public AlertDialog createCustomDialog(final Evaluacion evaluacion){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton editar = view.findViewById(R.id.imBEditar) ;
        ImageButton eliminar = view.findViewById(R.id.imBEliminar);
        TextView textViewv = view.findViewById(R.id.tituloAlert);
        textViewv.setText(evaluacion.getNomEvaluacion());
        builder.setView(view);
        alertDialog = builder.create();
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editarEvaluacion){
                    Toast.makeText(EvaluacionActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        //guardar id de evaluacion que se tocó
                        int id = evaluacion.getIdEvaluacion();
                        //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                        Intent intent = new Intent(EvaluacionActivity.this, NuevaEditarEvaluacionActivity.class);
                        //se mete en un extra del intent, el id
                        intent.putExtra(IDENTIFICADOR_EVALUACION, id);
                        intent.putExtra(OPERACION_EVALUACION, EDITAR_EVALUACION);
                        intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                        intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                        //inicia la activity
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(EvaluacionActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                alertDialog.dismiss();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eliminarEvaluacion){
                    Toast.makeText(EvaluacionActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        EvaluacionVM.deleteEval(evaluacion);

                        Toast.makeText(EvaluacionActivity.this, getText(R.string.inic_notif_eval) +
                                        evaluacion.getNomEvaluacion() +getText(R.string.accion_borrar_notif_eval),
                                Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }catch (Exception e){
                        Toast.makeText(EvaluacionActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.evaluacion_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nueva_solicitud_extraord:
                //crea intent (de la clase en parámetro)
                Intent intent = new Intent(this, NuevaSolicitudExtraordinarioActivity.class);
                //coloca datos en extras de intent (identificadorDeExtra,valor)
                intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                //inicia actividad
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

