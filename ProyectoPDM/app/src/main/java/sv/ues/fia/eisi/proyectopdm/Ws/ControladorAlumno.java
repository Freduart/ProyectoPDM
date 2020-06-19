package sv.ues.fia.eisi.proyectopdm.Ws;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;

public class ControladorAlumno {
    public List<Alumno> obtenerAlumnosGet(String url, Context context){
        List<Alumno> alumnoList=new ArrayList<>();
        try{

            //Instancia de conexion
            Conexion conexion=new Conexion();

            //Respuesta de conexion
            String response=conexion.execute(url).get();

            //Arreglo JSON a partir de la respuesta
            JSONArray arregloJson = new JSONArray(response);

            //Recorrer arreglo
            for(int i=0;i<=arregloJson.length();i++){
                //objeto json auxiliar (acá se vacía cada uno de los elementos del arreglo)
                JSONObject objetoJson = arregloJson.getJSONObject(i);
                //objeto alumno auxiliar
                Alumno alumnoAuxiliar=new Alumno();

                //Settear atributos del objeto
                alumnoAuxiliar.setCarnetAlumno(objetoJson.getString("CARNETALUMNO"));
                alumnoAuxiliar.setNombre(objetoJson.getString("NOMBRE"));
                alumnoAuxiliar.setApellido(objetoJson.getString("APELLIDO"));
                alumnoAuxiliar.setCorreo(objetoJson.getString("CORREO"));
                alumnoAuxiliar.setCarrera(objetoJson.getString("CARRERA"));
                alumnoAuxiliar.setIdUsuarioFk(objetoJson.getInt("IDUSUARIOFK"));

                alumnoList.add(alumnoAuxiliar);

            }


        }catch (Exception e){
            Log.d("Error en request Get", "obtenerEvaluacionesGet: "+e.getMessage(),e.fillInStackTrace());
            Toast.makeText(context, "Ocurrio un error con el servicio", Toast.LENGTH_SHORT).show();
        }
        return alumnoList;
    }
}
