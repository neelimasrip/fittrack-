package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.databinding.ActivityMicroWorkoutBinding;
import java.util.Locale;

public class MicroWorkoutActivity extends BaseActivity {

    private ActivityMicroWorkoutBinding binding;
    private String currentExercise = "Push-ups";
    private int selectedMinutes = 5;
    private int currentReps = 15;
    private int currentSets = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMicroWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_workout);
        
        binding.btnBack.setOnClickListener(v -> finish());

        // Default Load
        selectTime(binding.card5min, 5, "Push-ups", 
            "Push-ups - 2 Min", "Squats - 2 Min", "Plank - 1 Min", 
            "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800", 15, 3);

        binding.card5min.setOnClickListener(v -> selectTime(binding.card5min, 5, "Push-ups", 
            "Push-ups - 2 Min", "Squats - 2 Min", "Plank - 1 Min", 
            "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800", 15, 3));
            
        binding.card7min.setOnClickListener(v -> selectTime(binding.card7min, 7, "Jumping Jacks", 
            "Jumping Jacks - 2 Min", "Push-ups - 3 Min", "Burpees - 2 Min", 
            "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800", 20, 4));
            
        binding.card10min.setOnClickListener(v -> selectTime(binding.card10min, 10, "Bodyweight Squats", 
            "Squats - 4 Min", "Lunges - 3 Min", "Glute Bridges - 3 Min", 
            "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800", 12, 5));
            
        binding.card15min.setOnClickListener(v -> selectTime(binding.card15min, 15, "Mountain Climbers", 
            "High Knees - 5 Min", "Mountain Climbers - 5 Min", "Bicycle Crunches - 5 Min", 
            "https://images.unsplash.com/photo-1434682881908-b43d0467b798?w=800", 25, 6));

        binding.btnStartMicro.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", currentExercise);
            intent.putExtra("duration_seconds", selectedMinutes * 60);
            intent.putExtra("exercise_image", getImageUrlForExercise(currentExercise));
            intent.putExtra("reps", currentReps);
            intent.putExtra("total_sets", currentSets);
            startActivity(intent);
        });
    }

    private String getImageUrlForExercise(String ex) {
        if (ex.contains("Push-ups")) return "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800";
        if (ex.contains("Jumping Jacks")) return "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800";
        if (ex.contains("Bodyweight Squats")) return "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800";
        return "https://images.unsplash.com/photo-1434682881908-b43d0467b798?w=800";
    }

    private void selectTime(CardView selectedCard, int min, String exercise, String ex1, String ex2, String ex3, String imageUrl, int reps, int sets) {
        currentExercise = exercise;
        selectedMinutes = min;
        currentReps = reps;
        currentSets = sets;
        
        binding.card5min.setCardBackgroundColor(Color.WHITE);
        binding.card7min.setCardBackgroundColor(Color.WHITE);
        binding.card10min.setCardBackgroundColor(Color.WHITE);
        binding.card15min.setCardBackgroundColor(Color.WHITE);

        selectedCard.setCardBackgroundColor(Color.parseColor("#EEF4FB"));
        
        binding.tvWorkoutPreviewTitle.setText(String.format(Locale.getDefault(), "Your %d-Minute Workout", min));
        binding.tvMicroEx1.setText(ex1);
        binding.tvMicroEx2.setText(ex2);
        binding.tvMicroEx3.setText(ex3);

        Glide.with(this)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_workout)
            .error(R.drawable.ic_workout)
            .centerCrop()
            .into(binding.ivMicroPreview);
    }
}