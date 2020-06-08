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
                    Intent intent = new Intent(EvaluacionActivity.this, NuevaEvaluacionActivity.class);
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
                    intent.putExtra("ID_Evaluacion_Actual", id);
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
            setTitle("Evaluaciones");
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
        ImageButton eliminar = (ImageButton) view.findViewById(R.id.imBEliminarCargo);
        TextView textViewv = (TextView) view.findViewById(R.id.tvADCargo);
        textViewv.setText(evaluacion.getNomEvaluacion());
        builder.setView(view);
        alertDialog = builder.create();
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EvaluacionVM.deleteEval(evaluacion);
                    Toast.makeText(EvaluacionActivity.this, "La evaluación " +
                                    evaluacion.getNomEvaluacion() +" ha sido borrado exitosamente",
                            Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(EvaluacionActivity.this, e.getMessage() + " " + e.getCause(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return alertDialog;
    }
}

