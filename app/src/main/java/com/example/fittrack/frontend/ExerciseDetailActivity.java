package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.databinding.ActivityExerciseDetailBinding;
import java.util.Objects;

public class ExerciseDetailActivity extends AppCompatActivity {

    private ActivityExerciseDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String exerciseName = getIntent().getStringExtra("exercise_name");
        String imageUrl = getIntent().getStringExtra("exercise_image");
        
        if (exerciseName != null) {
            binding.tvExerciseTitle.setText(exerciseName);
            updateContentForExercise(exerciseName);
        }

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnGotIt.setOnClickListener(v -> finish());
        
        loadImages(imageUrl);
    }

    private void updateContentForExercise(String name) {
        if (name.contains("Jumping Jacks")) {
            binding.tvInstructions.setText("1. Stand with feet together and arms at sides.\n2. Jump while spreading legs and clapping hands overhead.\n3. Jump back to starting position.\n4. Repeat in a rhythmic motion.");
        } else if (name.contains("Burpees")) {
            binding.tvInstructions.setText("1. Start in a standing position.\n2. Drop into a squat and place hands on the floor.\n3. Kick feet back into a plank, then return to squat.\n4. Jump up explosively with hands overhead.");
        } else if (name.contains("Sun Salutation")) {
            binding.tvInstructions.setText("1. Start in Mountain Pose.\n2. Inhale, reach up. Exhale, fold forward.\n3. Move through Plank, Cobra, and Downward Dog.\n4. Return to standing position with focused breathing.");
        } else if (name.contains("Plank")) {
            binding.tvInstructions.setText("1. Place forearms on the floor, elbows under shoulders.\n2. Extend legs back, balancing on toes.\n3. Keep body in a straight line from head to heels.\n4. Hold position while engaging core.");
        } else if (name.contains("Deadlift")) {
            binding.tvInstructions.setText("1. Stand with feet hip-width apart, barbell over mid-foot.\n2. Bend at hips and knees, grab the bar.\n3. Lift by extending hips and knees, keeping back straight.\n4. Lower the bar under control.");
        }
    }

    private void loadImages(String imageUrl) {
        Glide.with(this)
            .load(Objects.requireNonNullElse(imageUrl, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800"))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_workout)
            .centerCrop()
            .into(binding.ivExerciseGif);
    }
}