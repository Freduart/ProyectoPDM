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
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class NuevoLocalActivity extends AppCompatActivity {
    private LocalViewModel localVM;
    private EditText editIdLocal;
    private EditText editNomLocal;
    private EditText editUbLocal;
    private EditText editLatitud;
    private EditText editLongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nuevo_local);

            //Título personalizado para Activity
            setTitle("Nuevo Local");

            //Inicializa elementos del Layout en Activity
            editIdLocal = findViewById(R.id.edit_id_Local);
            editNomLocal = findViewById(R.id.edit_nombre_Local);
            editUbLocal = findViewById(R.id.edit_ubicacion_Local);
            editLatitud = findViewById(R.id.edit_latitud_Local);
            editLongitud = findViewById(R.id.edit_longitud_Local);
        }catch (Exception e){
            Toast.makeText(NuevoLocalActivity.this, e.getMessage()+ " " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    private void guardarLocal(){
        try {
            //Almacena el valor del Id
            String idLocal = editIdLocal.getText().toString();
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

            //Se crea una instancia de Local para usar en el ViewModel
            Local aux = new Local(idLocal,nomLocal,ubLocal,latitud,longitud);

            //Inicializa el ViewModel
            localVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
            //Inserta el Local
            localVM.insert(aux);

            //Mensaje de éxito, si hay algún error se muestra el mensaje de error en el catch
            Toast.makeText(NuevoLocalActivity.this, "Insertado con éxito: " + idLocal + "-"+ nomLocal + "-" + ubLocal, Toast.LENGTH_SHORT).show();

            finish();
        }catch (Exception e){
            Toast.makeText(NuevoLocalActivity.this, e.getMessage() + " - " + e.fillInStackTrace().toString(), Toast.LENGTH_LONG).show();
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
                guardarLocal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
