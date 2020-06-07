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
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class EvaluacionAdapter extends RecyclerView.Adapter<EvaluacionAdapter.EvaluacionHolder> {
    private List<Evaluacion> evaluaciones=new ArrayList<>();

    //clase Holder
    class EvaluacionHolder extends RecyclerView.ViewHolder{
        private TextView nombreEval;
        private TextView nombreMateria;

        public EvaluacionHolder(@NonNull View itemView) {
            super(itemView);
            nombreEval=itemView.findViewById(R.id.text_eval_nom);
            nombreMateria=itemView.findViewById(R.id.text_eval_mat);
        }
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
        holder.nombreEval.setText(id + ". " + evaluacionActual.getNomEvaluacion());
        holder.nombreMateria.setText(evaluacionActual.getCodigoAsignaturaFK());
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
