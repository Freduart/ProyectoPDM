package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Activity.GraficaEvaluacion.AdaptadorTabs;
import sv.ues.fia.eisi.proyectopdm.Activity.GraficaEvaluacion.FragmentCurvaNotas;
import sv.ues.fia.eisi.proyectopdm.Activity.GraficaEvaluacion.FragmentPastelAprobacion;
import sv.ues.fia.eisi.proyectopdm.Notas;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionGraficasActivity extends AppCompatActivity {

    private Evaluacion evaluacionActual;
    private AdaptadorTabs adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_graficas);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        final Bundle extra = getIntent().getExtras();
        int idEvalActual = extra.getInt(VerEvaluacionActivity.ID_EVAL);
        FragmentCurvaNotas fragmentCurvaNotas = FragmentCurvaNotas.newInstance(idEvalActual);
        FragmentPastelAprobacion fragmentPastelAprobacion = FragmentPastelAprobacion.newInstance(idEvalActual);
        adapter = new AdaptadorTabs(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.agregarFragment(fragmentCurvaNotas, getText(R.string.headline_grafico_eval).toString());
        adapter.agregarFragment(fragmentPastelAprobacion, getText(R.string.graficopastel).toString());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setTitle(getText(R.string.estad_eval));
    }
}
