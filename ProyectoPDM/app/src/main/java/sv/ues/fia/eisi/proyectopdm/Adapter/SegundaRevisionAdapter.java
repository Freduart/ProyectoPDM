package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.db.entity.SegundaRevision;

public class SegundaRevisionAdapter extends RecyclerView.Adapter<SegundaRevisionAdapter.SegundaRevisionHolder> {
    private List<SegundaRevision> segundasRevisiones=new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(SegundaRevision segundaRevision);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    //listener para click LARGO
    public interface OnItemLongClickListener{
        void onItemLongClick(SegundaRevision segundaRevision);
    }
    //set listener click LARGO
    public void setOnLongClickListener(OnItemLongClickListener longListener){
        this.longListener = longListener;
    }

    //implementacion interfaz
    @NonNull
    @Override
    public SegundaRevisionAdapter.SegundaRevisionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item view
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.segundarevision_item,parent,false);
        return new SegundaRevisionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SegundaRevisionAdapter.SegundaRevisionHolder holder, int position) {
        //obtener segundaRevision en la posicion actual
        SegundaRevision segundaRevisionActual = segundasRevisiones.get(position);

        //obtener id de la segundaRevision actual
        int id = segundaRevisionActual.getIdSegundaRevision();
        //settea los datos que se mostraran en los elementos de los items de lista
        holder.nombreSegundaRevision.setText(id + ". " + segundaRevisionActual.getIdPrimeraRevisionFK());
    }

    @Override
    public int getItemCount() {
        return segundasRevisiones.size();
    }

    public void setSegundaRevisiones(List<SegundaRevision>segundasRevisiones){
        this.segundasRevisiones=segundasRevisiones;
        notifyDataSetChanged();
    }

    //clase Holder
    class SegundaRevisionHolder extends RecyclerView.ViewHolder{
        private TextView nombreSegundaRevision;

        public SegundaRevisionHolder(@NonNull View itemView) {
            super(itemView);
            //asocia los elementos de los items de lista con los atriutos de la clase interna
            nombreSegundaRevision=itemView.findViewById(R.id.text_segundarevision_nom);

            //settea evento de click CORTO
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemClick(segundasRevisiones.get(posicion));
                }
            });

            //settea evento de click LARGO
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(longListener != null && posicion != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(segundasRevisiones.get(posicion));
                        return true;
                    } else
                        return false;
                }
            });
        }
    }
}
