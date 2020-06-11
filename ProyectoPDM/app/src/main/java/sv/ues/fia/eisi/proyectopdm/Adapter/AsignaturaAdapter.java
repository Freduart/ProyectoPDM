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
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;

public class AsignaturaAdapter extends RecyclerView.Adapter<AsignaturaAdapter.AsignaturaHolder> implements View.OnClickListener{

    private List<Asignatura> asignaturas = new ArrayList<>();

    private View.OnClickListener listener;

    @NonNull
    @Override
    public AsignaturaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.asignatura_item,parent,false);
        itemView.setOnClickListener(this);
        return new AsignaturaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AsignaturaHolder holder, int position) {
        Asignatura currentAsignatura = asignaturas.get(position);
        /*
            Con el objeto holder recibimos objetos de tipo String y
            mostramos los valores en cada uno de los TextView y se ciclen con el LiveData
            se declara uno por cada item que querramos mostar en la cardview del recyclerView
        */
        holder.idAs.setText(currentAsignatura.getCodigoAsignatura());
        holder.idDpto.setText(String.valueOf(currentAsignatura.getIdDepartamentoFK()));
        holder.nomAs.setText(currentAsignatura.getNomasignatura());
    }

    @Override
    public int getItemCount() {
        return asignaturas.size();
    }

    public void setAsignaturas(List<Asignatura> asignaturas){
        this.asignaturas = asignaturas;
        notifyDataSetChanged();
    }

    //Para obtener asignatura en una posicion especifica de la lista
    public Asignatura getAsignaturaAt(int position){
        return asignaturas.get(position);
    }



    public void setOnClickListener(View.OnClickListener listener){this.listener=listener;}
    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    class AsignaturaHolder extends RecyclerView.ViewHolder {
        private TextView idAs;
        private TextView idDpto;
        private TextView nomAs;
        public AsignaturaHolder(@NonNull View itemView) {
            super(itemView);
            idAs = itemView.findViewById(R.id.idAsignatura);
            idDpto = itemView.findViewById(R.id.idDepartamento);
            nomAs = itemView.findViewById(R.id.nombreAsignatura);
        }
    }
}
