package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.Activity.ItemClickListenerImpresion;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class ListaSolicitudesImpresionAdapter extends RecyclerView.Adapter<ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes>{

    List<SolicitudImpresion> listaSolicitudesImpresion = new ArrayList<>();
    ItemClickListenerImpresion itemClickListener;

    public void setOnItemClickListener(ItemClickListenerImpresion itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ListaSolicitudesImpresionAdapter(List<SolicitudImpresion> listaSolicitudesImpresion) {
        this.listaSolicitudesImpresion = listaSolicitudesImpresion;
    }

    @NonNull
    @Override
    public ViewHolderSolicitudes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_soliticud_impresion,parent,false);
        return new ViewHolderSolicitudes(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes holder, final int position) {
        holder.textTitulo.setText(listaSolicitudesImpresion.get(position).getCarnetDocenteFK());
        holder.textDocumentos.setText(listaSolicitudesImpresion.get(position).getDocumento());
        holder.textEstado.setText(listaSolicitudesImpresion.get(position).getEstadoSolicitud());
        holder.textDetallesImpresion.setText(listaSolicitudesImpresion.get(position).getDetalleImpresion());
        String fechayhora=listaSolicitudesImpresion.get(position).getFechaSolicitud();
        String[] splitfecha=fechayhora.split(" ");
        String fecha=splitfecha[0],hora=splitfecha[1];
        holder.textFechaSolicitud.setText(fecha);
        holder.textHoraSolicitud.setText(hora);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.OnItemClick(position,listaSolicitudesImpresion.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaSolicitudesImpresion.size();
    }

    public class ViewHolderSolicitudes extends RecyclerView.ViewHolder {

        TextView textTitulo,textDocumentos,textEstado,textDetallesImpresion,textFechaSolicitud,textHoraSolicitud;

        public ViewHolderSolicitudes(@NonNull View itemView) {
            super(itemView);
            textTitulo=(TextView)itemView.findViewById(R.id.textTitulo);
            textDocumentos=(TextView)itemView.findViewById(R.id.textDocumentos);
            textEstado=(TextView)itemView.findViewById(R.id.textEstado);
            textDetallesImpresion=(TextView)itemView.findViewById(R.id.textDetallesDeImpresion);
            textFechaSolicitud=(TextView)itemView.findViewById(R.id.textFechaSolicitud);
            textHoraSolicitud=(TextView)itemView.findViewById(R.id.textHoraSolicitud);
        }
    }
}
