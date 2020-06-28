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
import sv.ues.fia.eisi.proyectopdm.ViewModel.AreaAdmViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.CargoViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.AreaAdm;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;

public class DocenteAdapter extends RecyclerView.Adapter<DocenteAdapter.ViewHolderDocente> {

    private List<Docente> listDocentes=new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnItemLongCLickListener onItemLongCLickListener;
    private CargoViewModel cargoViewModel;
    private AreaAdmViewModel areaAdmViewModel;

    public interface OnItemClickListener{
        void OnItemClick(int position,Docente docente);
    }

    public interface OnItemLongCLickListener{
        void OnItemLongClick(int position,Docente docente);
    }

    public void setListDocentes(List<Docente> listDocentes) {
        this.listDocentes = listDocentes;
        notifyDataSetChanged();
    }

    public void setAreaAdmViewModel(AreaAdmViewModel areaAdmViewModel) {
        this.areaAdmViewModel = areaAdmViewModel;
    }

    public void setCargoViewModel(CargoViewModel cargoViewModel) {
        this.cargoViewModel = cargoViewModel;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongCLickListener(OnItemLongCLickListener onItemLongCLickListener) {
        this.onItemLongCLickListener = onItemLongCLickListener;
    }

    @NonNull
    @Override
    public ViewHolderDocente onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.docente_item,parent,false);
        return new ViewHolderDocente(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDocente holder, int position) {
        Docente docente=listDocentes.get(position);
        String nomDocente=docente.getNomDocente()+" "+docente.getApellidoDocente();
        holder.textTitulo.setText(nomDocente);
        try {
            String cargoDocente=cargoViewModel.getCargo(docente.getIdCargoFK()).getNomCargo()+" "+areaAdmViewModel.getAreaAdm(
                    cargoViewModel.getCargo(docente.getIdCargoFK()).getIdAreaAdminFK()).getNomDepartamento();
            holder.textCargoDocente.setText(cargoDocente);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        holder.textCorreoDocente.setText(docente.getCorreoDocente());
        holder.textCarnetDocente.setText(docente.getCarnetDocente());
        holder.textTelefonoDocente.setText(docente.getTelefonoDocente());
        //OnClickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null && position != RecyclerView.NO_POSITION){
                    onItemClickListener.OnItemClick(position,listDocentes.get(position));
                }
            }
        });
        //OnLongItemClickListener
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongCLickListener!=null && position != RecyclerView.NO_POSITION){
                    onItemLongCLickListener.OnItemLongClick(position,listDocentes.get(position));
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDocentes.size();
    }

    public class ViewHolderDocente extends RecyclerView.ViewHolder {

        TextView textTitulo,textCargoDocente,textCorreoDocente,textTelefonoDocente,textCarnetDocente;

        public ViewHolderDocente(@NonNull View itemView) {
            super(itemView);
            textTitulo=(TextView)itemView.findViewById(R.id.textNomDOcente);
            textCargoDocente=(TextView)itemView.findViewById(R.id.textCargoDocente);
            textCorreoDocente=(TextView)itemView.findViewById(R.id.textCorreoDocente);
            textTelefonoDocente=(TextView)itemView.findViewById(R.id.textTelefonoDocente);
            textCarnetDocente=(TextView)itemView.findViewById(R.id.textCarnetDocente);
        }
    }
}
