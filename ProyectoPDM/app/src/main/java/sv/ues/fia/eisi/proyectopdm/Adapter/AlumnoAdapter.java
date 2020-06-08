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

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoHolder> {
    private List<Alumno> alumnos = new ArrayList<>();

    @NonNull
    @Override
    public AlumnoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.alumno_item, parent, false);
        return new AlumnoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoHolder holder, int position) {
        Alumno currentAlumno = alumnos.get(position);
        holder.carnet.setText(currentAlumno.getCarnetAlumno());
        holder.nombre.setText(currentAlumno.getNombre());
        holder.apellido.setText(currentAlumno.getApellido());
        holder.carrera.setText(currentAlumno.getCarrera());
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    public void setAlumnos(List<Alumno> alumnos){
        this.alumnos = alumnos;
        notifyDataSetChanged();
    }

    class AlumnoHolder extends RecyclerView.ViewHolder{
        private TextView carnet;
        private TextView nombre;
        private TextView apellido;
        private TextView carrera;

        public AlumnoHolder(@NonNull View itemView) {
            super(itemView);
            carnet = itemView.findViewById(R.id.tw1);
            nombre = itemView.findViewById(R.id.nomAlum);
            apellido = itemView.findViewById(R.id.apeAlum);
            carrera = itemView. findViewById(R.id.nombreCarrera);
        }
    }
}
