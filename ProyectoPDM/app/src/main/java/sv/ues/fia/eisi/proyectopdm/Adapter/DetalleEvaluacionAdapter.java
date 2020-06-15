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

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EscuelaViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;

public class DetalleEvaluacionAdapter extends RecyclerView.Adapter<DetalleEvaluacionAdapter.DetalleEvaluacionHolder> {
    private List<DetalleEvaluacion> detalleEvaluaciones=new ArrayList<>();
    private OnItemClickListener shortListener;
    private OnItemLongClickListener listener;
    private AlumnoViewModel alumnoViewModel;
    private EscuelaViewModel escuelaViewModel;

    //clase Holder
    class DetalleEvaluacionHolder extends RecyclerView.ViewHolder{
        private TextView nombreAlumno;
        private TextView carnetAlumno;
        private TextView carreraAlumno;
        private TextView notaalumno;

        public DetalleEvaluacionHolder(@NonNull View itemView) {
            super(itemView);
            //asocia los elementos de los items de lista con los atriutos de la clase interna
            nombreAlumno=itemView.findViewById(R.id.disp_nombre_alumno_detalle);
            carnetAlumno=itemView.findViewById(R.id.disp_carnet_alumno_detalle);
            carreraAlumno=itemView.findViewById(R.id.disp_carrera_alumno_detalle);
            notaalumno=itemView.findViewById(R.id.disp_nota_detalle);
            //settea evento de click CORTO
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(shortListener != null && posicion != RecyclerView.NO_POSITION)
                        shortListener.onItemClick(detalleEvaluaciones.get(posicion));
                }
            });

            //settea evento de click LARGO
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemLongClick(detalleEvaluaciones.get(posicion));
                    return true;
                }
            });

        }
    }

    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(DetalleEvaluacion detalleEvaluacion);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.shortListener = listener;
    }

    //listener para click corto
    public interface OnItemLongClickListener{
        void onItemLongClick(DetalleEvaluacion detalleEvaluacion);
    }
    //set listener click corto
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.listener = listener;
    }


    //implementacion interfaz
    @NonNull
    @Override
    public DetalleEvaluacionAdapter.DetalleEvaluacionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item view
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detalleevaluacion_item,parent,false);
        return new DetalleEvaluacionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleEvaluacionAdapter.DetalleEvaluacionHolder holder, int position) {
        //obtener detalleEvaluacion en la posicion actual
        DetalleEvaluacion detalleEvaluacionActual = detalleEvaluaciones.get(position);
        try {
            Alumno alumnoActual = alumnoViewModel.getAlumn(detalleEvaluacionActual.getCarnetAlumnoFK());
            Escuela escuelaActual = escuelaViewModel.getEscuela(Integer.parseInt(alumnoActual.getCarrera()));
            //settea los datos que se mostraran en los elementos de los items de lista
            holder.nombreAlumno.setText(String.format("%s %s", alumnoActual.getNombre(), alumnoActual.getApellido()));
            holder.carnetAlumno.setText(detalleEvaluacionActual.getCarnetAlumnoFK());
            holder.carreraAlumno.setText(escuelaActual.getCarrera());
            if(detalleEvaluacionActual.getNota() == -1)
                holder.notaalumno.setText("-");
            else
                holder.notaalumno.setText(String.format("%.2f", detalleEvaluacionActual.getNota()));
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
        return detalleEvaluaciones.size();
    }

    public void setDetalleEvaluaciones(List<DetalleEvaluacion>detalleEvaluaciones, AlumnoViewModel alumnoViewModel, EscuelaViewModel escuelaViewModel){
        this.detalleEvaluaciones=detalleEvaluaciones;
        this.alumnoViewModel=alumnoViewModel;
        this.escuelaViewModel=escuelaViewModel;
        notifyDataSetChanged();
    }
}
