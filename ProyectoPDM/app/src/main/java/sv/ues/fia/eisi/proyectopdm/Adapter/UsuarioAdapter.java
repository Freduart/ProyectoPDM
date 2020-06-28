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
import sv.ues.fia.eisi.proyectopdm.ViewModel.AccesoUsuarioViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Usuario;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolderUsuarios> {

    private AccesoUsuarioViewModel accesoUsuarioViewModel;
    private List<Usuario> usuarios=new ArrayList<>();
    private OnItemLongClickListener itemLongClickListener;

    public interface OnItemLongClickListener{
        void OnItemLongClick(int position,Usuario usuario);
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
        notifyDataSetChanged();
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolderUsuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario,parent,false);
        return new ViewHolderUsuarios(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapter.ViewHolderUsuarios holder, int position) {
        Usuario usuario=usuarios.get(position);
        holder.textIdUsuario.setText(String.valueOf(usuario.getIdUsuario()));
        holder.textNomUsuario.setText(usuario.getNombreUsuario());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(itemLongClickListener!=null && position != RecyclerView.NO_POSITION){
                    itemLongClickListener.OnItemLongClick(position,usuarios.get(position));
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolderUsuarios extends RecyclerView.ViewHolder {

        TextView textNomUsuario,textIdUsuario,textControlAcceso;

        public ViewHolderUsuarios(@NonNull View itemView) {
            super(itemView);
            textNomUsuario=(TextView)itemView.findViewById(R.id.textNomUsuario);
            textIdUsuario=(TextView)itemView.findViewById(R.id.tetxIdUsuario);
        }
    }
}
