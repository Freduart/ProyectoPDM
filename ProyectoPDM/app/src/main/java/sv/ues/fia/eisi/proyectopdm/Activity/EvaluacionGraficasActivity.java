package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;

public class EvaluacionGraficasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_graficas);
        try{
            //asociar con control de layout
            LineChart chart = findViewById(R.id.chart);
            //obtener datos en arreglo o lista (Datos de prueba)
            DetalleEvaluacion[] dataPrueba = {new DetalleEvaluacion(1,"F1501", 10),
                    new DetalleEvaluacion(2,"F1602", 11),
                    new DetalleEvaluacion(3,"F1501", 9),
                    new DetalleEvaluacion(4,"F1602", 7),
                    new DetalleEvaluacion(5,"F1501", 5)};
            //crear lista de "entradas" de la tabla (x,y)
            List<Entry> entradas = new ArrayList<>();
            //Añadir datos a la lista de entradas
            for (DetalleEvaluacion detalle : dataPrueba){
                float x, y;
                x = (float) detalle.getIdEvaluacionFK();
                y = (float) detalle.getNota();
                entradas.add(new Entry(x,y));
            }
            //Crear LineDataSet (es una especie de contenedor para los mismos tipos de datos
            // por esto, tiene un parametro label con el fin de identificarlo)
            LineDataSet dataSet = new LineDataSet(entradas, "Leyenda de entradas");
            //asigna color al data set
            dataSet.setColor(Color.rgb(211,47,47));
            //color de los texto de los valores
            dataSet.setValueTextColor(Color.BLACK);
            //Contenedor de todos los datos del grafico
            LineData lineData = new LineData(dataSet);
            //salida final del gráfico
            chart.setData(lineData);
            //refrescar gráfico
            chart.invalidate();
        } catch (Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
