package sv.ues.fia.eisi.proyectopdm.Activity;

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
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;

public class SolicitudExtraordinarioActivity extends AppCompatActivity {
    public static final String IDENTIFICADOR_SOLI_EXTRA = "ID_SOLI_EXTRA_ACTUAL";

    private SolicitudExtraordinarioViewModel soliExtraVM;
    SolicitudExtraordinario soliExtraActual;
    int cod;
    private int id_usuario, rol_usuario;
    private boolean crearSoliExtr,editarSoliExtr,eliminarSoliExtr;
    private AccesoUsuarioViewModel accesoUsuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_solicitud_extraordinario);

            //Título personalizado para Activity
            setTitle("Solicitudes de Evaluación Extraordinaria");

            accesoUsuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AccesoUsuarioViewModel.class);

            final Bundle extras = getIntent().getExtras();
            //verifica que los extra no estén vacíos
            if(extras != null){
                //recibe id del usuario desde el extra
                id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
                //recibe rol del usuario desde el extra
                rol_usuario = extras.getInt(LoginActivity.USER_ROL);
            }
            accesoUsuarioViewModel.obtenerAccesosPorUsuario(id_usuario).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        if(acceso.getIdOpcionFK()==42){
                            crearSoliExtr=true;
                        }
                        if(acceso.getIdOpcionFK()==43){
                            editarSoliExtr=true;
                        }
                        if(acceso.getIdOpcionFK()==44){
                            eliminarSoliExtr=true;
                        }
                    }
                }
            });

            //Para Agregar Solicitud Extraordinaria: Inicializa botón flotante de acción
            FloatingActionButton botonNuevaSoliExtra = findViewById(R.id.add_soliExtra_button);
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
            //Inicializando ViewModel
            soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);

            switch (rol_usuario){
                case 1:
                case 2:
                    botonNuevaSoliExtra.setVisibility(View.GONE);

                    soliExtraVM.obtenerSolicitudesDocente(soliExtraVM.getDocenteConUsuario(id_usuario).getCarnetDocente()).observe(this, new Observer<List<SolicitudExtraordinario>>() {
                        @Override
                        public void onChanged(final List<SolicitudExtraordinario> solicitudExtraordinarios) {
                            adaptador.setSolicitudesExtra(solicitudExtraordinarios);
                        }
                    });

                    //Consultar Ciclo con click corto
                    adaptador.setOnItemClickListener(new SolicitudExtraordinarioAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(SolicitudExtraordinario solicitudExtraordinario) {
                            cod = solicitudExtraordinario.getIdSolicitud();
                            Intent intent = new Intent(SolicitudExtraordinarioActivity.this, VerSolicitudExtraordinarioActivity.class);
                            intent.putExtra(IDENTIFICADOR_SOLI_EXTRA, cod);
                            intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                            intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                            startActivity(intent);
                        }
                    });
                    //Click largo para mostrar alertdialog con opciones
                    adaptador.setOnLongClickListner(new SolicitudExtraordinarioAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(SolicitudExtraordinario solicitudExtraordinario) {
                            try {
                                cod = solicitudExtraordinario.getIdSolicitud();
                                createCustomDialog(solicitudExtraordinario).show();
                            }catch (Exception e){
                                Toast.makeText(SolicitudExtraordinarioActivity.this, e.getMessage() + " - " +e.getCause(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                break;

                case 3:
                    //al hacer un clic corto
                    botonNuevaSoliExtra.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!crearSoliExtr){
                                Toast.makeText(SolicitudExtraordinarioActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                            }else{
                                //intent hacia NuevaSoliExtraActivity
                                Intent intent = new Intent(SolicitudExtraordinarioActivity.this, NuevaSolicitudExtraordinarioActivity.class);
                                //iniciar activity
                                startActivity(intent);
                            }
                        }
                    });

                    soliExtraVM.obtenerSolicitudesAlumno(soliExtraVM.getAlumnConUsuario(id_usuario).getCarnetAlumno()).observe(this, new Observer<List<SolicitudExtraordinario>>() {
                        @Override
                        public void onChanged(final List<SolicitudExtraordinario> solicitudExtraordinarios) {
                            adaptador.setSolicitudesExtra(solicitudExtraordinarios);
                        }
                    });

                    //Consultar Ciclo con click corto
                    adaptador.setOnItemClickListener(new SolicitudExtraordinarioAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(SolicitudExtraordinario solicitudExtraordinario) {
                            cod = solicitudExtraordinario.getIdSolicitud();
                            Intent intent = new Intent(SolicitudExtraordinarioActivity.this, VerSolicitudExtraordinarioActivity.class);
                            intent.putExtra(IDENTIFICADOR_SOLI_EXTRA, cod);
                            intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                            intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                            startActivity(intent);
                        }
                    });
                break;

                case 5:
                default:
                    botonNuevaSoliExtra.setVisibility(View.GONE);

                    soliExtraVM.getAllSolicitudesExtraordinario().observe(this, new Observer<List<SolicitudExtraordinario>>() {
                        @Override
                        public void onChanged(final List<SolicitudExtraordinario> solicitudExtraordinarios) {
                            adaptador.setSolicitudesExtra(solicitudExtraordinarios);
                        }
                    });

                    //Consultar Ciclo con click corto
                    adaptador.setOnItemClickListener(new SolicitudExtraordinarioAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(SolicitudExtraordinario solicitudExtraordinario) {
                            cod = solicitudExtraordinario.getIdSolicitud();
                            Intent intent = new Intent(SolicitudExtraordinarioActivity.this, VerSolicitudExtraordinarioActivity.class);
                            intent.putExtra(IDENTIFICADOR_SOLI_EXTRA, cod);
                            startActivity(intent);
                        }
                    });
                    //Click largo para mostrar alertdialog con opciones
                    adaptador.setOnLongClickListner(new SolicitudExtraordinarioAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(SolicitudExtraordinario solicitudExtraordinario) {
                            try {
                                cod = solicitudExtraordinario.getIdSolicitud();
                                createCustomDialog(solicitudExtraordinario).show();
                            }catch (Exception e){
                                Toast.makeText(SolicitudExtraordinarioActivity.this, e.getMessage() + " - " +e.getCause(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                break;
            }

        }catch (Exception e){
            Toast.makeText(SolicitudExtraordinarioActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomDialog(SolicitudExtraordinario solicitudExtraordinario){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton del = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton edit = (ImageButton) v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(String.valueOf(cod));
        builder.setView(v);
        alertDialog = builder.create();

        //Botón del: Elimina la Solicitud Extraordinaria seleccionado
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eliminarSoliExtr){
                    Toast.makeText(SolicitudExtraordinarioActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        soliExtraVM.delete(soliExtraActual);
                        Toast.makeText(SolicitudExtraordinarioActivity.this, "Local" + " " + String.valueOf(soliExtraActual.getIdSolicitud()) + " ha sido borrado exitosamente", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(SolicitudExtraordinarioActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                    }
                }
                alertDialog.dismiss();
            }
        });

        //Botón edit: Redirige a EditarSolicitudExtraordinario
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editarSoliExtr){
                    Toast.makeText(SolicitudExtraordinarioActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        Intent intent = new Intent(SolicitudExtraordinarioActivity.this, EditarSolicitudExtraordinarioActivity.class);
                        intent.putExtra(IDENTIFICADOR_SOLI_EXTRA, cod);
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(SolicitudExtraordinarioActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                    }
                }
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
}
