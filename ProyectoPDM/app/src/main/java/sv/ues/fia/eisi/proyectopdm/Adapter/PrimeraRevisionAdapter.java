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

public class PrimeraRevisionAdapter extends RecyclerView.Adapter<PrimeraRevisionAdapter.PrimeraRevisionHolder> {
    private List<PrimeraRevision> prs = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;


    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(PrimeraRevision primeraRevision);
    }

    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener; }

    //listener para click largo
    public interface OnItemLongClickListener{
        void onItemLongClick(PrimeraRevision primeraRevision);
    }

    //set listener click largo
    public void setOnLongClickListener(OnItemLongClickListener longListener){
        this.longListener = longListener;
    }


    @NonNull
    @Override
    public PrimeraRevisionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.primerarevision_item, parent, false);
        return new PrimeraRevisionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrimeraRevisionHolder holder, int position) {
        PrimeraRevision currentPR = prs.get(position);

        holder.codPR.setText(String.valueOf(currentPR.getIdPrimerRevision()));
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





    class PrimeraRevisionHolder extends RecyclerView.ViewHolder{
        private TextView codPR;
        private TextView idDetalleEvFK;


        public PrimeraRevisionHolder(@NonNull View itemView) {
            super(itemView);
            codPR = itemView.findViewById(R.id.codigoPR);
            idDetalleEvFK = itemView.findViewById(R.id.detalleEvFK);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();
                    if (listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemClick(prs.get(posicion));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int posicion = getAdapterPosition();
                    if (longListener != null && posicion != RecyclerView.NO_POSITION){
                        longListener.onItemLongClick(prs.get(posicion));
                        return true;
                    }else
                        return false;
                }
            });
        }
    }

}
