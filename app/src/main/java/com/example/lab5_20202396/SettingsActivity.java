package com.example.lab5_20202396;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.example.lab5_20202396.notification.MotivationalMessageWorker;
import com.google.android.material.textfield.TextInputEditText;
import java.util.concurrent.TimeUnit;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MedicationAppPrefs";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_MOTIVATIONAL_MESSAGE = "motivational_message";
    private static final String KEY_MOTIVATIONAL_FREQUENCY = "motivational_frequency";
    private static final String WORK_MOTIVATIONAL = "work_motivational";

    private TextInputEditText userNameInput;
    private TextInputEditText motivationalMessageInput;
    private TextInputEditText motivationalFrequencyInput;
    private Button saveSettingsButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        initializeViews();
        loadSettings();
        setupSaveButton();
    }

    private void initializeViews() {
        userNameInput = findViewById(R.id.userNameInput);
        motivationalMessageInput = findViewById(R.id.motivationalMessageInput);
        motivationalFrequencyInput = findViewById(R.id.motivationalFrequencyInput);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);
    }

    private void loadSettings() {
        userNameInput.setText(sharedPreferences.getString(KEY_USER_NAME, ""));
        motivationalMessageInput.setText(sharedPreferences.getString(KEY_MOTIVATIONAL_MESSAGE, 
            "¡Mantén un buen control de tus medicamentos!"));
        motivationalFrequencyInput.setText(String.valueOf(
            sharedPreferences.getInt(KEY_MOTIVATIONAL_FREQUENCY, 24)));
    }

    private void setupSaveButton() {
        saveSettingsButton.setOnClickListener(v -> {
            String userName = userNameInput.getText().toString().trim();
            String motivationalMessage = motivationalMessageInput.getText().toString().trim();
            String frequencyStr = motivationalFrequencyInput.getText().toString().trim();

            if (userName.isEmpty() || motivationalMessage.isEmpty() || frequencyStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int frequency;
            try {
                frequency = Integer.parseInt(frequencyStr);
                if (frequency < 1) {
                    Toast.makeText(this, "La frecuencia debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La frecuencia debe ser un número válido", Toast.LENGTH_SHORT).show();
                return;
            }

            sharedPreferences.edit()
                .putString(KEY_USER_NAME, userName)
                .putString(KEY_MOTIVATIONAL_MESSAGE, motivationalMessage)
                .putInt(KEY_MOTIVATIONAL_FREQUENCY, frequency)
                .apply();

            scheduleMotivationalWork(frequency);
            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void scheduleMotivationalWork(int frequencyHours) {
        PeriodicWorkRequest motivationalWork = new PeriodicWorkRequest.Builder(
            MotivationalMessageWorker.class,
            frequencyHours, TimeUnit.HOURS)
            .build();

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                WORK_MOTIVATIONAL,
                ExistingPeriodicWorkPolicy.UPDATE,
                motivationalWork
            );
    }
} 