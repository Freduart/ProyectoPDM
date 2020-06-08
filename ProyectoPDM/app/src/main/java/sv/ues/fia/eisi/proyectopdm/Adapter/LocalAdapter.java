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

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalHolder> implements View.OnClickListener{

    private List<Local> locales = new ArrayList<>();
    private View.OnClickListener listener;

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
        }
    }

    //Implementación
    @NonNull
    @Override
    public LocalAdapter.LocalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_item,parent,false);
        itemView.setOnClickListener(this);
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

    public void setOnClickListener(View.OnClickListener listener){this.listener=listener;}
    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }
}
