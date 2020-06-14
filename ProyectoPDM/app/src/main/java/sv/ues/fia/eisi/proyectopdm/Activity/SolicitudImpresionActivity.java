package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Adapter.ListaArchivosAdapter;
import sv.ues.fia.eisi.proyectopdm.Adapter.ListaSolicitudesImpresionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EncargadoImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class SolicitudImpresionActivity extends AppCompatActivity {

    public static final String IDENTIFICADOR_IMPRESION = "ID_IMPREISON";
    private SolicitudImpresionViewModel solicitudImpresionViewModel;
    private DocenteViewModel docenteViewModel;
    private EncargadoImpresionViewModel encargadoImpresionViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_impresion);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("SOLICITUD IMPRESIÓN");

        final RecyclerView recyclerSolicitudImpresiones=(RecyclerView)findViewById(R.id.recycler_lista_solicitudes);
        recyclerSolicitudImpresiones.setLayoutManager(new LinearLayoutManager(this));
        //AdapterSolicitudesimpresion
        final ListaSolicitudesImpresionAdapter listaSolicitudesImpresionAdapter=new ListaSolicitudesImpresionAdapter();
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
                    listaSolicitudesImpresionAdapter.setListaSolicitudesImpresion(solicitudImpresions);
                    listaSolicitudesImpresionAdapter.setOnItemClickListener(new ListaSolicitudesImpresionAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(int position, SolicitudImpresion solicitudImpresion) {
                            dialogVerSolicitud(solicitudImpresion).show();
                        }
                    });
                    listaSolicitudesImpresionAdapter.setOnLongClickListener(new ListaSolicitudesImpresionAdapter.OnItemLongClickListener() {
                        @Override
                        public void OnItemLongClick(int position, SolicitudImpresion solicitudImpresion) {
                            //Opciones de AlertDialog
                            createCustomAlertDialog(position,solicitudImpresion).show();
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

    public AlertDialog createCustomAlertDialog(int position, SolicitudImpresion solicitudImpresion){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton eliminar = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton editar = (ImageButton)v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(solicitudImpresion.getCarnetDocenteFK());
        builder.setView(v);
        alertDialog = builder.create();
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idSolicitud=solicitudImpresion.getIdImpresion();
                Intent intent=new Intent(SolicitudImpresionActivity.this,EditarSolicitudImpresionActivity.class);
                intent.putExtra(IDENTIFICADOR_IMPRESION,idSolicitud);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitudImpresionViewModel.delete(solicitudImpresion);
                Toast.makeText(SolicitudImpresionActivity.this, "Solicitud #"+
                        solicitudImpresion.getIdImpresion()+" de "+solicitudImpresion.getCarnetDocenteFK()+
                        " ha sido borrada exitosamente", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    public AlertDialog dialogVerSolicitud(SolicitudImpresion solicitudImpresion){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.ver_solicitud_impresion, null);
        TextView carnetDocente = (TextView) v.findViewById(R.id.textCarnetDocenteEditar);
        TextView docDirector = (TextView) v.findViewById(R.id.textDocDirectorEditar);
        TextView encImpres = (TextView) v.findViewById(R.id.textEncImpresEditar);
        TextView textImpresiones = (TextView) v.findViewById(R.id.text_impresiones_editar);
        TextView textAnexos = (TextView) v.findViewById(R.id.text_anexos_editar);
        TextView textDetallesImpresion = (TextView) v.findViewById(R.id.text_detalleImpresion_editar);
        //RecyclerView
        RecyclerView recyclerArchivos = (RecyclerView) v.findViewById(R.id.recycler_archivos_ver);
        builder.setView(v);
        alertDialog = builder.create();
        ArrayList<String> listaDocumentos = new ArrayList<>();
        Uri uriSeleccionado=Uri.fromFile(new File(solicitudImpresion.getDocumento()));
        listaDocumentos.add(solicitudImpresion.getDocumento());
        recyclerArchivos.setLayoutManager(new LinearLayoutManager(this));
        ListaArchivosAdapter listaArchivosAdapter = new ListaArchivosAdapter(listaDocumentos);
        recyclerArchivos.setAdapter(listaArchivosAdapter);
        listaArchivosAdapter.setOnItemClickListener(new ListaArchivosAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, String documento) {
                //Previsualizar
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setDataAndType(uriSeleccionado, "application/pdf");
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        //Asignacion de parametros a vistas
        //Obtenemos el nombre y apellido del docente con el carnetDocenteFK
        docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(DocenteViewModel.class);
        try {
            String docenteDirector=docenteViewModel.getDocente(solicitudImpresion.getDocDirector()).getNomDocente()+" "+
                    docenteViewModel.getDocente(solicitudImpresion.getDocDirector()).getApellidoDocente();
            docDirector.setText(docenteDirector);
            String docente=docenteViewModel.getDocente(solicitudImpresion.getCarnetDocenteFK()).getNomDocente()+" "+
                    docenteViewModel.getDocente(solicitudImpresion.getCarnetDocenteFK()).getApellidoDocente();
            carnetDocente.setText(docente);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        //Obtenemos el nombre del encargado de impresión con el idEncargado
        encargadoImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(EncargadoImpresionViewModel.class);
        try {
            String encargadoImpresion=encargadoImpresionViewModel.ObtenerEncargadoImpresion(solicitudImpresion.getIdEncargadoFK()).getNomEncargado();
            encImpres.setText(encargadoImpresion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        textImpresiones.setText(Integer.toString(solicitudImpresion.getNumImpresiones()));
        String[] splitAnexos=solicitudImpresion.getDetalleImpresion().split("\n");
        textAnexos.setText(splitAnexos[0]);
        textDetallesImpresion.setText(splitAnexos[1]);
        return alertDialog;
    }
}
