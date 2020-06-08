package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Adapter.EscuelaAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

/*
    De ahora en adelante si querran hacer pruebas de funcionalidad pueden modificar el intent del
    login para redireccionar a la lista de Escuelas, se modificara
 */
public class MainActivity extends AppCompatActivity {

    private EscuelaViewModel escuelaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
            Se debe enlazar el objeto recyclerView con la vista que implementaremos aca hemos hecho uso
            de los layout escuela_item y el activity_main en la cual esta el recycle_list
         */

        RecyclerView recyclerView=findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);     //Para actualizaciones

        //Aca enlazamos la vista con el adaptador y modificamos el recycleView a medida se actualize el adaptador
        final EscuelaAdapter adapter=new EscuelaAdapter();
        recyclerView.setAdapter(adapter);

        try{
            /*
                debido a que ViewModel esta desfasado se tienen que buscar alternativas en este caso usamos AndroidViewModelFactory para dicha
                solucion para comprobarlo podemos mandar un mensaje en un toast
            */

            escuelaViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);
            escuelaViewModel.getAllEscuelas().observe(this, new Observer<List<Escuela>>() {
                @Override
                public void onChanged(@Nullable List<Escuela> escuelas) {
                    //actualiza el RecyclerView
                    adapter.setEscuelas(escuelas);
                }
            });
        }catch (Exception e){
            //En caso de cierre inoportuno
            Toast.makeText(MainActivity.this, "Error en el ViewModel", Toast.LENGTH_SHORT).show();
        }
    }

    public void alumnoactivityOnClick(View view){
        try{
            Intent intent = new Intent(this, AlumnoActivity.class);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, "Error en el AlumnoActivity "+e, Toast.LENGTH_LONG).show();
        }

    }
}
