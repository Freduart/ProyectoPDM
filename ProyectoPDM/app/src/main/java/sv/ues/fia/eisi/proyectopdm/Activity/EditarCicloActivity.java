package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CicloViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;

public class EditarCicloActivity extends AppCompatActivity {
    private CicloViewModel cicloVM;
    private EditText editNombreCiclo;
    private DatePicker dpFechaInicio;
    private DatePicker dpFechaFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nuevo_ciclo);

            //Inicializa variable de Barra de Acción (Barra Superior)
            ActionBar actionBar = getSupportActionBar();
            //Título personalizado para Activity
            actionBar.setTitle("Editar Ciclo");

            //Inicializa elementos del Layout en Activity
            editNombreCiclo = findViewById(R.id.edit_nombre_ciclo);
            dpFechaInicio = findViewById(R.id.edit_fechaInicio_ciclo);
            dpFechaFin = findViewById(R.id.edit_fechaFin_ciclo);
        }catch(Exception e){
            Toast.makeText(EditarCicloActivity.this, e.getMessage()+ " " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    private void guardarCambiosCiclo(){
        try {
            //Inicializa variable auxiliar para extraer nombreCiclo
            String nomAux = editNombreCiclo.getText().toString();
            //Se convierte la variable auxiliar a int
            int nomCiclo = Integer.parseInt(nomAux);

            //Inicializa el constructor de String para Fecha de Inicio
            StringBuilder fechaIniBuilder = new StringBuilder(10);
            //Concatena los valores de fecha de inicio
            fechaIniBuilder.append(dpFechaInicio.getDayOfMonth()).append("/").append(dpFechaInicio.getMonth()).append("/").append(dpFechaInicio.getYear());
            //Asigna la cadena de texto desde el constructor de String
            String fechaInicio = fechaIniBuilder.toString();

            //Inicializa el constructor de String para Fecha de Finalización
            StringBuilder fechaFinBuilder = new StringBuilder(10);
            //Concatena los valores de fecha de finalización
            fechaIniBuilder.append(dpFechaFin.getDayOfMonth()).append("/").append(dpFechaFin.getMonth()).append("/").append(dpFechaFin.getYear());
            //Asigna la cadena de texto desde el constructor de String
            String fechaFin = fechaIniBuilder.toString();

            //Se extrae el idCiclo del Intent
            Bundle extras = getIntent().getExtras();
            //Se designa variable auxiliar para id
            int idCiclo = 0;
            if(extras != null){
                //Se asigna el valor del id (en Intent) en la variable Auxiliar
                idCiclo = extras.getInt("ID Ciclo Actual");
            }

            //Se crea una instancia de Ciclo auxiliar para usar en el ViewModel
            Ciclo aux = new Ciclo(fechaInicio, fechaFin, nomCiclo);
            //Se asigna el id correspondiente
            aux.setIdCiclo(idCiclo);

            //Inicializa el ViewModel
            cicloVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CicloViewModel.class);
            //Actualiza el ciclo
            cicloVM.updateCiclo(aux);

            //Mensaje de éxito, si hay algún error se muestra el mensaje de error en el catch
            Toast.makeText(EditarCicloActivity.this, "Actualizado con éxito: " + nomAux + "-"+ fechaInicio + "-" + fechaFin, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(EditarCicloActivity.this, e.getMessage() + " - " + e.fillInStackTrace().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.nuevo_ciclo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_ciclo:
                guardarCambiosCiclo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}