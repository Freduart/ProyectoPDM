package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaSolicitudesImpresionAdapter extends RecyclerView.Adapter<ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes> {
    @NonNull
    @Override
    public ListaSolicitudesImpresionAdapter.ViewHolderSolicitudes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
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
