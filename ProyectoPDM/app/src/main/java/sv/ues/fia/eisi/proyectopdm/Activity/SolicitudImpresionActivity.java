package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
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

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.ListaSolicitudesImpresionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class SolicitudImpresionActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    SolicitudImpresionViewModel solicitudImpresionViewModel;
    ListaSolicitudesImpresionAdapter listaSolicitudesImpresionAdapter;
    RecyclerView recyclerSolicitudImpresiones;
    ArrayList<SolicitudImpresion> solicitudImpresion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_impresion);
        solicitudImpresion=new ArrayList<>();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("SOLICITUD IMPRESIÓN");

        recyclerSolicitudImpresiones=(RecyclerView)findViewById(R.id.recycler_lista_solicitudes);
        recyclerSolicitudImpresiones.setLayoutManager(new LinearLayoutManager(this));
        //AdapterSolicitudesimpresion
        listaSolicitudesImpresionAdapter=new ListaSolicitudesImpresionAdapter(solicitudImpresion);
        recyclerSolicitudImpresiones.setAdapter(listaSolicitudesImpresionAdapter);

        FloatingActionButton nuevaSolicitud=(FloatingActionButton)findViewById(R.id.nuevaSolicitudImpresion);
        nuevaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaSolicitud=new Intent(getApplicationContext(), NuevaSolicitudImpresionActivity.class);
                startActivity(nuevaSolicitud);
            }
        });
        try{
            solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudImpresionViewModel.class);
            solicitudImpresionViewModel.getAllSolicitudesImpresion().observe(this, new Observer<List<SolicitudImpresion>>() {
                @Override
                public void onChanged(final List<SolicitudImpresion> solicitudImpresions) {
                    solicitudImpresion.addAll(solicitudImpresions);
                    listaSolicitudesImpresionAdapter.setOnItemClickListener(new ItemClickListenerImpresion() {
                        @Override
                        public void OnItemClick(int position, SolicitudImpresion solicitudImpresion) {

                        }
                    });
                    listaSolicitudesImpresionAdapter.notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }
}
