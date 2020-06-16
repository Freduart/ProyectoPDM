package sv.ues.fia.eisi.proyectopdm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.AlumnoAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;

/*
Funcionalidad de Alumno enlace con sus pantallas correspondientes
 */

public class AlumnoActivity extends AppCompatActivity {
    private AlumnoViewModel alumnoViewModel;

    public static final int AÑADIR_ALUMNO = 1;
    public static final int EDITAR_ALUMNO = 2;
    public static final String OPERACION_ALUMNO = "Operacion_AE_Alumno";
    public static final String IDENTIFICADOR_ALUMNO = "ID_Alumno_Actual";

    public void addAlumno(View view) {
        try {
            Intent intent = new Intent(this, AgregarAlumnoActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Algo salio mal" + e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        RecyclerView AlumnoRV = findViewById(R.id.recycler_alum_view);
        AlumnoRV.setLayoutManager(new LinearLayoutManager(this));
        AlumnoRV.setHasFixedSize(true);

        final AlumnoAdapter adapter = new AlumnoAdapter();
        AlumnoRV.setAdapter(adapter);

        alumnoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
        alumnoViewModel.getAllAlumnos().observe(this, new Observer<List<Alumno>>() {
            @Override
            public void onChanged(List<Alumno> alumnos) {
                adapter.setAlumnos(alumnos);
            }
        });


        //Consultar alumno
        //Funcionalidad al hacer click sobre el objeto de la lista
        try {
            adapter.setOnItemClickListener(new AlumnoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Alumno alumno) {

                    //Opciones a mostrar
                    //Guardamos el id del item seleccionado para la nueva pantalla
                    String carnet = alumno.getCarnetAlumno();
                    //Inicializamos la pantalla con los datos del alumno seleccionado
                    Intent intent = new Intent(AlumnoActivity.this, verAlumnoActivity.class);
                    //Lo enlazamos con un putExtra para que no haya perdida de informacion
                    intent.putExtra(IDENTIFICADOR_ALUMNO, carnet);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "ERROR AL VISUALIZAR DATOS " + e, Toast.LENGTH_SHORT).show();
        }


        //Para opciones de larga pulsacion
        adapter.setOnLongClickListener(new AlumnoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Alumno alumno) {
                String id = alumno.getCarnetAlumno();
                createCustomDialog(alumno).show();
            }
        });
    }

    //Opciones para pulsacion larga
    private AlertDialog createCustomDialog(final Alumno alumno) {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        //Opciones a mostrar
        View view = inflater.inflate(R.layout.dialog_opciones, null);
        ImageButton editar = view.findViewById(R.id.imBEditar);
        ImageButton eliminar = view.findViewById(R.id.imBEliminar);
        TextView textViewv = view.findViewById(R.id.tituloAlert);     //Enlazar el carnet con el textView que mostrara el carnet
        textViewv.setText(alumno.getCarnetAlumno());
        builder.setView(view);
        alertDialog = builder.create();

        //EditarAlumno
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Guardar el carnet del item seleccionado
                    String carnet = alumno.getCarnetAlumno();
                    //Intent para redirigir al detalle del alumno que se toco
                    Intent intent = new Intent(AlumnoActivity.this, NuevaEditarAlumnoActivity.class);
                    intent.putExtra(IDENTIFICADOR_ALUMNO, carnet);
                    intent.putExtra(OPERACION_ALUMNO, EDITAR_ALUMNO);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(AlumnoActivity.this, " ERROR AL INTENTAR EDITAR " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //EliminarAlumno
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    alumnoViewModel.delete(alumno);
                    Toast.makeText(AlumnoActivity.this, "El alumno " + alumno.getCarnetAlumno() + " a sido borrado exitosamente",
                            Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                } catch (Exception e) {
                    Toast.makeText(AlumnoActivity.this, " ERROR AL INTENTAR BORRAR " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return alertDialog;
    }
}
