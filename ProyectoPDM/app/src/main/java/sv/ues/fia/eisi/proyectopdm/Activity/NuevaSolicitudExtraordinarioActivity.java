package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;

import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;

public class NuevaSolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinarioViewModel soliExtraVM;

    private EditText idAlumno;
    private EditText idEvaluacion;
    private EditText tipoSoli;
    private EditText motivoSoli;
    private EditText fechaSoli;
    private CheckBox justiSoli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_solicitud_extraordinario);

            //Inicializa elementos del Layout en Activity
            idAlumno = (EditText) findViewById(R.id.editNCarnetAlumno);
            idEvaluacion = (EditText) findViewById(R.id.editNIdEvaluacion);
            tipoSoli = (EditText) findViewById(R.id.editNTipoSoliExtra);
            motivoSoli = (EditText) findViewById(R.id.editNMotivoSoliExtra);
            fechaSoli = (EditText) findViewById(R.id.editNFechaSoliExtra);
            justiSoli = (CheckBox) findViewById(R.id.NJustiSoliExtra);

            //Título personalizado para Activity
            setTitle("Nueva Solicitud Extraordinaria");
        }catch(Exception e){
            Toast.makeText(NuevaSolicitudExtraordinarioActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarSoliExtra(){
        try {
            String carnetAlumno = idAlumno.getText().toString();
            int idEval = Integer.parseInt(idEvaluacion.getText().toString());
            String tipo = tipoSoli.getText().toString();
            String motivo = motivoSoli.getText().toString();
            String fecha = fechaSoli.getText().toString();
            boolean justi = justiSoli.isSelected();

            SolicitudExtraordinario soliAux = new SolicitudExtraordinario(carnetAlumno, idEval, tipo, motivo, fecha, justi);

            soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);
            soliExtraVM.insert(soliAux);

            Toast.makeText(NuevaSolicitudExtraordinarioActivity.this, "Solicitud Insertada con éxito", Toast.LENGTH_SHORT).show();

            finish();
        } catch(Exception e){
            Toast.makeText(NuevaSolicitudExtraordinarioActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nueva_soli_extra_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar_soli_extra:
                guardarSoliExtra();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
