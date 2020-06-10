package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

//import sv.ues.fia.eisi.proyectopdm.Adapter.SpinAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;


public class NuevaEditarAreaAdmActivity extends AppCompatActivity {
    private EditText editNombreAreaAdm;
    private Spinner spinEscuela;
    private EscuelaViewModel escuelaVM;
    private AreaAdmViewModel areaAdmViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_editar_area_adm);
            //obtener extras del intent
            final Bundle extras = getIntent().getExtras();

            //inicialización de elementos del layout
            editNombreAreaAdm = findViewById(R.id.edit_nombre_area);
            spinEscuela = findViewById(R.id.edit_escuela_area);

            //LLENAR SPINNERS
            //Spinner asignatura
            //listas para alamacenar nombre e id de asignatura
            final ArrayList<Escuela> escuelasNom = new ArrayList<>();
            //adaptador a arreglos para spinner, recibe (contexto, layout de spinner por defecto de android, arreglo a mostrar)
            final ArrayAdapter<Escuela> adaptadorSpinnerEscuela = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,escuelasNom);
            //settea layout de dropdown del spinner (layout por defecto de android)
            adaptadorSpinnerEscuela.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //settea el adaptador creado en el spinner
            spinEscuela.setAdapter(adaptadorSpinnerEscuela);
            //instancia asignatura view model
            escuelaVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EscuelaViewModel.class);
            //obtener todas las asignaturas en livedata
            escuelaVM.getAllEscuelas().observe(this, new Observer<List<Escuela>>() {
                @Override
                public void onChanged(@Nullable List<Escuela> escuelas) {
                    try {
                        if(extras.getInt(AreaAdmActivity.OPERACION_AREA) == AreaAdmActivity.EDITAR_AREA){
                            AreaAdm ev = areaAdmViewModel.getAreaAdm(extras.getInt(AreaAdmActivity.IDENTIFICADOR_AREA));
                            Escuela as = escuelaVM.getEscuela(ev.getIdEscuelaFK());
                            //añade los elementos del livedata a las listas para alamcenar nombre e id de asignatura
                            for (Escuela x : escuelas) {
                                escuelasNom.add(x);
                                if(x.getIdEscuela() == as.getIdEscuela())
                                    spinEscuela.setSelection(escuelasNom.indexOf(x));
                            }
                            //refresca (necesario para mostrar los datos recuperados en el spinner)
                            adaptadorSpinnerEscuela.notifyDataSetChanged();
                        } else {
                            for (Escuela x : escuelas)
                                escuelasNom.add(x);
                            adaptadorSpinnerEscuela.notifyDataSetChanged();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });
            //--fin llenado spinner asignatura

            //nomeacuerdoxdddddd
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

            //PARA EDITAR
            //instancia View Model de areaAdm
            areaAdmViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);
            AreaAdm auxiliar;
            int idAreaAdm = 0, operacionEv = 0;
            //si tiene extras
            if (extras != null) {
                //obtener id escuela de extras
                idAreaAdm = extras.getInt(AreaAdmActivity.IDENTIFICADOR_AREA);
                //obtener operación que se hará
                operacionEv = extras.getInt(AreaAdmActivity.OPERACION_AREA);
                //verificar extras de intent, si se editará entonces:
                if(operacionEv == AreaAdmActivity.EDITAR_AREA && idAreaAdm != 0){
                    //obtener areaAdm auxiliar
                    auxiliar = areaAdmViewModel.getAreaAdm(idAreaAdm);
                    //obtener escuela actual
                    Escuela docenteActual = escuelaVM.getEscuela(auxiliar.getIdEscuelaFK());
                    //settear editTexts con los objetos obtenidos
                    editNombreAreaAdm.setText(auxiliar.getNomDepartamento());
                    //coloca título de pantalla
                    setTitle(R.string.titulo_EA_editararea);
                }
                //si se añadirá una nueva area
                else if (operacionEv == AreaAdmActivity.AÑADIR_AREA && idAreaAdm == 0){
                    setTitle(R.string.titulo_EA_nuevaarea);
                }
            }

        } catch (Exception e){
            Toast.makeText(NuevaEditarAreaAdmActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void guardarAreaAdm() {
        try {
            //---alamcenar NOMBRE areaAdm
            String nombre = editNombreAreaAdm.getText().toString();

            //---obtener valor de spinner ASIGNATURA
            Escuela escuelaAux = (Escuela) spinEscuela.getSelectedItem();

            if(nombre.trim().isEmpty()){
                Toast.makeText(this,getText(R.string.error_form_incompleto_eval), Toast.LENGTH_LONG).show();
                return;
            }
            //instancia View Model de areaAdm
            areaAdmViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AreaAdmViewModel.class);

            //obtener extras del intent
            Bundle extras = getIntent().getExtras();
            int idAreaAdm = 0, operacionEv = 0;
            if (extras != null) {
                idAreaAdm = extras.getInt(AreaAdmActivity.IDENTIFICADOR_AREA);
                operacionEv = extras.getInt(AreaAdmActivity.OPERACION_AREA);
                //verificar extras de intent
                if (operacionEv == AreaAdmActivity.EDITAR_AREA && idAreaAdm != 0) {
                    //Objeto Areauación auxiliar construido a partir de los datos almacenados
                    AreaAdm aux = new AreaAdm(escuelaAux.getIdEscuela(),nombre);
                    aux.setIdDeptarmento(idAreaAdm);
                    //insertar
                    areaAdmViewModel.updateAreaAdm(aux);
                    //convertir a string
                    String idArea = aux.getIdDeptarmento() + "";
                    //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
                    Toast.makeText(NuevaEditarAreaAdmActivity.this, getText(R.string.inic_notif_aarea) + idArea + "-" + nombre + getText(R.string.accion_actualizar_notif_eval), Toast.LENGTH_LONG).show();
                } else if (operacionEv == AreaAdmActivity.AÑADIR_AREA && idAreaAdm == 0) {
                    //Objeto Areauación auxiliar construido a partir de los datos almacenados
                    AreaAdm aux = new AreaAdm(escuelaAux.getIdEscuela(),nombre);
                    //insertar
                    areaAdmViewModel.insertAreaAdm(aux);
                    //mensaje de éxito (si falla, el try lo atrapa y en vez de mostrar este toast, muestra el toast con la excepción más abajo)
                    Toast.makeText(NuevaEditarAreaAdmActivity.this, getText(R.string.inic_notif_aarea) + nombre  + getText(R.string.accion_insertar_notif_eval), Toast.LENGTH_LONG).show();
                }
            }
            //salir de la actividad
            finish();
        } catch (Exception e){
            Toast.makeText(NuevaEditarAreaAdmActivity.this, e.getMessage() + " /// " + " /// " + e.fillInStackTrace().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador = getMenuInflater();
        inflador.inflate(R.menu.nueva_area_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar_areaadm:
                guardarAreaAdm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
