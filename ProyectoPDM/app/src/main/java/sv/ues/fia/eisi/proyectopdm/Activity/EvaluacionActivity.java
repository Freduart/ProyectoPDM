package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.EvaluacionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionActivity extends AppCompatActivity {
    private EvaluacionViewModel EvaluacionVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion);

        FloatingActionButton botonNuevaEvaluacion = findViewById(R.id.add_eval_button);
        botonNuevaEvaluacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EvaluacionActivity.this, NuevaEvaluacionActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView EvalRecycler = findViewById(R.id.recycler_eval_view);
        EvalRecycler.setLayoutManager(new LinearLayoutManager(this));
        EvalRecycler.setHasFixedSize(true);

        final EvaluacionAdapter adaptador = new EvaluacionAdapter();
        EvalRecycler.setAdapter(adaptador);

        EvaluacionVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
        EvaluacionVM.getEvalAll().observe(this, new Observer<List<Evaluacion>>() {
            @Override
            public void onChanged(List<Evaluacion> evaluacions) {
                adaptador.setEvaluaciones(evaluacions);
            }
        });

    }
}

