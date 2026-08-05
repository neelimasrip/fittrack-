package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.databinding.ActivityFitnessScoreBinding;

public class FitnessScoreActivity extends BaseActivity {

    private ActivityFitnessScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFitnessScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.ivScoreWorkout.setOnClickListener(v -> startActivity(new android.content.Intent(this, WorkoutHomeActivity.class)));
        binding.ivScoreDiet.setOnClickListener(v -> startActivity(new android.content.Intent(this, DietHomeActivity.class)));
        binding.ivScoreSleep.setOnClickListener(v -> startActivity(new android.content.Intent(this, WaterTrackerActivity.class)));
        binding.ivScoreMind.setOnClickListener(v -> startActivity(new android.content.Intent(this, StressMapperActivity.class)));
        loadImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        com.example.fittrack.backend.FitnessScoreCalculator.ScoreResult result = 
            com.example.fittrack.backend.FitnessScoreCalculator.calculateScore(this);
            
        binding.tvScoreNumber.setText(String.valueOf(result.finalScore));
        binding.tvScoreStatus.setText(result.status);
        binding.tvScoreStatus.setTextColor(result.color);
        binding.scoreProgress.setProgress(result.finalScore);
    }

    private void loadImages() {
        Glide.with(this).load("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_workout).centerCrop().into(binding.ivScoreWorkout);
        Glide.with(this).load("https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=400").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_diet).centerCrop().into(binding.ivScoreDiet);
        Glide.with(this).load("https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=400").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_clock).centerCrop().into(binding.ivScoreSleep);
        Glide.with(this).load("https://images.unsplash.com/photo-1499209974431-9dac3adaf471?w=400").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_mood).centerCrop().into(binding.ivScoreMind);
    }
}