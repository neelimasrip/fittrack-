package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import com.bumptech.glide.Glide;
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
            
        // Note: btnStart listener is set dynamically in loadUserData
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

        String greeting;
        if (hour < 12) {
            greeting = "Good Morning";
        } else if (hour < 17) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        binding.tvGreeting.setText(greeting);
        binding.tvUserName.setText(String.format("Welcome, %s 👋", name));

        binding.tvCurrentWeight.setText(String.format(Locale.getDefault(), "Weight: %.1f kg", weight));
        if (height > 0) {
            float heightMeters = height / 100f;
            float bmi = weight / (heightMeters * heightMeters);
            binding.tvBmi.setText(String.format(Locale.getDefault(), "BMI: %.1f", bmi));
        }
        
        float targetWeight = preferenceManager.getGoalWeight();
        int progress = (targetWeight > 0) ? (int)((weight / targetWeight) * 100) : 0;
        if (progress > 100) progress = 100;

        binding.progressDailyGoal.setProgress(progress);
        binding.tvGoalProgress.setText(String.format(Locale.getDefault(), "%d%% Goal Completed", progress));

        String imageUri = preferenceManager.getProfileImage();
        if (!imageUri.isEmpty()) {
            Glide.with(this)
                    .load(android.net.Uri.parse(imageUri))
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(binding.ivUserAvatar);
        } else {
            Glide.with(this)
                .load("https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=200")
                .placeholder(R.drawable.ic_person)
                .into(binding.ivUserAvatar);
        }

        // Handle Workout of the Day
        WorkoutData.Workout todayWorkout = WorkoutData.getWorkoutOfTheDay();
        binding.tvTodayWorkoutName.setText(todayWorkout.title);
        binding.tvTodayWorkoutInfo.setText(todayWorkout.subtitle);

        Glide.with(this)
            .load(todayWorkout.imageUrl)
            .placeholder(R.drawable.ic_workout)
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
            startActivity(intent);
        });
    }
}