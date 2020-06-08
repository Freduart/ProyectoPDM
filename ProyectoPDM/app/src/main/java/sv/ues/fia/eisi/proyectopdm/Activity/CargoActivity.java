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

import sv.ues.fia.eisi.proyectopdm.Adapter.CargoAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;

public class CargoActivity extends AppCompatActivity {
    private CargoViewModel cargoViewModel;
    Cargo cargoAt;
    String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.AppBarNameCargos);

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
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Obtiene el código del cargo seleccionado
                            codigo = String.valueOf(cargos.get(recyclerView
                                    .getChildAdapterPosition(v)).getIdCargo());
                            //Obtiene el cargo seleccionado
                            cargoAt = adapter.getCargoAt(recyclerView.getChildAdapterPosition(v));
                            createCustomDialog().show();
                        }
                    });
                }
            });

        }catch (Exception e){
            Toast.makeText(CargoActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Para cuando se selecciona un cargo
    public AlertDialog createCustomDialog(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones_cargo, null);
        ImageButton eliminar = (ImageButton) v.findViewById(R.id.imBEliminarCargo);
        TextView tv = (TextView) v.findViewById(R.id.tvADCargo);
        tv.setText(codigo);
        builder.setView(v);
        alertDialog = builder.create();
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cargoViewModel.delete(cargoAt);
                    Toast.makeText(CargoActivity.this, "Cargo" + " "+
                            cargoAt.getIdCargo() +" ha sido borrado exitosamente",
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
