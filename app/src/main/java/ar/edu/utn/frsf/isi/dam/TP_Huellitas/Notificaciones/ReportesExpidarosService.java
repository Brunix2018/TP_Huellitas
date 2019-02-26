package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Notificaciones;

import android.app.IntentService;
import android.content.Intent;





public class ReportesExpidarosService extends IntentService {
    public ReportesExpidarosService() {
        super("ReportesExpidarosService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("onHandleIntent");
        Runnable r = new Runnable() {
            @Override
            public void run() {

                    Intent br = new Intent(ReportesExpidarosService.this,ReportesReceiver.class);
                    br.setAction(ReportesReceiver.EVENTO_CANCELADO);
                    sendBroadcast(br);

                }


        };
        Thread unHilo = new Thread(r);
        unHilo.start();








    }
}
