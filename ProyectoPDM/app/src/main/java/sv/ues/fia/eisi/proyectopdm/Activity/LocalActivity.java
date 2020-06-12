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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        //Título personalizado para Activity
        setTitle("Lista de Locales");

        //Para Agregar Local: Inicializa botón flotante de acción
        FloatingActionButton botonNuevoLocal = findViewById(R.id.add_ciclo_button);

        //al hacer un clic corto
        botonNuevoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent hacia nuevo Local activity
                Intent intent = new Intent(LocalActivity.this, NuevoLocalActivity.class);
                //iniciar activity
                startActivity(intent);
            }
        });

        //Inicializando RecyclerView
        final RecyclerView LocalRecycler = findViewById(R.id.recycler_local_view);
        //Asignando Layout
        LocalRecycler.setLayoutManager(new LinearLayoutManager(this));
        //Asignando tamaño al RecyclerView
        LocalRecycler.setHasFixedSize(true);

        //Inicializando adaptador para RecyclerView
        final LocalAdapter adaptador = new LocalAdapter();
        //Asignando adaptador
        LocalRecycler.setAdapter(adaptador);

        try{
            //Inicializando ViewModel
            LocalVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
            //Obtiene la lista de Locales en un LiveData
            LocalVM.getAllLocales().observe(this, new Observer<List<Local>>() {
                @Override
                public void onChanged(final List<Local> locals) {
                    //Asigna los locales extraídos al adaptador
                    adaptador.setLocales(locals);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Obtiene código del Local seleccionado y lo asigna a un string
                            cod=String.valueOf(locals.get(LocalRecycler.getChildAdapterPosition(v)).getIdLocal());
                            //Obtiene local actual
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
        ImageButton edit = (ImageButton) v.findViewById(R.id.imBEditarLocal);
        TextView tv = (TextView) v.findViewById(R.id.tvADLocal);
        tv.setText(cod);
        builder.setView(v);
        alertDialog=builder.create();

        //Botón Ver: Redirige a VerLocalActivity.
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(LocalActivity.this, VerLocalActivity.class);
                    intent.putExtra("ID Local Actual", cod);
                    startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(LocalActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botón Borrar: Borra el local seleccionado.
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

        //Botón edit: Redirige a EditarLocalActivity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(LocalActivity.this, EditarLocalActivity.class);
                    intent.putExtra("ID Local Actual", cod);
                    startActivity(intent);
                    alertDialog.dismiss();
                }catch(Exception e){
                    Toast.makeText(LocalActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return alertDialog;
    }
}
