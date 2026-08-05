package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.databinding.ActivityWorkoutHomeBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutHomeActivity extends BaseActivity {

    private ActivityWorkoutHomeBinding binding;
    private final List<TextView> tabs = new ArrayList<>();
    private String currentCategory = "All";
    private String currentDifficulty = "All Levels";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_workout);
        loadImages();

        // Initialize Tabs
        tabs.add(binding.tabAll);
        tabs.add(binding.tabStrength);
        tabs.add(binding.tabCardio);
        tabs.add(binding.tabYoga);

        for (TextView tab : tabs) {
            tab.setOnClickListener(v -> selectTab(tab));
        }

        WorkoutData.Workout today = WorkoutData.getWorkoutOfTheDay();
        binding.tvHeroWorkoutTitle.setText(today.title);
        binding.tvHeroWorkoutInfo.setText(today.subtitle);
        
        Glide.with(this)
            .load(today.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_workout_white)
            .centerCrop()
            .into(binding.ivHeroWorkout);

        binding.btnResume.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", today.title);
            intent.putExtra("exercise_image", today.imageUrl);
            intent.putExtra("reps", today.reps);
            intent.putExtra("total_sets", today.sets);
            int duration = 30 * 60;
            try {
                String mins = today.subtitle.split(" ")[0];
                duration = Integer.parseInt(mins) * 60;
            } catch (Exception e) { duration = 20 * 60; }
            intent.putExtra("duration_seconds", duration);
            startActivity(intent);
        });

        binding.cardResumeToday.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", today.title);
            intent.putExtra("exercise_image", today.imageUrl);
            intent.putExtra("reps", today.reps);
            intent.putExtra("total_sets", today.sets);
            int duration = 20 * 60;
            try {
                String mins = today.subtitle.split(" ")[0];
                duration = Integer.parseInt(mins) * 60;
            } catch (Exception e) { }
            intent.putExtra("duration_seconds", duration);
            startActivity(intent);
        });

        binding.cardStressMapper.setOnClickListener(v -> startActivity(new Intent(this, StressMapperActivity.class)));
        binding.cardMicroWorkout.setOnClickListener(v -> startActivity(new Intent(this, MicroWorkoutActivity.class)));

        List<WorkoutData.Workout> workouts = WorkoutData.getAllWorkouts();
        binding.cardWorkout1.setOnClickListener(v -> openWorkoutDetail(workouts.get(0).title, workouts.get(0).subtitle, workouts.get(0).description, workouts.get(0).exercise, workouts.get(0).imageUrl, workouts.get(0).reps, workouts.get(0).sets, workouts.get(0).steps));
        binding.cardWorkout2.setOnClickListener(v -> openWorkoutDetail(workouts.get(1).title, workouts.get(1).subtitle, workouts.get(1).description, workouts.get(1).exercise, workouts.get(1).imageUrl, workouts.get(1).reps, workouts.get(1).sets, workouts.get(1).steps));
        binding.cardWorkout3.setOnClickListener(v -> openWorkoutDetail(workouts.get(2).title, workouts.get(2).subtitle, workouts.get(2).description, workouts.get(2).exercise, workouts.get(2).imageUrl, workouts.get(2).reps, workouts.get(2).sets, workouts.get(2).steps));
        binding.cardWorkout4.setOnClickListener(v -> openWorkoutDetail(workouts.get(3).title, workouts.get(3).subtitle, workouts.get(3).description, workouts.get(3).exercise, workouts.get(3).imageUrl, workouts.get(3).reps, workouts.get(3).sets, workouts.get(3).steps));
        binding.cardWorkout5.setOnClickListener(v -> openWorkoutDetail(workouts.get(4).title, workouts.get(4).subtitle, workouts.get(4).description, workouts.get(4).exercise, workouts.get(4).imageUrl, workouts.get(4).reps, workouts.get(4).sets, workouts.get(4).steps));

        binding.btnFilter.setOnClickListener(this::showFilterMenu);
        binding.btnMenu.setOnClickListener(v -> Toast.makeText(this, "Menu drawer opening...", Toast.LENGTH_SHORT).show());
        binding.btnBell.setOnClickListener(v -> Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show());
    }

    private void showFilterMenu(android.view.View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("All Levels");
        popup.getMenu().add("Easy");
        popup.getMenu().add("Medium");
        popup.getMenu().add("Hard");
        popup.setOnMenuItemClickListener(item -> {
            CharSequence title = item.getTitle();
            if (title != null) {
                currentDifficulty = title.toString();
                applyFilters();
                Toast.makeText(this, "Filtered by " + currentDifficulty, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        popup.show();
    }

    private void selectTab(TextView selectedTab) {
        for (TextView tab : tabs) {
            if (tab == selectedTab) {
                tab.setTextColor(ContextCompat.getColor(this, R.color.primary));
                tab.setTypeface(null, Typeface.BOLD);
                tab.setBackgroundResource(R.drawable.tab_selected);
            } else {
                tab.setTextColor(ContextCompat.getColor(this, R.color.grey_text));
                tab.setTypeface(null, Typeface.NORMAL);
                tab.setBackground(null);
            }
        }
        String tabText = selectedTab.getText().toString();
        if (Objects.equals(tabText, "All")) currentCategory = "All";
        else if (Objects.equals(tabText, "Strength")) currentCategory = "Strength";
        else if (Objects.equals(tabText, "Cardio")) currentCategory = "Cardio";
        else if (tabText.contains("Yoga")) currentCategory = "Yoga";
        applyFilters();
    }

    private void applyFilters() {
        boolean show1 = isMatch("Cardio", "Easy");
        boolean show2 = isMatch("Strength", "Hard");
        boolean show3 = isMatch("Cardio", "Medium");
        boolean show4 = isMatch("Yoga", "Easy");
        boolean show5 = isMatch("Strength", "Medium");
        binding.cardWorkout1.setVisibility(show1 ? android.view.View.VISIBLE : android.view.View.GONE);
        binding.cardWorkout2.setVisibility(show2 ? android.view.View.VISIBLE : android.view.View.GONE);
        binding.cardWorkout3.setVisibility(show3 ? android.view.View.VISIBLE : android.view.View.GONE);
        binding.cardWorkout4.setVisibility(show4 ? android.view.View.VISIBLE : android.view.View.GONE);
        binding.cardWorkout5.setVisibility(show5 ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private boolean isMatch(String category, String difficulty) {
        boolean categoryMatch = currentCategory.equals("All") || currentCategory.equals(category);
        boolean difficultyMatch = currentDifficulty.equals("All Levels") || currentDifficulty.equals(difficulty);
        return categoryMatch && difficultyMatch;
    }

    private void openWorkoutDetail(String title, String subtitle, String desc, String exercise, String imageUrl, int reps, int sets, String[] steps) {
        Intent intent = new Intent(this, WorkoutDetailActivity.class);
        intent.putExtra("workout_title", title);
        intent.putExtra("workout_subtitle", subtitle);
        intent.putExtra("workout_description", desc);
        intent.putExtra("workout_exercise", exercise);
        intent.putExtra("workout_image", imageUrl);
        intent.putExtra("workout_reps", reps);
        intent.putExtra("workout_sets", sets);
        if (steps != null) {
            intent.putExtra("workout_steps", steps);
        }
        startActivity(intent);
    }

    private void loadImages() {
        Glide.with(this).load("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_workout).centerCrop().into(binding.ivWorkout1);
        Glide.with(this).load("https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_workout).centerCrop().into(binding.ivWorkout2);
        Glide.with(this).load("https://images.unsplash.com/photo-1517963879433-6ad2b056d712?w=800").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_workout).centerCrop().into(binding.ivWorkout3);
        Glide.with(this).load("https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_workout).centerCrop().into(binding.ivWorkout4);
        Glide.with(this).load("https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_workout).centerCrop().into(binding.ivWorkout5);
    }
}