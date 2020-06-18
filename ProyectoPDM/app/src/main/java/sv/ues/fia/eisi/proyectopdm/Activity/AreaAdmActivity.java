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

import sv.ues.fia.eisi.proyectopdm.Adapter.AreaAdmAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;

public class AreaAdmActivity extends AppCompatActivity {
    public static final int AÑADIR_AREA = 1;
    public static final int EDITAR_AREA = 2;
    public static final String OPERACION_AREA = "Operacion_AE_AreaAdm";
    public static final String IDENTIFICADOR_AREA = "ID_AreaAdm_Actual";

    private AreaAdmViewModel AreaAdmVM;
    private String identificador;
    private int id_usuario, rol_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_area_adm);
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                //recibe id del usuario desde el extra
                id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
                //recibe rol del usuario desde el extra
                rol_usuario = extras.getInt(LoginActivity.USER_ROL);
            }
            //--nueva areaAdm
            //inicializa boton flotante de acción
            FloatingActionButton botonNuevaAreaAdm = findViewById(R.id.add_areaa_button);
            //al hacer un clic corto
            botonNuevaAreaAdm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent hacia nueva areaAdm activity
                    Intent intent = new Intent(AreaAdmActivity.this, NuevaEditarAreaAdmActivity.class);
                    //añadir extra que definirá si añade o edita
                    intent.putExtra(OPERACION_AREA, AÑADIR_AREA);
                    intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                    intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                    //iniciar activity
                    startActivity(intent);
                }
            });

            //--lista de areaAdmes
            //inicializa recycler view
            RecyclerView AreaRecycler = findViewById(R.id.recycler_areaa_view);
            //set layout manager
            AreaRecycler.setLayoutManager(new LinearLayoutManager(this));
            //recycler tiene tamaño fijo = true
            AreaRecycler.setHasFixedSize(true);
            //adaptador para recycler
            final AreaAdmAdapter adaptador = new AreaAdmAdapter();
            //linkea adaptador en el recycler
            AreaRecycler.setAdapter(adaptador);
            //inicializa viewmodel de areaAdm
            AreaAdmVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);
            //obtiene todas las areaAdmes en un livedata
            switch(rol_usuario) {
                case 1:
                case 2:
                    EvaluacionViewModel evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
                    AreaAdmVM.getAreaAdmDocentes(evaluacionViewModel.getDocenteConUsuario(id_usuario).getCarnetDocente()).observe(this, new Observer<List<AreaAdm>>() {
                        @Override
                        public void onChanged(List<AreaAdm> areaAdms) {
                            //mete las areaAdmes en el adaptador
                            adaptador.setAreaAdmes(areaAdms);
                        }
                    });
                    break;
                default:
                    AreaAdmVM.getAreaAdmAll().observe(this, new Observer<List<AreaAdm>>() {
                        @Override
                        public void onChanged(List<AreaAdm> areaAdms) {
                            //mete las areaAdmes en el adaptador
                            adaptador.setAreaAdmes(areaAdms);
                        }
                    });
                    break;
            }
            //--consultar areaAdm
            //al hacer clic corto en un objeto del recycler
            adaptador.setOnItemClickListener(new AreaAdmAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(AreaAdm areaAdm) {
                    //guardar id de areaAdm que se tocó
                    int id = areaAdm.getIdDeptarmento();
                    //inicializa intent que dirige hacia el detalle de la areaAdm que se tocó
                    Intent intent = new Intent(AreaAdmActivity.this, VerAreaAdmActivity.class);
                    //se mete en un extra del intent, el id
                    intent.putExtra(IDENTIFICADOR_AREA, id);
                    //inicia la activity
                    startActivity(intent);
                }
            });

            //--eliminar areaAdm
            adaptador.setOnLongClickListener(new AreaAdmAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(AreaAdm areaAdm) {
                    //guardar id de areaAdm que se tocó
                    int id = areaAdm.getIdDeptarmento();
                    createCustomDialog(areaAdm).show();
                }
            });

            //título de la pantalla
            setTitle(getText(R.string.area_adm));
        } catch (Exception e){
            Toast.makeText(this,e.getMessage() ,Toast.LENGTH_LONG).show();
        }
    }

    //Para cuando se selecciona un cargo
    public AlertDialog createCustomDialog(final AreaAdm areaAdm){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton editar = view.findViewById(R.id.imBEditar) ;
        ImageButton eliminar = view.findViewById(R.id.imBEliminar);
        TextView textViewv = view.findViewById(R.id.tituloAlert);
        textViewv.setText(areaAdm.getNomDepartamento());
        builder.setView(view);
        alertDialog = builder.create();
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //guardar id de areaAdm que se tocó
                    int id = areaAdm.getIdDeptarmento();
                    //inicializa intent que dirige hacia el detalle de la areaAdm que se tocó
                    Intent intent = new Intent(AreaAdmActivity.this, NuevaEditarAreaAdmActivity.class);
                    //se mete en un extra del intent, el id
                    intent.putExtra(IDENTIFICADOR_AREA, id);
                    intent.putExtra(OPERACION_AREA, EDITAR_AREA);
                    intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                    intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                    //inicia la activity
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(AreaAdmActivity.this, e.getMessage() + " " + e.getCause(),Toast.LENGTH_LONG).show();
                }
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AreaAdmVM.deleteAreaAdm(areaAdm);
                    Toast.makeText(AreaAdmActivity.this, getText(R.string.inic_notif_aarea) +
                                    areaAdm.getNomDepartamento() + getText(R.string.accion_borrar_notif_eval),
                            Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(AreaAdmActivity.this, e.getMessage() ,Toast.LENGTH_LONG).show();
                }
            }
        });
        return alertDialog;
    }
}