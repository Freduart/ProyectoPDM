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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Adapter.CargoAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;

public class CargoActivity extends AppCompatActivity {
    public static final String IDENTIFICADOR_CARGO = "ID_CARGO_ACTUAL";
    private CargoViewModel cargoViewModel;
    private AreaAdmViewModel areaAdmViewModel;
    private AccesoUsuarioViewModel accesoUsuarioViewModel;
    private boolean crearCargo,editarCargo,eliminarCargo;
    private int id_usuario,rol_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.AppBarNameCargos);

        final Bundle extras = getIntent().getExtras();
        //verifica que los extra no estén vacíos
        if(extras != null) {
            //recibe id del usuario desde el extra
            id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
            //recibe rol del usuario desde el extra
            rol_usuario = extras.getInt(LoginActivity.USER_ROL);
        }

        accesoUsuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AccesoUsuarioViewModel.class);
        try {
            accesoUsuarioViewModel.obtenerAccesosPorUsuario(id_usuario).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        if(acceso.getIdOpcionFK()==21){
                            crearCargo=true;
                        }
                        if(acceso.getIdOpcionFK()==22){
                            editarCargo=true;
                        }
                        if(acceso.getIdOpcionFK()==23){
                            eliminarCargo=true;
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

        FloatingActionButton btnNuevoCargo = findViewById(R.id.add_cargo_button);
        btnNuevoCargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!crearCargo){
                    Toast.makeText(CargoActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(CargoActivity.this, NuevoCargoActivity.class);
                    startActivity(intent);
                }
            }
        });

        areaAdmViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);

        /*Para enlazar el objeto RecyclerView con la View que se implementa para cargos,
          se debe usar los layout cargo_item y activity_cargo(contiene a cargo_item)
          como la lista de RecyclerView*/
        final RecyclerView recyclerView = findViewById(R.id.recycler_list_cargo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);//Para actualizaciones

        /*Enlace de la View con el Adapter y Modificación de recyclerView con las actualizaciones
          del Adapter*/

        final CargoAdapter adapter = new CargoAdapter();
        recyclerView.setAdapter(adapter);

        try {
            cargoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                    .create(CargoViewModel.class);
            cargoViewModel.getAllCargos().observe(this, new Observer<List<Cargo>>() {
                @Override
                public void onChanged(final List<Cargo> cargos) {
                    //Actualiza el RecyclerView
                    adapter.setCargos(cargos);
                    adapter.setAreaAdmViewModel(areaAdmViewModel);
                }
            });
            //Consultar cargo con click corto
            adapter.setOnItemClickListener(new CargoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Cargo cargo) {
                    int id = cargo.getIdCargo();
                    Intent intent = new Intent(CargoActivity.this, VerCargoActivity.class);
                    intent.putExtra(IDENTIFICADOR_CARGO, id);
                    startActivity(intent);
                }
            });
            //Click largo para mostrar alertdialog con opciones
            adapter.setOnLongClickListner(new CargoAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(Cargo cargo) {
                    try {
                        int id = cargo.getIdCargo();
                        createCustomDialog(cargo).show();
                    }catch (Exception e){
                        Toast.makeText(CargoActivity.this, e.getMessage() + " - " +e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            setTitle(R.string.AppBarNameCargos);

        }catch (Exception e){
            Toast.makeText(CargoActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Para cuando se selecciona un cargo
    public AlertDialog createCustomDialog(final Cargo cargo){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton eliminar = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton editar = (ImageButton)v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(cargo.getNomCargo());
        builder.setView(v);
        alertDialog = builder.create();
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editarCargo){
                    Toast.makeText(CargoActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        int id = cargo.getIdCargo();
                        Intent intent = new Intent(CargoActivity.this, EditarCargoActivity.class);
                        intent.putExtra(IDENTIFICADOR_CARGO, id);
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(CargoActivity.this, e.getMessage() + " " + e.getCause(),Toast.LENGTH_LONG).show();
                    }
                }
                alertDialog.dismiss();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eliminarCargo){
                    Toast.makeText(CargoActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        cargoViewModel.delete(cargo);
                        Toast.makeText(CargoActivity.this, R.string.cargoeliminado,
                                Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(CargoActivity.this, e.getMessage() + " " + e.getCause(),Toast.LENGTH_LONG).show();
                    }
                }
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

}
