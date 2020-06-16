package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Adapter.SegundaRevision_DocenteAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevision_DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision_Docente;

public class SegundaRevision_DocenteActivity extends AppCompatActivity {
    private SegundaRevision_DocenteViewModel segundaRevision_docenteViewModel;
    private DocenteViewModel docenteViewModel;

    private Button botonAgregar;
    private Spinner spinnerDocentes;
    private int idRevision;
    private SegundaRevision_DocenteAdapter adaptador;
    private ArrayAdapter<Docente> adaptadorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        segundaRevision_docenteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevision_DocenteViewModel.class);
        docenteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_revision__docente);
        RecyclerView recyclerView = findViewById(R.id.recycler_docentes_segundarev);
        botonAgregar = findViewById(R.id.agregar_docente_segundarev);
        spinnerDocentes = findViewById(R.id.spinner_docentes_segundarev);
        final Bundle extras = getIntent().getExtras();
        adaptador = new SegundaRevision_DocenteAdapter();

        idRevision = extras.getInt(NuevaEditarSegundaRevisionActivity.ID_SR_D);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adaptador);
        final List<SegundaRevision_Docente> listaSalida = new ArrayList<>();

        segundaRevision_docenteViewModel.obtenerRelacionesConSR(idRevision).observe(this, new Observer<List<SegundaRevision_Docente>>() {
            @Override
            public void onChanged(List<SegundaRevision_Docente> segundaRevision_docentes) {
                listaSalida.clear();
                for(SegundaRevision_Docente x : segundaRevision_docentes)
                    listaSalida.add(x);
                adaptador.setRelacionSR_DAdapter(listaSalida,docenteViewModel,segundaRevision_docenteViewModel);
                adaptador.notifyDataSetChanged();
            }
        });

        adaptador.setOnItemLongClickListener(new SegundaRevision_DocenteAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(SegundaRevision_Docente segundaRevision_docente) {
                createCustomDialog(segundaRevision_docente).show();
            }
        });
        actualizarScrollDocentes();
        setTitle(R.string.agregar_docentes);
    }

    public AlertDialog createCustomDialog(final SegundaRevision_Docente segundaRevision_docente){
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton editar = view.findViewById(R.id.imBEditar) ;
        editar.setVisibility(View.GONE);
        ImageButton eliminar = view.findViewById(R.id.imBEliminar);
        TextView textViewv = view.findViewById(R.id.tituloAlert);
        textViewv.setText(segundaRevision_docente.getCarnetDocenteFK());
        builder.setView(view);
        alertDialog = builder.create();
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    segundaRevision_docenteViewModel.eliminarSegundaRevision_Docente(segundaRevision_docente);

                    Toast.makeText(SegundaRevision_DocenteActivity.this, getText(R.string.eliminar_docentesmsg1) +
                                    segundaRevision_docente.getCarnetDocenteFK() +getText(R.string.eliminar_docentesmsg2),
                            Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    actualizarScrollDocentes();
                }catch (Exception e){
                    Toast.makeText(SegundaRevision_DocenteActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        return alertDialog;
    }

    public void actualizarScrollDocentes(){
        final ArrayList<Docente> docentes = new ArrayList<>();
        adaptadorSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,docentes);
        adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDocentes.setAdapter(adaptadorSpinner);
        docenteViewModel.getTodosDocentes().observe(this, new Observer<List<Docente>>() {
            @Override
            public void onChanged(@Nullable List<Docente> todosDocentes) {
                docentes.clear();
                boolean flagExcl;
                for (Docente x : todosDocentes) {
                    try {
                        List<SegundaRevision_Docente> relAux = segundaRevision_docenteViewModel.obtenerRelacionesUsandoDocente(x.getCarnetDocente());
                        flagExcl = false;
                        if(!relAux.isEmpty())
                            for(SegundaRevision_Docente relAuxActual : relAux) {
                                if (relAuxActual.getIdSegundaRevisionFK() != idRevision && !docentes.contains(x) && flagExcl == false){
                                    docentes.add(x);
                                    flagExcl = true;
                                }
                                else if (relAuxActual.getIdSegundaRevisionFK() == idRevision){
                                    docentes.remove(x);
                                    flagExcl = true;
                                }
                            }
                        else
                            docentes.add(x);
                        //refresca (necesario para mostrar los datos recuperados en el spinner)
                        adaptadorSpinner.notifyDataSetChanged();
                        adaptador.notifyDataSetChanged();
                        if(adaptadorSpinner.getCount()==0)
                            botonAgregar.setEnabled(false);
                        else
                            botonAgregar.setEnabled(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }});
    }

    public void ejecutarAñadirRelacion(View view){
        try {
            Docente docente = (Docente) spinnerDocentes.getSelectedItem();
            SegundaRevision_Docente relAux = new SegundaRevision_Docente(docente.getCarnetDocente(),idRevision);
            segundaRevision_docenteViewModel.insertarSegundaRevision_Docente(relAux);
            adaptador.notifyDataSetChanged();
            actualizarScrollDocentes();
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.editar_sr_doc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_sr_det:
                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
