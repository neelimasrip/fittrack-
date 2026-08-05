package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityGoalPredictorBinding;

public class GoalPredictorActivity extends BaseActivity {

    private ActivityGoalPredictorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoalPredictorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_progress);
        loadImages();

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1483721310020-03333e577078?w=800") // Mountain success
            .placeholder(R.drawable.ic_progress_white)
            .into(binding.ivGoalHeader);
    }
}