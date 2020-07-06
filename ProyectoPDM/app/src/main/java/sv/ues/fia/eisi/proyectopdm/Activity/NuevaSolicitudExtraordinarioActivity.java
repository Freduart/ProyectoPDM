package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import sv.ues.fia.eisi.proyectopdm.Activity.GraficaEvaluacion.FragmentPastelAprobacion;
import sv.ues.fia.eisi.proyectopdm.R;

<<<<<<< HEAD
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
=======
>>>>>>> parent of c53d5a2... Revert "SincronizacioService"
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.TipoEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;

public class NuevaSolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinarioViewModel soliExtraVM;
    private TipoEvaluacionViewModel tipoEvaVM;
    private DocenteViewModel docenteVM;
    private EvaluacionViewModel evaluacionVM;

    private int id_usuario, rol_usuario, graficas;
    private String id_alum, id_eval;
    private String sMail;
    private String sPass;

    private EditText idAlumno;
    private EditText idEvaluacion;
    private Spinner tipoSoli;
    private EditText motivoSoli;
    private DatePicker dpFechaSoli;
    private CheckBox justiSoli;

    //Clase Privada para método de enviado de JavaMail API
    private class SendMail extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        private SendMail() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(NuevaSolicitudExtraordinarioActivity.this, "Por favor espere", "Enviando Correo...", true, false);
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Message... messages) {
            try {
                //Método de JavaMail para envío
                Transport.send(messages[0]);
                //Mensaje de respuesta de éxito
                return "Exito";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            this.progressDialog.dismiss();
            if (s.equals("Exito")) {
                //Manda AlertDialog para avisar del enviado correcto del correo de notificación
                AlertDialog.Builder builder = new AlertDialog.Builder(NuevaSolicitudExtraordinarioActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Éxito</font>"));
                builder.setMessage("Solicitud Ingresada exitosamente. Correo de notificación enviado.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.show();
                return;
            }
            //Si no se envió correctamente, devuelve un Toast con el error
            Toast.makeText(NuevaSolicitudExtraordinarioActivity.this.getApplicationContext(), "¿Algo salió mal?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_solicitud_extraordinario);

            //Inicializa elementos del Layout en Activity
            idAlumno = (EditText) findViewById(R.id.editNCarnetAlumno);
            idEvaluacion = (EditText) findViewById(R.id.editNIdEvaluacion);
            tipoSoli = (Spinner) findViewById(R.id.editNTipoSoli);
            motivoSoli = (EditText) findViewById(R.id.editNMotivoSoliExtra);
            dpFechaSoli = (DatePicker) findViewById(R.id.editNFechaSoliExtra);
            justiSoli = (CheckBox) findViewById(R.id.NJustiSoliExtra);

            //Título personalizado para Activity
            setTitle("Nueva Solicitud Extraordinaria");

            //Carga datos del correo remitente de la notificación (A enviar usando JavaMail API)
            sMail = "pdmproyecto2020@gmail.com";
            sPass = "proyectopdm";

            final Bundle extras = getIntent().getExtras();
            if (extras != null){
                //Extras cuando se llega desde el menú
                id_usuario = extras.getInt(LoginActivity.ID_USUARIO);
                rol_usuario = extras.getInt(LoginActivity.USER_ROL);
                //Extras cuando se llega desde las gráficas a solicitar repetido
                graficas = extras.getInt(FragmentPastelAprobacion.KEY_IS_ROLE);
                id_alum = extras.getString(FragmentPastelAprobacion.KEY_ID_ESTUDIANTE);
                id_eval = extras.getString(FragmentPastelAprobacion.KEY_ID_EVALUACION);
            }

            //Inicializando VM para extraer datos para correo de notificación
            evaluacionVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            docenteVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);

            //Spinner Tipo evaluacion
            final ArrayList<String> tipoEvaluacionesNom = new ArrayList<>();
            final ArrayAdapter<String> adaptadorSpinnerTipoEval = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipoEvaluacionesNom);
            adaptadorSpinnerTipoEval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tipoSoli.setAdapter(adaptadorSpinnerTipoEval);
            tipoEvaVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);
            tipoEvaVM.getTodosTiposEvaluaciones().observe(this, new Observer<List<TipoEvaluacion>>() {
                @Override
                public void onChanged(@Nullable List<TipoEvaluacion> tiposEvaluaciones) {
                    try {
                        for (TipoEvaluacion x : tiposEvaluaciones) {
                            tipoEvaluacionesNom.add(x.getIdTipoEvaluacion()+ " - "+x.getTipoEvaluacion());
                            if(graficas==1){
                                //Si la Activity se ha cargado desde el Fragmento para solicitar repetido, se deja fija la opción
                                tipoSoli.setSelection(graficas);
                                tipoSoli.setEnabled(false);
                            }
                        }
                        adaptadorSpinnerTipoEval.notifyDataSetChanged();
                    } catch (Exception e){
                        Toast.makeText(NuevaSolicitudExtraordinarioActivity.this, "Detalle: "+e.getMessage()+ " - "+e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            //Si se carga la Actividad desde el fragmento de grafica para solicitar repetido, se setean los campos
            if(graficas==1){
                //Se importa el ID del Alumno
                idAlumno.setText(id_alum);
                //Se deshabilita el ET
                idAlumno.setEnabled(false);

                //Se importa el ID de la Evaluación
                idEvaluacion.setText(id_eval);
                //Se deshabilita el ET
                idEvaluacion.setEnabled(false);

                //Setea la Justificación para que exista y no se pueda cambiar
                justiSoli.setChecked(true);
                justiSoli.setClickable(false);

                //Se deja el motivo predeterminado
                motivoSoli.setText("Mejora de Calificación");
                //Se deshabilita el ET
                motivoSoli.setEnabled(false);
            }
        }catch(Exception e){
            Toast.makeText(NuevaSolicitudExtraordinarioActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarSoliExtra(){
        try {
            String carnetAlumno = idAlumno.getText().toString();
            int idEval = Integer.parseInt(idEvaluacion.getText().toString());
            String motivo = motivoSoli.getText().toString();

            //Inicializa el constructor de String para Fecha de Solicitud
            StringBuilder fechaBuilder = new StringBuilder(10);
            //Concatena los valores de fecha
            fechaBuilder.append(dpFechaSoli.getDayOfMonth()).append("-").append(dpFechaSoli.getMonth()+1).append("-").append(dpFechaSoli.getYear());
            //Asigna la cadena de texto desde el constructor de String
            String fecha = fechaBuilder.toString();

            boolean justi = justiSoli.isSelected();

            //---obtener valor de spinner TIPO EVALUACION
            String tipoEvalAux1 = tipoSoli.getSelectedItem().toString();
            String[] tipoEvalAux2 = tipoEvalAux1.split("-");
            //almacenar TIPO EVAL
            String tipoEvaAux3 = tipoEvalAux2[0].trim();
            int tipoEva = Integer.parseInt(tipoEvaAux3);

            //Obteniendo el correo del docente
            //Se extrae la evaluación correspondiente
            Evaluacion evaAux = evaluacionVM.getEval(idEval);
            //Se saca el id del docente evaluador
            String idDocenteAux = evaAux.getCarnetDocenteFK();
            //Se obtiene el docente
            Docente docenteAux = docenteVM.getDocente(idDocenteAux);
            //Se extrae el correo del docente
            String correoDocente = docenteAux.getCorreoDocente();

            //Construyendo mensaje de correo de notificación
            String mensajeAux = "El Alumno con carnet: " + carnetAlumno + " ha solicitado una evaluación " + tipoEvalAux2[1].trim();
            mensajeAux = mensajeAux + " Por favor revisar la solicitud a brevedad.";

            //Se verifica que no se seleccione Ordinario, por ser una Solicitud Extraordinaria
            if(tipoEva == 1){
                //Si se selecciona Ordinario, devuelve a la Activity anterior.
                Toast.makeText(NuevaSolicitudExtraordinarioActivity.this, "No puede seleccionar tipo Ordinario. Seleccione el tipo de evaluación extraordinaria que desea realizar", Toast.LENGTH_LONG).show();
            } else {
                //Se crea un objeto para ingresar el registro al VM
                SolicitudExtraordinario soliAux = new SolicitudExtraordinario(carnetAlumno, idEval, tipoEva, motivo, fecha, justi);

                //Se inicializa de nuevo el ViewModel
                soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);
                //Se inserta la Solicitud
                soliExtraVM.insert(soliAux);

                //Se setean propiedades para la conectividad del correo
                Properties properties = new Properties();
                String str = "true";
                properties.put("mail.smtp.auth", str);
                properties.put("mail.smtp.starttls.enable", str);
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                try {
                    //Se crea una instancia de Message para enviarla a la clase SendMail
                    Message message = new MimeMessage(Session.getInstance(properties, new Authenticator() {
                        public PasswordAuthentication getPasswordAuthentication() {
                            //Se autentifican los datos del remitente
                            return new PasswordAuthentication(sMail, sPass);
                        }
                    }));
                    //Seteamos remitente
                    message.setFrom(new InternetAddress(sMail));
                    //Seteamos destinatario (extraído desde alumnoActual)
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDocente.trim()));
                    //Seteamos Asunto
                    message.setSubject("Nueva Solicitud " + tipoEvalAux2[1].trim());
                    //Seteamos Mensaje del correo
                    message.setText(mensajeAux.trim());
                    //Se ejecuta la clase SendMail como una Tarea Asíncrona
                    new SendMail().execute(new Message[]{message});
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e){
            Toast.makeText(NuevaSolicitudExtraordinarioActivity.this, e.getMessage() + " " +
                    e.getCause(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nueva_soli_extra_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar_soli_extra:
                guardarSoliExtra();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
