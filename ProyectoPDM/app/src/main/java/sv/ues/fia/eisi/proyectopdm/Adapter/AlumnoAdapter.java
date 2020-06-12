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
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;



/*
Enlace para la pantalla para de Alumno
 */
public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoHolder> {
    private List<Alumno> alumnos = new ArrayList<>();

    //Listener necesarios para las pulsaciones en los items de la lista
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    //Enlace de codigo con pantalla
    class AlumnoHolder extends RecyclerView.ViewHolder {
        private TextView carnet;
        private TextView nombre;
        private TextView apellido;
        //private TextView carrera;
        private TextView correo;

        //Metodo Holder para la pantalla
        public AlumnoHolder(@NonNull View itemView) {
            super(itemView);
            carnet = itemView.findViewById(R.id.tw1);
            nombre = itemView.findViewById(R.id.nomAlum);
            apellido = itemView.findViewById(R.id.apeAlum);
            //carrera = itemView. findViewById(R.id.nombreCarrera);
            correo = itemView.findViewById(R.id.correo);

            //Acciones para los item de la lista, pulsacion corta
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Obtenemos la posicion
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(alumnos.get(position));
                }
            });


            //Accion para los item de la lista, pulsacion larga
            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    //Validacion
                    if (longListener != null && position != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(alumnos.get(position));
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    //Para pulsacion corta
    public interface OnItemClickListener{
        void onItemClick(Alumno alumno);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    //listener para click LARGO
    public interface OnItemLongClickListener{
        void onItemLongClick(Alumno alumno);
    }
    //set listener click LARGO
    public void setOnLongClickListener(OnItemLongClickListener longListener){
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public AlumnoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.alumno_item, parent, false);
        return new AlumnoHolder(itemView);
    }


    //Obtener datos a presentar en los items de la lista
    @Override
    public void onBindViewHolder(@NonNull AlumnoHolder holder, int position) {
        Alumno currentAlumno = alumnos.get(position);
        holder.carnet.setText(currentAlumno.getCarnetAlumno());
        holder.nombre.setText(currentAlumno.getNombre());
        holder.apellido.setText(currentAlumno.getApellido());
        //holder.carrera.setText(currentAlumno.getCarrera());
        holder.correo.setText(currentAlumno.getCorreo());

    }

    //Obtenemos su tamaño
    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    //Actualizamos las vistas en el momento
    public void setAlumnos(List<Alumno> alumnos){
        this.alumnos = alumnos;
        notifyDataSetChanged();
    }
}
