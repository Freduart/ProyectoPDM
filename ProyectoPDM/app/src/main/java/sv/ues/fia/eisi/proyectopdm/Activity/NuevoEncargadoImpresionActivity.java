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

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EncargadoImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;

public class NuevoEncargadoImpresionActivity extends AppCompatActivity {

    TextInputLayout textNomenc;
    private EncargadoImpresionViewModel encargadoImpresionViewModel;
    String idU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_encargado_impresion);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("NUEVO ENCARGADO");

        textNomenc=(TextInputLayout)findViewById(R.id.textNomEncNuevo);

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
            EncargadoImpresion encargadoImpresion=new EncargadoImpresion(nomEnc, 4);
            try {
                encargadoImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EncargadoImpresionViewModel.class);
                encargadoImpresionViewModel.insert(encargadoImpresion);
                Toast.makeText(this, "Guardado Exitosamente.", Toast.LENGTH_SHORT).show();
                finish();
            }catch (Exception e){
                Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
