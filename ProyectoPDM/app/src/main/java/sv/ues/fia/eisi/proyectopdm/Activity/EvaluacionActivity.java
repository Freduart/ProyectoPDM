package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.EvaluacionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionActivity extends AppCompatActivity {
    public static final int AÑADIR_EVALUACION = 1;
    public static final int EDITAR_EVALUACION = 2;
    public static final String OPERACION_EVALUACION = "Operacion_AE_Evaluacion";
    public static final String IDENTIFICADOR_EVALUACION = "ID_Evaluacion_Actual";

    private EvaluacionViewModel EvaluacionVM;
    private String identificador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_evaluacion);
            //--nueva evaluacion
            //inicializa boton flotante de acción
            FloatingActionButton botonNuevaEvaluacion = findViewById(R.id.add_eval_button);
            //al hacer un clic corto
            botonNuevaEvaluacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent hacia nueva evaluacion activity
                    Intent intent = new Intent(EvaluacionActivity.this, NuevaEditarEvaluacionActivity.class);
                    //añadir extra que definirá si añade o edita
                    intent.putExtra(OPERACION_EVALUACION, AÑADIR_EVALUACION);
                    //iniciar activity
                    startActivity(intent);
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
            //inicializa viewmodel de evaluacion
            EvaluacionVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            //obtiene todas las evaluaciones en un livedata
            EvaluacionVM.getEvalAll().observe(this, new Observer<List<Evaluacion>>() {
                @Override
                public void onChanged(List<Evaluacion> evaluacions) {
                    //mete las evaluaciones en el adaptador
                    adaptador.setEvaluaciones(evaluacions);
                }
            });

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
                    //inicia la activity
                    startActivity(intent);
                }
            });

            //--eliminar evaluacion
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
        View view = inflater.inflate(R.layout.dialog_opciones_cargo, null);
        ImageButton editar = view.findViewById(R.id.imBEditarCargo) ;
        ImageButton eliminar = view.findViewById(R.id.imBEliminarCargo);
        TextView textViewv = view.findViewById(R.id.tvADCargo);
        textViewv.setText(evaluacion.getNomEvaluacion());
        builder.setView(view);
        alertDialog = builder.create();
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //guardar id de evaluacion que se tocó
                    int id = evaluacion.getIdEvaluacion();
                    //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                    Intent intent = new Intent(EvaluacionActivity.this, NuevaEditarEvaluacionActivity.class);
                    //se mete en un extra del intent, el id
                    intent.putExtra(IDENTIFICADOR_EVALUACION, id);
                    intent.putExtra(OPERACION_EVALUACION, EDITAR_EVALUACION);
                    //inicia la activity
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(EvaluacionActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        return alertDialog;
    }
}

