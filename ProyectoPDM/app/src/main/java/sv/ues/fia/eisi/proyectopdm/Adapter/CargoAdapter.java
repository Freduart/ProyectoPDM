package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.Activity.CargoActivity;
import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;

public class CargoAdapter extends RecyclerView.Adapter<CargoAdapter.CargoHolder> {
    private List<Cargo> cargos =  new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;
    private AreaAdmViewModel areaAdmViewModel;

    public void setAreaAdmViewModel(AreaAdmViewModel areaAdmViewModel) {
        this.areaAdmViewModel = areaAdmViewModel;
    }

    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(Cargo cargo);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener =listener;
    }
    //listener para click largo
    public interface OnItemLongClickListener{
        void onItemLongClick(Cargo cargo);
    }
    //set listener click largo
    public void setOnLongClickListner(OnItemLongClickListener longListener){
        this.longListener=longListener;
    }

    @NonNull
    @Override
    public CargoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cargo_item,parent,false);

        return new CargoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CargoHolder holder, int position) {
        Cargo currentCargo = cargos.get(position);
        /*
            Con el objeto holder recibimos objetos de tipo String y
            mostramos los valores en cada uno de los TextView y se ciclen con el LiveData
            Haremos uno por cada item que querramos mostar
        */
        try {
            holder.idCargo.setText(String.valueOf(currentCargo.getIdCargo()));
            String areaAdmin=areaAdmViewModel.getAreaAdm(currentCargo.getIdAreaAdminFK()).getNomDepartamento();
            holder.idEscuela.setText(areaAdmin);
            holder.nomCargo.setText(currentCargo.getNomCargo());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cargos.size();
    }

    public void setCargos(List<Cargo> cargos){
        this.cargos = cargos;
        notifyDataSetChanged();
    }


    class CargoHolder extends  RecyclerView.ViewHolder{
        private TextView idCargo;
        private TextView idEscuela;
        private TextView nomCargo;

        public CargoHolder(@NonNull View itemView){
            super(itemView);
            idCargo = itemView.findViewById(R.id.idCar);
            idEscuela = itemView.findViewById(R.id.idEsc);
            nomCargo = itemView.findViewById(R.id.nomCar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Obtener  posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemClick(cargos.get(posicion));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(longListener != null && posicion != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(cargos.get(posicion));
                        return true;
                    } else
                        return false;
                }
            });
        }
    }
}
