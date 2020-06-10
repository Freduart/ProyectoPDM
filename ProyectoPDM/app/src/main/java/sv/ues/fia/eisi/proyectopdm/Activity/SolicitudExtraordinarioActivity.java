package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.SolicitudExtraordinarioAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;

public class SolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinarioViewModel soliExtraVM;
    SolicitudExtraordinario soliExtraActual;
    String cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_extraordinario);

        //Inicializa variable de Barra de Acción (Barra Superior)
        ActionBar actionBar = getSupportActionBar();
        //Título personalizado para Activity
        actionBar.setTitle("Solicitudes de Evaluación Extraordinaria");

        //Para Agregar Solicitud Extraordinaria: Inicializa botón flotante de acción
        FloatingActionButton botonNuevaSoliExtra = findViewById(R.id.add_soliExtra_button);
        //al hacer un clic corto
        botonNuevaSoliExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent hacia NuevaSoliExtraActivity
                Intent intent = new Intent(SolicitudExtraordinarioActivity.this, NuevaSolicitudExtraordinarioActivity.class);
                //iniciar activity
                startActivity(intent);
            }
        });

        //Inicializando RecyclerView
        final RecyclerView SoliExtraRecycler = findViewById(R.id.recycler_soliExtra_view);
        //Asignando Layout
        SoliExtraRecycler.setLayoutManager(new LinearLayoutManager(this));
        //Asignando Tamaño al RecyclerView
        SoliExtraRecycler.setHasFixedSize(true);

        //Inicializando Adaptador para RecyclerView
        final SolicitudExtraordinarioAdapter adaptador = new SolicitudExtraordinarioAdapter();
        //Asignando Adaptador
        SoliExtraRecycler.setAdapter(adaptador);

        try {
            //Inicializando ViewModel
            soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);
            soliExtraVM.getAllSolicitudesExtraordinario().observe(this, new Observer<List<SolicitudExtraordinario>>() {
                @Override
                public void onChanged(final List<SolicitudExtraordinario> solicitudExtraordinarios) {
                    adaptador.setSolicitudesExtra(solicitudExtraordinarios);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cod = String.valueOf(solicitudExtraordinarios.get(SoliExtraRecycler.getChildAdapterPosition(v)).getIdSolicitud());
                            soliExtraActual = adaptador.getSoliExtraAt(SoliExtraRecycler.getChildAdapterPosition(v));
                            createCustomDialog().show();
                        }
                    });
                }
            });

        }catch (Exception e){
            Toast.makeText(SolicitudExtraordinarioActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomDialog(){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones_soli_ext, null);
        ImageButton ver = (ImageButton) v.findViewById(R.id.imBVerSoliExt);
        ImageButton del = (ImageButton) v.findViewById(R.id.imBEliminarSoliExt);
        ImageButton edit = (ImageButton) v.findViewById(R.id.imBEditarSoliExt);
        TextView tv = (TextView) v.findViewById(R.id.tvADSoliExt);
        tv.setText(cod);
        builder.setView(v);
        alertDialog = builder.create();

        //Botón ver: Redirige a VerSolicitudExtraordinarioActivity
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = soliExtraActual.getIdEvaluacion();
                    Intent intent = new Intent(SolicitudExtraordinarioActivity.this, VerCicloActivity.class);
                    intent.putExtra("ID Solicitud Extraordinaria Actual", id);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(SolicitudExtraordinarioActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botón del: Elimina la Solicitud Extraordinaria seleccionado
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    soliExtraVM.delete(soliExtraActual);
                    Toast.makeText(SolicitudExtraordinarioActivity.this, "Local" + " " + soliExtraActual.getIdSolicitud() + " ha sido borrado exitosamente", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(SolicitudExtraordinarioActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                }catch (Exception e){
                    Toast.makeText(SolicitudExtraordinarioActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return alertDialog;
    }
}
