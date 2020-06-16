package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Notas;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionGraficasActivity extends AppCompatActivity {

    private Evaluacion evaluacionActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_graficas);
        //obtener id de evaluacion
        final Bundle extra = getIntent().getExtras();
        int idEvalActual = extra.getInt(VerEvaluacionActivity.ID_EVAL);
        EvaluacionViewModel evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
        DetalleEvaluacionViewModel detalleEvaluacionViewModel =new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
        try{
            //obtiene evaluacion actual
            evaluacionActual = evaluacionViewModel.getEval(idEvalActual);
            //Obtener lista de detalles correspondientes a evaluacion
            List<DetalleEvaluacion> dataPrueba = detalleEvaluacionViewModel.getNotasEvaluacion(evaluacionActual.getIdEvaluacion());

            //asociar con control de layout
            LineChart chart = findViewById(R.id.chart);
            //crear lista de "entradas" de la tabla (x,y)
            List<Entry> entradas = new ArrayList<>();

            //lista donde se guarda cantidades de notas
            List<Notas> notas = new ArrayList<>();
            //objeto de Cantidades de nota auxiliar
            Notas notaAux = new Notas();

            //primer ingreso de nota
            notaAux.setNota((float) dataPrueba.get(0).getNota());
            notaAux.setCantidad(1);
            notas.add(notaAux);

            //contar las notas coincidentes dentro de los detalles de evaluacion
            boolean flag = false;
            for(int i = 1; i < dataPrueba.size(); i++){
                flag = false;
                for(Notas x : notas){
                    if(x.getNota()  == dataPrueba.get(i).getNota()){
                        notaAux = new Notas();
                        notaAux.setNota((float) dataPrueba.get(i).getNota());
                        notaAux.setCantidad(x.getCantidad() + 1);
                        notas.set(notas.indexOf(x),notaAux);
                        flag = true;
                    }
                }
                if(!flag){
                    notaAux = new Notas();
                    notaAux.setNota((float) dataPrueba.get(i).getNota());
                    notaAux.setCantidad(1);
                    notas.add(notaAux);
                }
            }
            //se obtienen las notas y sus respectivas cantidades en la lista notas del ciclo

            //ordena la lista notas por notas
            Collections.sort(notas,Notas.ComparadorNotas);

            //Meter datos en arreglo
            for(int i = 0; i < notas.size(); i++){
                float y = (float) notas.get(i).getCantidad();
                entradas.add(new Entry(notas.get(i).getNota(),y));
            }

            //Añadir datos a la lista de entradas
            for (DetalleEvaluacion detalle : dataPrueba){
            }
            //Crear LineDataSet (es una especie de contenedor para los mismos tipos de datos
            // por esto, tiene un parametro label con el fin de identificarlo)
            LineDataSet dataSet = new LineDataSet(entradas, getText(R.string.leyenda_estadisticas).toString());
            //asigna color al data set
            dataSet.setColor(Color.rgb(211,47,47));
            //color de los texto de los valores
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setCircleColor(Color.rgb(211,47,47));
            dataSet.setLineWidth(2f);
            dataSet.setValueFormatter(new FormatterPersonal());
            //Contenedor de todos los datos del grafico
            LineData lineData = new LineData(dataSet);
            //salida final del gráfico
            chart.setData(lineData);
            chart.setPinchZoom(false);
            chart.setDoubleTapToZoomEnabled(false);
            chart.setScaleEnabled(false);
            chart.getDescription().setEnabled(false);
            chart.getAxisLeft().setAxisMinimum(0);
            chart.getAxisLeft().setGranularity(1f);
            chart.getAxisLeft().setAxisLineWidth(1.5f);
            chart.getAxisRight().setEnabled(false);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getXAxis().setGranularity(1f);
            chart.getXAxis().setAxisMinimum(0);
            chart.getXAxis().setAxisMaximum(notas.get(notas.size() - 1).getNota() + 1);
            chart.getXAxis().setAxisLineWidth(1.5f);
            chart.setNoDataText(getText(R.string.mensaje_grafica_vacia).toString());
            //refrescar gráfico
            chart.invalidate();
            setTitle(getText(R.string.estad_eval));
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
