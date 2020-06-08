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

public class CicloAdapter extends RecyclerView.Adapter<CicloAdapter.CicloHolder> implements View.OnClickListener{

    private List<Ciclo> ciclos = new ArrayList<>();
    private View.OnClickListener listener;

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
        }
    }

    //Implementación
    @NonNull
    @Override
    public CicloAdapter.CicloHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ciclo_item, parent, false);
        itemView.setOnClickListener(this);
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
        holder.idCiclo.setText(ciclo.getIdCiclo());
        holder.nomCiclo.setText(ciclo.getNomCiclo());
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

    public void setOnClickListener(View.OnClickListener listener){this.listener=listener;}
    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }
}
