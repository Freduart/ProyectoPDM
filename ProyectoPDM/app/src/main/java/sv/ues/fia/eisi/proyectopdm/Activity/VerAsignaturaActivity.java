package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class VerAsignaturaActivity extends AppCompatActivity {
    public static final String EXTRA_ID_ASIGNATURA = "sv.ues.fia.eisi.proyectopdm.Activity.EXTRA_ID_ASIGNATURA";

    private Asignatura asignaturaActual;
    private AreaAdm areaAdmActual;
    private AsignaturaViewModel asignaturaViewModel;
    private AreaAdmViewModel areaAdmViewModel;
    private TextView idAsignatura;
    private TextView idAreaAdmAsignatura;
    private TextView nomAsignatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_asignatura);
        try {
            idAsignatura = (TextView) findViewById(R.id.tvCodAsignatura);
            idAreaAdmAsignatura = (TextView) findViewById(R.id.tvIdAreaAdmAsign);
            nomAsignatura = (TextView) findViewById(R.id.tvNomAsignatura);
            //Instancias viewmodel
            asignaturaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AsignaturaViewModel.class);
            areaAdmViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);
            //Obtiene intent de AsignaturaActivity
            Bundle extras = getIntent().getExtras();
            String idCodigo = "";
            if (extras != null) {
                idCodigo = extras.getString("ID_Asignatura_Actual");
            }
            //Obtiene asignatura actual por medio de EXTRA_ID de intent
            asignaturaActual = asignaturaViewModel.obtenerAsignatura(idCodigo);
            //Obtener idAreaAdm actual
            areaAdmActual = areaAdmViewModel.getAreaAdm(asignaturaActual.getIdDepartamentoFK());

            idAsignatura.setText(asignaturaActual.getCodigoAsignatura());
            nomAsignatura.setText(asignaturaActual.getNomasignatura());
            idAreaAdmAsignatura.setText(String.valueOf(areaAdmActual.getIdDeptarmento() + " - " + areaAdmActual.getNomDepartamento()));

            //título appBar
            setTitle("Detalle de asignatura");

        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.getCause(),Toast.LENGTH_LONG).show();
        }
    }
}
