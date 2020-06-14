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
import sv.ues.fia.eisi.proyectopdm.db.entity.Ciclo;

public class CicloAdapter extends RecyclerView.Adapter<CicloAdapter.CicloHolder>{

    private List<Ciclo> ciclos = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(Ciclo ciclo);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener =listener;
    }
    //listener para click largo
    public interface OnItemLongClickListener{
        void onItemLongClick(Ciclo ciclo);
    }
    //set listener click largo
    public void setOnLongClickListner(OnItemLongClickListener longListener){
        this.longListener=longListener;
    }

    //Class Holder
    class CicloHolder extends RecyclerView.ViewHolder{
        private TextView idCiclo;
        private TextView nomCiclo;
        private TextView fechaDesde;
        private TextView fechaHasta;

        public CicloHolder(@NonNull View itemView){
            super(itemView);
            idCiclo=itemView.findViewById(R.id.idCiclo);
            nomCiclo=itemView.findViewById(R.id.nomCiclo);
            fechaDesde=itemView.findViewById(R.id.fechaDesde);
            fechaHasta=itemView.findViewById(R.id.fechaHasta);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Obtener  posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemClick(ciclos.get(posicion));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(longListener != null && posicion != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(ciclos.get(posicion));
                        return true;
                    } else
                        return false;
                }
            });
        }
    }

    //Implementación
    @NonNull
    @Override
    public CicloHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ciclo_item, parent, false);
        return new CicloHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CicloAdapter.CicloHolder holder, int position){
        Ciclo ciclo = ciclos.get(position);
        /*
            Con el objeto holder recibimos objetos de tipo String y
            mostramos los valores en cada uno de los TextView y se ciclen con el LiveData
            se declara uno por cada item que querramos mostar en la cardview del recyclerView
        */
        holder.idCiclo.setText(String.valueOf(ciclo.getIdCiclo()));
        holder.nomCiclo.setText(String.valueOf(ciclo.getNomCiclo()));
        holder.fechaDesde.setText(ciclo.getFechaDesde());
        holder.fechaHasta.setText(ciclo.getFechaHasta());
    }

    @Override
    public int getItemCount() {
        return ciclos.size();
    }

    public void setCiclos(List<Ciclo> ciclos){
        this.ciclos=ciclos;
        notifyDataSetChanged();
    }

    //Para obtener un Ciclo en una posición específica
    public Ciclo getCicloAt(int position){
        return ciclos.get(position);
    }

}
