package sv.ues.fia.eisi.proyectopdm.Ws;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;

public class ControladorEvaluacion {

    //método para obtener listado de la base
    public List<Evaluacion> obtenerEvaluacionesGet(String url, Context contexto){
        //Listado de evaluaciones de salida
        List<Evaluacion> evaluacionList = new ArrayList<>();
        try{
            //instanciar conexion
            Conexion conexion = new Conexion();
            //obtener respuesta de conexión
            String response = conexion.execute(url).get();
            //instanciar arreglo json a partir de respuesta
            JSONArray arregloJson = new JSONArray(response);
            //recorrer arreglo
            for(int i = 0; i < arregloJson.length(); i++){
                //objeto json auxiliar (acá se vacía cada uno de los elementos del arreglo)
                JSONObject objetoJson = arregloJson.getJSONObject(i);
                //objeto evaluacion auxiliar
                Evaluacion evaluacionAuxiliar = new Evaluacion();
                //settear atributos del objeto (Name: nombre de la columna en la base)
                evaluacionAuxiliar.setIdEvaluacion(objetoJson.getInt("IDEVALUACION"));
                evaluacionAuxiliar.setIdTipoEvaluacionFK(objetoJson.getInt("IDTIPOEVALUACIONFK"));
                evaluacionAuxiliar.setCodigoAsignaturaFK(objetoJson.getString("CODIGOASIGNATURAFK"));
                evaluacionAuxiliar.setCarnetDocenteFK(objetoJson.getString("CARNETDOCENTEFK"));
                evaluacionAuxiliar.setNomEvaluacion(objetoJson.getString("NOMEVALUACION"));
                evaluacionAuxiliar.setFechaInicio(objetoJson.getString("FECHAINICIO"));
                evaluacionAuxiliar.setFechaFin(objetoJson.getString("FECHAFIN"));
                evaluacionAuxiliar.setDescripcion(objetoJson.getString("DESCRIPCION"));
                evaluacionAuxiliar.setFechaEntregaNotas(objetoJson.getString("FECHAENTREGANOTAS"));
                evaluacionAuxiliar.setNumParticipantes(objetoJson.getInt("NUMPARTICIPANTES"));
                //se añade la evaluacion obtenida
                evaluacionList.add(evaluacionAuxiliar);
            }
        } catch (Exception e){
            Log.d("Error en request Get", "obtenerEvaluacionesGet: "+e.getMessage(),e.fillInStackTrace());
            Toast.makeText(contexto,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return evaluacionList;
    }

}
