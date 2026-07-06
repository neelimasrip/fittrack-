package com.example.fittrack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityMicroWorkoutBinding;
import java.util.Locale;

public class MicroWorkoutActivity extends BaseActivity {

    private ActivityMicroWorkoutBinding binding;
    private String currentExercise = "Push-ups";
    private int selectedMinutes = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMicroWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_workout);
        loadImages();

        binding.btnBack.setOnClickListener(v -> finish());

        binding.card5min.setOnClickListener(v -> selectTime(binding.card5min, 5, "Push-ups", 
            "Push-ups - 2 Min", "Squats - 2 Min", "Plank - 1 Min", 
            "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400"));
            
        binding.card7min.setOnClickListener(v -> selectTime(binding.card7min, 7, "Jumping Jacks", 
            "Jumping Jacks - 2 Min", "Push-ups - 3 Min", "Burpees - 2 Min", 
            "https://images.unsplash.com/photo-1538805060514-97d9cc17730c?w=400"));
            
        binding.card10min.setOnClickListener(v -> selectTime(binding.card10min, 10, "Bodyweight Squats", 
            "Squats - 4 Min", "Lunges - 3 Min", "Glute Bridges - 3 Min", 
            "https://images.unsplash.com/photo-1583454110551-21f2fa227355?w=400"));
            
        binding.card15min.setOnClickListener(v -> selectTime(binding.card15min, 15, "Mountain Climbers", 
            "High Knees - 5 Min", "Mountain Climbers - 5 Min", "Bicycle Crunches - 5 Min", 
            "https://images.unsplash.com/photo-1434682881908-b43d0467b798?w=400"));

        binding.btnStartMicro.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", currentExercise);
            intent.putExtra("duration_seconds", selectedMinutes * 60);
            startActivity(intent);
        });
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400")
            .placeholder(R.drawable.ic_workout)
            .into(binding.ivMicroPreview);
    }

    private void selectTime(CardView selectedCard, int min, String exercise, String ex1, String ex2, String ex3, String imageUrl) {
        currentExercise = exercise;
        selectedMinutes = min;
        
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
            .into(binding.ivMicroPreview);
    }
}