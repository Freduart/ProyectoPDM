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
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;

public class AreaAdmAdapter extends RecyclerView.Adapter<AreaAdmAdapter.AreaAdmHolder> {
    private List<AreaAdm> areasAdm=new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    //clase Holder
    class AreaAdmHolder extends RecyclerView.ViewHolder{
        private TextView nombreAreaAdm;

        public AreaAdmHolder(@NonNull View itemView) {
            super(itemView);
            //asocia los elementos de los items de lista con los atriutos de la clase interna
            nombreAreaAdm=itemView.findViewById(R.id.text_areaadm_nom);

            //settea evento de click CORTO
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemClick(areasAdm.get(posicion));
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
                        longListener.onItemLongClick(areasAdm.get(posicion));
                        return true;
                    } else
                        return false;
                }
            });
        }
    }

    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(AreaAdm areaAdm);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    //listener para click LARGO
    public interface OnItemLongClickListener{
        void onItemLongClick(AreaAdm areaAdm);
    }
    //set listener click LARGO
    public void setOnLongClickListener(OnItemLongClickListener longListener){
        this.longListener = longListener;
    }

    //implementacion interfaz
    @NonNull
    @Override
    public AreaAdmAdapter.AreaAdmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item view
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.areaadm_item,parent,false);
        return new AreaAdmHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaAdmAdapter.AreaAdmHolder holder, int position) {
        //obtener areaAdm en la posicion actual
        AreaAdm areaAdmActual = areasAdm.get(position);

        //obtener id de la areaAdm actual
        int id = areaAdmActual.getIdDeptarmento();
        //settea los datos que se mostraran en los elementos de los items de lista
        holder.nombreAreaAdm.setText(id + ". " + areaAdmActual.getNomDepartamento());
    }

    @Override
    public int getItemCount() {
        return areasAdm.size();
    }

    public void setAreaAdmes(List<AreaAdm>areasAdm){
        this.areasAdm=areasAdm;
        notifyDataSetChanged();
    }
}
