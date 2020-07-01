package sv.ues.fia.eisi.proyectopdm.Activity.GraficaEvaluacion;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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
 * Use the {@link FragmentPastelAprobacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPastelAprobacion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_IDEVAL = "idEvaluacion";

    // TODO: Rename and change types of parameters
    private Evaluacion evaluacionActual;
    private int idEvaluacionActual;
    private int notaMaximaActual;

    private EvaluacionViewModel evaluacionViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;

    public FragmentPastelAprobacion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idevalactual Parameter 1.
     * @return A new instance of fragment FragmentPastelAprobacion.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPastelAprobacion newInstance(int idevalactual) {
        FragmentPastelAprobacion fragment = new FragmentPastelAprobacion();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_pastel_aprobacion, container, false);

        //manejo de notas y gráfica
        try{
            //obtiene evaluacion actual
            evaluacionActual = evaluacionViewModel.getEval(idEvaluacionActual);
            notaMaximaActual = evaluacionActual.getNotaMaxima();

            //define nota de aprobación
            float notaAprobacion = (float)((float)notaMaximaActual * 0.6);
            int cantAprob = 0, cantRepr = 0, cantTotal = 0;
            float porAprob, porRepr;

            //Obtener lista de detalles correspondientes a evaluacion
            List<DetalleEvaluacion> dataPrueba = detalleEvaluacionViewModel.getNotasEvaluacion(evaluacionActual.getIdEvaluacion());

            //asociar con control de layout
            PieChart chart = thisView.findViewById(R.id.chart);
            if(!dataPrueba.isEmpty()){
                //crear lista de "entradas" de la tabla (x,y)
                List<PieEntry> entradas = new ArrayList<>();

                //lista donde se guarda cantidades de notas
                List<Notas> notas = new ArrayList<>();
                //objeto de Cantidades de nota auxiliar
                Notas notaAux = new Notas();

                //primer ingreso de nota
                notaAux.setNota((float) dataPrueba.get(0).getNota());
                notaAux.setCantidad(1);
                notas.add(notaAux);

                //contar las notas coincidentes dentro de los detalles de evaluacion
                boolean flag;
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

                for(Notas nota : notas){
                    if(nota.getNota() < notaAprobacion){
                        cantRepr++;
                    } else {
                        cantAprob++;
                    }
                }
                cantTotal = cantAprob + cantRepr;
                //obtiene porcentaje de aprobados
                porAprob = (float)cantAprob / (float)cantTotal;
                porRepr = (float)cantRepr / (float)cantTotal;

                entradas.add(new PieEntry(porAprob,getText(R.string.aprobados).toString()));
                entradas.add(new PieEntry(porRepr,getText(R.string.reprobados).toString()));

                //Crear dataset (es una especie de contenedor para los mismos tipos de datos
                // por esto, tiene un parametro label con el fin de identificarlo)
                PieDataSet dataSet = new PieDataSet(entradas,"");

                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.rgb(25,118,210));
                colors.add(Color.rgb(211,47,47));

                //asigna color al data set
                dataSet.setColors(colors);
                //color de los texto de los valores
                dataSet.setValueTextColor(Color.WHITE);
                dataSet.setValueFormatter(new ValueFormatter(){
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format(Locale.US,"%.2f %s",value*100, "%");
                    }
                });
                dataSet.setValueTextSize(15f);
                //Contenedor de todos los datos del grafico
                PieData pieData = new PieData(dataSet);
                //salida final del gráfico
                chart.setData(pieData);
                chart.getDescription().setEnabled(false);
                //refrescar gráfico
                chart.invalidate();
            }
            chart.setNoDataText(getText(R.string.mensaje_grafica_vacia).toString());
            chart.setTouchEnabled(false);
        } catch (Exception e) {
            Toast.makeText(super.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        //fin grafica

        return thisView;
    }
}