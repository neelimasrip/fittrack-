package com.example.fittrack;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        binding.btnBack.setOnClickListener(v -> finish());
        
        binding.cardProfile.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Initialize Notification Switch
        binding.switchNotifications.setChecked(preferenceManager.isNotificationsEnabled());
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setNotificationsEnabled(isChecked);
            String message = isChecked ? "Notifications enabled" : "Notifications disabled";
            android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
        });

        // Initialize Dark Mode Switch
        binding.switchDarkMode.setChecked(preferenceManager.isDarkMode());
        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setDarkMode(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        binding.rowHelp.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SENDTO);
            intent.setData(android.net.Uri.parse("mailto:support@fittrack.com"));
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FitTrack Support");
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                android.widget.Toast.makeText(this, "No email app found", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        binding.tvSettingsName.setText(preferenceManager.getUserName());
        binding.tvSettingsEmail.setText(preferenceManager.getUserEmail());

        String imageUri = preferenceManager.getProfileImage();
        if (!imageUri.isEmpty()) {
            Glide.with(this)
                .load(android.net.Uri.parse(imageUri))
                .placeholder(R.drawable.ic_person)
                .into(binding.ivSettingsPic);
        } else {
            Glide.with(this)
                .load("https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=200")
                .into(binding.ivSettingsPic);
        }
    }
}