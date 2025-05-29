package com.example.lab5_20202396;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab5_20202396.data.Medication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicationListActivity extends AppCompatActivity {
    private static final String TAG = "MedicationListActivity";
    private RecyclerView medicationsRecyclerView;
    private MedicationAdapter medicationAdapter;
    private MedicationViewModel medicationViewModel;
    private FloatingActionButton addMedicationFab;
    private TextView emptyListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate started");
        setContentView(R.layout.activity_medication_list);

        try {
            initializeViews();
            setupRecyclerView();
            setupViewModel();
            setupFab();
            Log.d(TAG, "onCreate completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error al cargar la lista de medicamentos", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        Log.d(TAG, "Initializing views");
        medicationsRecyclerView = findViewById(R.id.medicationsRecyclerView);
        addMedicationFab = findViewById(R.id.addMedicationFab);
        emptyListText = findViewById(R.id.emptyListText);
        if (medicationsRecyclerView == null) {
            throw new IllegalStateException("RecyclerView not found");
        }
        if (addMedicationFab == null) {
            throw new IllegalStateException("FAB not found");
        }
        if (emptyListText == null) {
            throw new IllegalStateException("Empty list text not found");
        }
        Log.d(TAG, "Views initialized successfully");
    }

    private void setupRecyclerView() {
        medicationAdapter = new MedicationAdapter(new ArrayList<>(), this::showDeleteConfirmationDialog);
        medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicationsRecyclerView.setAdapter(medicationAdapter);
    }

    private void setupViewModel() {
        Log.d(TAG, "Setting up ViewModel");
        try {
            medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
            medicationViewModel.getAllMedications().observe(this, medications -> {
                Log.d(TAG, "Received medications update: " + (medications != null ? medications.size() : "null"));
                if (medications != null) {
                    medicationAdapter.setMedications(medications);
                    if (medications.isEmpty()) {
                        emptyListText.setVisibility(View.VISIBLE);
                        medicationsRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyListText.setVisibility(View.GONE);
                        medicationsRecyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.w(TAG, "Medications list is null");
                    Toast.makeText(this, "No hay medicamentos para mostrar", Toast.LENGTH_SHORT).show();
                    emptyListText.setVisibility(View.VISIBLE);
                    medicationsRecyclerView.setVisibility(View.GONE);
                }
            });
            Log.d(TAG, "ViewModel setup completed");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up ViewModel", e);
            Toast.makeText(this, "Error al cargar los medicamentos", Toast.LENGTH_SHORT).show();
            throw e;
        }
    }

    private void setupFab() {
        addMedicationFab.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, AddMedicationActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error launching AddMedicationActivity", e);
                Toast.makeText(this, "Error al abrir la pantalla de agregar medicamento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog(Medication medication) {
        new AlertDialog.Builder(this)
            .setTitle("Eliminar medicamento")
            .setMessage("¿Estás seguro de que deseas eliminar este medicamento?")
            .setPositiveButton("Eliminar", (dialog, which) -> {
                try {
                    medicationViewModel.delete(medication);
                    Toast.makeText(this, "Medicamento eliminado", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting medication", e);
                    Toast.makeText(this, "Error al eliminar el medicamento", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}

class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {
    private static final String TAG = "MedicationAdapter";
    private List<Medication> medications;
    private final OnMedicationClickListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public MedicationAdapter(List<Medication> medications, OnMedicationClickListener listener) {
        this.medications = medications;
        this.listener = listener;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
        notifyDataSetChanged();
        Log.d(TAG, "Medications updated: " + medications.size());
    }

    @Override
    public MedicationViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        View view = android.view.LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedicationViewHolder holder, int position) {
        try {
            Medication medication = medications.get(position);
            holder.bind(medication, listener);
        } catch (Exception e) {
            Log.e(TAG, "Error binding medication at position " + position, e);
        }
    }

    @Override
    public int getItemCount() {
        return medications != null ? medications.size() : 0;
    }

    static class MedicationViewHolder extends RecyclerView.ViewHolder {
        private final TextView medicationName;
        private final TextView medicationType;
        private final TextView medicationFrequency;
        private final TextView medicationStartTime;
        private final ImageButton deleteButton;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        public MedicationViewHolder(View itemView) {
            super(itemView);
            medicationName = itemView.findViewById(R.id.medicationName);
            medicationType = itemView.findViewById(R.id.medicationType);
            medicationFrequency = itemView.findViewById(R.id.medicationFrequency);
            medicationStartTime = itemView.findViewById(R.id.medicationStartTime);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Medication medication, OnMedicationClickListener listener) {
            try {
                medicationName.setText(medication.getName());
                medicationType.setText(String.format("Tipo: %s - Dosis: %s", medication.getType(), medication.getDose()));
                medicationFrequency.setText(String.format("Frecuencia: Cada %d horas", medication.getFrequencyHours()));
                medicationStartTime.setText(String.format("Inicio: %s", dateFormat.format(medication.getStartDateTime())));

                deleteButton.setOnClickListener(v -> listener.onMedicationClick(medication));
            } catch (Exception e) {
                Log.e(TAG, "Error binding medication data", e);
            }
        }
    }

    interface OnMedicationClickListener {
        void onMedicationClick(Medication medication);
    }
} 