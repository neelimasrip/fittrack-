package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityWorkoutDetailBinding;

public class WorkoutDetailActivity extends BaseActivity {

    private ActivityWorkoutDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        loadImages();

        binding.btnStartWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", "Full Body Strength");
            intent.putExtra("duration_seconds", 30);
            startActivity(intent);
        });

        binding.row1.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_name", "Dumbbell Bench Press");
            startActivity(intent);
        });
        
        binding.btnBookmark.setOnClickListener(v -> {
            Toast.makeText(this, "Workout bookmarked!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?w=800") // Heavy lift
            .into(binding.ivDetailHero);
    }
}