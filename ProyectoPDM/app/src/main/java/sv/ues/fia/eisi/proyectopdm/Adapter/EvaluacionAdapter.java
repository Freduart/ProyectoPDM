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
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionAdapter extends RecyclerView.Adapter<EvaluacionAdapter.EvaluacionHolder> {
    private List<Evaluacion> evaluaciones=new ArrayList<>();
    private OnItemClickListener listener;

    //clase Holder
    class EvaluacionHolder extends RecyclerView.ViewHolder{
        private TextView nombreEvaluacion;
        private TextView descripcionEvaluacion;
        private TextView fechaFin;

        public EvaluacionHolder(@NonNull View itemView) {
            super(itemView);
            nombreEvaluacion=itemView.findViewById(R.id.text_eval_nom);
            descripcionEvaluacion=itemView.findViewById(R.id.text_eval_mat);
            fechaFin=itemView.findViewById(R.id.text_eval_fechaFin);

            //obtener evento de click
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemClick(evaluaciones.get(posicion));
                }
            });
        }
    }

    //listener

    public interface OnItemClickListener{
        void onItemClick(Evaluacion evaluacion);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    //implementacion
    @NonNull
    @Override
    public EvaluacionAdapter.EvaluacionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evaluacion_item,parent,false);
        return new EvaluacionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluacionAdapter.EvaluacionHolder holder, int position) {
        Evaluacion evaluacionActual = evaluaciones.get(position);
        int id = evaluacionActual.getIdEvaluacion();
        holder.nombreEvaluacion.setText(id + ". " + evaluacionActual.getNomEvaluacion());
        holder.descripcionEvaluacion.setText(evaluacionActual.getDescripcion());
        holder.fechaFin.setText(evaluacionActual.getFechaFin());
    }

    @Override
    public int getItemCount() {
        return evaluaciones.size();
    }

    public void setEvaluaciones(List<Evaluacion>evaluaciones){
        this.evaluaciones=evaluaciones;
        notifyDataSetChanged();
    }
}
