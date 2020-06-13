package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Adapter.DetalleEvaluacionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;

public class DetalleEvaluacionActivity extends AppCompatActivity {
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
    private AlumnoViewModel alumnoViewModel;
    private EscuelaViewModel escuelaViewModel;

    private Button botonAgregar;
    private Spinner spinnerAlumnos;
    private int idEvaluacion;
    private DetalleEvaluacionAdapter adaptador;
    private ArrayAdapter<Alumno> adaptadorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detalle_evaluacion);
            spinnerAlumnos = findViewById(R.id.spinner_alumnos_detalle);
            //Obtener bundle de extras (Del intent)
            final Bundle extras = getIntent().getExtras();
            //obtener int con el nombre EvaluacionActivity.IDENTIFICADOR_EVALUACION
            idEvaluacion = extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION);
            botonAgregar = findViewById(R.id.agregar_alumno_detalle);

            //--lista de evaluaciones
            //inicializa recycler view
            RecyclerView DetalleRecycler = findViewById(R.id.recycler_detalle_view);
            //set layout manager
            DetalleRecycler.setLayoutManager(new LinearLayoutManager(this));
            //recycler tiene tamaño fijo = true
            DetalleRecycler.setHasFixedSize(true);
            //adaptador para recycler
            adaptador = new DetalleEvaluacionAdapter();
            //linkea adaptador en el recycler
            DetalleRecycler.setAdapter(adaptador);
            final List<DetalleEvaluacion> listaSalida = new ArrayList<>();
            //inicializa viewmodel de detalleevaluacion
            alumnoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            escuelaViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);
            //obtiene todas los detalles en un livedata
            detalleEvaluacionViewModel.getAllDetalles().observe(this, new Observer<List<DetalleEvaluacion>>() {
                @Override
                public void onChanged(List<DetalleEvaluacion> detalles) {
                    listaSalida.clear();
                    for(DetalleEvaluacion x : detalles)
                        if (x.getIdEvaluacionFK()==idEvaluacion)
                            listaSalida.add(x);
                    //mete los detalles en el adaptador
                    adaptador.setDetalleEvaluaciones(listaSalida, alumnoViewModel, escuelaViewModel);
                    adaptador.notifyDataSetChanged();
                }
            });
            //Spinner alumnos
            final ArrayList<Alumno> alumnosLista = new ArrayList<>();
            adaptadorSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,alumnosLista);
            adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAlumnos.setAdapter(adaptadorSpinner);
            alumnoViewModel.getAllAlumnos().observe(this, new Observer<List<Alumno>>() {
                @Override
                public void onChanged(@Nullable List<Alumno> alumnos) {
                    alumnosLista.clear();
                    boolean flagExcl;
                        for (Alumno x : alumnos) {
                            try {
                                List<DetalleEvaluacion> detalleAux = detalleEvaluacionViewModel.getDetallePorAlumno(x.getCarnetAlumno());
                                flagExcl = false;
                                if(!detalleAux.isEmpty())
                                for(DetalleEvaluacion det : detalleAux) {
                                    if (det.getIdEvaluacionFK() != idEvaluacion && !alumnosLista.contains(x) && flagExcl == false){
                                        alumnosLista.add(x);
                                        flagExcl = true;
                                    }
                                    else if (det.getIdEvaluacionFK() == idEvaluacion){
                                        alumnosLista.remove(x);
                                        flagExcl = true;
                                    }
                                } else
                                    alumnosLista.add(x);
                                //refresca (necesario para mostrar los datos recuperados en el spinner)
                                adaptadorSpinner.notifyDataSetChanged();
                                adaptador.notifyDataSetChanged();
                                if(adaptadorSpinner.getCount()==0)
                                    botonAgregar.setEnabled(false);
                                else
                                    botonAgregar.setEnabled(true);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            } catch (ExecutionException ex) {
                                ex.printStackTrace();
                            } catch (TimeoutException ex) {
                                ex.printStackTrace();
                            }
                        }

                }});

        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void ejecutarAñadirDetalle(View view){
        try {
            Alumno alumno = (Alumno) spinnerAlumnos.getSelectedItem();
            DetalleEvaluacion detalleAux = new DetalleEvaluacion(idEvaluacion, alumno.getCarnetAlumno(), -1);
            detalleEvaluacionViewModel.insertDetaleEvalulacion(detalleAux);
            adaptador.notifyDataSetChanged();

            //Spinner alumnos
            final ArrayList<Alumno> alumnosLista = new ArrayList<>();
            adaptadorSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,alumnosLista);
            adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAlumnos.setAdapter(adaptadorSpinner);
            alumnoViewModel.getAllAlumnos().observe(this, new Observer<List<Alumno>>() {
                @Override
                public void onChanged(@Nullable List<Alumno> alumnos) {
                    alumnosLista.clear();
                    boolean flagExcl;
                    for (Alumno x : alumnos) {
                        try {
                            List<DetalleEvaluacion> detalleAux = detalleEvaluacionViewModel.getDetallePorAlumno(x.getCarnetAlumno());
                            flagExcl = false;
                            if(!detalleAux.isEmpty())
                                for(DetalleEvaluacion det : detalleAux) {
                                    if (det.getIdEvaluacionFK() != idEvaluacion && !alumnosLista.contains(x) && flagExcl == false){
                                        alumnosLista.add(x);
                                        flagExcl = true;
                                    }
                                    else if (det.getIdEvaluacionFK() == idEvaluacion){
                                        alumnosLista.remove(x);
                                        flagExcl = true;
                                    }
                                } else
                                alumnosLista.add(x);
                            //refresca (necesario para mostrar los datos recuperados en el spinner)
                            adaptadorSpinner.notifyDataSetChanged();
                            adaptador.notifyDataSetChanged();
                            if(adaptadorSpinner.getCount()==0)
                                botonAgregar.setEnabled(false);
                            else
                                botonAgregar.setEnabled(true);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        } catch (ExecutionException ex) {
                            ex.printStackTrace();
                        } catch (TimeoutException ex) {
                            ex.printStackTrace();
                        }
                    }

                }});
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
