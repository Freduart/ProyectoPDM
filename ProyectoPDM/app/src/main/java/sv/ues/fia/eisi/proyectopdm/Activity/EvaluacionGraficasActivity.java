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

    private AdaptadorTabs adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_graficas);
        //inicializar viewpager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //inicializar tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //obtener extras de intent
        final Bundle extra = getIntent().getExtras();
        //obtener id de evaluacion actual
        int idEvalActual = extra.getInt(VerEvaluacionActivity.ID_EVAL);
        int rol_usuario = extra.getInt(LoginActivity.USER_ROL);
        int id_usuario = extra.getInt(LoginActivity.ID_USUARIO);

        //crear nueva instancia de FragmentCurvaNotas, provee id de evaluacion actual
        FragmentCurvaNotas fragmentCurvaNotas = FragmentCurvaNotas.newInstance(idEvalActual);
        //crear nueva instancia de FragmentPastelAprobacion, provee id de evaluacion actual
        FragmentPastelAprobacion fragmentPastelAprobacion = FragmentPastelAprobacion.newInstance(idEvalActual,rol_usuario, id_usuario);

        //construye adaptador de fragments para las tabs (pestañas)
        adapter = new AdaptadorTabs(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //agrega fragmento curva de notas al adaptador y settea su titulo
        adapter.agregarFragment(fragmentCurvaNotas, getText(R.string.headline_grafico_eval).toString());
        //agrega fragmento pastel de aprobación al adaptador y settea su titulo
        adapter.agregarFragment(fragmentPastelAprobacion, getText(R.string.graficopastel).toString());

        //setea el adaptador construido al viewpager
        viewPager.setAdapter(adapter);
        //setea el viewpager al layout de las pestañas
        tabLayout.setupWithViewPager(viewPager);
        setTitle(getText(R.string.estad_eval));
    }
}
