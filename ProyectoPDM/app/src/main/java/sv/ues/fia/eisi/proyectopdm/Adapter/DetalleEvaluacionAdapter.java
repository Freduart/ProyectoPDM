package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;

public class DetalleEvaluacionAdapter extends RecyclerView.Adapter<DetalleEvaluacionAdapter.DetalleEvaluacionHolder> {
    private List<DetalleEvaluacion> detalles = new ArrayList<>();
    private View.OnClickListener listener;

    @NonNull
    @Override
    public DetalleEvaluacionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleEvaluacionHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setDetalles(List<DetalleEvaluacion> detalles){
        this.detalles = detalles;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener){this.listener=listener;}



    class DetalleEvaluacionHolder extends RecyclerView.ViewHolder{

        public DetalleEvaluacionHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
