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
import sv.ues.fia.eisi.proyectopdm.db.entity.Cargo;

public class CargoAdapter extends RecyclerView.Adapter<CargoAdapter.CargoHolder> {
    private List<Cargo> cargos =  new ArrayList<>();

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
        holder.idCargo.setText(currentCargo.getIdCargo());
        holder.idEscuela.setText(currentCargo.getIdEscuelaFK());
        holder.nomCargo.setText(currentCargo.getNomCargo());
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
        }
    }
}