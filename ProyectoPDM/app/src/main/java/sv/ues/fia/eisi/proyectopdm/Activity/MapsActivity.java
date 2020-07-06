package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Local local;
    private LocalViewModel localViewModel;
    private TextView lat, longi, alt, dir;
    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSIONS_REQUEST_LOCATION;
    private Marker marcaDestino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        lat = (TextView) findViewById(R.id.lat);
        longi = (TextView) findViewById(R.id.longi);
        alt = (TextView) findViewById(R.id.alt);
        dir = (TextView) findViewById(R.id.dir);
        datosUbicacion();
    }

    private void datosUbicacion() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    lat.setText(String.valueOf(location.getLatitude()));
                    longi.setText(String.valueOf(location.getLongitude()));
                    alt.setText(String.valueOf(location.getAltitude()));
                    Geocoder g = new Geocoder(getApplicationContext());
                    List<Address> ad = null;
                    try {
                        ad = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (ad != null && ad.isEmpty() == false) {
                        dir.setText(ad.get(0).getThoroughfare());
                    }
                    Geocoder dirLocal= new Geocoder(getApplicationContext());
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        localViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
        Bundle extra = getIntent().getExtras();
        String idLocal = "";
        if (extra != null) {
            idLocal = extra.getString(LocalActivity.IDENTIFICADOR_LOCAL);
        }
        try {
            local = localViewModel.getLoc(idLocal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng localSeleccionado = new LatLng(local.getLatitud(), local.getLongitud());
        marcaDestino = mMap.addMarker(new MarkerOptions().position(localSeleccionado).
                title(local.getIdLocal()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localSeleccionado, 18.2f));
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(marcaDestino)){
            String la, lo;
            String d = null;
            la= Double.toString(local.getLatitud());
            lo= Double.toString(local.getLongitud());
            Geocoder ge = new Geocoder(getApplicationContext());
            List<Address> adr = null;
            try {
                adr = ge.getFromLocation(local.getLatitud(), local.getLongitud(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (adr != null && adr.isEmpty() == false) {
                d = adr.get(0).getThoroughfare();
            }
            Toast toast = Toast.makeText(MapsActivity.this, la + " - "+ lo+ " - "+ d, Toast.LENGTH_SHORT );
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 90, 0);
            toast.show();
        }
        return false;
    }
}