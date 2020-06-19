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

import sv.ues.fia.eisi.proyectopdm.Adapter.LocalAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class LocalActivity extends AppCompatActivity {
    public static final String IDENTIFICADOR_LOCAL = "ID_LOCAL_ACTUAL";

    private LocalViewModel LocalVM;
    String cod;
    private int id_usuario, rol_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            //recibe id del usuario desde el extra
            id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
            //recibe rol del usuario desde el extra
            rol_usuario = extras.getInt(LoginActivity.USER_ROL);
        }

        //Título personalizado para Activity
        setTitle("Lista de Locales");

        //Para Agregar Local: Inicializa botón flotante de acción
        FloatingActionButton botonNuevoLocal = findViewById(R.id.add_local_button);

        //al hacer un clic corto
        botonNuevoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent hacia nuevo Local activity
                Intent intent = new Intent(LocalActivity.this, NuevoLocalActivity.class);
                //iniciar activity
                startActivity(intent);
            }
        });

        //Inicializando RecyclerView
        final RecyclerView LocalRecycler = findViewById(R.id.recycler_local_view);
        //Asignando Layout
        LocalRecycler.setLayoutManager(new LinearLayoutManager(this));
        //Asignando tamaño al RecyclerView
        LocalRecycler.setHasFixedSize(true);

        //Inicializando adaptador para RecyclerView
        final LocalAdapter adaptador = new LocalAdapter();
        //Asignando adaptador
        LocalRecycler.setAdapter(adaptador);

        try{
            //Inicializando ViewModel
            LocalVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);

            switch (rol_usuario){
                case 5:
                default:
                    //Obtiene la lista de Locales en un LiveData
                    LocalVM.getAllLocales().observe(this, new Observer<List<Local>>() {
                        @Override
                        public void onChanged(final List<Local> locals) {
                            //Asigna los locales extraídos al adaptador
                            adaptador.setLocales(locals);
                        }
                    });

                    //Consultar Local con click corto
                    adaptador.setOnItemClickListener(new LocalAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Local local) {
                            cod = local.getIdLocal();
                            Intent intent = new Intent(LocalActivity.this, VerLocalActivity.class);
                            intent.putExtra(IDENTIFICADOR_LOCAL, cod);
                            startActivity(intent);
                        }
                    });
                    //Click largo para mostrar alertdialog con opciones
                    adaptador.setOnLongClickListner(new LocalAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(Local local) {
                            try {
                                cod = local.getIdLocal();
                                createCustomDialog(local).show();
                            }catch (Exception e){
                                Toast.makeText(LocalActivity.this, e.getMessage() + " - " +e.getCause(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                break;
            }
        }catch(Exception e){
            Toast.makeText(LocalActivity.this, "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog createCustomDialog(final Local local){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton del = (ImageButton) v.findViewById(R.id.imBEliminar);
        ImageButton edit = (ImageButton) v.findViewById(R.id.imBEditar);
        TextView tv = (TextView) v.findViewById(R.id.tituloAlert);
        tv.setText(local.getIdLocal());
        builder.setView(v);
        alertDialog=builder.create();

        //Botón edit: Redirige a EditarLocalActivity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cod = local.getIdLocal();
                    Intent intent = new Intent(LocalActivity.this, EditarLocalActivity.class);
                    intent.putExtra(IDENTIFICADOR_LOCAL, cod);
                    startActivity(intent);
                    alertDialog.dismiss();
                }catch(Exception e){
                    Toast.makeText(LocalActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botón del: Elimina el Local seleccionado
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LocalVM.deleteLocal(local);
                    Toast.makeText(LocalActivity.this, "Local" + " " + local.getIdLocal() + " ha sido borrado exitosamente", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(LocalActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return alertDialog;
    }
}
