package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.databinding.ActivityDashboardBinding;
import java.util.Calendar;
import java.util.Locale;

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_home);

        binding.btnWorkout.setOnClickListener(v -> 
            startActivity(new Intent(this, WorkoutHomeActivity.class)));

        binding.btnDiet.setOnClickListener(v -> 
            startActivity(new Intent(this, DietHomeActivity.class)));

        binding.btnProgress.setOnClickListener(v -> 
            startActivity(new Intent(this, ProgressActivity.class)));

        binding.btnAi.setOnClickListener(v -> 
            startActivity(new Intent(this, FitnessScoreActivity.class)));

        binding.cardWater.setOnClickListener(v -> 
            startActivity(new Intent(this, WaterTrackerActivity.class)));

        binding.cardCalories.setOnClickListener(v -> 
            startActivity(new Intent(this, DietHomeActivity.class)));

        binding.ivUserAvatar.setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class)));

        binding.btnBell.setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void updateFitnessScore(int calories, int water) {
        float waterScore = (Math.min(water, 8) / 8.0f) * 25;
        float ds = 0;
        if (calories > 0) {
            float ratio = calories / 2000.0f;
            if (ratio > 1.0f) ratio = 2.0f - ratio;
            ds = Math.max(0, ratio * 25);
        }
        int workouts = preferenceManager.getTotalWorkouts();
        float workoutScore = (Math.min(workouts, 10) / 10.0f) * 50;

        int finalScore = Math.round(waterScore + ds + workoutScore);
        if (finalScore < 10) finalScore = 10;

        binding.tvFitnessScore.setText(String.valueOf(finalScore));

        if (finalScore >= 80) {
            binding.tvFitnessLevel.setText("Excellent");
            binding.tvFitnessLevel.setBackgroundResource(R.drawable.bg_chip_green);
        } else if (finalScore >= 60) {
            binding.tvFitnessLevel.setText("Good");
            binding.tvFitnessLevel.setBackgroundResource(R.drawable.bg_chip_green);
        } else if (finalScore >= 40) {
            binding.tvFitnessLevel.setText("Average");
            binding.tvFitnessLevel.setBackgroundColor(android.graphics.Color.parseColor("#F59E0B")); 
        } else {
            binding.tvFitnessLevel.setText("Needs Focus");
            binding.tvFitnessLevel.setBackgroundColor(android.graphics.Color.RED);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        String name = preferenceManager.getUserName();
        float weight = preferenceManager.getCurrentWeight();
        float height = preferenceManager.getUserHeight();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting = (hour < 12) ? "Good Morning" : (hour < 17) ? "Good Afternoon" : "Good Evening";
        binding.tvGreeting.setText(greeting);
        binding.tvUserName.setText(String.format("Welcome, %s 👋", name));

        binding.tvCurrentWeight.setText(String.format(Locale.getDefault(), "Weight: %.1f kg", weight));
        if (height > 0) {
            float heightMeters = height / 100f;
            float bmi = weight / (heightMeters * heightMeters);
            binding.tvBmi.setText(String.format(Locale.getDefault(), "BMI: %.1f", bmi));
        }
        
        float targetWeight = preferenceManager.getGoalWeight();
        int progressPercent = (targetWeight > 0) ? (int)((weight / targetWeight) * 100) : 0;
        if (progressPercent > 100) progressPercent = 100;

        binding.progressDailyGoal.setProgress(progressPercent);
        binding.tvGoalProgress.setText(String.format(Locale.getDefault(), "%d%% Goal Completed", progressPercent));

        int consumedCals = 0;
        if (preferenceManager.isMealDone("Breakfast")) consumedCals += preferenceManager.getMealKcal("Breakfast");
        if (preferenceManager.isMealDone("Lunch")) consumedCals += preferenceManager.getMealKcal("Lunch");
        if (preferenceManager.isMealDone("Dinner")) consumedCals += preferenceManager.getMealKcal("Dinner");
        if (preferenceManager.isMealDone("Snacks")) consumedCals += preferenceManager.getMealKcal("Snacks");
        
        binding.tvCaloriesProgress.setText(String.format(Locale.getDefault(), "%d/2000", consumedCals));
        binding.progressCalories.setProgress((consumedCals * 100) / 2000);

        int waterGlasses = preferenceManager.getWaterGlasses();
        binding.tvWaterProgress.setText(String.format(Locale.getDefault(), "%d/8 Glasses", waterGlasses));
        binding.progressWater.setProgress((waterGlasses * 100) / 8);

        updateFitnessScore(consumedCals, waterGlasses);

        String imageUri = preferenceManager.getProfileImage();
        Glide.with(this)
                .load(!imageUri.isEmpty() ? android.net.Uri.parse(imageUri) : "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(binding.ivUserAvatar);

        WorkoutData.Workout todayWorkout = WorkoutData.getWorkoutOfTheDay();
        binding.tvTodayWorkoutName.setText(todayWorkout.title);
        binding.tvTodayWorkoutInfo.setText(todayWorkout.subtitle);

        Glide.with(this)
            .load(todayWorkout.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_workout)
            .centerCrop()
            .into(binding.ivTodayWorkout);

        binding.btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkoutDetailActivity.class);
            intent.putExtra("workout_title", todayWorkout.title);
            intent.putExtra("workout_subtitle", todayWorkout.subtitle);
            intent.putExtra("workout_description", todayWorkout.description);
            intent.putExtra("workout_exercise", todayWorkout.exercise);
            intent.putExtra("workout_image", todayWorkout.imageUrl);
            intent.putExtra("workout_reps", todayWorkout.reps);
            intent.putExtra("workout_sets", todayWorkout.sets);
            if (todayWorkout.steps != null) {
                intent.putExtra("workout_steps", todayWorkout.steps);
            }
            startActivity(intent);
        });
    }
}