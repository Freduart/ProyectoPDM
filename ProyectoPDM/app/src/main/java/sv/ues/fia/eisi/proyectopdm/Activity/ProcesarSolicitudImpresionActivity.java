package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class ProcesarSolicitudImpresionActivity extends AppCompatActivity {

    RadioButton radioSolicitudExitosa,radioSolicitudFallida;
    EditText textObservaciones;
    private SolicitudImpresion solicitudImpresion;
    private SolicitudImpresionViewModel solicitudImpresionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procesar_solicitud_impresion);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PROCESAR SOLICITUD");

        Bundle bundle=getIntent().getExtras();
        int idImpresion=0;
        if(bundle!=null){
            idImpresion=bundle.getInt(SolicitudImpresionActivity.IDENTIFICADOR_IMPRESION);
        }
        solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudImpresionViewModel.class);
        try {
            solicitudImpresion=solicitudImpresionViewModel.obtenerSolicitudImpresion(idImpresion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        radioSolicitudExitosa = (RadioButton) findViewById(R.id.radioSolicitudExitosa);
        radioSolicitudFallida = (RadioButton) findViewById(R.id.radioSolicitudFallida);
        textObservaciones = (EditText)findViewById(R.id.textObservacionesImpresion);

        if(radioSolicitudExitosa.isChecked()){
            textObservaciones.setEnabled(false);
        }else{
            textObservaciones.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.agregar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar:
                procesarSolicitud();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void procesarSolicitud(){
        if(radioSolicitudExitosa.isChecked()){
            solicitudImpresion.setEstadoSolicitud("TERMINADA");
            solicitudImpresionViewModel.update(solicitudImpresion);
            Toast.makeText(this, "Guardado Exitósamente", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            if(textObservaciones.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Debe Ingresar Las Observaciones De Impresion", Toast.LENGTH_SHORT).show();
            }else{
                solicitudImpresion.setEstadoSolicitud("FALLIDA");
                solicitudImpresion.setResultadoImpresion(textObservaciones.getText().toString());
                solicitudImpresionViewModel.update(solicitudImpresion);
                Toast.makeText(this, "Guardado Exitósamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
