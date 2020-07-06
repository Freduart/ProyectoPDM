package sv.ues.fia.eisi.proyectopdm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import sv.ues.fia.eisi.proyectopdm.R;
import sv.ues.fia.eisi.proyectopdm.ViewModel.AlumnoViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.DetalleEvaluacionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.LocalViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.PrimeraRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.ViewModel.SegundaRevisionViewModel;
import sv.ues.fia.eisi.proyectopdm.db.entity.Alumno;
import sv.ues.fia.eisi.proyectopdm.db.entity.DetalleEvaluacion;
import sv.ues.fia.eisi.proyectopdm.db.entity.Local;
import sv.ues.fia.eisi.proyectopdm.db.entity.PrimeraRevision;

public class EditarPrimeraRevisionActivity extends AppCompatActivity {

    public static  final int AÑADIR_SEGUNDA_REVISION = 1;
    public static final int EDITAR_SEGUNDA_REVISION = 2;
    public static final String OPERACION_SEGUNDA_REVISION = "Operacion_AE_sr";
    public static final String IDENTIFICADOR_PRIMERA_REVISION = "ID_pr_Actual";

    private PrimeraRevision primeraRevisionActual;
    private Alumno alumnoActual;

    private PrimeraRevisionViewModel primeraRevisionViewModel;
    private LocalViewModel localViewModel;
    private DetalleEvaluacionViewModel detalleEvaluacionViewModel;
    private SegundaRevisionViewModel segundaRevisionViewModel;
    private AlumnoViewModel alumnoViewModel;

    private Spinner spinlocalFK;
    private Spinner spindetalleEFK;
    private DatePicker dpickfechaSoli;
    private Spinner spinestado;
    private EditText notaAntes;
    private EditText notaDespues;
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
            this.progressDialog = ProgressDialog.show(EditarPrimeraRevisionActivity.this, "Por favor espere", "Enviando Correo...", true, false);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarPrimeraRevisionActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Éxito</font>"));
                builder.setMessage("Local Agregado. Correo de notificación enviado.");
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
            Toast.makeText(EditarPrimeraRevisionActivity.this.getApplicationContext(), "¿Algo salió mal?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_primera_revision);
            final Bundle extra = getIntent().getExtras();
            spinlocalFK = (Spinner) findViewById(R.id.editarLocalFK);
            spindetalleEFK = (Spinner) findViewById(R.id.editarDetalleEvaFK);
            dpickfechaSoli = (DatePicker) findViewById(R.id.editarFechaSolicitud);
            spinestado = (Spinner) findViewById(R.id.editarEstadoPR);
            notaAntes = (EditText) findViewById(R.id.editarNotaAntesPR);
            notaDespues = (EditText) findViewById(R.id.editarNotaDespuesPR);
            observaciones = (EditText) findViewById(R.id.editarObservacionesPR);
            notaAntes.setEnabled(false);

            //Carga datos del correo remitente de la notificación (A enviar usando JavaMail API)
            sMail = "pdmproyecto2020@gmail.com";
            sPass = "proyectopdm";

            alumnoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(AlumnoViewModel.class);
            primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
            int idPR= 0;
            if(extra != null){
                idPR = extra.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR);
            }
            primeraRevisionActual = primeraRevisionViewModel.getPrimeraRevision(idPR);

            //Llenar spinner de local
            //Lista para almacenar id de local
            final ArrayList<String> localesId = new ArrayList<>();
            //Adaptador a arreglo para spinner
            final ArrayAdapter<String> adaptadorSpinnerLocal = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localesId);
            //settea layout de dropdown del spinner
            adaptadorSpinnerLocal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //setea el adaptador creado en el spinner
            spinlocalFK.setAdapter(adaptadorSpinnerLocal);
            //Instancia LocalViewModel
            localViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocalViewModel.class);
            //Obtener todos los locales en livedata
            localViewModel.getAllLocales().observe(this, new Observer<List<Local>>() {
                @Override
                public void onChanged(List<Local> locals) {
                    try {
                       PrimeraRevision pr = primeraRevisionViewModel.getPrimeraRevision(extra.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR));
                       Local loc = localViewModel.getLoc(pr.getIdLocalFK());
                       //añade los elementos del livedata a la lista localesId
                       for(Local l : locals){
                           localesId.add(l.getIdLocal());
                           if(l.getIdLocal().equals(loc.getIdLocal())){
                               spinlocalFK.setSelection(localesId.indexOf(l.getIdLocal()));
                           }
                       }
                       //refresca para mostrar los datos recuperados en el spinner
                        adaptadorSpinnerLocal.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(EditarPrimeraRevisionActivity.this, "Local: "+e.getMessage()+ " - "+e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            //Spinner de DetalleEvaluación
            final ArrayList<String> detallesIdNom = new ArrayList<>();
            final ArrayAdapter<String> adaptadorSpinnerDetalle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, detallesIdNom);
            adaptadorSpinnerDetalle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spindetalleEFK.setAdapter(adaptadorSpinnerDetalle);
            detalleEvaluacionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(DetalleEvaluacionViewModel.class);
            detalleEvaluacionViewModel.getAllDetalles().observe(this, new Observer<List<DetalleEvaluacion>>() {
                @Override
                public void onChanged(List<DetalleEvaluacion> detalleEvaluacions) {
                    try {
                        PrimeraRevision pr = primeraRevisionViewModel.getPrimeraRevision(extra.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR));
                        DetalleEvaluacion dete = detalleEvaluacionViewModel.getDetalleEvaluacion(pr.getIdDetalleEvFK());
                        for(DetalleEvaluacion d : detalleEvaluacions){
                            detallesIdNom.add(d.getIdDetalleEv() + " - " +d.getCarnetAlumnoFK());
                            if(d.getIdDetalleEv()== dete.getIdDetalleEv()){
                                spindetalleEFK.setSelection(detallesIdNom.indexOf(d.getIdDetalleEv() + " - " + d.getCarnetAlumnoFK()));
                                spindetalleEFK.setEnabled(false);
                            }
                        }
                        adaptadorSpinnerDetalle.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(EditarPrimeraRevisionActivity.this, "Detalle: "+e.getMessage()+ " - "+e.getCause(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            //Datepicker
            String[] fechaAuxiliar = primeraRevisionActual.getFechaSolicitudPrimRev().split("/");
            //se ingresa la fecha desde el array
            dpickfechaSoli.init(Integer.parseInt(fechaAuxiliar[2]),Integer.parseInt(fechaAuxiliar[1])-1,Integer.parseInt(fechaAuxiliar[0]), null);
            dpickfechaSoli.setEnabled(false);
            //Spinner de estado
            ArrayAdapter<CharSequence> adapterSpinnerEstado = ArrayAdapter.createFromResource(this, R.array.estado_array, android.R.layout.simple_spinner_item);
            adapterSpinnerEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinestado.setAdapter(adapterSpinnerEstado);

            DetalleEvaluacion detalleEvActual;
            detalleEvActual = detalleEvaluacionViewModel.getDetalleEvaluacion(primeraRevisionActual.getIdDetalleEvFK());

            alumnoActual = alumnoViewModel.getAlumn(detalleEvActual.getCarnetAlumnoFK());

            notaAntes.setText(String.valueOf(detalleEvActual.getNota()));
            notaDespues.setText(String.valueOf(primeraRevisionActual.getNotaDespuesPrimeraRev()));
            observaciones.setText(primeraRevisionActual.getObservacionesPrimeraRev());
            //titulo appbar
            setTitle(R.string.EditarPrimera);

        }catch (Exception e){
            Toast.makeText(this,e.getMessage() +  " - " + e.fillInStackTrace().toString(),Toast.LENGTH_LONG).show();
        }
    }
    private void actualizarPrimeraRevision(){
        try {
            int id= primeraRevisionActual.getIdPrimerRevision();
            //Obtener valor de spinner de Local
            String local = spinlocalFK.getSelectedItem().toString();
            //Obtener valor de spinner de DetalleEvaluación
            String detalleAuxiliar = spindetalleEFK.getSelectedItem().toString();
            String[] detalleAuxiliar2 = detalleAuxiliar.split("-");
            String detalle = detalleAuxiliar2[0].trim();
            //Obtener el valor de datepicker
            StringBuilder f = new StringBuilder(10);
            //Concatenar
            f.append(dpickfechaSoli.getDayOfMonth()).append("/").append(dpickfechaSoli.getMonth()).append("/").append(dpickfechaSoli.getYear());
            //Almacenar
            String fecha = f.toString();
            //Obtener valor de spinner de estado
            String est = spinestado.getSelectedItem().toString();
            //Almacenar notaAntes
            String notaA = notaAntes.getText().toString();
            String notaD = notaDespues.getText().toString();
            String ob = observaciones.getText().toString();

            //Extraer correo de alumno
            String correoAlumno = alumnoActual.getCorreo();

            //Construir mensaje para correo de notificación
            String mensajeAux = "Su solicitud de Primera revisión ha sido aprobada. Ésta se realizará en el local ";
            mensajeAux = mensajeAux + local + ", 5 (cinco) días después de enviado este correo.";

            if(notaA.trim().isEmpty()||notaD.trim().isEmpty()||ob.trim().isEmpty()){
                Toast.makeText(this, R.string.error_form_incompleto_eval, Toast.LENGTH_LONG).show();
            }
            Bundle extras = getIntent().getExtras();
            //validacion nota ingresada
            if(Float.parseFloat(notaD.trim()) > (float)extras.getInt(DetalleNotasActivity.NOTAMAX)){
                String notaMaxAux = String.format("%s",(float)extras.getInt(DetalleNotasActivity.NOTAMAX));
                //mensaje de error, informa al usuario que la nota no puede ser mayor a la nota máxima
                Toast.makeText(this,getText(R.string.notamaxerror) + notaMaxAux, Toast.LENGTH_LONG).show();
                return;
            }
            DetalleEvaluacion detalleEvaluacionActual;
            detalleEvaluacionActual = detalleEvaluacionViewModel.getDetalleEvaluacion(primeraRevisionActual.getIdDetalleEvFK());

            if(Double.parseDouble(notaD)<detalleEvaluacionActual.getNota()){
                Toast.makeText(EditarPrimeraRevisionActivity.this, R.string.errodenota, Toast.LENGTH_LONG).show();
            }else{
                primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
                PrimeraRevision p = primeraRevisionViewModel.getPrimeraRevision(id);
                p.setIdLocalFK(local);
                p.setIdDetalleEvFK(Integer.parseInt(detalle));
                p.setFechaSolicitudPrimRev(fecha);
                p.setEstadoPrimeraRev(Boolean.parseBoolean(est));
                p.setNotasAntesPrimeraRev(Double.parseDouble(notaA));
                p.setNotaDespuesPrimeraRev(Double.parseDouble(notaD));
                p.setObservacionesPrimeraRev(ob);
                //Actualizar
                primeraRevisionViewModel.updatePrimeraRevision(p);

                setResult(RESULT_OK);

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
                    message.setSubject("Estado de su solicitud de Primera Revisión");
                    //Seteamos Mensaje del correo
                    message.setText(mensajeAux.trim());
                    //Se ejecuta la clase SendMail como una Tarea Asíncrona
                    new SendMail().execute(new Message[]{message});
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            Toast.makeText(EditarPrimeraRevisionActivity.this, e.getMessage() + " " + e.getCause(), Toast.LENGTH_LONG).show();
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
        switch (item.getItemId()){
            case R.id.guardar:
                actualizarPrimeraRevision();
                primeraRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PrimeraRevisionViewModel.class);
                segundaRevisionViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SegundaRevisionViewModel.class);
                Bundle extras = getIntent().getExtras();
                int idPrimeraR = 0;
                if(extras != null){
                    idPrimeraR = extras.getInt(PrimeraRevisionActivity.IDENTIFICADOR_PR);
                }
                try {
                    PrimeraRevision pRActual = primeraRevisionViewModel.getPrimeraRevision(idPrimeraR);
                    if(segundaRevisionViewModel.getSegundaRevision(pRActual.getIdPrimerRevision())!=null && (String.valueOf(pRActual.isEstadoPrimeraRev()) == "false")){
                        Intent intent = new Intent(EditarPrimeraRevisionActivity.this, NuevaEditarSegundaRevisionActivity.class);
                        intent.putExtra(OPERACION_SEGUNDA_REVISION, EDITAR_SEGUNDA_REVISION);
                        intent.putExtra(IDENTIFICADOR_PRIMERA_REVISION, pRActual.getIdPrimerRevision());
                        startActivity(intent);
                        Toast.makeText(EditarPrimeraRevisionActivity.this, "Se editará solicitud de segunda revisión", Toast.LENGTH_LONG).show();
                    }else if(segundaRevisionViewModel.getSegundaRevision(pRActual.getIdPrimerRevision())==null && (String.valueOf(pRActual.isEstadoPrimeraRev()) == "false")){
                        Intent intent = new Intent(EditarPrimeraRevisionActivity.this, NuevaEditarSegundaRevisionActivity.class);
                        intent.putExtra(OPERACION_SEGUNDA_REVISION, AÑADIR_SEGUNDA_REVISION);
                        intent.putExtra(IDENTIFICADOR_PRIMERA_REVISION, pRActual.getIdPrimerRevision());
                        startActivity(intent);
                        Toast.makeText(EditarPrimeraRevisionActivity.this, "Se agregará solicitud de segunda revisión", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(EditarPrimeraRevisionActivity.this, e.getMessage()+" - " + e.getCause(), Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
