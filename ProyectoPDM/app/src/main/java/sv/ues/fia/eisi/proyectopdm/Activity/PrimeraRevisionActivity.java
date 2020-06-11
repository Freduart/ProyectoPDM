package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.PrimeraRevisionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class PrimeraRevisionActivity extends AppCompatActivity {
    private PrimeraRevisionViewModel primeraRevisionViewModel;
    PrimeraRevision primeraRevisionAt;
    String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera_revision);

        setTitle(R.string.AppBarNamePrimerasRevisiones);

        final RecyclerView recyclerView = findViewById(R.id.recycler_list_primerarevision);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PrimeraRevisionAdapter adapter = new PrimeraRevisionAdapter();
        recyclerView.setAdapter(adapter);

        try {
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);

            primeraRevisionViewModel.getAllPrimerasRevisiones().observe(this, new Observer<List<PrimeraRevision>>() {
                @Override
                public void onChanged(List<PrimeraRevision> primeraRevisiones) {
                    adapter.setPrs(primeraRevisiones);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Obtiene la primera revision seleccionada
                            primeraRevisionAt = adapter.getPrimeraRevisionAt(recyclerView.getChildAdapterPosition(v));
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(PrimeraRevisionActivity.this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }
    }
    //AlertDialog cuando se selecciona un item
    /*public AlertDialog createCustomDialog(){
        //final AlertDialog al
    }*/
}
