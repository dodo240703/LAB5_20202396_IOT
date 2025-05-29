package com.example.lab5_20202396.notification;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.xml.transform.Result;

public class MotivationalMessageWorker extends Worker {
    private static final String PREFS_NAME = "MedicationAppPrefs";
    private static final String KEY_MOTIVATIONAL_MESSAGE = "motivational_message";
    private final Context context;
    private final NotificationHelper notificationHelper;

    public MotivationalMessageWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        this.notificationHelper = new NotificationHelper(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String message = prefs.getString(KEY_MOTIVATIONAL_MESSAGE, 
                "¡Mantén un buen control de tus medicamentos!");

            notificationHelper.showMotivationalMessage(message);
            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }
} 