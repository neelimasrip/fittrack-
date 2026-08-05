package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityHabitCoachBinding;

public class HabitCoachActivity extends BaseActivity {

    private ActivityHabitCoachBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitCoachBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_progress);
        loadImages();

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400") // Sarah
            .placeholder(R.drawable.ic_person)
            .circleCrop()
            .into(binding.ivCoachAvatar);
    }
}