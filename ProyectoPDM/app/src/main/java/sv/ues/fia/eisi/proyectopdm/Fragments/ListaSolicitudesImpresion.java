package sv.ues.fia.eisi.proyectopdm.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Activity.CargoActivity;
import sv.ues.fia.eisi.proyectopdm.Activity.NuevaSolicitudImpresionActivity;
import sv.ues.fia.eisi.proyectopdm.Adapter.ListaSolicitudesImpresionAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudImpresionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class ListaSolicitudesImpresion extends Fragment {
    public static final int REQUEST_CODE = 11;
    public static final int RESULT_CODE = 12;
    private ArrayList<SolicitudImpresion> listaSolicitudesImpresion;
    private SolicitudImpresionViewModel solicitudImpresionViewModel;

    public ListaSolicitudesImpresion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_lista_solicitudes_impresion, container, false);
        FloatingActionButton nuevaSolicitud=(FloatingActionButton)view.findViewById(R.id.nuevaSolicitudImpresion);
        nuevaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevaSolicitud=new Intent(getActivity(), NuevaSolicitudImpresionActivity.class);
                startActivityForResult(nuevaSolicitud,REQUEST_CODE);
            }
        });
        RecyclerView recyclerSolicitudes=(RecyclerView)view.findViewById(R.id.recycler_lista_solicitudes);
        recyclerSolicitudes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSolicitudes.setHasFixedSize(true);
        //AdapterSolicitudesimpresion
        final ListaSolicitudesImpresionAdapter listaSolicitudesImpresionAdapter=new ListaSolicitudesImpresionAdapter();
        recyclerSolicitudes.setAdapter(listaSolicitudesImpresionAdapter);

        try{
            solicitudImpresionViewModel=new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(SolicitudImpresionViewModel.class);
            solicitudImpresionViewModel.getAllSolicitudesImpresion().observe(this, new Observer<List<SolicitudImpresion>>() {
                @Override
                public void onChanged(List<SolicitudImpresion> solicitudImpresions) {
                    listaSolicitudesImpresionAdapter.setListaSolicitudesImpresion(solicitudImpresions);
                    listaSolicitudesImpresionAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //custom alertDialog...
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "Error en el ViewModel",
                    Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
