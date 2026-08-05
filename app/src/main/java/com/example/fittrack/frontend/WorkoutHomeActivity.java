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
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutHomeActivity extends BaseActivity {

    private ActivityWorkoutHomeBinding binding;
    private final List<TextView> tabs = new ArrayList<>();
    private String currentCategory = "All";
    private String currentDifficulty = "All Levels";
    private WorkoutAdapter adapter;
    private List<WorkoutData.Workout> allWorkouts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_workout);

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

        
        allWorkouts = WorkoutData.getAllWorkouts();
        adapter = new WorkoutAdapter(this, new ArrayList<>());
        binding.rvWorkouts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvWorkouts.setAdapter(adapter);

        binding.btnFilter.setOnClickListener(this::showFilterMenu);
        binding.btnMenu.setOnClickListener(v -> Toast.makeText(this, "Menu drawer opening...", Toast.LENGTH_SHORT).show());
        binding.btnBell.setOnClickListener(v -> Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show());
        
        applyFilters();
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
        List<WorkoutData.Workout> filtered = new ArrayList<>();
        for (WorkoutData.Workout w : allWorkouts) {
            if (isMatch(w.category, w.difficulty)) {
                filtered.add(w);
            }
        }
        adapter.setWorkouts(filtered);
    }

    private boolean isMatch(String category, String difficulty) {
        boolean categoryMatch = currentCategory.equals("All") || currentCategory.equals(category);
        boolean difficultyMatch = currentDifficulty.equals("All Levels") || currentDifficulty.equals(difficulty);
        return categoryMatch && difficultyMatch;
    }

}
