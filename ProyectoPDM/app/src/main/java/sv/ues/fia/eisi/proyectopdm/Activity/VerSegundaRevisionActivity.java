package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;

public class VerSegundaRevisionActivity extends AppCompatActivity {
    private SegundaRevision segundaRevision;

    private SegundaRevisionViewModel segundaRevisionViewModel;

    private TextView dispSegundaRevision;
    private TextView dispPrimeraRevision;
    private TextView dispHoraRevision;
    private TextView dispFechaRevision;
    private TextView dispObservaciones;
    private TextView dispNotaFinal;
    private TextView dispFechaSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ver_segunda_revision);

            dispSegundaRevision = findViewById(R.id.disp_codigo_segundarevision);
            dispPrimeraRevision = findViewById(R.id.disp_codigo_primerarevision);
            dispFechaRevision = findViewById(R.id.disp_fecha_segundarevision);
            dispFechaSolicitud = findViewById(R.id.disp_fechasol_segundarevision);
            dispObservaciones = findViewById(R.id.disp_observaciones_segundarevision);
            dispNotaFinal = findViewById(R.id.disp_nota_segundarevision);
            dispHoraRevision = findViewById(R.id.disp_hora_segundarevision);

            segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);

            //obtener intent de activity
            Bundle extras = getIntent().getExtras();
            String idPrimeraRevision = "";
            if (extras != null) {
                idPrimeraRevision = extras.getString(VerPrimeraRevisionActivity.IDENTIFICADOR_PRIMERA_REVISION);
            }
            try {
                segundaRevision = segundaRevisionViewModel.getSegundaRevision(idPrimeraRevision);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            dispSegundaRevision.setText(segundaRevision.getIdSegundaRevision());
            dispPrimeraRevision.setText(idPrimeraRevision);
            dispObservaciones.setText(segundaRevision.getObservacionesSegundaRev());
            dispNotaFinal.setText(String.format("%s", segundaRevision.getNotaFinalSegundaRev()));
            String[] horas = segundaRevision.getHoraSegundaRev().split(":");
            int hora = Integer.parseInt(horas[0]);
            int horaen12 = 0;
            if (Integer.parseInt(horas[0]) + 1 > 12) {
                horaen12 = hora - 12;
                dispHoraRevision.setText(String.format("%s:%s%s", String.format("%d", horaen12), horas[1], getText(R.string.posmeridiano)));
            } else
                dispHoraRevision.setText(String.format("%s:%s%s", horas[0], horas[1], getText(R.string.antesmeridiano)));
            dispFechaRevision.setText(segundaRevision.getFechaSegundaRev());
            dispFechaSolicitud.setText(segundaRevision.getFechaSolicitudSegRev());
            setTitle(getText(R.string.titulo_ver_segundarevision));
        } catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString() ,Toast.LENGTH_LONG).show();
        }
    }
}
