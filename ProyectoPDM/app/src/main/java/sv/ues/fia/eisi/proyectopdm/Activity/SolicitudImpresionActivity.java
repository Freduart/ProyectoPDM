package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import sv.ues.fia.eisi.proyectopdm.Fragments.ListaSolicitudesImpresion;
import sv.ues.fia.eisi.proyectopdm.R;

public class SolicitudImpresionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_impresion);
        ListaSolicitudesImpresion listaSolicitudesImpresion=new ListaSolicitudesImpresion();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.contenedor_solicitudes_impresion,listaSolicitudesImpresion,"listaSolicitudesImpresion");
        fragmentTransaction.commit();
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
