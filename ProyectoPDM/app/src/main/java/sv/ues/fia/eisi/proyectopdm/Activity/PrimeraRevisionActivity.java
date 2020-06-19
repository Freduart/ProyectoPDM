package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.PrimeraRevisionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class PrimeraRevisionActivity extends AppCompatActivity {
    public static final String IDENTIFICADOR_PR = "ID_PRIMERA_REVISION_ACTUAL";

    private PrimeraRevisionViewModel primeraRevisionViewModel;
    private List<PrimeraRevision> primeraRevisionDocente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera_revision);
        final Bundle extraidAlUser = getIntent().getExtras();
        setTitle(R.string.AppBarNamePrimerasRevisiones);

        int idUser = 0;
        int rolUser = 0;
        String username = "";
        if(extraidAlUser != null){
            idUser = extraidAlUser.getInt(LoginActivity.ID_USUARIO);
            rolUser = extraidAlUser.getInt(LoginActivity.USER_ROL);
            username = extraidAlUser.getString(LoginActivity.USERNAME);
        }

        FloatingActionButton btnNuevaPR = findViewById(R.id.add_pr_button);

        btnNuevaPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrimeraRevisionActivity.this, NuevaPrimeraRevisionActivity.class);
                try {
                    startActivity(intent);
                }catch (Exception e){
                       Toast.makeText(PrimeraRevisionActivity.this, e.getMessage()+" - "+e.getCause(), Toast.LENGTH_LONG).show();
                }
            }
        });

        if(rolUser == 1 || rolUser == 2 || rolUser == 5){
            btnNuevaPR.setVisibility(View.GONE);
        }

        final RecyclerView recyclerView = findViewById(R.id.recycler_list_primerarevision);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PrimeraRevisionAdapter adapter = new PrimeraRevisionAdapter();
        recyclerView.setAdapter(adapter);

        try {
        primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);

       /*     primeraRevisionViewModel.getAllPrimerasRevisiones().observe(this, new Observer<List<PrimeraRevision>>() {
                @Override
                public void onChanged(List<PrimeraRevision> primeraRevisiones) {
                    adapter.setPrs(primeraRevisiones);
                }
            });*/
            DocenteViewModel docenteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
            primeraRevisionViewModel.obtenerPRDocente(primeraRevisionViewModel.obtenerDocUsuario(idUser).getCarnetDocente()).observe(this, new Observer<List<PrimeraRevision>>() {
                @Override
                public void onChanged(List<PrimeraRevision> primeraRevisions) {
                            adapter.setPrs(primeraRevisions);
                }
            });

            //Consultar primera revision
            adapter.setOnItemClickListener(new PrimeraRevisionAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PrimeraRevision primeraRevision) {
                    int id = primeraRevision.getIdPrimerRevision();
                    Intent intent = new Intent(PrimeraRevisionActivity.this, VerPrimeraRevisionActivity.class);
                    intent.putExtra(IDENTIFICADOR_PR, id);
                    startActivity(intent);
                }
            });
            adapter.setOnLongClickListener(new PrimeraRevisionAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(PrimeraRevision primeraRevision) {
                    createCustomDialog(primeraRevision).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(PrimeraRevisionActivity.this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }
    }
    //AlertDialog cuando se selecciona un item
    public AlertDialog createCustomDialog(final PrimeraRevision primeraRevision){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton editar = (ImageButton) view.findViewById(R.id.imBEditar);
        ImageButton eliminar = (ImageButton) view.findViewById(R.id.imBEliminar);
        TextView tv = (TextView) view.findViewById(R.id.tituloAlert);
        tv.setText(String.valueOf(primeraRevision.getIdPrimerRevision()));
        builder.setView(view);
        alertDialog = builder.create();
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = primeraRevision.getIdPrimerRevision();
                    Intent intent = new Intent(PrimeraRevisionActivity.this, EditarPrimeraRevisionActivity.class);
                    intent.putExtra(IDENTIFICADOR_PR, id);
                    startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(PrimeraRevisionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    primeraRevisionViewModel.deletePrimeraRevision(primeraRevision);
                    Toast.makeText(PrimeraRevisionActivity.this, R.string.preliminada, Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }catch (Exception e){
                    Toast.makeText(PrimeraRevisionActivity.this, e.getMessage() + " " +
                            e.getCause(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return alertDialog;
    }
}
