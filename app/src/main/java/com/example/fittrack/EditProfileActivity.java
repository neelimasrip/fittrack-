package com.example.fittrack;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private PreferenceManager preferenceManager;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<String> pickImage =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            String internalPath = saveImageToInternalStorage(uri);

                            if (internalPath != null) {
                                selectedImageUri = Uri.parse(internalPath);

                                binding.ivProfilePic.setImageURI(selectedImageUri);

                                preferenceManager.saveProfileImage(internalPath);

                                Toast.makeText(
                                        this,
                                        "Saved: " + internalPath,
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                    });

    private String saveImageToInternalStorage(Uri uri) {
        String fileName = "profile_pic_" + System.currentTimeMillis() + ".jpg";
        java.io.File newFile = new java.io.File(getFilesDir(), fileName);

        try (java.io.InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             java.io.FileOutputStream outputStream =
                     new java.io.FileOutputStream(newFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return Uri.fromFile(newFile).toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.ivProfilePic.setOnClickListener(
                v -> pickImage.launch("image/*"));

        binding.ivCamera.setOnClickListener(
                v -> pickImage.launch("image/*"));

        binding.btnSave.setOnClickListener(v -> {

            String name =
                    binding.etName.getText().toString().trim();

            String email =
                    binding.etEmail.getText().toString().trim();

            String phone =
                    binding.etPhone.getText().toString().trim();

            String weightStr =
                    binding.etWeight.getText().toString().trim();

            String heightStr =
                    binding.etHeight.getText().toString().trim();

            float weight =
                    weightStr.isEmpty() ? 0 :
                            Float.parseFloat(weightStr);

            float height =
                    heightStr.isEmpty() ? 0 :
                            Float.parseFloat(heightStr);

            preferenceManager.saveProfile(
                    name,
                    email,
                    phone,
                    weight,
                    height
            );

            Toast.makeText(
                    this,
                    "Profile Updated",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });

        loadUserData();
    }

    private void loadUserData() {

        binding.etName.setText(
                preferenceManager.getUserName());

        binding.etEmail.setText(
                preferenceManager.getUserEmail());

        binding.etPhone.setText(
                preferenceManager.getUserPhone());

        binding.etWeight.setText(
                String.valueOf(
                        preferenceManager.getCurrentWeight()));

        binding.etHeight.setText(
                String.valueOf(
                        (int) preferenceManager.getUserHeight()));

        String imageUri =
                preferenceManager.getProfileImage();

        if (!imageUri.isEmpty()) {

            Glide.with(this)
                    .load(Uri.parse(imageUri))
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .placeholder(R.drawable.ic_person)
                    .into(binding.ivProfilePic);

        } else {

            Glide.with(this)
                    .load("https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=200")
                    .into(binding.ivProfilePic);
        }
    }
}