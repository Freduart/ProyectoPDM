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
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;

public class SolicitudExtraordinarioAdapter extends RecyclerView.Adapter<SolicitudExtraordinarioAdapter.SolicitudExtraordinarioHolder> {

    private List<SolicitudExtraordinario> solicitudesExtra = new ArrayList<>();

    @NonNull
    @Override
    public SolicitudExtraordinarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.soli_extra_item, parent, false);
        return new SolicitudExtraordinarioHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudExtraordinarioHolder holder, int position){
        SolicitudExtraordinario currentSoliExtra = solicitudesExtra.get(position);
    }

    @Override
    public int getItemCount() {
        return solicitudesExtra.size();
    }

    public void setAlumnos(List<SolicitudExtraordinario> solicitudesExtra){
        this.solicitudesExtra = solicitudesExtra;
        notifyDataSetChanged();
    }

    class SolicitudExtraordinarioHolder extends RecyclerView.ViewHolder{
        private TextView idSolicitud;
        private TextView carnetAlumno;
        private TextView idEvaluacion;
        private TextView tipoSolicitud;
        private TextView motivoSolicitud;
        private TextView fechaSolicitud;
        private TextView justificacion;

        public SolicitudExtraordinarioHolder(@NonNull View itemView){
            super(itemView);
            idSolicitud = itemView.findViewById(R.id.idSoliExtra);
            carnetAlumno = itemView.findViewById(R.id.seCarnetAlumno);
            idEvaluacion = itemView.findViewById(R.id.seIdEva);
            tipoSolicitud = itemView.findViewById(R.id.seTipoSoli);
            motivoSolicitud = itemView.findViewById(R.id.seMotivo);
            fechaSolicitud = itemView.findViewById(R.id.seFecha);
            justificacion = itemView.findViewById(R.id.seJusti);
        }
    }
}
