package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sv.ues.fia.eisi.proyectopdm.Fragments.ListaSolicitudesImpresion;
import sv.ues.fia.eisi.proyectopdm.R;

public class SolicitudImpresionActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_impresion);
        FloatingActionButton nuevaSolicitud=(FloatingActionButton)findViewById(R.id.nuevaSolicitudImpresion);
        nuevaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaSolicitud=new Intent(getApplicationContext(), NuevaSolicitudImpresionActivity.class);
                startActivityForResult(nuevaSolicitud,REQUEST_CODE);
            }
        });
        /*ListaSolicitudesImpresion listaSolicitudesImpresion=new ListaSolicitudesImpresion();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contenedor_solicitudes_impresion,listaSolicitudesImpresion,"listaSolicitudesImpresion");
        fragmentTransaction.commit();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }
}
