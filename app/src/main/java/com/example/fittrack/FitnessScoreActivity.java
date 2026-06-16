package com.example.fittrack;

import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityFitnessScoreBinding;

public class FitnessScoreActivity extends BaseActivity {

    private ActivityFitnessScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFitnessScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        loadImages();
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=200") // Workout
            .into(binding.ivScoreWorkout);

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=200") // Diet
            .into(binding.ivScoreDiet);

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=200") // Sleep
            .into(binding.ivScoreSleep);

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1499209974431-9dac3adaf471?w=200") // Mind
            .into(binding.ivScoreMind);
    }
}