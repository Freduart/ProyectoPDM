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

import sv.ues.fia.eisi.proyectopdm.Adapter.CargoAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;

public class CargoActivity extends AppCompatActivity {
    public static final String IDENTIFICADOR_CARGO = "ID_CARGO_ACTUAL";
    private CargoViewModel cargoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.AppBarNameCargos);

        FloatingActionButton btnNuevoCargo = findViewById(R.id.add_cargo_button);

        btnNuevoCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CargoActivity.this, NuevoCargoActivity.class);
                startActivity(intent);
            }
        });

        /*Para enlazar el objeto RecyclerView con la View que se implementa para cargos,
          se debe usar los layout cargo_item y activity_cargo(contiene a cargo_item)
          como la lista de RecyclerView*/
        final RecyclerView recyclerView = findViewById(R.id.recycler_list_cargo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);//Para actualizaciones

        /*Enlace de la View con el Adapter y Modificación de recyclerView con las actualizaciones
          del Adapter*/

        final CargoAdapter adapter = new CargoAdapter();
        recyclerView.setAdapter(adapter);

        try {
            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(CargoViewModel.class);
            cargoViewModel.getAllCargos().observe(this, new Observer<List<Cargo>>() {
                @Override
                public void onChanged(final List<Cargo> cargos) {
                    //Actualiza el RecyclerView
                    adapter.setCargos(cargos);
                }
            });
            //Consultar cargo con click corto
            adapter.setOnItemClickListener(new CargoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Cargo cargo) {
                    int id = cargo.getIdCargo();
                    Intent intent = new Intent(CargoActivity.this, VerCargoActivity.class);
                    intent.putExtra(IDENTIFICADOR_CARGO, id);
                    startActivity(intent);
                }
            });
            //Click largo para mostrar alertdialog con opciones
            adapter.setOnLongClickListner(new CargoAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(Cargo cargo) {
                    try {
                        int id = cargo.getIdCargo();
                        createCustomDialog(cargo).show();
                    }catch (Exception e){
                        Toast.makeText(CargoActivity.this, e.getMessage() + " - " +e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }catch (Exception e){
            Toast.makeText(CargoActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Para cuando se selecciona un cargo
    public AlertDialog createCustomDialog(final Cargo cargo){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton eliminar = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton editar = (ImageButton)v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(cargo.getNomCargo());
        builder.setView(v);
        alertDialog = builder.create();
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = cargo.getIdCargo();
                    Intent intent = new Intent(CargoActivity.this, EditarCargoActivity.class);
                    intent.putExtra(IDENTIFICADOR_CARGO, id);
                    startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(CargoActivity.this, e.getMessage() + " " + e.getCause(),Toast.LENGTH_LONG).show();
                }
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cargoViewModel.delete(cargo);
                    Toast.makeText(CargoActivity.this, "Cargo" + " "+
                                    cargo.getIdCargo() +" ha sido borrado exitosamente",
                            Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(CargoActivity.this, e.getMessage() + " " + e.getCause(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return alertDialog;
    }
}
