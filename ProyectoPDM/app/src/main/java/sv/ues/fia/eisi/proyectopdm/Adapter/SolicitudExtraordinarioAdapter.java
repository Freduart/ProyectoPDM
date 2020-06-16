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

public class SolicitudExtraordinarioAdapter extends RecyclerView.Adapter<SolicitudExtraordinarioAdapter.SolicitudExtraordinarioHolder>{

    private List<SolicitudExtraordinario> solicitudesExtra = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    //listener para click corto
    public interface OnItemClickListener{
        void onItemClick(SolicitudExtraordinario solicitudExtraordinario);
    }
    //set listener click corto
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener =listener;
    }
    //listener para click largo
    public interface OnItemLongClickListener{
        void onItemLongClick(SolicitudExtraordinario solicitudExtraordinario);
    }
    //set listener click largo
    public void setOnLongClickListner(OnItemLongClickListener longListener){
        this.longListener=longListener;
    }

    class SolicitudExtraordinarioHolder extends RecyclerView.ViewHolder {
        private TextView idSolicitud;
        private TextView carnetAlumno;
        private TextView idEvaluacion;
        private TextView tipoSolicitud;
        private TextView motivoSolicitud;
        private TextView fechaSolicitud;
        private TextView justificacion;

        public SolicitudExtraordinarioHolder(@NonNull View itemView) {
            super(itemView);
            idSolicitud = itemView.findViewById(R.id.idSoliExtra);
            carnetAlumno = itemView.findViewById(R.id.seCarnetAlumno);
            idEvaluacion = itemView.findViewById(R.id.seIdEva);
            tipoSolicitud = itemView.findViewById(R.id.seTipoSoli);
            motivoSolicitud = itemView.findViewById(R.id.seMotivo);
            fechaSolicitud = itemView.findViewById(R.id.seFecha);
            justificacion = itemView.findViewById(R.id.seJusti);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Obtener  posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(listener != null && posicion != RecyclerView.NO_POSITION)
                        listener.onItemClick(solicitudesExtra.get(posicion));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //obtener posicion
                    int posicion = getAdapterPosition();
                    //validar
                    if(longListener != null && posicion != RecyclerView.NO_POSITION) {
                        longListener.onItemLongClick(solicitudesExtra.get(posicion));
                        return true;
                    } else
                        return false;
                }
            });
        }
    }

        @NonNull
        @Override
        public SolicitudExtraordinarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.soli_extra_item, parent, false);
            return new SolicitudExtraordinarioHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SolicitudExtraordinarioHolder holder, int position) {
            SolicitudExtraordinario currentSoliExtra = solicitudesExtra.get(position);
            /*
            Con el objeto holder recibimos objetos de tipo String y
            mostramos los valores en cada uno de los TextView y se ciclen con el LiveData
            se declara uno por cada item que querramos mostar en la cardview del recyclerView
            */
            holder.idSolicitud.setText(String.valueOf(currentSoliExtra.getIdSolicitud()));
            holder.carnetAlumno.setText(currentSoliExtra.getCarnetAlumnoFK());
            holder.idEvaluacion.setText(String.valueOf(currentSoliExtra.getIdEvaluacion()));
            holder.tipoSolicitud.setText(String.valueOf(currentSoliExtra.getTipoSolicitud()));
            holder.motivoSolicitud.setText(currentSoliExtra.getMotivoSolicitud());
            holder.fechaSolicitud.setText(currentSoliExtra.getFechaSolicitudExtr());
            holder.justificacion.setText(String.valueOf(currentSoliExtra.isJustificacion()));
        }

    @Override
    public int getItemCount() {
        return solicitudesExtra.size();
    }

    public void setSolicitudesExtra(List<SolicitudExtraordinario> solicitudesExtra) {
        this.solicitudesExtra = solicitudesExtra;
        notifyDataSetChanged();
    }

    //Para obtener un Ciclo en una posición específica
    public SolicitudExtraordinario getSoliExtraAt(int position) {
        return solicitudesExtra.get(position);
    }
}
