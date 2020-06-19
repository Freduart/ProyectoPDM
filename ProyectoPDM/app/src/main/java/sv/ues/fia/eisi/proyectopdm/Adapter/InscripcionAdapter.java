package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AsignaturaViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.InscripcionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Escuela;
import sv.ues.fia.eisi.proyectopdm.db.entity.Inscripcion;

public class InscripcionAdapter{ /*extends RecyclerView.Adapter<InscripcionAdapter.InscripcionHolder> {
    private List<Inscripcion>inscripcions=new ArrayList<>();
    private InscripcionViewModel inscripcionViewModel;
    private AlumnoViewModel alumnoViewModel;
    private AsignaturaViewModel asignaturaViewModel;


    @NonNull
    @Override
    public InscripcionHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_inscripcion,parent,false);
        return new InscripcionHolder(itemView);
    }

    class InscripcionHolder extends RecyclerView.ViewHolder{
        private TextView nombre;

        public InscripcionHolder(@NonNull View itemView) {
            super(itemView);
            nombre=itemView.findViewById(R.id.et_Carnet);
        }
    }*/
}
