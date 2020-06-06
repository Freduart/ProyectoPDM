package sv.ues.fia.eisi.proyectopdm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.EvaluacionAdapter;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionActivity extends AppCompatActivity {
    private EvaluacionViewModel EvaluacionVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion);

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
