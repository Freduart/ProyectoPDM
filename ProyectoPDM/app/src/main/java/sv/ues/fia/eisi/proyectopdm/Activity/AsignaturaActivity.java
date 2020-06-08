package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.AsignaturaAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;

public class AsignaturaActivity extends AppCompatActivity {
    private AsignaturaViewModel asignaturaViewModel;
    Asignatura asignaturaAt;
    String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignatura);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle(R.string.AppBarNameAsignaturas);


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
                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Obtiene el codigo de la asignatura seleccionada
                                    codigo=asignaturas.get(recyclerView.getChildAdapterPosition(v)).getCodigoAsignatura();
                                    //Obtiene la asignatura seleccionada
                                    asignaturaAt = adapter.getAsignaturaAt(recyclerView.getChildAdapterPosition(v));
                                    createCustomDialog().show();
                                }
                            });
                        }
                    });
        }catch (Exception e){
            Toast.makeText(AsignaturaActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Para cuando se selecciona una asignatura
    public AlertDialog createCustomDialog(){

        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones_asignaturas, null);
        //ImageButton ver = (ImageButton) v.findViewById(R.id.imBVer);
        //ImageButton editar = (ImageButton) v.findViewById(R.id.imBEditar);
        ImageButton eliminar = (ImageButton) v. findViewById(R.id.imBEliminar);
        TextView tv = (TextView) v.findViewById(R.id.tvADAsignatura);
        tv.setText(codigo);
        builder.setView(v);
        alertDialog = builder.create();

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    asignaturaViewModel.delete(asignaturaAt);
                    Toast.makeText(AsignaturaActivity.this, "Asignatura" + " " +
                            asignaturaAt.getCodigoAsignatura() + " ha sido borrada exitosamente",
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
