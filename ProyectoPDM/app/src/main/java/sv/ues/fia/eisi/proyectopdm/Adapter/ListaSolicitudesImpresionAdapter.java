package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudImpresion;

public class ListaSolicitudesImpresionAdapter extends RecyclerView.Adapter<ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes>{

    private List<SolicitudImpresion> listaSolicitudesImpresion=new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener longListener;
    private DocenteViewModel docenteViewModel;

    public interface OnItemClickListener{
        void OnItemClick(int position,SolicitudImpresion solicitudImpresion);
    }

    public interface OnItemLongClickListener{
        void OnItemLongClick(int position, SolicitudImpresion solicitudImpresion);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnItemLongClickListener longListener) {
        this.longListener = longListener;
    }

    public void setListaSolicitudesImpresion(List<SolicitudImpresion> listaSolicitudesImpresion) {
        this.listaSolicitudesImpresion = listaSolicitudesImpresion;
        notifyDataSetChanged();
    }

    public void setDocenteViewModel(DocenteViewModel docenteViewModel) {
        this.docenteViewModel = docenteViewModel;
    }

    @NonNull
    @Override
    public ViewHolderSolicitudes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_soliticud_impresion,parent,false);
        return new ViewHolderSolicitudes(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSolicitudes holder, final int position) {
        String titulo="";
        try {
            Docente docente=docenteViewModel.getDocente(listaSolicitudesImpresion.get(position).getCarnetDocenteFK());
            titulo=docente.getNomDocente()+" "+docente.getApellidoDocente();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        holder.textTitulo.setText(titulo);
        String docImpresion=listaSolicitudesImpresion.get(position).getDocumento();
        //Obtenemos el nombre del documento...
        String[] docSplit=docImpresion.split("/");
        String nomDocumento=docSplit[docSplit.length-1];
        holder.textDocumentos.setText(nomDocumento);
        holder.textEstado.setText(listaSolicitudesImpresion.get(position).getEstadoSolicitud());
        if(listaSolicitudesImpresion.get(position).getEstadoSolicitud().equals("NUEVA")){
            holder.textEstado.setTextColor(Color.parseColor("#800000"));
        }else if(listaSolicitudesImpresion.get(position).getEstadoSolicitud().equals("APROBADA/\nEN CURSO")){
            holder.textEstado.setTextColor(Color.parseColor("#00FF00"));
        }else if(listaSolicitudesImpresion.get(position).getEstadoSolicitud().equals("REPROBADA")){
            holder.textEstado.setTextColor(Color.parseColor("#FF0000"));
        }else if(listaSolicitudesImpresion.get(position).getEstadoSolicitud().equals("FALLIDA")){
            holder.textEstado.setTextColor(Color.parseColor("#FF0000"));
        }else if(listaSolicitudesImpresion.get(position).getEstadoSolicitud().equals("TERMINADA")){
            holder.textEstado.setTextColor(Color.parseColor("#008000"));
        }else if(listaSolicitudesImpresion.get(position).getEstadoSolicitud().equals("MODIFICADA")){
            holder.textEstado.setTextColor(Color.parseColor("#000080"));
        }
        holder.textDetallesImpresion.setText(listaSolicitudesImpresion.get(position).getDetalleImpresion());
        String fechayhora=listaSolicitudesImpresion.get(position).getFechaSolicitud();
        String[] splitfecha=fechayhora.split(" ");
        String fecha=splitfecha[0],hora=splitfecha[1];
        holder.textFechaSolicitud.setText(fecha);
        holder.textHoraSolicitud.setText(hora);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null && position != RecyclerView.NO_POSITION){
                    itemClickListener.OnItemClick(position,listaSolicitudesImpresion.get(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(longListener != null && position != RecyclerView.NO_POSITION){
                    longListener.OnItemLongClick(position,listaSolicitudesImpresion.get(position));
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaSolicitudesImpresion.size();
    }

    class ViewHolderSolicitudes extends RecyclerView.ViewHolder {

        TextView textTitulo,textDocumentos,textEstado,textDetallesImpresion,textFechaSolicitud,textHoraSolicitud;

        public ViewHolderSolicitudes(@NonNull View itemView) {
            super(itemView);
            textTitulo=(TextView)itemView.findViewById(R.id.textNomEnc);
            textDocumentos=(TextView)itemView.findViewById(R.id.textIdEncImpres);
            textEstado=(TextView)itemView.findViewById(R.id.textTelefonoDocenteVer);
            textDetallesImpresion=(TextView)itemView.findViewById(R.id.textNomEncVer);
            textFechaSolicitud=(TextView)itemView.findViewById(R.id.textFechaSolicitud);
            textHoraSolicitud=(TextView)itemView.findViewById(R.id.textHoraSolicitud);
        }
    }
}
