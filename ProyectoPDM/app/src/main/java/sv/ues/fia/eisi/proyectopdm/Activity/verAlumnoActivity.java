package sv.ues.fia.eisi.proyectopdm.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sv.ues.fia.eisi.proyectopdm.R;

public class verAlumnoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.alumno_item);


        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+ " "+e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

}
