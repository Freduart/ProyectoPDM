package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CicloViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;

public class VerCicloActivity extends AppCompatActivity {
    //Atributos de Clase
    private Ciclo cicloActual;
    private CicloViewModel cicloVM;

    private TextView dispNomCiclo;
    private TextView dispFDesde;
    private TextView dispFHasta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_ciclo);

            //Título personalizado para Activity
            setTitle("Detalle de Ciclo");

            //Asociando tVs de XML con tVs de Activity
            dispNomCiclo = (TextView) findViewById(R.id.vciclo_disp_nomCiclo);
            dispFDesde = (TextView) findViewById(R.id.vciclo_disp_fDesde);
            dispFHasta = (TextView)findViewById(R.id.vciclo_disp_fHasta);

            //Inicializando ViewModel
            cicloVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CicloViewModel.class);

            //Obteniendo ID de Ciclo Seleccionado del Intent
            Bundle extras = getIntent().getExtras();
            int idCiclo = 0;
            if(extras != null){
                idCiclo = extras.getInt(CicloActivity.IDENTIFICADOR_CICLO);
            }

            //Extraer Ciclo Actual
            cicloActual = cicloVM.getCic(idCiclo);

            //Convirtiendo nomCiclo en String
            String nomCicloAux = cicloActual.getNomCiclo() + "";

            //Asignando valores de Ciclo Actual en tVs
            dispNomCiclo.setText(nomCicloAux);
            dispFDesde.setText(cicloActual.getFechaDesde());
            dispFHasta.setText(cicloActual.getFechaHasta());
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString() ,Toast.LENGTH_LONG).show();
        }
    }
}
