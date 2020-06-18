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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import sv.ues.fia.eisi.proyectopdm.ViewModel.UsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;

public class SolicitudImpresionActivity extends AppCompatActivity {

    public static final String IDENTIFICADOR_IMPRESION = "ID_IMPREISON";
    private SolicitudImpresionViewModel solicitudImpresionViewModel;
    private DocenteViewModel docenteViewModel;
    private EncargadoImpresionViewModel encargadoImpresionViewModel;
    private UsuarioViewModel usuarioViewModel;
    private Usuario usuarioIngresado;
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
        //Obtenemos los datos del usuario
        String usuario=PreferenceSingleton.getInstance().readPreference(LoginActivity.USERNAME);
        String password=PreferenceSingleton.getInstance().readPreference(LoginActivity.USER_PASSWORD);
        String[] credenciales={usuario,password};
        usuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(UsuarioViewModel.class);
        try {
            usuarioIngresado=usuarioViewModel.obtenerCredenciales(credenciales);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(DocenteViewModel.class);

        FloatingActionButton nuevaSolicitud=(FloatingActionButton)findViewById(R.id.nuevaSolicitudImpresion);
        //Para cualquier otro usuario que no sea docente, se deshabilita la opcion de agregar solicitudes de impresion
        if(usuarioIngresado.getRol()!=2){
            nuevaSolicitud.setVisibility(View.INVISIBLE);
        }
        nuevaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaSolicitud=new Intent(getApplicationContext(), NuevaSolicitudImpresionActivity.class);
                startActivity(nuevaSolicitud);
            }
        });
        try{
            solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudImpresionViewModel.class);
            //Para un usuario tipo encargado impresion mostramos solo las solicitudes aprobadas...
            if(usuarioIngresado.getRol()==4){
                solicitudImpresionViewModel.obtenerSolicitudPorEstado("APROBADA/\nEN CURSO").observe(this, new Observer<List<SolicitudImpresion>>() {
                    @Override
                    public void onChanged(List<SolicitudImpresion> solicitudImpresions) {
                        listaSolicitudesImpresionAdapter.setListaSolicitudesImpresion(solicitudImpresions);
                        listaSolicitudesImpresionAdapter.setDocenteViewModel(docenteViewModel);
                        listaSolicitudesImpresionAdapter.setOnItemClickListener(new ListaSolicitudesImpresionAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(int position, SolicitudImpresion solicitudImpresion) {
                                dialogVerSolicitud(solicitudImpresion).show();
                            }
                        });
                        listaSolicitudesImpresionAdapter.setOnLongClickListener(new ListaSolicitudesImpresionAdapter.OnItemLongClickListener() {
                            @Override
                            public void OnItemLongClick(int position, SolicitudImpresion solicitudImpresion) {
                                procesarSolicitudImpresionAlertDialog(solicitudImpresion).show();
                            }
                        });
                    }
                });
            }else{
                //De lo contrario se veran todas las solicitudes de cualquier tipo para docentes...
                solicitudImpresionViewModel.getAllSolicitudesImpresion().observe(this, new Observer<List<SolicitudImpresion>>() {
                    @Override
                    public void onChanged(final List<SolicitudImpresion> solicitudImpresions) {
                        listaSolicitudesImpresionAdapter.setListaSolicitudesImpresion(solicitudImpresions);
                        listaSolicitudesImpresionAdapter.setDocenteViewModel(docenteViewModel);
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
                                //Si el usuario es un director, le asignamos los botones para aprobar/reprobar la solicitud
                                //En lugar de editar y eliminar
                                if(usuarioIngresado.getRol()==1){
                                    createCustomAlertDialog2(solicitudImpresion).show();
                                }else{
                                    createCustomAlertDialog(solicitudImpresion).show();
                                }
                            }
                        });
                        listaSolicitudesImpresionAdapter.notifyDataSetChanged();
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }
    }
    //AlertDialog para docentes..
    public AlertDialog createCustomAlertDialog(SolicitudImpresion solicitudImpresion){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton eliminar = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton editar = (ImageButton)v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);

        docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(DocenteViewModel.class);
        try {
            String titulo=solicitudImpresion.getCarnetDocenteFK()+"-"+
                    docenteViewModel.getDocente(solicitudImpresion.getCarnetDocenteFK()).getNomDocente()+" "+
                    docenteViewModel.getDocente(solicitudImpresion.getCarnetDocenteFK()).getApellidoDocente();
            tv.setText(titulo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
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
    //AlertDialog para el director..
    public AlertDialog createCustomAlertDialog2(SolicitudImpresion solicitudImpresion){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones_director, null);
        Button btnAprobar=(Button)v.findViewById(R.id.btnAprobar);
        Button btnReprobar=(Button)v.findViewById(R.id.btnReprobar);
        builder.setView(v);
        alertDialog = builder.create();
        solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudImpresionViewModel.class);
        btnAprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitudImpresion.setEstadoSolicitud("APROBADA/\nEN CURSO");
                solicitudImpresionViewModel.update(solicitudImpresion);
                alertDialog.dismiss();
            }
        });
        btnReprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitudImpresion.setEstadoSolicitud("REPROBADA");
                solicitudImpresionViewModel.update(solicitudImpresion);
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
    //AlertDialog para ver las solicitud de impresion seleccionada
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
    //AlertDialog para encargado de impresion
    public AlertDialog procesarSolicitudImpresionAlertDialog(SolicitudImpresion solicitudImpresion){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.opciones_usuario, null);
        Button btnProcesar=(Button)v.findViewById(R.id.btnCerrarSesion);
        btnProcesar.setText("PROCESAR SOLICITUD");
        builder.setView(v);
        alertDialog = builder.create();
        solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudImpresionViewModel.class);
        btnProcesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SolicitudImpresionActivity.this,ProcesarSolicitudImpresionActivity.class);
                intent.putExtra(IDENTIFICADOR_IMPRESION,solicitudImpresion.getIdImpresion());
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
}
