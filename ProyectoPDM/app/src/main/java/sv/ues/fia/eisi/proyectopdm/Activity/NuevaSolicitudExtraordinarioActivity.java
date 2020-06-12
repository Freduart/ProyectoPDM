package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;

import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.TipoEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

public class NuevaSolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinarioViewModel soliExtraVM;
    private TipoEvaluacionViewModel tipoEvaVM;

    private EditText idAlumno;
    private EditText idEvaluacion;
    private Spinner tipoSoli;
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
            tipoSoli = (Spinner) findViewById(R.id.editNTipoSoli);
            motivoSoli = (EditText) findViewById(R.id.editNMotivoSoliExtra);
            fechaSoli = (EditText) findViewById(R.id.editNFechaSoliExtra);
            justiSoli = (CheckBox) findViewById(R.id.NJustiSoliExtra);

            //Título personalizado para Activity
            setTitle("Nueva Solicitud Extraordinaria");

            //Spinner Tipo evaluacion
            final ArrayList<String> tipoEvaluacionesNom = new ArrayList<>();
            final ArrayAdapter<String> adaptadorSpinnerTipoEval = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipoEvaluacionesNom);
            adaptadorSpinnerTipoEval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tipoSoli.setAdapter(adaptadorSpinnerTipoEval);
            tipoEvaVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);
            tipoEvaVM.getTodosTiposEvaluaciones().observe(this, new Observer<List<TipoEvaluacion>>() {
                @Override
                public void onChanged(@Nullable List<TipoEvaluacion> tiposEvaluaciones) {
                    for (TipoEvaluacion x : tiposEvaluaciones) {
                        String cadena = "Ordinario";
                        String cadValida = x.getTipoEvaluacion();
                        if(cadValida.equals(cadena)){
                            break;
                        }else{
                            tipoEvaluacionesNom.add(x.getIdTipoEvaluacion()+" - " + x.getTipoEvaluacion());
                        }
                    }
                    adaptadorSpinnerTipoEval.notifyDataSetChanged();
                }
            });
        }catch(Exception e){
            Toast.makeText(NuevaSolicitudExtraordinarioActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarSoliExtra(){
        try {
            String carnetAlumno = idAlumno.getText().toString();
            int idEval = Integer.parseInt(idEvaluacion.getText().toString());
            String motivo = motivoSoli.getText().toString();
            String fecha = fechaSoli.getText().toString();
            boolean justi = justiSoli.isSelected();

            //---obtener valor de spinner TIPO EVALUACION
            String tipoEvalAux1 = tipoSoli.getSelectedItem().toString();
            String[] tipoEvalAux2 = tipoEvalAux1.split("-");
            //almacenar TIPO EVAL
            String tipoEvaAux3 = tipoEvalAux2[0].trim();
            int tipoEva = Integer.parseInt(tipoEvaAux3);

            SolicitudExtraordinario soliAux = new SolicitudExtraordinario(carnetAlumno, idEval, tipoEva, motivo, fecha, justi);

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
