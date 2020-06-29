package sv.ues.fia.eisi.proyectopdm.Activity.GraficaEvaluacion;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import sv.ues.fia.eisi.proyectopdm.Notas;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCurvaNotas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCurvaNotas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_IDEVAL = "idEvaluacion";

    // TODO: Rename and change types of parameters
    private Evaluacion evaluacionActual;
    private int idEvaluacionActual;

    private EvaluacionViewModel evaluacionViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;


    public FragmentCurvaNotas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idevalactual Parametro que contiene el id de evaluación actual.
     * @return A new instance of fragment FragmentCurvaNotas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCurvaNotas newInstance(int idevalactual) {
        FragmentCurvaNotas fragment = new FragmentCurvaNotas();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_IDEVAL, idevalactual);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //obtener id de evaluacion
            idEvaluacionActual = getArguments().getInt(ARG_PARAM_IDEVAL);
        }
        evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(super.getActivity()).getApplication()).create(EvaluacionViewModel.class);
        detalleEvaluacionViewModel =new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(super.getActivity()).getApplication()).create(DetalleEvaluacionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_curva_notas, container, false);
        try{
            //obtiene evaluacion actual
            evaluacionActual = evaluacionViewModel.getEval(idEvaluacionActual);
            //Obtener lista de detalles correspondientes a evaluacion
            List<DetalleEvaluacion> dataPrueba = detalleEvaluacionViewModel.getNotasEvaluacion(evaluacionActual.getIdEvaluacion());

            //asociar con control de layout
            LineChart chart = thisView.findViewById(R.id.chart);
            if(!dataPrueba.isEmpty()){
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

                //Crear LineDataSet (es una especie de contenedor para los mismos tipos de datos
                // por esto, tiene un parametro label con el fin de identificarlo)
                LineDataSet dataSet = new LineDataSet(entradas, getText(R.string.leyenda_estadisticas).toString());
                //asigna color al data set
                dataSet.setColor(Color.rgb(211,47,47));
                //color de los texto de los valores
                dataSet.setValueTextColor(Color.BLACK);
                dataSet.setCircleColor(Color.rgb(211,47,47));
                dataSet.setLineWidth(2f);
                dataSet.setValueFormatter(new ValueFormatter(){
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.US,"%.2f",value);
                    }

                    @Override
                    public String getPointLabel(Entry entry) {
                        return getFormattedValue(entry.getX());
                    }
                });
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
                //refrescar gráfico
                chart.invalidate();
            }
            chart.setNoDataText(getText(R.string.mensaje_grafica_vacia).toString());
        } catch (Exception e) {
            Toast.makeText(super.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return thisView;
    }
}