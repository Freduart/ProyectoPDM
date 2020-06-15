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
import sv.ues.fia.eisi.proyectopdm.db.entity.EncargadoImpresion;

public class EncargadoImpresionAdapter extends RecyclerView.Adapter<EncargadoImpresionAdapter.ViewHolderEncImpres> {

    private List<EncargadoImpresion> listEncImpres=new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongCLickListener;

    public interface OnItemClickListener{
        void OnItemClick(EncargadoImpresion encargadoImpresion);
    }

    public interface OnItemLongClickListener{
        void OnItemLongClick(EncargadoImpresion encargadoImpresion);
    }

    public void setListEncImpres(List<EncargadoImpresion> listEncImpres) {
        this.listEncImpres = listEncImpres;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongCLickListener(OnItemLongClickListener itemLongCLickListener) {
        this.itemLongCLickListener = itemLongCLickListener;
    }

    @NonNull
    @Override
    public ViewHolderEncImpres onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.enc_impres_item,parent,false);
        return new ViewHolderEncImpres(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEncImpres holder, int position) {
        EncargadoImpresion encargadoImpresion=listEncImpres.get(position);
        holder.textTitulo.setText(encargadoImpresion.getNomEncargado());
        holder.textIdEnc.setText(Integer.toString(encargadoImpresion.getIdEncargadoImpresion()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null && position != RecyclerView.NO_POSITION){
                    itemClickListener.OnItemClick(listEncImpres.get(position));
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(itemLongCLickListener!=null && position != RecyclerView.NO_POSITION){
                    itemLongCLickListener.OnItemLongClick(listEncImpres.get(position));
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEncImpres.size();
    }

    public class ViewHolderEncImpres extends RecyclerView.ViewHolder {

        TextView textTitulo,textIdEnc;

        public ViewHolderEncImpres(@NonNull View itemView) {
            super(itemView);
            textTitulo=(TextView)itemView.findViewById(R.id.textNomEnc);
            textIdEnc=(TextView)itemView.findViewById(R.id.textIdEncImpres);
        }
    }
}
