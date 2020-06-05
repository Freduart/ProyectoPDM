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
import sv.ues.fia.eisi.proyectopdm.db.entity.Asignatura;

public class AsignaturaAdapter extends RecyclerView.Adapter<AsignaturaAdapter.AsignaturaHolder> {

    private List<Asignatura> asignaturas = new ArrayList<>();

    @NonNull
    @Override
    public AsignaturaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.asinatura_item,parent,false);
        return new AsignaturaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AsignaturaHolder holder, int position) {
        Asignatura currentAsignatura = asignaturas.get(position);
        holder.idAs.setText(currentAsignatura.getCodigoAsignatura());
        holder.idDpto.setText(currentAsignatura.getIdDepartamentoFK());
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
