package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;

public class DetalleNotasActivity extends AppCompatActivity {
    private int idActual;
    private DetalleEvaluacion detalleEvaluacion;
    private TextView nombreEstudiante;
    private EditText notaEstudiante;
    private Button editarPrimeraRevision;

    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_notas);
        nombreEstudiante = findViewById(R.id.titulo_nombre_detalleestudiante);
        notaEstudiante = findViewById(R.id.nota_editar_detalle);
        editarPrimeraRevision = findViewById(R.id.boton_primera_revision_detalle);
        detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
        Double notaAux = 0d;
        //obtener extras del intent
        final Bundle extras = getIntent().getExtras();
        if (extras != null){
            idActual = extras.getInt(DetalleEvaluacionActivity.ID_DETALLE_EVAL);
            try {
                detalleEvaluacion = detalleEvaluacionViewModel.getDetalleEvaluacion(idActual);
                notaAux = detalleEvaluacion.getNota();
                if(notaAux < 0)
                    notaEstudiante.setText("");
                else
                    notaEstudiante.setText(String.format("%s", notaAux));
                nombreEstudiante.setText(detalleEvaluacion.getCarnetAlumnoFK());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*
        //al hacer clic corto en un objeto del recycler
        editarPrimeraRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                Intent intent = new Intent(DetalleNotasActivity.this, EditarPrimeraRevisionActivity.class);
                //se mete en un extra del intent, el id
                intent.putExtra(PrimeraRevisionActivity.IDENTIFICADOR_PR, id);
                //inicia la activity
                startActivity(intent);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.editar_detalle_evaluacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_detalleeval:
                guardarDetalle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void guardarDetalle(){
        DetalleEvaluacion aux = new DetalleEvaluacion(detalleEvaluacion.getIdEvaluacionFK(),detalleEvaluacion.getCarnetAlumnoFK());
        Double notaAux = Double.parseDouble(notaEstudiante.getText().toString());
        aux.setNota(notaAux);
        aux.setIdDetalleEv(detalleEvaluacion.getIdDetalleEv());
        detalleEvaluacionViewModel.updateDetalleEvaluacion(aux);
        Toast.makeText(DetalleNotasActivity.this, getText(R.string.inic_notif_eval), Toast.LENGTH_LONG).show();
        finish();
    }
}
