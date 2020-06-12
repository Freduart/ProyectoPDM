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
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class ListaSolicitudesImpresionAdapter extends RecyclerView.Adapter<ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes> implements View.OnClickListener{

    List<SolicitudImpresion> listaSolicitudesImpresion = new ArrayList<>();
    View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public ListaSolicitudesImpresionAdapter(List<SolicitudImpresion> listaSolicitudesImpresion) {
        this.listaSolicitudesImpresion = listaSolicitudesImpresion;
    }

    @NonNull
    @Override
    public ViewHolderSolicitudes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_soliticud_impresion,parent,false);
        itemView.setOnClickListener(this);
        return new ViewHolderSolicitudes(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes holder, int position) {
        holder.textTitulo.setText(listaSolicitudesImpresion.get(position).getCarnetDocenteFK());
        holder.textDocumentos.setText(listaSolicitudesImpresion.get(position).getDocumento());
        holder.textEstado.setText(listaSolicitudesImpresion.get(position).getEstadoSolicitud());
        holder.textDetallesImpresion.setText(listaSolicitudesImpresion.get(position).getDetalleImpresion());
        String fechayhora=listaSolicitudesImpresion.get(position).getFechaSolicitud();
        String[] splitfecha=fechayhora.split(" ");
        String fecha=splitfecha[0],hora=splitfecha[1];
        holder.textFechaSolicitud.setText(fecha);
        holder.textHoraSolicitud.setText(hora);
    }

    @Override
    public int getItemCount() {
        return listaSolicitudesImpresion.size();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
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
