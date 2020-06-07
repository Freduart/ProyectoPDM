package sv.ues.fia.eisi.proyectopdm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.CicloAdapter;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CicloViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;

public class CicloActivity extends AppCompatActivity {
    private CicloViewModel CicloVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciclo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ciclos Académicos");

        RecyclerView CicloRecycler = findViewById(R.id.recycler_ciclo_view);
        CicloRecycler.setLayoutManager(new LinearLayoutManager(this));
        CicloRecycler.setHasFixedSize(true);

        final CicloAdapter adaptador = new CicloAdapter();
        CicloRecycler.setAdapter(adaptador);

        CicloVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CicloViewModel.class);
        CicloVM.getAllCiclos().observe(this, new Observer<List<Ciclo>>() {
            @Override
            public void onChanged(List<Ciclo> ciclos) {
                adaptador.setCiclos(ciclos);
            }
        });
    }
}
