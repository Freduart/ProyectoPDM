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
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
/*
    Las clases Adapter son las encargadas de la relacion entre lo logico y lo visual
 */

public class EscuelaAdapter extends RecyclerView.Adapter<EscuelaAdapter.EscuelaHolder> {

    private List<Escuela> escuelas=new ArrayList<>();

    @NonNull
    @Override
    public EscuelaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.escuela_item,parent,false);
        return new EscuelaHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EscuelaHolder holder, int position) {
        Escuela currentEscuela=escuelas.get(position);
        /*
            Con el objeto holder recibimos objetos de tipo String y
            mostramos los valores en cada uno de los TextView y se ciclen con el LiveData
            Haremos uno por cada item que querramos mostar
        */
        holder.nombre.setText(currentEscuela.getNomEscuela());

    }

    @Override
    public int getItemCount() {
        return escuelas.size();
    }

    public void setEscuelas(List<Escuela>escuelas){
        this.escuelas=escuelas;
        notifyDataSetChanged();
    }

    class EscuelaHolder extends RecyclerView.ViewHolder{
        private TextView nombre;

        public EscuelaHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.text1);
        }
    }
}
