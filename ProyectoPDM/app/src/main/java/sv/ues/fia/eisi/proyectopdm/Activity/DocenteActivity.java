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
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;

public class DocenteActivity extends AppCompatActivity {

    public static final String CARNET_DOCENTE="CarnetDocente";
    FloatingActionButton añadirDocente;
    private DocenteViewModel docenteViewModel;
    private CargoViewModel cargoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("DOCENTES");
        añadirDocente=(FloatingActionButton)findViewById(R.id.nuevoDocente);

        final RecyclerView recyclerDocentes=(RecyclerView)findViewById(R.id.recycler_lista_docentes);
        recyclerDocentes.setLayoutManager(new LinearLayoutManager(this));
        final DocenteAdapter docenteAdapter=new DocenteAdapter();
        recyclerDocentes.setAdapter(docenteAdapter);

        añadirDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DocenteActivity.this,NuevoDocenteActivity.class);
                startActivity(intent);
            }
        });

        try {
            docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
            docenteViewModel.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
                @Override
                public void onChanged(List<Docente> docentes) {
                    docenteAdapter.setListDocentes(docentes);
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
                String carnetDocente=docente.getCarnetDocente();
                Intent intent=new Intent(DocenteActivity.this,EditarDocenteActivity.class);
                intent.putExtra(CARNET_DOCENTE,carnetDocente);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docenteViewModel.deleteDocente(docente);
                Toast.makeText(DocenteActivity.this, "Docente "+docente.getNomDocente()+
                        " ha sido eliminado exitosamente.", Toast.LENGTH_SHORT).show();
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
            String cargoDocente=cargoViewModel.getCargo(docente.getIdCargoFK()).getNomCargo();
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
