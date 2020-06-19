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
    private Ciclo cicloActual;

    private EditText editNombreCiclo;
    private DatePicker dpFechaInicio;
    private DatePicker dpFechaFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_ciclo);

            //Título personalizado para Activity
            setTitle("Editar Ciclo");

            //Inicializa elementos del Layout en Activity
            editNombreCiclo = findViewById(R.id.edit_nombre_ciclo);
            dpFechaInicio = findViewById(R.id.edit_fechaInicio_ciclo);
            dpFechaFin = findViewById(R.id.edit_fechaFin_ciclo);

            //Inicializa el ViewModel
            cicloVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CicloViewModel.class);

            //Se extrae el identificador del ciclo a editar del Intent
            Bundle extras = getIntent().getExtras();
            int idCicloAct = 0;
            if(extras != null){
                idCicloAct = extras.getInt(CicloActivity.IDENTIFICADOR_CICLO);
            }

            //Se asigna el objeto extraído del ViewModel usando el id
            cicloActual = cicloVM.getCic(idCicloAct);

            //Se asigna el valor correspondiente en el elemento del Layout
            editNombreCiclo.setText(String.valueOf(cicloActual.getNomCiclo()));
        }catch(Exception e){
            Toast.makeText(EditarCicloActivity.this, e.getMessage()+ " " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    private void guardarCambiosCiclo(){
        try {
            //Se convierte la variable auxiliar a int
            int nomCiclo = Integer.parseInt(editNombreCiclo.getText().toString());

            //Inicializa el constructor de String para Fecha de Inicio
            StringBuilder fechaIniBuilder = new StringBuilder(10);
            //Concatena los valores de fecha de inicio
            fechaIniBuilder.append(dpFechaInicio.getDayOfMonth()).append("-").append(dpFechaInicio.getMonth()+1).append("-").append(dpFechaInicio.getYear());
            //Asigna la cadena de texto desde el constructor de String
            String fechaInicio = fechaIniBuilder.toString();

            //Inicializa el constructor de String para Fecha de Finalización
            StringBuilder fechaFinBuilder = new StringBuilder(10);
            //Concatena los valores de fecha de finalización
            fechaFinBuilder.append(dpFechaFin.getDayOfMonth()).append("-").append(dpFechaFin.getMonth()+1).append("-").append(dpFechaFin.getYear());
            //Asigna la cadena de texto desde el constructor de String
            String fechaFin = fechaFinBuilder.toString();

            //Se extrae el identificador del ciclo a editar del Intent
            Bundle extras = getIntent().getExtras();
            int idCiclo = 0;
            if(extras != null){
                idCiclo = extras.getInt(CicloActivity.IDENTIFICADOR_CICLO);
            }

            //Inicializa el ViewModel
            cicloVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CicloViewModel.class);

            //Se crea una instancia de Ciclo auxiliar para usar en el ViewModel
            Ciclo aux = cicloVM.getCic(idCiclo);
            aux.setNomCiclo(nomCiclo);
            aux.setFechaDesde(fechaInicio);
            aux.setFechaHasta(fechaFin);

            //Actualiza el ciclo
            cicloVM.updateCiclo(aux);

            //Mensaje de éxito, si hay algún error se muestra el mensaje de error en el catch
            Toast.makeText(EditarCicloActivity.this, "Actualizado con éxito: " + String.valueOf(nomCiclo) + "-"+ fechaInicio + "-" + fechaFin, Toast.LENGTH_SHORT).show();

            finish();
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
