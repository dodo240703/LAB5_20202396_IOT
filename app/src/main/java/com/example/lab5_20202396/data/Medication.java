package com.example.lab5_20202396.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "medications")
public class Medication {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;
    private String type; // Pastilla, Jarabe, Ampolla, Cápsula
    private String dose;
    private int frequencyHours;
    private Date startDateTime;
    
    public Medication(String name, String type, String dose, int frequencyHours, Date startDateTime) {
        this.name = name;
        this.type = type;
        this.dose = dose;
        this.frequencyHours = frequencyHours;
        this.startDateTime = startDateTime;
    }
    
    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
    
    public int getFrequencyHours() { return frequencyHours; }
    public void setFrequencyHours(int frequencyHours) { this.frequencyHours = frequencyHours; }
    
    public Date getStartDateTime() { return startDateTime; }
    public void setStartDateTime(Date startDateTime) { this.startDateTime = startDateTime; }
} 