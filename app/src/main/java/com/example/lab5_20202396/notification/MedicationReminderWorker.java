package com.example.lab5_20202396.notification;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.lab5_20202396.data.AppDatabase;
import com.example.lab5_20202396.data.Medication;
import java.util.List;

public class MedicationReminderWorker extends Worker {
    private final Context context;
    private final NotificationHelper notificationHelper;

    public MedicationReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        this.notificationHelper = new NotificationHelper(context);
    }

    @NonNull
    @Override
    public androidx.work.ListenableWorker.Result doWork() {
        try {
            List<Medication> medications = AppDatabase.getInstance(context)
                .medicationDao()
                .getAllMedicationsSync();

            for (Medication medication : medications) {
                notificationHelper.showMedicationReminder(medication);
            }

            return androidx.work.ListenableWorker.Result.success();
        } catch (Exception e) {
            return androidx.work.ListenableWorker.Result.failure();
        }
    }
} 