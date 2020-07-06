package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class VerCargoActivity extends AppCompatActivity {
    public static final String EXTRA_ID_CARGO = "sv.ues.fia.eisi.proyectopdm.Activity.EXTRA_ID_CARGO";

    private Cargo cargoActual;
    private AreaAdm areaAdmActual;

    private CargoViewModel cargoViewModel;
    private AreaAdmViewModel areaAdmViewModel;

    private TextView idCargo;
    private TextView idAreaCargo;
    private TextView nomCargo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_cargo);

            idCargo = (TextView) findViewById(R.id.textViewIdCargo);
            idAreaCargo = (TextView) findViewById(R.id.textViewEscuelaCargo);
            nomCargo = (TextView) findViewById(R.id.textViewNomCargo);

            //Instancias viewmodels
            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CargoViewModel.class);
            areaAdmViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);
            //obtiene intent de CargoActivity
            Bundle extras = getIntent().getExtras();
            int identCargo = 0;
            if (extras != null) {
                identCargo = extras.getInt(CargoActivity.IDENTIFICADOR_CARGO);
            }
            //Obtiene cargo actual por medio de EXTRA_ID_CARGO
            cargoActual =cargoViewModel.getCargo(identCargo);
            //Obtener idAreaCargo actual
            areaAdmActual = areaAdmViewModel.getAreaAdm(cargoActual.getIdAreaAdminFK());
            idCargo.setText(String.valueOf(cargoActual.getIdCargo()));
            idAreaCargo.setText(areaAdmActual.getIdDeptarmento() + " - " + areaAdmActual.getNomDepartamento());
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
