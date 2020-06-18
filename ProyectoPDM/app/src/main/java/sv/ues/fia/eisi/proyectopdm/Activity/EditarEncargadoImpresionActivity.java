package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EncargadoImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;

public class EditarEncargadoImpresionActivity extends AppCompatActivity {

    TextInputLayout textNomenc;
    EncargadoImpresion encargadoImpresionActual;
    private EncargadoImpresionViewModel encargadoImpresionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_encargado_impresion);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("EDITAR ENCARGADO");

        textNomenc=(TextInputLayout)findViewById(R.id.textNombreEdit);

        Bundle bundle=getIntent().getExtras();
        int idEncargado=0;
        if(bundle!=null){
            idEncargado=bundle.getInt(EncargadoImpresionActivity.ID_ENCARGADO);
        }
        encargadoImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EncargadoImpresionViewModel.class);
        try {
            encargadoImpresionActual=encargadoImpresionViewModel.ObtenerEncargadoImpresion(idEncargado);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        textNomenc.getEditText().setText(encargadoImpresionActual.getNomEncargado());
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
                guardarEncImpres();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void guardarEncImpres(){
        if(textNomenc.getEditText().getText().toString().trim().isEmpty()){
            textNomenc.setError("Ingrese El Nombre Del Nuevo Encargado.");
        }else{
            String nomEnc=textNomenc.getEditText().getText().toString();
            EncargadoImpresion encargadoImpresion=new EncargadoImpresion(nomEnc, 1);
            encargadoImpresion.setIdEncargadoImpresion(encargadoImpresionActual.getIdEncargadoImpresion());
            try {
                encargadoImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EncargadoImpresionViewModel.class);
                encargadoImpresionViewModel.update(encargadoImpresion);
                Toast.makeText(this, "Guardado Exitosamente.", Toast.LENGTH_SHORT).show();
                finish();
            }catch (Exception e){
                Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
