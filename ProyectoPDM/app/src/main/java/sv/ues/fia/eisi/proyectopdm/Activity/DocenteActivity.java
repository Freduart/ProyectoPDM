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

import sv.ues.fia.eisi.proyectopdm.Adapter.DocenteAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;

public class DocenteActivity extends AppCompatActivity {

    FloatingActionButton añadirDocente;
    private DocenteViewModel docenteViewModel;

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
}
