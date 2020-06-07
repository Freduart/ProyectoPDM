package sv.ues.fia.eisi.proyectopdm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sv.ues.fia.eisi.proyectopdm.R;

public class ListaArchivosAdapter extends RecyclerView.Adapter<ListaArchivosAdapter.ViewHolderArchivos> implements View.OnClickListener{

    ArrayList<String> listaDocumentos;
    private View.OnClickListener listener;

    public ListaArchivosAdapter(ArrayList<String> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    @NonNull
    @Override
    public ListaArchivosAdapter.ViewHolderArchivos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_archivos_impresion,null,false);
        view.setOnClickListener(this);
        return new ViewHolderArchivos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaArchivosAdapter.ViewHolderArchivos holder, int position) {
        //Obtenemos el nombre del documento a partir de la ruta dada.
        String ruta=listaDocumentos.get(position);
        String[] path=ruta.split("/");
        int posicion=0;
        posicion=path.length-1;
        String nomDoc=path[posicion];
        //Asignamos los datos al holder
        holder.nombreDocumento.setText(nomDoc);
        holder.rutaDocumento.setText(ruta);
    }

    @Override
    public int getItemCount() {
        return listaDocumentos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderArchivos extends RecyclerView.ViewHolder {

        TextView nombreDocumento,rutaDocumento;

        public ViewHolderArchivos(@NonNull View itemView) {
            super(itemView);
            nombreDocumento=(TextView)itemView.findViewById(R.id.textNombreDocumento);
            rutaDocumento=(TextView)itemView.findViewById(R.id.textRuta);
        }
    }
}
