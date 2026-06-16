package com.example.fittrack;

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
            .into(binding.ivGoalHeader);
    }
}