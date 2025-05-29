package com.example.lab5_20202396.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.lab5_20202396.MainActivity;
import com.example.lab5_20202396.R;
import com.example.lab5_20202396.data.Medication;

public class NotificationHelper {
    private static final String CHANNEL_PASTILLA = "pastilla_channel";
    private static final String CHANNEL_JARABE = "jarabe_channel";
    private static final String CHANNEL_AMPOLLA = "ampolla_channel";
    private static final String CHANNEL_CAPSULA = "capsula_channel";
    private static final String CHANNEL_MOTIVACIONAL = "motivacional_channel";

    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel pastillaChannel = new NotificationChannel(
                CHANNEL_PASTILLA,
                "Pastillas",
                NotificationManager.IMPORTANCE_HIGH
            );
            pastillaChannel.setDescription("Notificaciones para medicamentos en pastillas");
            pastillaChannel.enableVibration(true);

            NotificationChannel jarabeChannel = new NotificationChannel(
                CHANNEL_JARABE,
                "Jarabes",
                NotificationManager.IMPORTANCE_HIGH
            );
            jarabeChannel.setDescription("Notificaciones para medicamentos en jarabe");
            jarabeChannel.enableVibration(true);

            NotificationChannel ampollaChannel = new NotificationChannel(
                CHANNEL_AMPOLLA,
                "Ampollas",
                NotificationManager.IMPORTANCE_HIGH
            );
            ampollaChannel.setDescription("Notificaciones para medicamentos en ampollas");
            ampollaChannel.enableVibration(true);

            NotificationChannel capsulaChannel = new NotificationChannel(
                CHANNEL_CAPSULA,
                "Cápsulas",
                NotificationManager.IMPORTANCE_HIGH
            );
            capsulaChannel.setDescription("Notificaciones para medicamentos en cápsulas");
            capsulaChannel.enableVibration(true);

            NotificationChannel motivacionalChannel = new NotificationChannel(
                CHANNEL_MOTIVACIONAL,
                "Mensajes Motivacionales",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            motivacionalChannel.setDescription("Mensajes motivacionales personalizados");
            motivacionalChannel.enableVibration(false);

            notificationManager.createNotificationChannels(java.util.Arrays.asList(
                pastillaChannel, jarabeChannel, ampollaChannel, capsulaChannel, motivacionalChannel
            ));
        }
    }

    public void showMedicationReminder(Medication medication) {
        String channelId = getChannelIdForType(medication.getType());
        String actionText = getActionTextForType(medication.getType(), medication.getDose());

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
            .setSmallIcon(getIconForType(medication.getType()))
            .setContentTitle("Recordatorio de medicamento")
            .setContentText(medication.getName() + ": " + actionText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        notificationManager.notify((int) medication.getId(), builder.build());
    }

    public void showMotivationalMessage(String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_MOTIVACIONAL)
            .setSmallIcon(R.drawable.ic_motivational)
            .setContentTitle("Mensaje Motivacional")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        notificationManager.notify(0, builder.build());
    }

    private String getChannelIdForType(String type) {
        switch (type.toLowerCase()) {
            case "pastilla":
                return CHANNEL_PASTILLA;
            case "jarabe":
                return CHANNEL_JARABE;
            case "ampolla":
                return CHANNEL_AMPOLLA;
            case "cápsula":
                return CHANNEL_CAPSULA;
            default:
                return CHANNEL_PASTILLA;
        }
    }

    private String getActionTextForType(String type, String dose) {
        switch (type.toLowerCase()) {
            case "pastilla":
                return "Tomar " + dose;
            case "jarabe":
                return "Tomar " + dose + " de jarabe";
            case "ampolla":
                return "Aplicar " + dose;
            case "cápsula":
                return "Tomar " + dose;
            default:
                return "Tomar medicamento";
        }
    }

    private int getIconForType(String type) {
        switch (type.toLowerCase()) {
            case "pastilla":
                return R.drawable.ic_pill;
            case "jarabe":
                return R.drawable.ic_syrup;
            case "ampolla":
                return R.drawable.ic_ampoule;
            case "cápsula":
                return R.drawable.ic_capsule;
            default:
                return R.drawable.ic_medication;
        }
    }
} 