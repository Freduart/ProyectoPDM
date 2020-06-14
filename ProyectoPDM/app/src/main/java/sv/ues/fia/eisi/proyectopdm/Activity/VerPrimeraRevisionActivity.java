package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;

public class VerPrimeraRevisionActivity extends AppCompatActivity {
    public static  final int AÑADIR_SEGUNDA_REVISION = 1;
    public static final int EDITAR_SEGUNDA_REVISION = 2;
    public static final String OPERACION_SEGUNDA_REVISION = "Operacion_AE_sr";
    public static final String IDENTIFICADOR_PRIMERA_REVISION = "ID_pr_Actual";

    private PrimeraRevision primeraRevisionActual;
    private DetalleEvaluacion detalleEvaluacionActual;
    private Local localActual;

    private PrimeraRevisionViewModel primeraRevisionViewModel;
    private LocalViewModel localViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
    private SegundaRevisionViewModel segundaRevisionViewModel;

    private TextView codPR;
    private TextView localFK;
    private TextView detalleEFK;
    private TextView fechaSoli;
    private TextView estado;
    private TextView notaAntes;
    private TextView notaDespues;
    private TextView observaciones;

    public String NOTA_PH_PR;
    private TextView tvnotaD;
    private TextView tvlocal;
    private TextView tvestado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_primera_revision);

            codPR = (TextView) findViewById(R.id.tvCodPR);
            localFK = (TextView) findViewById(R.id.tvLocalFK);
            detalleEFK = (TextView) findViewById(R.id.tvDetalleEvaFK);
            fechaSoli = (TextView) findViewById(R.id.tvFechaSolicitud);
            estado = (TextView) findViewById(R.id.tvEstadoPR);
            notaAntes = (TextView) findViewById(R.id.tvNotaAntesPR);
            notaDespues = (TextView) findViewById(R.id.tvNotaDespuesPR);
            observaciones = (TextView) findViewById(R.id.tvObservacionesPR);


            NOTA_PH_PR = getText(R.string.nota_place_holder_PR).toString();
            tvlocal = (TextView) findViewById(R.id.localPR);
            tvnotaD = (TextView) findViewById(R.id.notaDespuesPR);
            tvestado = (TextView) findViewById(R.id.estadoPR);


            //Instancias ViewModels
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            localViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);

            //Obtiene intent de PrimeraRevisionActivity
            Bundle extras = getIntent().getExtras();
            int idPR = 0;
            if(extras != null){
                idPR = extras.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR);
            }
            //Obtiene pr actual
            primeraRevisionActual = primeraRevisionViewModel.getPrimeraRevision(idPR);
            //Obtiene local actual
            localActual = localViewModel.getLoc(primeraRevisionActual.getIdLocalFK());
            //Obtiene detalle actual
            detalleEvaluacionActual = detalleEvaluacionViewModel.getDetalleEvaluacion(primeraRevisionActual.getIdDetalleEvFK());

            //settear valores de pr actual
            codPR.setText(String.valueOf(primeraRevisionActual.getIdPrimerRevision()));
            localFK.setText(localActual.getIdLocal()+ " - " + localActual.getUbicacion());
            detalleEFK.setText(detalleEvaluacionActual.getIdDetalleEv()+ " - " + detalleEvaluacionActual.getCarnetAlumnoFK());
            fechaSoli.setText(primeraRevisionActual.getFechaSolicitudPrimRev());
            estado.setText(String.valueOf(primeraRevisionActual.isEstadoPrimeraRev()));
            notaAntes.setText(String.valueOf(primeraRevisionActual.getNotasAntesPrimeraRev()));
            notaDespues.setText(String.valueOf(primeraRevisionActual.getNotaDespuesPrimeraRev()));
            observaciones.setText(primeraRevisionActual.getObservacionesPrimeraRev());


            if(String.valueOf(primeraRevisionActual.getNotaDespuesPrimeraRev()).equals(NOTA_PH_PR)){
                tvlocal.setVisibility(View.GONE);
                localFK.setVisibility(View.GONE);
                tvnotaD.setVisibility(View.GONE);
                notaDespues.setVisibility(View.GONE);
                tvestado.setVisibility(View.GONE);
                estado.setVisibility(View.GONE);
            }else if (!String.valueOf(primeraRevisionActual.getNotaDespuesPrimeraRev()).equals(NOTA_PH_PR)){
                tvlocal.setVisibility(View.VISIBLE);
                localFK.setVisibility(View.VISIBLE);
                tvnotaD.setVisibility(View.VISIBLE);
                notaDespues.setVisibility(View.VISIBLE);
                tvestado.setVisibility(View.VISIBLE);
                estado.setVisibility(View.VISIBLE);
            }


            //Titulo appbar
            setTitle(R.string.detallePrimera);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
        segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);
        Bundle extras = getIntent().getExtras();
        int idPrimeraR = 0;
        if(extras != null){
            idPrimeraR = extras.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR);
        }
        //Obtiene pr actual
        try {
            PrimeraRevision pRActual = primeraRevisionViewModel.getPrimeraRevision(idPrimeraR);
            if(segundaRevisionViewModel.getSegundaRevision(pRActual.getIdPrimerRevision())!=null){
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.segunda_rev_menu, menu);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }catch (Exception e){
            Toast.makeText(VerPrimeraRevisionActivity.this, e.getMessage()+" - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.segunda:
                Intent intent = new Intent(VerPrimeraRevisionActivity.this, VerSegundaRevisionActivity.class);
                intent.putExtra(IDENTIFICADOR_PRIMERA_REVISION, primeraRevisionActual.getIdPrimerRevision());
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
