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

import sv.ues.fia.eisi.proyectopdm.Adapter.CicloAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CicloViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;

public class CicloActivity extends AppCompatActivity {
    private CicloViewModel CicloVM;
    Ciclo cicloAt;
    String cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciclo);

        //Título personalizado para Activity
        setTitle("Ciclos Académicos");

        //Para Agregar Ciclo: Inicializa botón flotante de acción
        FloatingActionButton botonNuevoCiclo = findViewById(R.id.add_ciclo_button);
        //al hacer un clic corto
        botonNuevoCiclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent hacia nuevo ciclo activity
                Intent intent = new Intent(CicloActivity.this, NuevoCicloActivity.class);
                //iniciar activity
                startActivity(intent);
            }
        });

        //Incializando RecyclerView
        final RecyclerView CicloRecycler = findViewById(R.id.recycler_ciclo_view);
        //Asignando Layout
        CicloRecycler.setLayoutManager(new LinearLayoutManager(this));
        //Asignando Tamaño al RecyclerView
        CicloRecycler.setHasFixedSize(true);

        //Inicializando Adaptador para RecyclerView
        final CicloAdapter adaptador = new CicloAdapter();
        //Asignando adaptador
        CicloRecycler.setAdapter(adaptador);

        try {
            //Inicializando ViewModel
            CicloVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CicloViewModel.class);
            //Obtiene Lista de ciclos en un LiveData
            CicloVM.getAllCiclos().observe(this, new Observer<List<Ciclo>>() {
                @Override
                public void onChanged(final List<Ciclo> ciclos) {
                    //Asigan ciclos extraídos al adaptador
                    adaptador.setCiclos(ciclos);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Obtiene id del ciclo seleccionado, y lo guarda en un String
                            cod = String.valueOf(ciclos.get(CicloRecycler.getChildAdapterPosition(v)).getIdCiclo());
                            //Obtiene el Ciclo Seleccionado
                            cicloAt = adaptador.getCicloAt(CicloRecycler.getChildAdapterPosition(v));
                            createCustomDialog().show();
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(CicloActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomDialog(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones_ciclo, null);
        ImageButton ver = (ImageButton) v.findViewById(R.id.imBVerCiclo);
        ImageButton del = (ImageButton) v.findViewById(R.id.imBEliminarCiclo);
        ImageButton edit = (ImageButton) v.findViewById(R.id.imBEditarCiclo);
        TextView tv = (TextView) v.findViewById(R.id.tvADCiclo);
        tv.setText(cod);
        builder.setView(v);
        alertDialog = builder.create();

        //Botón ver: Redirige a VerCicloActivity
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = cicloAt.getIdCiclo();
                    Intent intent = new Intent(CicloActivity.this, VerCicloActivity.class);
                    intent.putExtra("ID Ciclo Actual", id);
                    startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(CicloActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botón del: Elimina el Ciclo seleccionado
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CicloVM.deleteCiclo(cicloAt);
                    Toast.makeText(CicloActivity.this, "Local" + " " + cicloAt.getIdCiclo() + " ha sido borrado exitosamente", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(CicloActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botón edit: Redirige a EditarCicloActivity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = cicloAt.getIdCiclo();
                    Intent intent = new Intent(CicloActivity.this, EditarCicloActivity.class);
                    intent.putExtra("ID Ciclo Actual", id);
                    startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(CicloActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return alertDialog;
    }
}
