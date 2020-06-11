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
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class PrimeraRevisionAdapter extends RecyclerView.Adapter<PrimeraRevisionAdapter.PrimeraRevisionHolder> implements View.OnClickListener{
    private List<PrimeraRevision> prs = new ArrayList<>();
    private View.OnClickListener listener;

    @NonNull
    @Override
    public PrimeraRevisionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.primerarevision_item, parent, false);
        itemView.setOnClickListener(this);
        return new PrimeraRevisionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrimeraRevisionHolder holder, int position) {
        PrimeraRevision currentPR = prs.get(position);
        holder.codPR.setText(currentPR.getIdPrimerRevision());
        holder.idDetalleEvFK.setText(String.valueOf(currentPR.getIdDetalleEvFK()));
    }

    @Override
    public int getItemCount() {
        return prs.size();
    }

    public void setPrs(List<PrimeraRevision> prs){
        this.prs =prs;
        notifyDataSetChanged();
    }

    public PrimeraRevision getPrimeraRevisionAt(int position) {return prs.get(position);}

    public void setOnClickListener(View.OnClickListener listener){this.listener=listener;}

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    class PrimeraRevisionHolder extends RecyclerView.ViewHolder{
        private TextView codPR;
        private TextView idDetalleEvFK;

        public PrimeraRevisionHolder(@NonNull View itemView) {
            super(itemView);
            codPR = itemView.findViewById(R.id.codigoPR);
            idDetalleEvFK = itemView.findViewById(R.id.detalleEvFK);
        }
    }

}
