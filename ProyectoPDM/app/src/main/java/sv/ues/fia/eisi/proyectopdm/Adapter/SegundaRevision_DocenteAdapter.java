package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevision_DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision_Docente;

public class SegundaRevision_DocenteAdapter extends RecyclerView.Adapter<SegundaRevision_DocenteAdapter.SegundaRevision_DocenteHolder> {
    private List<SegundaRevision_Docente> segundaRevision_docentes=new ArrayList<>();
    private OnItemLongClickListener listener;
    private SegundaRevision_DocenteViewModel segundaRevision_docenteViewModel;
    private DocenteViewModel docenteViewModel;

    //listener para click corto
    public interface OnItemLongClickListener{
        void onItemLongClick(SegundaRevision_Docente segundaRevision_docente);
    }

    //set listener click corto
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.listener = listener;
    }

    public void setRelacionSR_DAdapter(List<SegundaRevision_Docente> rels, DocenteViewModel dvm, SegundaRevision_DocenteViewModel sr_dvm){
        this.segundaRevision_docentes=rels;
        this.docenteViewModel=dvm;
        this.segundaRevision_docenteViewModel=sr_dvm;
    }

    @Override
    public void onBindViewHolder(@NonNull SegundaRevision_DocenteHolder holder, int position) {
        SegundaRevision_Docente relacionActual = segundaRevision_docentes.get(position);
        try{
            Docente docente = docenteViewModel.getDocente(relacionActual.getCarnetDocenteFK());
            Cargo cargo = segundaRevision_docenteViewModel.obtenerCargosDeDocentesEnSegundaRevision(docente.getCarnetDocente()).get(0);
            holder.nombreDocente.setText(String.format("%s, %s",docente.getApellidoDocente(),docente.getNomDocente()));
            holder.carnetDocente.setText(docente.getCarnetDocente());
            holder.cargoDocente.setText(cargo.getNomCargo());
        } catch (Exception e){
            e.fillInStackTrace();
        }
    }

    @NonNull
    @Override
    public SegundaRevision_DocenteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.segundarevision_docenteitem,parent,false);
        return new SegundaRevision_DocenteHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return segundaRevision_docentes.size();
    }

    //clase holder
    class SegundaRevision_DocenteHolder extends RecyclerView.ViewHolder {
        private TextView nombreDocente;
        private TextView carnetDocente;
        private TextView cargoDocente;

        public SegundaRevision_DocenteHolder(@NonNull View itemView) {
            super(itemView);
            nombreDocente=itemView.findViewById(R.id.disp_docente_segunda_rev);
            carnetDocente=itemView.findViewById(R.id.disp_carnet_segunda_rev);
            cargoDocente=itemView.findViewById(R.id.disp_cargo_docente_segunda_rev);

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemLongClick(segundaRevision_docentes.get(posicion));
                    return true;
                }
            });
        }
    }
}
