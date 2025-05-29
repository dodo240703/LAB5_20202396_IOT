package com.example.lab5_20202396.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface MedicationDao {
    @Insert
    long insert(Medication medication);
    
    @Update
    void update(Medication medication);
    
    @Delete
    void delete(Medication medication);
    
    @Query("SELECT * FROM medications ORDER BY startDateTime DESC")
    LiveData<List<Medication>> getAllMedications();
    
    @Query("SELECT * FROM medications ORDER BY startDateTime DESC")
    List<Medication> getAllMedicationsSync();
    
    @Query("SELECT * FROM medications WHERE id = :id")
    LiveData<Medication> getMedication(long id);
} 