package sv.ues.fia.eisi.proyectopdm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.EscuelaAdapter;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class MainActivity extends AppCompatActivity {

    private EscuelaViewModel escuelaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final EscuelaAdapter adapter=new EscuelaAdapter();
        recyclerView.setAdapter(adapter);

        try{
            /*
                debido a que ViewModel esta desfasado se tienen que buscar alternativas en este caso usamos AndroidViewModelFactory para dicha
                solucion para comprobarlo podemos mandar un mensaje en un toast
            */
            //escuelaViewModel= new ViewModelProvider(this).get(EscuelaViewModel.class);
            escuelaViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);
            //escuelaViewModel=new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(EscuelaViewModel.class);
            escuelaViewModel.getAllEscuelas().observe(this, new Observer<List<Escuela>>() {
                @Override
                public void onChanged(@Nullable List<Escuela> escuelas) {
                    //actualiza el RecyclerView
                    adapter.setEscuelas(escuelas);
                }
            });
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Error en el VIewModel", Toast.LENGTH_SHORT).show();
        }
    }
}
