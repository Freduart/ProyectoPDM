package sv.ues.fia.eisi.proyectopdm.Activity;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Activity.zxing.CaptureActivityPortrait;
import sv.ues.fia.eisi.proyectopdm.Adapter.DetalleEvaluacionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;

public class DetalleEvaluacionActivity extends AppCompatActivity {
    public static final String ID_DETALLE_EVAL = "ID_Actual";

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
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detalle_evaluacion);
            spinnerAlumnos = findViewById(R.id.spinner_alumnos_detalle);
            botonAgregar = findViewById(R.id.agregar_alumno_detalle);
            //Obtener bundle de extras (Del intent)
            final Bundle extras = getIntent().getExtras();
            //obtener int con el nombre EvaluacionActivity.IDENTIFICADOR_EVALUACION
            idEvaluacion = extras.getInt(EvaluacionActivity.IDENTIFICADOR_EVALUACION);

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
                    for (DetalleEvaluacion x : detalles)
                        if (x.getIdEvaluacionFK() == idEvaluacion)
                            listaSalida.add(x);
                    //mete los detalles en el adaptador
                    adaptador.setDetalleEvaluaciones(listaSalida, alumnoViewModel, escuelaViewModel);
                    adaptador.notifyDataSetChanged();
                }
            });
            //al hacer clic corto en un objeto del recycler
            adaptador.setOnItemClickListener(new DetalleEvaluacionAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DetalleEvaluacion detalleEvaluacion) {
                    //guardar id de evaluacion que se tocó
                    int id = detalleEvaluacion.getIdDetalleEv();
                    //inicializa intent que dirige hacia el detalle de la evaluacion que se tocó
                    Intent intent = new Intent(DetalleEvaluacionActivity.this, DetalleNotasActivity.class);
                    //se mete en un extra del intent, el id
                    intent.putExtra(ID_DETALLE_EVAL, id);
                    //inicia la activity
                    startActivity(intent);
                }
            });
            adaptador.setOnItemLongClickListener(new DetalleEvaluacionAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(DetalleEvaluacion detalleEvaluacion) {
                    createCustomDialog(detalleEvaluacion).show();
                }
            });
            actualizarScrollAlumnos();
            setTitle(getText(R.string.evaluacion));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void ejecutarAñadirDetalle(View view) {
        try {
            Alumno alumno = (Alumno) spinnerAlumnos.getSelectedItem();
            DetalleEvaluacion detalleAux = new DetalleEvaluacion(idEvaluacion, alumno.getCarnetAlumno(), -1);
            detalleEvaluacionViewModel.insertDetaleEvalulacion(detalleAux);
            adaptador.notifyDataSetChanged();
            actualizarScrollAlumnos();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public AlertDialog createCustomDialog(final DetalleEvaluacion detalleEvaluacion) {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton editar = view.findViewById(R.id.imBEditar);
        editar.setVisibility(View.GONE);
        ImageButton eliminar = view.findViewById(R.id.imBEliminar);
        TextView textViewv = view.findViewById(R.id.tituloAlert);
        textViewv.setText(detalleEvaluacion.getCarnetAlumnoFK());
        builder.setView(view);
        alertDialog = builder.create();
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    detalleEvaluacionViewModel.deleteDetalleEvaluacion(detalleEvaluacion);

                    Toast.makeText(DetalleEvaluacionActivity.this, getText(R.string.inic_notif_detalle) +
                                    detalleEvaluacion.getCarnetAlumnoFK() + getText(R.string.accion_borrar_notif_Detalle),
                            Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    actualizarScrollAlumnos();
                } catch (Exception e) {
                    Toast.makeText(DetalleEvaluacionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return alertDialog;
    }

    public void actualizarScrollAlumnos() {
        try {
            //Spinner alumnos
            final ArrayList<Alumno> alumnosLista = new ArrayList<>();
            adaptadorSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, alumnosLista);
            adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAlumnos.setAdapter(adaptadorSpinner);
            EvaluacionViewModel evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            evaluacionViewModel.getAlumnosDesdeAsignatura(evaluacionViewModel.getAsignaturaEvaluacion(idEvaluacion).getCodigoAsignatura()).observe(this, new Observer<List<Alumno>>() {
                @Override
                public void onChanged(@Nullable List<Alumno> alumnos) {
                    alumnosLista.clear();
                    boolean flagExcl;
                    for (Alumno x : alumnos) {
                        try {
                            List<DetalleEvaluacion> detalleAux = detalleEvaluacionViewModel.getDetallePorAlumno(x.getCarnetAlumno());
                            flagExcl = false;
                            if (!detalleAux.isEmpty())
                                for (DetalleEvaluacion det : detalleAux) {
                                    if (det.getIdEvaluacionFK() != idEvaluacion && !alumnosLista.contains(x) && flagExcl == false) {
                                        alumnosLista.add(x);
                                        flagExcl = true;
                                    } else if (det.getIdEvaluacionFK() == idEvaluacion) {
                                        alumnosLista.remove(x);
                                        flagExcl = true;
                                    }
                                }
                            else
                                alumnosLista.add(x);
                            //refresca (necesario para mostrar los datos recuperados en el spinner)
                            adaptadorSpinner.notifyDataSetChanged();
                            adaptador.notifyDataSetChanged();
                            if (adaptadorSpinner.getCount() == 0)
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

                }
            });
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    //CODIGO DE FREDY PARA LIBRERIAS

    //Para agregar el icono de agregar un nuevo alumno con zxing en el layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.agregar_conzxing, menu);
        return true;
    }

    //Funcion de boton guardado en la barra de tareas
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agregar_zxing:
                escanear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Metodo para activar la camara y para poder escanear el codigo QR
    public void escanear() {
        IntentIntegrator zxingIntent = new IntentIntegrator(this);
        zxingIntent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        zxingIntent.setPrompt("ESCANEAR CODIGO");
        zxingIntent.setCameraId(0);
        zxingIntent.setOrientationLocked(false);
        zxingIntent.setBeepEnabled(false);
        zxingIntent.setCaptureActivity(CaptureActivityPortrait.class);
        zxingIntent.setBarcodeImageEnabled(false);
        zxingIntent.initiateScan();
    }

    //Metodo para obtener el dato del scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        IntentResult resultzxing=IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
        if(resultzxing!=null){
            if(resultzxing.getContents()!=null){
                try{
                    //resultzxing.getContents() es para devolver la informacion obtenida en el scaneo del codigo
                    DetalleEvaluacion detalleAux = new DetalleEvaluacion(idEvaluacion,resultzxing.getContents());
                    detalleEvaluacionViewModel.insertDetaleEvalulacion(detalleAux);
                    //Para actualizar tanto el layout como el listado de alumnos con los alumnos agregados al parcial
                    adaptador.notifyDataSetChanged();
                    actualizarScrollAlumnos();
                }catch (Exception e){
                    //Toast en caso de error al escanear un codigo
                    Toast.makeText(this, "ERROR AL INTENTAR AÑADIR", Toast.LENGTH_SHORT).show();
                }
            }else {
                //Mensaje de error al volver atras o no en caso que resultzxing no tenga informacion escanear codigo
                Toast.makeText(this, "ERROR!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

