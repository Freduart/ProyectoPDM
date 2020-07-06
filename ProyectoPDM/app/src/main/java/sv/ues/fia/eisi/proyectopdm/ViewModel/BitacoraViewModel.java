package sv.ues.fia.eisi.proyectopdm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import sv.ues.fia.eisi.proyectopdm.db.entity.Bitacora;
import sv.ues.fia.eisi.proyectopdm.repository.BitacoraRepository;

public class BitacoraViewModel extends AndroidViewModel {

    private BitacoraRepository bitacoraRepository;

    public BitacoraViewModel(@NonNull Application application) {
        super(application);
        bitacoraRepository=new BitacoraRepository(application);
    }

    public void insertarBitacora(Bitacora bitacora){
        bitacoraRepository.insertarBitacora(bitacora);
    }

    public List<Bitacora> obtenerBitacoraPorFecha() throws InterruptedException, ExecutionException, TimeoutException {
        return bitacoraRepository.obtenerBitacoraFecha();
    }
}
