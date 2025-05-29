package com.example.lab5_20202396;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MedicationAppPrefs";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_MOTIVATIONAL_MESSAGE = "motivational_message";
    private static final String KEY_PROFILE_IMAGE_PATH = "profile_image_path";

    private TextView greetingText;
    private TextView motivationalText;
    private ImageView profileImage;
    private Button viewMedicationsButton;
    private Button settingsButton;
    private SharedPreferences sharedPreferences;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImage = result.getData().getData();
                if (selectedImage != null) {
                    saveImageToInternalStorage(selectedImage);
                }
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        initializeViews();
        loadUserData();
        setupClickListeners();
    }

    private void initializeViews() {
        greetingText = findViewById(R.id.greetingText);
        motivationalText = findViewById(R.id.motivationalText);
        profileImage = findViewById(R.id.profileImage);
        viewMedicationsButton = findViewById(R.id.viewMedicationsButton);
        settingsButton = findViewById(R.id.settingsButton);
    }

    private void loadUserData() {
        String userName = sharedPreferences.getString(KEY_USER_NAME, "");
        String motivationalMessage = sharedPreferences.getString(KEY_MOTIVATIONAL_MESSAGE, 
            "¡Mantén un buen control de tus medicamentos!");
        String profileImagePath = sharedPreferences.getString(KEY_PROFILE_IMAGE_PATH, "");

        greetingText.setText(userName.isEmpty() ? "¡Hola!" : "¡Hola, " + userName + "!");
        motivationalText.setText(motivationalMessage);

        if (!profileImagePath.isEmpty()) {
            File imageFile = new File(getFilesDir(), profileImagePath);
            if (imageFile.exists()) {
                profileImage.setImageURI(Uri.fromFile(imageFile));
            }
        }
    }

    private void setupClickListeners() {
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        viewMedicationsButton.setOnClickListener(v -> {
            // TODO: Implement medication list activity
        });

        settingsButton.setOnClickListener(v -> {
            // TODO: Implement settings activity
        });
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                String fileName = "profile_image.jpg";
                File outputFile = new File(getFilesDir(), fileName);
                FileOutputStream outputStream = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                sharedPreferences.edit()
                    .putString(KEY_PROFILE_IMAGE_PATH, fileName)
                    .apply();

                profileImage.setImageURI(Uri.fromFile(outputFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}