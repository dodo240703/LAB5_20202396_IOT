package com.example.lab5_20202396;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.lab5_20202396.data.AppDatabase;
import com.example.lab5_20202396.data.Medication;
import com.example.lab5_20202396.data.MedicationDao;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicationViewModel extends AndroidViewModel {
    private static final String TAG = "MedicationViewModel";
    private final MedicationDao medicationDao;
    private final LiveData<List<Medication>> allMedications;
    private final ExecutorService executorService;

    public MedicationViewModel(Application application) {
        super(application);
        try {
            medicationDao = AppDatabase.getInstance(application).medicationDao();
            allMedications = medicationDao.getAllMedications();
            executorService = Executors.newSingleThreadExecutor();
            Log.d(TAG, "MedicationViewModel initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing MedicationViewModel", e);
            throw e;
        }
    }

    public LiveData<List<Medication>> getAllMedications() {
        return allMedications;
    }

    public void insert(Medication medication) {
        executorService.execute(() -> {
            try {
                medicationDao.insert(medication);
                Log.d(TAG, "Medication inserted successfully: " + medication.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error inserting medication", e);
            }
        });
    }

    public void update(Medication medication) {
        executorService.execute(() -> {
            try {
                medicationDao.update(medication);
                Log.d(TAG, "Medication updated successfully: " + medication.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error updating medication", e);
            }
        });
    }

    public void delete(Medication medication) {
        executorService.execute(() -> {
            try {
                medicationDao.delete(medication);
                Log.d(TAG, "Medication deleted successfully: " + medication.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error deleting medication", e);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
} 