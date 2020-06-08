package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class ListaSolicitudesImpresionAdapter extends RecyclerView.Adapter<ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes> implements View.OnClickListener{

    List<SolicitudImpresion> listaSolicitudesImpresion;
    View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setListaSolicitudesImpresion(List<SolicitudImpresion> listaSolicitudesImpresion) {
        this.listaSolicitudesImpresion = listaSolicitudesImpresion;
    }

    @NonNull
    @Override
    public ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_soliticud_impresion,null,false);
        view.setOnClickListener(this);
        return new ViewHolderSolicitudes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes holder, int position) {
        holder.textTitulo.setText(listaSolicitudesImpresion.get(position).getDetalleImpresion());
        holder.textDocente.setText(listaSolicitudesImpresion.get(position).getCarnetDocenteFK());
        holder.textDocumentos.setText(listaSolicitudesImpresion.get(position).getDocumento());
        holder.textEstado.setText(listaSolicitudesImpresion.get(position).getEstadoSolicitud());
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

        TextView textTitulo,textDocente,textDocumentos,textEstado;

        public ViewHolderSolicitudes(@NonNull View itemView) {
            super(itemView);
            textTitulo=(TextView)itemView.findViewById(R.id.textTitulo);
            textDocente=(TextView)itemView.findViewById(R.id.textDocente);
            textDocumentos=(TextView)itemView.findViewById(R.id.textDocumentos);
            textEstado=(TextView)itemView.findViewById(R.id.textEstado);
        }
    }
}
