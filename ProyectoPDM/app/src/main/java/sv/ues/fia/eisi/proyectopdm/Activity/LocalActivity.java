package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
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

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.LocalAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class LocalActivity extends AppCompatActivity {
    private LocalViewModel LocalVM;
    Local localAt;
    String cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista de Locales");

        final RecyclerView LocalRecycler = findViewById(R.id.recycler_local_view);
        LocalRecycler.setLayoutManager(new LinearLayoutManager(this));
        LocalRecycler.setHasFixedSize(true);

        final LocalAdapter adaptador = new LocalAdapter();
        LocalRecycler.setAdapter(adaptador);

        try{
            LocalVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
            LocalVM.getAllLocales().observe(this, new Observer<List<Local>>() {
                @Override
                public void onChanged(final List<Local> locals) {
                    adaptador.setLocales(locals);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cod=String.valueOf(locals.get(LocalRecycler.getChildAdapterPosition(v)).getIdLocal());
                            localAt=adaptador.getLocalAt(LocalRecycler.getChildAdapterPosition(v));
                            createCustomDialog().show();
                        }
                    });
                }
            });
        }catch(Exception e){
            Toast.makeText(LocalActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomDialog(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones_local, null);
        ImageButton ver = (ImageButton) v.findViewById(R.id.imBVerLocal);
        ImageButton del = (ImageButton) v.findViewById(R.id.imBEliminarLocal);
        TextView tv = (TextView) v.findViewById(R.id.tvADLocal);
        tv.setText(cod);
        builder.setView(v);
        alertDialog=builder.create();

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(LocalActivity.this, VerLocalActivity.class);
                    intent.putExtra("ID Local Actual", cod);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(LocalActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LocalVM.deleteLocal(localAt);
                    Toast.makeText(LocalActivity.this, "Local" + " " + localAt.getIdLocal() + " ha sido borrado exitosamente", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(LocalActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return alertDialog;
    }
}
