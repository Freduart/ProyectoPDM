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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Adapter.CicloAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CicloViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;

public class CicloActivity extends AppCompatActivity {
    public static final String IDENTIFICADOR_CICLO = "ID_CICLO_ACTUAL";

    private CicloViewModel CicloVM;
    private int cod;
    private int id_usuario, rol_usuario;
    private boolean crearCiclo,editarCiclo,eliminarCiclo;
    private AccesoUsuarioViewModel accesoUsuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciclo);

        accesoUsuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AccesoUsuarioViewModel.class);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            //recibe id del usuario desde el extra
            id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
            //recibe rol del usuario desde el extra
            rol_usuario = extras.getInt(LoginActivity.USER_ROL);
        }
        try {
            accesoUsuarioViewModel.obtenerAccesosPorUsuario(id_usuario).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        if(acceso.getIdOpcionFK()==36){
                            crearCiclo=true;
                        }
                        if(acceso.getIdOpcionFK()==37){
                            editarCiclo=true;
                        }
                        if(acceso.getIdOpcionFK()==38){
                            eliminarCiclo=true;
                        }
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        //Título personalizado para Activity
        setTitle("Ciclos Académicos");

        //Para Agregar Ciclo: Inicializa botón flotante de acción
        FloatingActionButton botonNuevoCiclo = findViewById(R.id.add_ciclo_button);
        //al hacer un clic corto
        botonNuevoCiclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!crearCiclo){
                    Toast.makeText(CicloActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    //intent hacia nuevo ciclo activity
                    Intent intent = new Intent(CicloActivity.this, NuevoCicloActivity.class);
                    intent.putExtra(LoginActivity.ID_USUARIO, id_usuario);
                    intent.putExtra(LoginActivity.USER_ROL, rol_usuario);
                    //iniciar activity
                    startActivity(intent);
                }
            }
        });

        //Incializando RecyclerView
        final RecyclerView CicloRecycler = findViewById(R.id.recycler_ciclo_view);
        //Asignando Layout
        CicloRecycler.setLayoutManager(new LinearLayoutManager(this));
        //Asignando Tamaño al RecyclerView
        CicloRecycler.setHasFixedSize(true);

        //Inicializando Adaptador para RecyclerView
        final CicloAdapter adaptador = new CicloAdapter();
        //Asignando adaptador
        CicloRecycler.setAdapter(adaptador);

        try {
            //Inicializando ViewModel
            CicloVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CicloViewModel.class);

            switch(rol_usuario){
                case 5:
                default:
                    //Obtiene Lista de ciclos en un LiveData
                    CicloVM.getAllCiclos().observe(this, new Observer<List<Ciclo>>() {
                        @Override
                        public void onChanged(final List<Ciclo> ciclos) {
                            //Asigan ciclos extraídos al adaptador
                            adaptador.setCiclos(ciclos);
                        }
                    });


                    //Consultar Ciclo con click corto
                    adaptador.setOnItemClickListener(new CicloAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Ciclo ciclo) {
                            cod = ciclo.getIdCiclo();
                            Intent intent = new Intent(CicloActivity.this, VerCicloActivity.class);
                            intent.putExtra(IDENTIFICADOR_CICLO, cod);
                            startActivity(intent);
                        }
                    });
                    //Click largo para mostrar alertdialog con opciones
                    adaptador.setOnLongClickListner(new CicloAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(Ciclo ciclo) {
                            try {
                                cod = ciclo.getIdCiclo();
                                createCustomDialog(ciclo).show();
                            }catch (Exception e){
                                Toast.makeText(CicloActivity.this, e.getMessage() + " - " +e.getCause(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                break;
            }
        }catch (Exception e){
            Toast.makeText(CicloActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomDialog(final Ciclo ciclo){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton del = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton edit = (ImageButton) v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(String.valueOf(ciclo.getIdCiclo()));
        builder.setView(v);
        alertDialog = builder.create();

        //Botón del: Elimina el Ciclo seleccionado
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eliminarCiclo){
                    Toast.makeText(CicloActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        CicloVM.deleteCiclo(ciclo);
                        Toast.makeText(CicloActivity.this, "Ciclo" + " " + ciclo.getIdCiclo() + " ha sido borrado exitosamente", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(CicloActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                    }
                }
                alertDialog.dismiss();
            }
        });

        //Botón edit: Redirige a EditarCicloActivity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editarCiclo){
                    Toast.makeText(CicloActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        int id = ciclo.getIdCiclo();
                        Intent intent = new Intent(CicloActivity.this, EditarCicloActivity.class);
                        intent.putExtra(IDENTIFICADOR_CICLO, id);
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(CicloActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                    }
                }
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

}
