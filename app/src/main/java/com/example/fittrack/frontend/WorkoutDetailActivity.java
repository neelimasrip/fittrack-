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
import com.example.fittrack.databinding.ActivityWorkoutDetailBinding;

public class WorkoutDetailActivity extends BaseActivity {

    private ActivityWorkoutDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        
        String title = getIntent().getStringExtra("workout_title");
        String subtitle = getIntent().getStringExtra("workout_subtitle");
        String desc = getIntent().getStringExtra("workout_description");
        String exercise = getIntent().getStringExtra("workout_exercise");
        String imageUrl = getIntent().getStringExtra("workout_image");
        int reps = getIntent().getIntExtra("workout_reps", 12);
        int sets = getIntent().getIntExtra("workout_sets", 3);

        if (title != null) {
            binding.tvDetailTitle.setText(title);
            binding.tvHeroTitle.setText(title);
        }
        if (desc != null) {
            binding.tvDescription.setText(desc);
        }
        if (exercise != null) {
            binding.tvExercise1.setText(exercise);
        }

        loadImages(imageUrl);

        binding.btnStartWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", title != null ? title : "Full Body Strength");
            intent.putExtra("exercise_image", imageUrl);
            intent.putExtra("reps", reps);
            intent.putExtra("total_sets", sets);
            
            int duration = 30; // default
            int calories = 300; // default estimated
            if (subtitle != null) {
                if (subtitle.contains("20 min")) { duration = 20 * 60; calories = 150; }
                else if (subtitle.contains("60 min")) { duration = 60 * 60; calories = 500; }
                else if (subtitle.contains("30 min")) { duration = 30 * 60; calories = 250; }
                else if (subtitle.contains("15 min")) { duration = 15 * 60; calories = 100; }
                else if (subtitle.contains("10 min")) { duration = 10 * 60; calories = 80; }
                else { duration = 30 * 60; calories = 200; }
            }
            
            intent.putExtra("duration_seconds", duration);
            intent.putExtra("calories_burned", calories);
            startActivity(intent);
        });

        binding.row1.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_name", exercise != null ? exercise : "Dumbbell Bench Press");
            intent.putExtra("exercise_image", imageUrl);
            startActivity(intent);
        });
        
        binding.btnBookmark.setOnClickListener(v -> {
            Toast.makeText(this, "Workout bookmarked!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadImages(String imageUrl) {
        Glide.with(this)
            .load(imageUrl != null ? imageUrl : "https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?w=800")
            .placeholder(R.drawable.ic_workout)
            .centerCrop()
            .into(binding.ivDetailHero);
    }
}