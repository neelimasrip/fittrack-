package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_profile);

        binding.btnEdit.setOnClickListener(v -> 
            startActivity(new Intent(this, EditProfileActivity.class)));
            
        binding.ivProfilePic.setOnClickListener(v -> 
            startActivity(new Intent(this, EditProfileActivity.class)));

        binding.btnChangePic.setOnClickListener(v ->
            startActivity(new Intent(this, EditProfileActivity.class)));
            
        binding.rowSettings.setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class)));

        binding.rowLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        binding.tvName.setText(preferenceManager.getUserName());
        binding.tvEmail.setText(preferenceManager.getUserEmail());
        binding.tvWeight.setText(String.valueOf(preferenceManager.getCurrentWeight()));
        binding.tvHeight.setText(String.valueOf((int) preferenceManager.getUserHeight()));

        String imageUri = preferenceManager.getProfileImage();
        if (!imageUri.isEmpty()) {
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_person)
                .into(binding.ivProfilePic);
        } else {
            Glide.with(this)
                .load("https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=200")
                .into(binding.ivProfilePic);
        }
    }
}