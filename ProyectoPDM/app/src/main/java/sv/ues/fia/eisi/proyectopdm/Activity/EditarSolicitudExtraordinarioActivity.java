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
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SolicitudExtraordinarioViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.TipoEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.SolicitudExtraordinario;
import sv.ues.fia.eisi.proyectopdm.db.entity.TipoEvaluacion;


public class EditarSolicitudExtraordinarioActivity extends AppCompatActivity {
    private SolicitudExtraordinario soliExtraActual;
    private Alumno alumnoActual;
    private Evaluacion evaActual;
    private TipoEvaluacion tipoEvaActual;

    private SolicitudExtraordinarioViewModel soliExtraVM;
    private AlumnoViewModel alumnoVM;
    private TipoEvaluacionViewModel tipoEvaVM;

    private EditText idAlumno;
    private EditText idEvaluacion;
    private Spinner tipoSoli;
    private EditText motivoSoli;
    private DatePicker dpFechaSoli;
    private CheckBox justiSoli;
    private CheckBox estadoSoli;

    private String sMail;
    private String sPass;

    //Clase Privada para método de enviado de JavaMail API
    private class SendMail extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        private SendMail() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(EditarSolicitudExtraordinarioActivity.this, "Por favor espere", "Enviando Correo...", true, false);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarSolicitudExtraordinarioActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Éxito</font>"));
                builder.setMessage("Cambios guardados. Correo de notificación enviado.");
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
            Toast.makeText(EditarSolicitudExtraordinarioActivity.this.getApplicationContext(), "¿Algo salió mal?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_solicitud_extraordinario);

            //Título personalizado para Activity
            setTitle("Editar Solicitud");

            //Inicializa elementos del Layout en Activity
            idAlumno = (EditText) findViewById(R.id.editCarnetAlumno);
            idEvaluacion = (EditText) findViewById(R.id.editIdEvaluacion);
            tipoSoli = (Spinner) findViewById(R.id.editTipoSoli);
            motivoSoli = (EditText) findViewById(R.id.editMotivoSoliExtra);
            dpFechaSoli = (DatePicker) findViewById(R.id.editFechaSoliExtra);
            justiSoli = (CheckBox) findViewById(R.id.JustiSoliExtra);
            estadoSoli = (CheckBox) findViewById(R.id.EstadoSoliExtra);

            //Carga datos del correo remitente de la notificación (A enviar usando JavaMail API)
            sMail = "pdmproyecto2020@gmail.com";
            sPass = "proyectopdm";

            //Inicializa el ViewModel
            soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);
            alumnoVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);

            //Se extrae el identificador de la solicitud a editar del Intent
            Bundle extras = getIntent().getExtras();
            int idSoliExtra = 0;
            if(extras != null){
                idSoliExtra = extras.getInt(SolicitudExtraordinarioActivity.IDENTIFICADOR_SOLI_EXTRA);
            }

            //Spinner Tipo evaluacion
            final ArrayList<String> tipoEvaluacionesNom = new ArrayList<>();
            final ArrayAdapter<String> adaptadorSpinnerTipoEval = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipoEvaluacionesNom);
            adaptadorSpinnerTipoEval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tipoSoli.setAdapter(adaptadorSpinnerTipoEval);
            tipoEvaVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TipoEvaluacionViewModel.class);
            tipoEvaVM.getTodosTiposEvaluaciones().observe(this, new Observer<List<TipoEvaluacion>>() {
                @Override
                public void onChanged(@Nullable List<TipoEvaluacion> tiposEvaluaciones) {
                    for (TipoEvaluacion x : tiposEvaluaciones) {
                        tipoEvaluacionesNom.add(x.getIdTipoEvaluacion()+" - " + x.getTipoEvaluacion());
                        if(x.getIdTipoEvaluacion()==soliExtraActual.getTipoSolicitud()){
                            //Se selecciona el tipo de evaluación que corresponda, y se deshabilita el spinner
                            tipoSoli.setSelection((soliExtraActual.getTipoSolicitud())-1);
                            tipoSoli.setEnabled(false);
                        }
                    }
                    adaptadorSpinnerTipoEval.notifyDataSetChanged();
                }
            });

            //Se asignan objetos extraídos del ViewModel
            soliExtraActual = soliExtraVM.getSoliExtra(idSoliExtra);
            evaActual = soliExtraVM.getEvaluacion(idSoliExtra);
            alumnoActual = soliExtraVM.getAlumno(idSoliExtra);
            tipoEvaActual = soliExtraVM.getTipoEval(idSoliExtra);

            //El DatePicker queda deshabilitado, la fecha no puede alterarse
            dpFechaSoli.setEnabled(false);

            //Se asignan los valores correspondientes en elementos del Layout
            idAlumno.setText(alumnoActual.getCarnetAlumno());
            idEvaluacion.setText(String.valueOf(evaActual.getIdEvaluacion()));
            motivoSoli.setText(soliExtraActual.getMotivoSolicitud());

            if(soliExtraActual.isJustificacion()){
                justiSoli.isChecked();
            }

            //Se deshabilitan los campos que no deben ser alterados, por ser llaves foráneas
            idAlumno.setEnabled(false);
            idEvaluacion.setEnabled(false);
            motivoSoli.setEnabled(false);
            justiSoli.setClickable(false);


        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),
                    "Error during request: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarSoliExtra(){
        try {
            //Almacena valores de los datos que se modifiquen
            String carnetAlumno = idAlumno.getText().toString();
            int idEval = Integer.parseInt(idEvaluacion.getText().toString());
            String motivo = motivoSoli.getText().toString();

            //Utiliza fecha original de solicitud.
            String fechaAux = soliExtraActual.getFechaSolicitudExtr();
            String fecha = fechaAux;

            boolean justi = justiSoli.isChecked();
            boolean estado = estadoSoli.isChecked();

            //---obtener valor de spinner TIPO EVALUACION
            String tipoEvalAux1 = tipoSoli.getSelectedItem().toString();
            String[] tipoEvalAux2 = tipoEvalAux1.split("-");
            //almacenar TIPO EVAL
            String tipoEvaAux3 = tipoEvalAux2[0].trim();
            int tipoEva = Integer.parseInt(tipoEvaAux3);

            //Extraemos el correo del alumno que hizo la solicitud
            String correoAlumno = alumnoActual.getCorreo();

            //Se construye mensaje para el correo de notificación según el tipo y el estado de la solicitud
            String mensajeAux = "Su solicitud de evaluación " + tipoEvalAux2[1].trim() + " ha sido ";
            if(estado==true){
                mensajeAux = mensajeAux + "aprobada";
            } else {
                mensajeAux = mensajeAux + "rechazada";
            }

            //Se extrae el identificador de la solicitud a editar del Intent
            Bundle extras = getIntent().getExtras();
            int idSoliExtra = 0;
            if(extras != null){
                idSoliExtra = extras.getInt(SolicitudExtraordinarioActivity.IDENTIFICADOR_SOLI_EXTRA);
            }

            //Se verifica que no se seleccione Ordinario, por ser una Solicitud Extraordinaria
            if(tipoEva == 1){
                //Si se selecciona Ordinaria, retorna a la Activity anterior.
                Toast.makeText(EditarSolicitudExtraordinarioActivity.this, "No puede seleccionar tipo Ordinario. Seleccione el tipo de evaluación extraordinaria que desea realizar", Toast.LENGTH_LONG).show();
            } else {
                //Se inicializa de nuevo el ViewModel
                soliExtraVM = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SolicitudExtraordinarioViewModel.class);

                //Se crea una instancia de la clase SolicitudExtraordinario para operar el VM
                SolicitudExtraordinario soliAux = soliExtraVM.getSoliExtra(idSoliExtra);
                //Se ingresan los campos
                soliAux.setCarnetAlumnoFK(carnetAlumno);
                soliAux.setIdEvaluacion(idEval);
                soliAux.setTipoSolicitud(tipoEva);
                soliAux.setMotivoSolicitud(motivo);
                soliAux.setFechaSolicitudExtr(fecha);
                soliAux.setJustificacion(justi);
                soliAux.setEstadoSolicitud(estado);

                //Se actualiza la Solicitud
                soliExtraVM.update(soliAux);

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
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoAlumno.trim()));
                    //Seteamos Asunto
                    message.setSubject("Estado de su solicitud extraordinaria");
                    //Seteamos Mensaje del correo
                    message.setText(mensajeAux.trim());
                    //Se ejecuta la clase SendMail como una Tarea Asíncrona
                    new SendMail().execute(new Message[]{message});
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e){
            Toast.makeText(EditarSolicitudExtraordinarioActivity.this, e.getMessage() + " " +
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
                actualizarSoliExtra();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
