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

import sv.ues.fia.eisi.proyectopdm.Adapter.AsignaturaAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;

public class AsignaturaActivity extends AppCompatActivity {
    public static final String IDENTIFICADOR_AS = "ID_ASIGNATURA_ACTUAL";

    private AsignaturaViewModel asignaturaViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignatura);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle(R.string.AppBarNameAsignaturas);

        FloatingActionButton btnNuevaAsignatura = findViewById(R.id.add_asign_button);


        btnNuevaAsignatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para dirigir a la activity de nueva asignatura
                Intent intent = new Intent(AsignaturaActivity.this, NuevaAsignaturaActivity.class);
                startActivity(intent);
            }
        });


        /*Para enlazar el objeto RecyclerView con la View que se implementa para asignaturas,
          se debe usar los layout asignatura_item y activity_asignatura(contiene a asignatura_item)
          como la lista de RecyclerView*/
        final RecyclerView recyclerView = findViewById(R.id.recycler_list_asignatura);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); //Para actualizaciones

        /*Enlace de la View con el Adapter y Modificación de recyclerView con las actualizaciones
          del Adapter*/

        final AsignaturaAdapter adapter = new AsignaturaAdapter();
        recyclerView.setAdapter(adapter);

        try {
            asignaturaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(AsignaturaViewModel.class);

            asignaturaViewModel.getAllAsignaturas().observe(this,
                    new Observer<List<Asignatura>>() {
                        @Override
                        public void onChanged(final List<Asignatura> asignaturas) {
                            //Actualiza el RecyclerView
                            adapter.setAsignaturas(asignaturas);
                        }
                    });

                    //Consultar asignatura seleccionada con click corto
                    adapter.setOnItemClickListener(new AsignaturaAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Asignatura asignatura) {
                            String id = asignatura.getCodigoAsignatura();
                            Intent intent = new Intent(AsignaturaActivity.this, VerAsignaturaActivity.class);
                            intent.putExtra(IDENTIFICADOR_AS, id);
                            startActivity(intent);
                        }
                    });

                    //Click largo para mostrar alert dialog con opciones
                    adapter.setOnLongClickListener(new AsignaturaAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(Asignatura asignatura) {
                            createCustomDialog(asignatura).show();
                        }
                    });

                    setTitle(R.string.AppBarNameAsignaturas);
        }catch (Exception e){
            Toast.makeText(AsignaturaActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Para cuando se selecciona una asignatura
    public AlertDialog createCustomDialog(final Asignatura asignatura){

        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton editar = (ImageButton) v.findViewById(R.id.imBEditar);
        ImageButton eliminar = (ImageButton) v. findViewById(R.id.imBEliminar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(asignatura.getCodigoAsignatura());
        builder.setView(v);
        alertDialog = builder.create();

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String id = asignatura.getCodigoAsignatura();
                    Intent intent = new Intent(AsignaturaActivity.this, EditarAsignaturaActivity.class);
                    intent.putExtra(IDENTIFICADOR_AS, id);
                    startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(AsignaturaActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    asignaturaViewModel.delete(asignatura);
                    Toast.makeText(AsignaturaActivity.this, R.string.asignaturaeliminada,
                            Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(AsignaturaActivity.this, e.getMessage() + " " +
                            e.getCause(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return alertDialog;
    }
}
