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
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalHolder> {

    private List<Local> locales = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(Local local);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener =listener;
    }
    //listener para click largo
    public interface OnItemLongClickListener{
        void onItemLongClick(Local local);
    }
    //set listener click largo
    public void setOnLongClickListner(OnItemLongClickListener longListener){
        this.longListener=longListener;
    }

    //Class Holder
    class LocalHolder extends RecyclerView.ViewHolder{
        private TextView codigo;
        private TextView nombreLocal;
        private TextView ubicacion;
        private TextView latitud;
        private TextView longitud;

        public LocalHolder(@NonNull View itemView){
            super(itemView);
            codigo=itemView.findViewById(R.id.idLocal);
            nombreLocal=itemView.findViewById(R.id.nomLocal);
            ubicacion=itemView.findViewById(R.id.ubLocal);
            latitud=itemView.findViewById(R.id.latLocal);
            longitud=itemView.findViewById(R.id.logLocal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Obtener  posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemClick(locales.get(posicion));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(longListener != null && posicion != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(locales.get(posicion));
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
    public LocalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_item,parent,false);
        return new LocalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalAdapter.LocalHolder holder, int position) {
        Local local=locales.get(position);
        /*
            Con el objeto holder recibimos objetos de tipo String y
            mostramos los valores en cada uno de los TextView y se ciclen con el LiveData
            se declara uno por cada item que querramos mostar en la cardview del recyclerView
        */
        holder.codigo.setText(local.getIdLocal());
        holder.nombreLocal.setText(local.getNombreLocal());
        holder.ubicacion.setText(local.getUbicacion());
        holder.latitud.setText(String.valueOf(local.getLatitud()));
        holder.longitud.setText(String.valueOf(local.getLongitud()));
    }

    @Override
    public int getItemCount() {
        return locales.size();
    }

    public void setLocales(List<Local>locals){
        this.locales=locals;
        notifyDataSetChanged();
    }

    //Para obtener un Local en una posición específica
    public Local getLocalAt(int position){
        return locales.get(position);
    }

}
