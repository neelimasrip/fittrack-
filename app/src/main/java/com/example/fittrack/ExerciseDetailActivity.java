package com.example.fittrack;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityExerciseDetailBinding;

public class ExerciseDetailActivity extends AppCompatActivity {

    private ActivityExerciseDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String exerciseName = getIntent().getStringExtra("exercise_name");
        if (exerciseName != null) {
            binding.tvExerciseTitle.setText(exerciseName);
        }

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnGotIt.setOnClickListener(v -> finish());
        
        loadImages();
    }

    private void loadImages() {
        // High-quality workout demonstration image
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1581009146145-b5ef03a7101b?w=800")
            .into(binding.ivExerciseGif);
    }
}