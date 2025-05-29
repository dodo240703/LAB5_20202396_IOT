package com.example.lab5_20202396;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.lab5_20202396.data.Medication;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddMedicationActivity extends AppCompatActivity {
    private TextInputEditText medicationNameInput;
    private AutoCompleteTextView medicationTypeInput;
    private TextInputEditText medicationDoseInput;
    private TextInputEditText medicationFrequencyInput;
    private Button datePickerButton;
    private Button timePickerButton;
    private TextView selectedDateTimeText;
    private Button saveButton;
    
    private MedicationViewModel medicationViewModel;
    private Calendar selectedDateTime;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    
    private static final String[] MEDICATION_TYPES = {"Pastilla", "Jarabe", "Ampolla", "Cápsula"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        selectedDateTime = Calendar.getInstance();

        initializeViews();
        setupTypeDropdown();
        setupDateTimePickers();
        setupSaveButton();
    }

    private void initializeViews() {
        medicationNameInput = findViewById(R.id.medicationNameInput);
        medicationTypeInput = findViewById(R.id.medicationTypeInput);
        medicationDoseInput = findViewById(R.id.medicationDoseInput);
        medicationFrequencyInput = findViewById(R.id.medicationFrequencyInput);
        datePickerButton = findViewById(R.id.datePickerButton);
        timePickerButton = findViewById(R.id.timePickerButton);
        selectedDateTimeText = findViewById(R.id.selectedDateTimeText);
        saveButton = findViewById(R.id.saveButton);

        updateDateTimeText();
    }

    private void setupTypeDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_dropdown_item_1line, MEDICATION_TYPES);
        medicationTypeInput.setAdapter(adapter);
    }

    private void setupDateTimePickers() {
        datePickerButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTimeText();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        timePickerButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    updateDateTimeText();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                true);
            timePickerDialog.show();
        });
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> {
            String name = medicationNameInput.getText().toString().trim();
            String type = medicationTypeInput.getText().toString().trim();
            String dose = medicationDoseInput.getText().toString().trim();
            String frequencyStr = medicationFrequencyInput.getText().toString().trim();

            if (name.isEmpty() || type.isEmpty() || dose.isEmpty() || frequencyStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int frequency;
            try {
                frequency = Integer.parseInt(frequencyStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La frecuencia debe ser un número válido", Toast.LENGTH_SHORT).show();
                return;
            }

            Medication medication = new Medication(name, type, dose, frequency, selectedDateTime.getTime());
            medicationViewModel.insert(medication);
            finish();
        });
    }

    private void updateDateTimeText() {
        selectedDateTimeText.setText(dateFormat.format(selectedDateTime.getTime()));
    }
} 