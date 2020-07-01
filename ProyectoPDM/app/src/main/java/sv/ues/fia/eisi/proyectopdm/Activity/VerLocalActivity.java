package sv.ues.fia.eisi.proyectopdm.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class VerLocalActivity extends AppCompatActivity {
    public static final String IDENTIFICADOR_Local = "ID_LOCAL_ACTUAL";
    //Atributos de Clase
    private Local localActual;
    private LocalViewModel localVM;

    private TextView dispIdLocal;
    private TextView dispNomLocal;
    private TextView dispUbLocal;
    private TextView dispLatLocal;
    private TextView dispLonLocal;
    //para geolocalización
    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSIONS_REQUEST_LOCATION;
    private ImageButton mapa;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_local);
            //para geolocalización
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            subirLatLongFirebase();
            mapa = (ImageButton) findViewById(R.id.btnMap);

            //Título personalizado para Activity
            setTitle("Detalle de Local");

            //Asociando tVs de XML con tVs de Activity
            dispIdLocal = (TextView) findViewById(R.id.vlocal_disp_idLocal);
            dispNomLocal = (TextView) findViewById(R.id.vlocal_disp_nomLocal);
            dispUbLocal = (TextView) findViewById(R.id.vlocal_disp_ubLocal);
            dispLatLocal = (TextView) findViewById(R.id.vlocal_disp_latitud);
            dispLonLocal = (TextView) findViewById(R.id.vlocal_disp_longitud);

            //Inicializando el ViewModel
            localVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);

            //Obteniendo ID de Local Seleccionado del Intent
            Bundle extras = getIntent().getExtras();
            String idLocal = "";
            if (extras != null) {
                idLocal = extras.getString(LocalActivity.IDENTIFICADOR_LOCAL);
            }

            //Extraer Local Actual
            localActual = localVM.getLoc(idLocal);

            //Convirtiendo valores de Latitud y Longitud a texto
            String latAux = localActual.getLatitud() + "";
            String lonAux = localActual.getLongitud() + "";

            //Asignando valores de Local Actual en tVs
            dispIdLocal.setText(localActual.getIdLocal());
            dispNomLocal.setText(localActual.getNombreLocal());
            dispUbLocal.setText(localActual.getUbicacion());
            dispLatLocal.setText(latAux);
            dispLonLocal.setText(lonAux);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage() + " - " + e.fillInStackTrace().toString(), Toast.LENGTH_LONG).show();
        }
    }



    private void subirLatLongFirebase() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(VerLocalActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    location.setLatitude(localActual.getLatitud());
                    location.setLongitude(localActual.getLongitud());
                    Log.e("Latitud: ", + location.getLatitude() + " Longitud: "+ location.getLongitude());
                    Map<String, Object> latLong = new HashMap<>();
                    latLong.put("latitud", location.getLatitude());
                    latLong.put("longitud", location.getLongitude());
                    mDatabase.child("locales").push().setValue(latLong);
                }
            }
        });
    }

    public void redirectMaps(View view){
        String id = localActual.getIdLocal();
        Intent intent = new Intent(VerLocalActivity.this, MapsActivity.class);
        intent.putExtra(IDENTIFICADOR_Local, id);
        try {
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(VerLocalActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }
}
