package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityDashboardBinding;

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_home);

        binding.btnWorkout.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutHomeActivity.class));
        });

        binding.btnDiet.setOnClickListener(v -> {
            startActivity(new Intent(this, DietHomeActivity.class));
        });

        binding.btnProgress.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgressActivity.class));
        });

        binding.btnAi.setOnClickListener(v -> {
            startActivity(new Intent(this, FitnessScoreActivity.class));
        });

        binding.btnStart.setOnClickListener(v -> {
            startActivity(new Intent(this, WorkoutDetailActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        String name = preferenceManager.getUserName();
        if (!name.isEmpty()) {
            binding.tvUserName.setText(name);
        }

        String imageUri = preferenceManager.getProfileImage();
        if (!imageUri.isEmpty()) {
            Glide.with(this)
                .load(android.net.Uri.parse(imageUri))
                .placeholder(R.drawable.ic_person)
                .into(binding.ivUserAvatar);
        } else {
            Glide.with(this)
                .load("https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=200") // User profile
                .into(binding.ivUserAvatar);
        }

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400") // Gym class
            .into(binding.ivTodayWorkout);
    }
}