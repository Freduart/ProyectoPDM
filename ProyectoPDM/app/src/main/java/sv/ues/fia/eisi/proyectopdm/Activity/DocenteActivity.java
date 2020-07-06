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

import sv.ues.fia.eisi.proyectopdm.Adapter.DocenteAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;

public class DocenteActivity extends AppCompatActivity {

    public static final String CARNET_DOCENTE="CarnetDocente";
    FloatingActionButton añadirDocente;
    private DocenteViewModel docenteViewModel;
    private CargoViewModel cargoViewModel;
    private AreaAdmViewModel areaAdmViewModel;
    private int id_usuario,rol_usuario;
    private boolean crearDocente,editarDocente,eliminarDocente;
    private AccesoUsuarioViewModel accesoUsuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("DOCENTES");

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            id_usuario=bundle.getInt(LoginActivity.ID_USUARIO);
            rol_usuario=bundle.getInt(LoginActivity.USER_ROL);
        }
        accesoUsuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AccesoUsuarioViewModel.class);
        cargoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CargoViewModel.class);
        docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
        añadirDocente=(FloatingActionButton)findViewById(R.id.nuevoDocente);

        try {
            accesoUsuarioViewModel.obtenerAccesosPorUsuario(id_usuario).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        if(acceso.getIdOpcionFK()==45){
                            crearDocente=true;
                        }
                        if(acceso.getIdOpcionFK()==46){
                            editarDocente=true;
                        }
                        if(acceso.getIdOpcionFK()==47){
                            eliminarDocente=true;
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

        areaAdmViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);

        final RecyclerView recyclerDocentes=(RecyclerView)findViewById(R.id.recycler_lista_docentes);
        recyclerDocentes.setLayoutManager(new LinearLayoutManager(this));
        final DocenteAdapter docenteAdapter=new DocenteAdapter();
        recyclerDocentes.setAdapter(docenteAdapter);

        añadirDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!crearDocente){
                    Toast.makeText(DocenteActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(DocenteActivity.this,NuevoDocenteActivity.class);
                    startActivity(intent);
                }
            }
        });

        try {
            docenteViewModel.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
                @Override
                public void onChanged(List<Docente> docentes) {
                    docenteAdapter.setListDocentes(docentes);
                    docenteAdapter.setCargoViewModel(cargoViewModel);
                    docenteAdapter.setAreaAdmViewModel(areaAdmViewModel);
                    docenteAdapter.setOnItemClickListener(new DocenteAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(int position, Docente docente) {
                            verDocenteAlertDialog(docente).show();
                        }
                    });
                    docenteAdapter.setOnItemLongCLickListener(new DocenteAdapter.OnItemLongCLickListener() {
                        @Override
                        public void OnItemLongClick(int position, Docente docente) {
                            createCustomAlertDialog(position,docente).show();
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomAlertDialog(int position,Docente docente){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton eliminar = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton editar = (ImageButton)v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(docente.getNomDocente()+" "+docente.getApellidoDocente());
        builder.setView(v);
        alertDialog = builder.create();
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editarDocente){
                    Toast.makeText(DocenteActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    String carnetDocente=docente.getCarnetDocente();
                    Intent intent=new Intent(DocenteActivity.this,EditarDocenteActivity.class);
                    intent.putExtra(CARNET_DOCENTE,carnetDocente);
                    startActivity(intent);
                }
                alertDialog.dismiss();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eliminarDocente){
                    Toast.makeText(DocenteActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    docenteViewModel.deleteDocente(docente);
                    Toast.makeText(DocenteActivity.this, "Docente "+docente.getNomDocente()+
                            " ha sido eliminado exitosamente.", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    public AlertDialog verDocenteAlertDialog(Docente docente){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.ver_docente, null);
        TextView textCarnetDocente=(TextView)v.findViewById(R.id.textCarnetDocenteVer);
        TextView textNomDocente=(TextView)v.findViewById(R.id.textNomDocenteVer);
        TextView textApellidoDocente=(TextView)v.findViewById(R.id.textApellidoDocenteVer);
        TextView textCargoDocente=(TextView)v.findViewById(R.id.textCargosVer);
        TextView textCorreoDocente=(TextView)v.findViewById(R.id.textNomEncVer);
        TextView textTelefonoDocente=(TextView)v.findViewById(R.id.textTelefonoDocenteVer);
        builder.setView(v);
        alertDialog = builder.create();

        textCarnetDocente.setText(docente.getCarnetDocente());
        textNomDocente.setText(docente.getNomDocente());
        textApellidoDocente.setText(docente.getApellidoDocente());
        textCorreoDocente.setText(docente.getCorreoDocente());
        textTelefonoDocente.setText(docente.getTelefonoDocente());
        cargoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CargoViewModel.class);
        try {
            String cargoDocente=cargoViewModel.getCargo(docente.getIdCargoFK()).getNomCargo()+" "+areaAdmViewModel.getAreaAdm(
                    cargoViewModel.getCargo(docente.getIdCargoFK()).getIdAreaAdminFK()).getNomDepartamento();
            textCargoDocente.setText(cargoDocente);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return alertDialog;
    }

}
