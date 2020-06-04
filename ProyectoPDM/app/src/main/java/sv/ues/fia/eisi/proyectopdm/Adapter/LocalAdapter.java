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

    @NonNull
    @Override
    public LocalAdapter.LocalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_item,parent,false);
        return new LocalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalAdapter.LocalHolder holder, int position) {
        Local local=locales.get(position);
        holder.codigo.setText(local.getIdLocal());
        holder.ubicacion.setText(local.getUbicacion());
        holder.latitud.setText(String.valueOf(local.getLatitud()));
        holder.longitud.setText(String.valueOf(local.getLongitud()));
    }

    @Override
    public int getItemCount() {
        return locales.size();
    }

    class LocalHolder extends RecyclerView.ViewHolder{
        private TextView codigo;
        private TextView ubicacion;
        private TextView latitud;
        private TextView longitud;

        public LocalHolder(@NonNull View itemView){
            super(itemView);
            codigo=itemView.findViewById(R.id.idLocal);
            ubicacion=itemView.findViewById(R.id.ubLocal);
            latitud=itemView.findViewById(R.id.latLocal);
            longitud=itemView.findViewById(R.id.logLocal);
        }
    }
}
