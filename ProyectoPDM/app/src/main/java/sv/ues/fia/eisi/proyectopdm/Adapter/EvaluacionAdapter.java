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
    private OnItemLongClickListener longListener;

    //clase Holder
    class EvaluacionHolder extends RecyclerView.ViewHolder{
        private TextView nombreEvaluacion;
        private TextView descripcionEvaluacion;
        private TextView fechaFin;

        public EvaluacionHolder(@NonNull View itemView) {
            super(itemView);
            //asocia los elementos de los items de lista con los atriutos de la clase interna
            nombreEvaluacion=itemView.findViewById(R.id.text_eval_nom);
            descripcionEvaluacion=itemView.findViewById(R.id.text_eval_mat);
            fechaFin=itemView.findViewById(R.id.text_eval_fechaFin);

            //settea evento de click CORTO
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

            //settea evento de click LARGO
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(longListener != null && posicion != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(evaluaciones.get(posicion));
                        return true;
                    } else
                        return false;
                }
            });
        }
    }

    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(Evaluacion evaluacion);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    //listener para click LARGO
    public interface OnItemLongClickListener{
        void onItemLongClick(Evaluacion evaluacion);
    }
    //set listener click LARGO
    public void setOnLongClickListener(OnItemLongClickListener longListener){
        this.longListener = longListener;
    }

    //implementacion interfaz
    @NonNull
    @Override
    public EvaluacionAdapter.EvaluacionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item view
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evaluacion_item,parent,false);
        return new EvaluacionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluacionAdapter.EvaluacionHolder holder, int position) {
        //obtener evaluacion en la posicion actual
        Evaluacion evaluacionActual = evaluaciones.get(position);
        //obtener id de la evaluacion actual
        int id = evaluacionActual.getIdEvaluacion();
        //settea los datos que se mostraran en los elementos de los items de lista
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
