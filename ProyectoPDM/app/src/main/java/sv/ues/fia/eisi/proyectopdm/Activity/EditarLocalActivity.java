package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class EditarLocalActivity extends AppCompatActivity {
    private LocalViewModel localVM;
    private Local localActual;

    private TextView editIdLocal;
    private EditText editNomLocal;
    private EditText editUbLocal;
    private EditText editLatitud;
    private EditText editLongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_local);

            //Título personalizado para Activity
            setTitle("Editar Local");

            //Inicializa elementos del Layout en Activity
            editIdLocal = findViewById(R.id.edit_id_Local);
            editNomLocal = findViewById(R.id.edit_nombre_Local);
            editUbLocal = findViewById(R.id.edit_ubicacion_Local);
            editLatitud = findViewById(R.id.edit_latitud_Local);
            editLongitud = findViewById(R.id.edit_longitud_Local);

            //Inicializa el ViewModel
            localVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);

            //Extrae el id del Local a editar del Intent
            Bundle extras = getIntent().getExtras();
            String idLoc = "";
            if(extras != null){
                idLoc = extras.getString(LocalActivity.IDENTIFICADOR_LOCAL);
            }

            //Se asigna el objeto extraído del ViewModel usando el id
            localActual = localVM.getLoc(idLoc);

            //Se asignan los valores correspondientes en elementos del Layout
            editIdLocal.setText(localActual.getIdLocal());
            editNomLocal.setText(localActual.getNombreLocal());
            editUbLocal.setText(localActual.getUbicacion());
            editLatitud.setText(String.valueOf(localActual.getLatitud()));
            editLongitud.setText(String.valueOf(localActual.getLongitud()));
        }catch (Exception e){
            Toast.makeText(EditarLocalActivity.this, e.getMessage()+ " " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    private void guardarCambiosLocal(){
        try {
            //Almacena el valor del Id
            String idLocal = localActual.getIdLocal();

            //Almacena el valor del Nombre
            String nomLocal = editNomLocal.getText().toString();
            //Almacena el valor de la Ubicación
            String ubLocal = editUbLocal.getText().toString();

            //Inicializa variable auxiliar para almacenar latitud
            String latAux = editLatitud.getText().toString();
            //Convierte variable auxiliar en Double
            double latitud = Double.parseDouble(latAux);

            //Inicializa variable auxiliar para almacenar longitud
            String lonAux = editLongitud.getText().toString();
            //Convierte variable auxiliar en Double
            double longitud = Double.parseDouble(lonAux);

            //Inicializa el ViewModel
            localVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);

            //Se crea una instancia de Local para usar en el ViewModel
            Local aux = localVM.getLoc(idLocal);
            aux.setNombreLocal(nomLocal);
            aux.setUbicacion(ubLocal);
            aux.setLatitud(latitud);
            aux.setLongitud(longitud);

            //Actualiza el Local
            localVM.updateLocal(aux);

            //Mensaje de éxito, si hay algún error se muestra el mensaje de error en el catch
            Toast.makeText(EditarLocalActivity.this, "Local " + aux.getIdLocal() + " Actualizado con éxito", Toast.LENGTH_LONG).show();

            finish();
        }catch (Exception e){
            Toast.makeText(EditarLocalActivity.this, e.getMessage() + " - " + e.fillInStackTrace().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.nuevo_local_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_local:
                guardarCambiosLocal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
