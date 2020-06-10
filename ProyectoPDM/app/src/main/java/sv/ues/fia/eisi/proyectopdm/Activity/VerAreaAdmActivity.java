package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class VerAreaAdmActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "sv.ues.fia.eisi.proyectopdm.Activity.EXTRA_ID";

    private AreaAdm areaAdmActual;
    private Escuela escuelaActual;

    private AreaAdmViewModel areaAdmViewModel;
    private EscuelaViewModel escuelaViewModel;

    private TextView dispNombreAreaAdm;
    private TextView dispEscuelaAreaAdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_area_adm);
            //asociar textviews de activity con controles del layout
            dispNombreAreaAdm = findViewById(R.id.disp_nombre_ver_area);
            dispEscuelaAreaAdm = findViewById(R.id.disp_escuela_ver_area);
            //inicializar viewmodels
            areaAdmViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);
            escuelaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);
            //obtener intent de activity
            Bundle extras = getIntent().getExtras();
            int idAreaAdm = 0;
            if (extras != null) {
                idAreaAdm = extras.getInt(AreaAdmActivity.IDENTIFICADOR_AREA);
            }
            //obtener area actual por medio de EXTRA_ID de intent
            areaAdmActual = areaAdmViewModel.getAreaAdm(idAreaAdm);
            //obtener objetos relacionados
            escuelaActual = escuelaViewModel.getEscuela(areaAdmActual.getIdEscuelaFK());
            //coloca texto en textviews
            dispNombreAreaAdm.setText(areaAdmActual.getNomDepartamento());
            dispEscuelaAreaAdm.setText(escuelaActual.getNomEscuela());
            //título
            setTitle(R.string.titulo_ver_area);
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString() ,Toast.LENGTH_LONG).show();
        }
    }
}