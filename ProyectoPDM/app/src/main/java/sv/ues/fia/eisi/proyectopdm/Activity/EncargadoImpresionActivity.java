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

import sv.ues.fia.eisi.proyectopdm.Adapter.EncargadoImpresionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EncargadoImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AccesoUsuario;
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;

public class EncargadoImpresionActivity extends AppCompatActivity {

    public static final String ID_ENCARGADO="id_enc_impres";
    FloatingActionButton añadirEncImpres;
    private EncargadoImpresionViewModel encargadoImpresionViewModel;
    private int id_usuario,rol_usuario;
    private boolean crearEncImpres,editarEncImpres,eliminarEncImpres;
    private AccesoUsuarioViewModel accesoUsuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encargado_impresion);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("ENCARGADO IMPRESIÓN");

        accesoUsuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AccesoUsuarioViewModel.class);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            id_usuario=bundle.getInt(LoginActivity.ID_USUARIO);
            rol_usuario=bundle.getInt(LoginActivity.USER_ROL);
        }
        try {
            accesoUsuarioViewModel.obtenerAccesosPorUsuario(id_usuario).observe(this, new Observer<List<AccesoUsuario>>() {
                @Override
                public void onChanged(List<AccesoUsuario> accesoUsuarios) {
                    for(AccesoUsuario acceso:accesoUsuarios){
                        if(acceso.getIdOpcionFK()==48){
                            crearEncImpres=true;
                        }
                        if(acceso.getIdOpcionFK()==49){
                            editarEncImpres=true;
                        }
                        if(acceso.getIdOpcionFK()==50){
                            eliminarEncImpres=true;
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

        añadirEncImpres=(FloatingActionButton)findViewById(R.id.nuevoEncImpres);
        final RecyclerView recyclerEncImpres=(RecyclerView)findViewById(R.id.recycler_lista_enc_impres);
        recyclerEncImpres.setLayoutManager(new LinearLayoutManager(this));
        final EncargadoImpresionAdapter encargadoImpresionAdapter=new EncargadoImpresionAdapter();
        recyclerEncImpres.setAdapter(encargadoImpresionAdapter);

        añadirEncImpres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!crearEncImpres){
                    Toast.makeText(EncargadoImpresionActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(EncargadoImpresionActivity.this,NuevoEncargadoImpresionActivity.class);
                    startActivity(intent);
                }
            }
        });

        try {
            encargadoImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EncargadoImpresionViewModel.class);
            encargadoImpresionViewModel.getAllEncargadoImpresion().observe(this, new Observer<List<EncargadoImpresion>>() {
                @Override
                public void onChanged(List<EncargadoImpresion> encargadoImpresions) {
                    encargadoImpresionAdapter.setListEncImpres(encargadoImpresions);
                    encargadoImpresionAdapter.setItemClickListener(new EncargadoImpresionAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(EncargadoImpresion encargadoImpresion) {
                            verEncImpresDialog(encargadoImpresion).show();
                        }
                    });
                    encargadoImpresionAdapter.setItemLongCLickListener(new EncargadoImpresionAdapter.OnItemLongClickListener() {
                        @Override
                        public void OnItemLongClick(EncargadoImpresion encargadoImpresion) {
                            createCustomAlertDialog(encargadoImpresion).show();
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomAlertDialog(EncargadoImpresion encargadoImpresion){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton eliminar = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton editar = (ImageButton)v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(encargadoImpresion.getNomEncargado());
        builder.setView(v);
        alertDialog = builder.create();

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editarEncImpres){
                    Toast.makeText(EncargadoImpresionActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(EncargadoImpresionActivity.this,EditarEncargadoImpresionActivity.class);
                    intent.putExtra(ID_ENCARGADO,encargadoImpresion.getIdEncargadoImpresion());
                    startActivity(intent);
                }
                alertDialog.dismiss();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eliminarEncImpres){
                    Toast.makeText(EncargadoImpresionActivity.this,"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }else{
                    encargadoImpresionViewModel.delete(encargadoImpresion);
                    Toast.makeText(EncargadoImpresionActivity.this, "Encargado: "+
                            encargadoImpresion.getIdEncargadoImpresion()+" ha sido eliminado exitosamente.", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

    public AlertDialog verEncImpresDialog(EncargadoImpresion encargadoImpresion){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.ver_enc_impres, null);
        TextView textIdEnc=(TextView)v.findViewById(R.id.texIdEncVer);
        TextView textNomEnc=(TextView)v.findViewById(R.id.textNomEncVer);
        textIdEnc.setText(Integer.toString(encargadoImpresion.getIdEncargadoImpresion()));
        textNomEnc.setText(encargadoImpresion.getNomEncargado());
        builder.setView(v);
        alertDialog = builder.create();
        return alertDialog;
    }

}
