package sv.ues.fia.eisi.proyectopdm.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Adapter.ListaArchivosAdapter;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;

public class VerSolicitudImpresion extends Fragment {
    private static final String DOCUMENTOS = "documentos",CARNET = "carnetDocente",DOCDIREC = "docDirector",
            ENCIMPRES = "encImpres",IMPRESIONES = "nImpresiones",ANEXOS = "nAnexos",DETALLES = "detalleImpresion";
    private String documento,carnetDocenteParam,docDirectorParam,encImpresParam,nImpreisones,nAnexos,detalleImpresion;
    private DocenteViewModel docenteViewModel;

    public VerSolicitudImpresion() {
        // Required empty public constructor
    }

    public static VerSolicitudImpresion newInstance(String documento,String carnetDocenteParam,String docDirectorParam,String encImpresParam,String nImpreisones,String nAnexos,String detalleImpresion) {
        VerSolicitudImpresion fragment = new VerSolicitudImpresion();
        Bundle args = new Bundle();
        args.putString(DOCUMENTOS,documento);
        args.putString(CARNET,carnetDocenteParam);
        args.putString(DOCDIREC,docDirectorParam);
        args.putString(ENCIMPRES,encImpresParam);
        args.putString(IMPRESIONES,nImpreisones);
        args.putString(ANEXOS,nAnexos);
        args.putString(DETALLES,detalleImpresion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            documento=getArguments().getString(DOCUMENTOS);
            carnetDocenteParam=getArguments().getString(CARNET);
            docDirectorParam=getArguments().getString(DOCDIREC);
            encImpresParam=getArguments().getString(ENCIMPRES);
            nImpreisones=getArguments().getString(IMPRESIONES);
            nAnexos=getArguments().getString(ANEXOS);
            detalleImpresion=getArguments().getString(DETALLES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_ver_solicitud_impresion, container, false);
        TextView carnetDocente = (TextView) view.findViewById(R.id.textCarnetDocenteVer);
        TextView docDirector = (TextView) view.findViewById(R.id.textDocDirectorVer);
        TextView encImpres = (TextView) view.findViewById(R.id.textEncImpresVer);
        TextView textImpresiones = (TextView) view.findViewById(R.id.text_impresiones_ver);
        TextView textAnexos = (TextView) view.findViewById(R.id.text_anexos_ver);
        TextView textDetallesImpresion = (TextView) view.findViewById(R.id.text_detalleImpresion_ver);
        //RecyclerView
        RecyclerView recyclerArchivos = (RecyclerView) view.findViewById(R.id.recycler_archivos_ver);
        ArrayList<String> listaDocumentos = new ArrayList<>();
        listaDocumentos.add(documento);
        recyclerArchivos.setLayoutManager(new LinearLayoutManager(getContext()));
        ListaArchivosAdapter listaArchivosAdapter = new ListaArchivosAdapter(listaDocumentos);
        recyclerArchivos.setAdapter(listaArchivosAdapter);
        //Asignacion de parametros a vistas
        carnetDocente.setText(carnetDocenteParam);
        //Obtenemos el nombre y apellido del docente con el carnetDocenteFK
        docenteViewModel=new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
                .create(DocenteViewModel.class);
        try {
            String docenteDirector=docenteViewModel.getDocente(docDirectorParam).getNomDocente()+" "+
                    docenteViewModel.getDocente(docDirectorParam).getApellidoDocente();
            docDirector.setText(docenteDirector);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        encImpres.setText(encImpresParam);
        textImpresiones.setText(nImpreisones);
        textAnexos.setText(nAnexos);
        textDetallesImpresion.setText(detalleImpresion);
        return view;
    }
}
