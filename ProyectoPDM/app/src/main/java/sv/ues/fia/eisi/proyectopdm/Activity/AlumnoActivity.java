package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.AlumnoAdapter;
import sv.ues.fia.eisi.proyectopdm.Adapter.EscuelaAdapter;
import sv.ues.fia.eisi.proyectopdm.Adapter.EvaluacionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

/*
Funcionalidad de Alumno enlace con sus pantallas correspondientes
 */

public class AlumnoActivity extends AppCompatActivity {
    private AlumnoViewModel alumnoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        RecyclerView AlumnoRV=findViewById(R.id.recycler_alum_view);
        AlumnoRV.setLayoutManager(new LinearLayoutManager(this));
        AlumnoRV.setHasFixedSize(true);

        final AlumnoAdapter adapter=new AlumnoAdapter();
        AlumnoRV.setAdapter(adapter);

        alumnoViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
        alumnoViewModel.getAllAlumnos().observe(this, new Observer<List<Alumno>>() {
                    @Override
                    public void onChanged(List<Alumno> alumnos) {
                        adapter.setAlumnos(alumnos);
                    }
        });
    }
}
