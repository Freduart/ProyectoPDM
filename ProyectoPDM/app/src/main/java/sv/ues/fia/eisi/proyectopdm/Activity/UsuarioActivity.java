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

import sv.ues.fia.eisi.proyectopdm.Adapter.UsuarioAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.UsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;

public class UsuarioActivity extends AppCompatActivity {

    public static final String ID_USUARIO_SELEC="Id_Usuario";
    FloatingActionButton nuevoUsuario;
    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("USUARIOS");

        nuevoUsuario=(FloatingActionButton)findViewById(R.id.nuevoUsuario);
        nuevoUsuario.setVisibility(View.GONE);

        usuarioViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(UsuarioViewModel.class);

        final RecyclerView recyclerUsuarios=(RecyclerView)findViewById(R.id.recycler_lista_usuarios);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this));
        final UsuarioAdapter usuarioAdapter=new UsuarioAdapter();
        recyclerUsuarios.setAdapter(usuarioAdapter);

        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UsuarioActivity.this,NuevoUsuarioActivity.class);
                startActivity(intent);
            }
        });

        try {
            usuarioViewModel.getAllUsers().observe(this, new Observer<List<Usuario>>() {
                @Override
                public void onChanged(List<Usuario> usuarios) {
                    usuarioAdapter.setUsuarios(usuarios);
                    usuarioAdapter.setItemLongClickListener(new UsuarioAdapter.OnItemLongClickListener() {
                        @Override
                        public void OnItemLongClick(int position, Usuario usuario) {
                            createCustomAlertDialog(position,usuario).show();
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }
    }

    private AlertDialog createCustomAlertDialog(int position,Usuario usuario){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton eliminar = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton editar = (ImageButton)v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(usuario.getNombreUsuario());
        builder.setView(v);
        alertDialog = builder.create();

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UsuarioActivity.this,EditarUsuarioActivity.class);
                intent.putExtra(ID_USUARIO_SELEC,usuario.getIdUsuario());
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UsuarioActivity.this,"No se permite eliminar usuarios...",Toast.LENGTH_SHORT).show();
            }
        });

        return alertDialog;
    }
}