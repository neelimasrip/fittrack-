package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

            FirebaseAuth.getInstance().signOut();

            preferenceManager.setLoggedIn(false);
            preferenceManager.clearProfile();

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
        String name = preferenceManager.getUserName();
        String email = preferenceManager.getUserEmail();
        String imageUri = preferenceManager.getProfileImage();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (email.isEmpty() && user.getEmail() != null) email = user.getEmail();
            if (name.isEmpty() && user.getDisplayName() != null) name = user.getDisplayName();
            if (imageUri.isEmpty() && user.getPhotoUrl() != null) imageUri = user.getPhotoUrl().toString();
        }

        binding.tvName.setText(!name.isEmpty() ? name : "FitTrack User");
        binding.tvEmail.setText(!email.isEmpty() ? email : "user@fittrack.com");

        if (!imageUri.isEmpty()) {
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(binding.ivProfilePic);
        } else {
            Glide.with(this)
                    .load("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200")
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(binding.ivProfilePic);
        }

        binding.tvWeight.setText(
                String.valueOf(preferenceManager.getCurrentWeight())
        );

        binding.tvHeight.setText(
                String.valueOf((int) preferenceManager.getUserHeight())
        );
    }
}