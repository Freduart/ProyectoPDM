package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.ListaSolicitudesImpresionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class SolicitudImpresionActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    private SolicitudImpresionViewModel solicitudImpresionViewModel;
    private ListaSolicitudesImpresionAdapter listaSolicitudesImpresionAdapter;
    private RecyclerView recyclerSolicitudImpresiones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_impresion);

        recyclerSolicitudImpresiones=(RecyclerView)findViewById(R.id.recycler_lista_solicitudes);
        recyclerSolicitudImpresiones.setLayoutManager(new LinearLayoutManager(this));
        recyclerSolicitudImpresiones.setHasFixedSize(true);
        //AdapterSolicitudesimpresion
        listaSolicitudesImpresionAdapter=new ListaSolicitudesImpresionAdapter();
        recyclerSolicitudImpresiones.setAdapter(listaSolicitudesImpresionAdapter);

        FloatingActionButton nuevaSolicitud=(FloatingActionButton)findViewById(R.id.nuevaSolicitudImpresion);
        nuevaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaSolicitud=new Intent(getApplicationContext(), NuevaSolicitudImpresionActivity.class);
                startActivity(nuevaSolicitud);
            }
        });
/*
        try{
            solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudImpresionViewModel.class);
            solicitudImpresionViewModel.getAllSolicitudesImpresion().observe(this, new Observer<List<SolicitudImpresion>>() {
                @Override
                public void onChanged(final List<SolicitudImpresion> solicitudImpresions) {
                    listaSolicitudesImpresionAdapter.setListaSolicitudesImpresion(solicitudImpresions);
                    listaSolicitudesImpresionAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //custom alertDialog...
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }*/

        /*ListaSolicitudesImpresion listaSolicitudesImpresion=new ListaSolicitudesImpresion();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contenedor_solicitudes_impresion,listaSolicitudesImpresion,"listaSolicitudesImpresion");
        fragmentTransaction.commit();*/
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }*/
}
