package sv.ues.fia.eisi.proyectopdm.Activity;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DocenteViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.EvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Docente;
import sv.ues.fia.eisi.proyectopdm.db.entity.Evaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class NuevaPrimeraRevisionActivity extends AppCompatActivity {

    private PrimeraRevisionViewModel primeraRevisionViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
    private List<DetalleEvaluacion> detalleEvaluacionPorAlumno;
    private EvaluacionViewModel evaluacionViewModel;
    private DocenteViewModel docenteViewModel;
    private Evaluacion evaluacionPr;
    public String FECHA_ENTREGA;
    public String LOCAL_PH_PR;
    public String NOTA_PH_PR;
    public String ESTADO_PH_PR;
    private Spinner spindetalleEFK;
    private DatePicker dpickfechaSoli;
    private EditText notaAntes;
    private EditText observaciones;

    private String sMail;
    private String sPass;

    //Clase Privada para método de enviado de JavaMail API
    private class SendMail extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        private SendMail() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = ProgressDialog.show(NuevaPrimeraRevisionActivity.this, "Por favor espere", "Enviando Correo...", true, false);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(NuevaPrimeraRevisionActivity.this);
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
            Toast.makeText(NuevaPrimeraRevisionActivity.this.getApplicationContext(), "¿Algo salió mal?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_primera_revision);
            final Bundle extraidAlUser = getIntent().getExtras();
            final Bundle idEvaExtra = getIntent().getExtras();
            NOTA_PH_PR = getText(R.string.nota_place_holder_PR).toString();
            ESTADO_PH_PR = getText(R.string.estado_placeholder_PR).toString();
            LOCAL_PH_PR = getText(R.string.local_placeholder_PR).toString();
            spindetalleEFK = (Spinner) findViewById(R.id.editarDetalleEvaFK);
            dpickfechaSoli = (DatePicker) findViewById(R.id.editarFechaSolicitud);
            notaAntes = (EditText) findViewById(R.id.editarNotaAntesPR);
            observaciones = (EditText) findViewById(R.id.editarObservacionesPR);
            FECHA_ENTREGA = getText(R.string.fecha_noenc_eval).toString();

            //Carga datos del correo remitente de la notificación (A enviar usando JavaMail API)
            sMail = "pdmproyecto2020@gmail.com";
            sPass = "proyectopdm";

            //Spinner de detalle por alumno loggueado
            final ArrayList<String> detallesNom = new ArrayList<>();
            final ArrayAdapter<String> adapterSpinnerDetalleE = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, detallesNom);
            spindetalleEFK.setAdapter(adapterSpinnerDetalleE);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            int idAlUser = 0;
            if (extraidAlUser != null) {
                idAlUser = extraidAlUser.getInt(LoginActivity.ID_USUARIO);
            }
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);
            docenteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DocenteViewModel.class);
            int idEvaPr = 0;
            if (idEvaExtra != null) {
                idEvaPr = idEvaExtra.getInt(VerEvaluacionActivity.ID_EVAL);
            }
            evaluacionPr = primeraRevisionViewModel.obtenerEvaPR(idEvaPr);

            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            detalleEvaluacionViewModel.getDetallePorUsuarioYEvaluacion(idAlUser, idEvaPr).observe(this, new Observer<List<DetalleEvaluacion>>() {
                @Override
                public void onChanged(List<DetalleEvaluacion> detalleEvaluacions) {
                    try {
                        for (DetalleEvaluacion d : detalleEvaluacions) {
                            detalleEvaluacionPorAlumno = detalleEvaluacionViewModel.getDetallePorAlumno(d.getCarnetAlumnoFK());
                            if (!detalleEvaluacionPorAlumno.isEmpty()) {
                                detallesNom.add(d.getIdDetalleEv() + " - " + d.getIdEvaluacionFK() + " / " + d.getCarnetAlumnoFK());
                            }
                            adapterSpinnerDetalleE.notifyDataSetChanged();
                        }
                    } catch (Exception e) {

                    }
                }
            });//fin de llenado spinner detalle por alumno

            dpickfechaSoli.setEnabled(false);

        } catch (Exception e) {
            Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    public void guardarPrimeraRevision() {
        try {
            //Obtener valor de spinner de Detalle de evaluación
            String detalleAux1 = spindetalleEFK.getSelectedItem().toString();
            String[] detalleAux2 = detalleAux1.split("-");
            String detalle = detalleAux2[0].trim();
            //Obtener el valor del datepicker
            StringBuilder fecha = new StringBuilder(10);
            //concatenar
            fecha.append(dpickfechaSoli.getDayOfMonth()).append("/").append(dpickfechaSoli.getMonth() + 1).append("/").append(dpickfechaSoli.getYear());
            //Almacenar fecha de solicitud
            String fechaSolicitud = fecha.toString();
            String notaAn = notaAntes.getText().toString();
            String ob = observaciones.getText().toString();
            if (notaAn.trim().isEmpty() || ob.isEmpty()) {
                Toast.makeText(this, R.string.error_form_incompleto_eval, Toast.LENGTH_LONG).show();
                return;
            }
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            evaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EvaluacionViewModel.class);

/*
            String[] fechaAuxiliar = evaluacionPr.getFechaEntregaNotas().split("/");
            int dia = Integer.parseInt(fechaAuxiliar[0]);
            int mes = Integer.parseInt(fechaAuxiliar[1]);
            int anio = Integer.parseInt(fechaAuxiliar[2]);
            int diaActual = dpickfechaSoli.getDayOfMonth();
            int mesActual = dpickfechaSoli.getMonth();
            int anioActual = dpickfechaSoli.getYear();
 */
            //Obteniendo el correo del docente evaluador
            int id_detAux = Integer.parseInt(detalle);
            //Se obtiene el detalle de evaluación correspondiente
            DetalleEvaluacion detaEvaAux = detalleEvaluacionViewModel.getDetalleEvaluacion(id_detAux);
            //Se extrae el id de la evaluación relacionada
            int id_evaAux = detaEvaAux.getIdEvaluacionFK();
            //Se obtiene la Evaluación correspondiente
            Evaluacion evaAux = evaluacionViewModel.getEval(id_evaAux);
            //Se saca el id del docente evaluador
            String idDocenteAux = evaAux.getCarnetDocenteFK();
            //Se obtiene el docente
            Docente docenteAux = docenteViewModel.getDocente(idDocenteAux);
            //Se extrae el correo del docente
            String correoDocente = docenteAux.getCorreoDocente();

            //Construyendo mensaje de correo de notificación
            String mensajeAux = "El Alumno con carnet: " + detaEvaAux.getCarnetAlumnoFK().trim() + " ha solicitado una revisión de su nota en la evaluación " + evaAux.getNomEvaluacion().trim();
            mensajeAux = mensajeAux + " Por favor revisar la solicitud a brevedad.";

            double nAF = detaEvaAux.getNota();

            //if (((diaActual >= dia) && (diaActual <= (dia + 5))) && (mesActual == mes) && (anioActual == anio)) {
                PrimeraRevision pr = new PrimeraRevision(LOCAL_PH_PR, Integer.parseInt(detalle), fechaSolicitud, Boolean.parseBoolean(ESTADO_PH_PR), nAF, nAF, ob);
                primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
                primeraRevisionViewModel.insertPrimeraRevision(pr);

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
                message.setSubject("Nueva Solicitud de Primera Revisión");
                //Seteamos Mensaje del correo
                message.setText(mensajeAux.trim());
                //Se ejecuta la clase SendMail como una Tarea Asíncrona
                new SendMail().execute(new Message[]{message});
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            /*} else if(diaActual>(dia+5)){
                Toast.makeText(NuevaPrimeraRevisionActivity.this, R.string.periodohabildespues, Toast.LENGTH_LONG).show();
            }if (evaluacionPr.getFechaEntregaNotas().equals(FECHA_ENTREGA)||(dia>diaActual)) {
                Toast.makeText(NuevaPrimeraRevisionActivity.this, R.string.peridohabilantes, Toast.LENGTH_LONG).show();
            }*/

        } catch (Exception e) {
            Toast.makeText(NuevaPrimeraRevisionActivity.this, e.getMessage() + " - " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.agregar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar:
                guardarPrimeraRevision();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
