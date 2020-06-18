package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class VerCargoActivity extends AppCompatActivity {
    public static final String EXTRA_ID_CARGO = "sv.ues.fia.eisi.proyectopdm.Activity.EXTRA_ID_CARGO";

    private Cargo cargoActual;
    private Escuela escuelaActual;

    private CargoViewModel cargoViewModel;
    private EscuelaViewModel escuelaViewModel;

    private TextView idCargo;
    private TextView idEscuelaCargo;
    private TextView nomCargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_cargo);

            idCargo = (TextView) findViewById(R.id.textViewIdCargo);
            idEscuelaCargo = (TextView) findViewById(R.id.textViewEscuelaCargo);
            nomCargo = (TextView) findViewById(R.id.textViewNomCargo);

            //Instancias viewmodels
            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CargoViewModel.class);
            escuelaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);
            //obtiene intent de CargoActivity
            Bundle extras = getIntent().getExtras();
            int identCargo = 0;
            if (extras != null) {
                identCargo = extras.getInt(CargoActivity.IDENTIFICADOR_CARGO);
            }
            //Obtiene cargo actual por medio de EXTRA_ID_CARGO
            cargoActual =cargoViewModel.getCargo(identCargo);
            //Obtener idEscuelaCargo actual
            escuelaActual = escuelaViewModel.getEscuela(cargoActual.getIdEscuelaFK());

            idCargo.setText(String.valueOf(cargoActual.getIdCargo()));
            idEscuelaCargo.setText(String.valueOf(escuelaActual.getIdEscuela())+ " - "+escuelaActual.getNomEscuela());
            nomCargo.setText(cargoActual.getNomCargo());

            //titulo appbar

            setTitle(R.string.DetalleCargo);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),
                    "Error during request: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
