package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.LocalAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class LocalActivity extends AppCompatActivity {
    private LocalViewModel LocalVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista de Locales");

        RecyclerView LocalRecycler = findViewById(R.id.recycler_local_view);
        LocalRecycler.setLayoutManager(new LinearLayoutManager(this));
        LocalRecycler.setHasFixedSize(true);

        final LocalAdapter adaptador = new LocalAdapter();
        LocalRecycler.setAdapter(adaptador);

        LocalVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
        LocalVM.getAllLocales().observe(this, new Observer<List<Local>>() {
            @Override
            public void onChanged(List<Local> locals) {
                adaptador.setLocales(locals);
            }
        });
    }
}
