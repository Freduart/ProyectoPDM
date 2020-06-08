package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sv.ues.fia.eisi.proyectopdm.R;

public class ListaSolicitudesImpresionAdapter extends RecyclerView.Adapter<ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes> {
    @NonNull
    @Override
    public ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_solicitud_impresion,null,false);
        return new ViewHolderSolicitudes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderSolicitudes extends RecyclerView.ViewHolder {
        public ViewHolderSolicitudes(@NonNull View itemView) {
            super(itemView);
        }
    }
}
