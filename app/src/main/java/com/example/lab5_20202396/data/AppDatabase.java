package com.example.lab5_20202396.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Medication.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "medication_db";
    private static AppDatabase instance;
    private static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    
    public abstract MedicationDao medicationDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .addCallback(new RoomDatabase.Callback() {
                @Override
                public void onCreate(SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    // Aquí podrías agregar datos iniciales si lo necesitas
                }
            })
            .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
} 