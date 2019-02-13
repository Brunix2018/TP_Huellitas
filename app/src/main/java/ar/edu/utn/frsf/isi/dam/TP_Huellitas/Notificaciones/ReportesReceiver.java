package ar.edu.utn.frsf.isi.dam.TP_Huellitas.Notificaciones;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import ar.edu.utn.frsf.isi.dam.TP_Huellitas.MainActivity;


public class ReportesReceiver extends BroadcastReceiver {

    public static final String EVENTO_CANCELADO="ar.edu.utn.frsf.isi.dam.TP_Huellitas.ESTADO_CANCELADO";
    public static final String EVENTO_Encontrado="ar.edu.utn.frsf.isi.dam.TP_Huellitas.ESTADO_ENCONTRADO";
    NotificationCompat.Builder notificarEstado;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving



        if (intent.getAction() == ReportesReceiver.EVENTO_CANCELADO) {




            String detalle = "Una de tus busquedas, ha superado los 6 meses de antiguedad, lamentamos tener que borrarla";

            // Create an explicit intent for an Activity in your app
            Intent destino = new Intent(context, MainActivity.class);
            destino.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, destino, PendingIntent.FLAG_UPDATE_CURRENT);

            notificarEstado = new NotificationCompat.Builder(context, "CANAL01")
                    .setContentTitle("Novedades en busquedas de Mascotas")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(detalle))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.notify(7, notificarEstado.build());


        }




    }
}
