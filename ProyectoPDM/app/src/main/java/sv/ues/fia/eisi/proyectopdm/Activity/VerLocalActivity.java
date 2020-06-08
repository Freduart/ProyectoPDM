package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class VerLocalActivity extends AppCompatActivity {
    //Atributos de Clase
    private Local localActual;
    private LocalViewModel localVM;

    private TextView dispIdLocal;
    private TextView dispNomLocal;
    private TextView dispUbLocal;
    private TextView dispLatLocal;
    private TextView dispLonLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_local);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Detalle de Local");

            //Asociando tVs de XML con tVs de Activity
            dispIdLocal = (TextView) findViewById(R.id.vlocal_disp_idLocal);
            dispNomLocal = (TextView) findViewById(R.id.vlocal_disp_nomLocal);
            dispUbLocal = (TextView) findViewById(R.id.vlocal_disp_ubLocal);
            dispLatLocal = (TextView) findViewById(R.id.vlocal_disp_latitud);
            dispLonLocal = (TextView) findViewById(R.id.vlocal_disp_longitud);

            //Inicializando el ViewModel
            localVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);

            //Obteniendo ID de Local Seleccionado del Intent
            Bundle extras = getIntent().getExtras();
            String idLocal = "";
            if(extras != null){
                idLocal = extras.getString("ID Local Actual");
            }

            //Extraer Local Actual
            localActual = localVM.getLoc(idLocal);

            //Convirtiendo valores de Latitud y Longitud a texto
            String latAux = localActual.getLatitud() + "";
            String lonAux = localActual.getLongitud() + "";

            //Asignando valores de Local Actual en tVs
            dispIdLocal.setText(localActual.getIdLocal());
            dispNomLocal.setText(localActual.getNombreLocal());
            dispUbLocal.setText(localActual.getUbicacion());
            dispLatLocal.setText(latAux);
            dispLonLocal.setText(lonAux);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString() ,Toast.LENGTH_LONG).show();
        }
    }
}
