package com.example.fittrack;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityWorkoutHomeBinding;
import java.util.ArrayList;
import java.util.List;

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

        // Hero - Workout of the Day
        WorkoutData.Workout today = WorkoutData.getWorkoutOfTheDay();
        binding.tvHeroWorkoutTitle.setText(today.title);
        binding.tvHeroWorkoutInfo.setText(today.subtitle);
        
        Glide.with(this)
            .load(today.imageUrl)
            .placeholder(R.drawable.ic_workout_white)
            .into(binding.ivHeroWorkout);

        binding.btnResume.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", today.title);
            intent.putExtra("exercise_image", today.imageUrl);
            intent.putExtra("reps", today.reps);
            intent.putExtra("total_sets", today.sets);
            
            // Extract duration from subtitle (e.g., "20 min • Easy")
            int duration = 30 * 60;
            try {
                String mins = today.subtitle.split(" ")[0];
                duration = Integer.parseInt(mins) * 60;
            } catch (Exception e) {}
            
            intent.putExtra("duration_seconds", duration);
            startActivity(intent);
        });

        binding.cardResumeToday.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", "Morning Cardio Blitz");
            intent.putExtra("duration_seconds", 20 * 60);
            startActivity(intent);
        });

        binding.cardStressMapper.setOnClickListener(v -> 
            startActivity(new Intent(this, StressMapperActivity.class)));

        binding.cardMicroWorkout.setOnClickListener(v -> 
            startActivity(new Intent(this, MicroWorkoutActivity.class)));

        binding.cardWorkout1.setOnClickListener(v -> openWorkoutDetail(
            "Morning Cardio Blitz", 
            "20 min • Easy", 
            "A high-energy cardio session designed to wake up your body and boost your metabolism for the day ahead.",
            "Jumping Jacks",
            "https://images.unsplash.com/photo-1538805060514-97d9cc17730c?w=400",
            20, 3
        ));

        binding.cardWorkout2.setOnClickListener(v -> openWorkoutDetail(
            "Advanced Power Lift", 
            "60 min • Hard", 
            "Focus on heavy compound movements to build maximum strength and power. Recommended for experienced lifters.",
            "Deadlift",
            "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400",
            8, 5
        ));

        binding.cardWorkout3.setOnClickListener(v -> openWorkoutDetail(
            "HIIT Cardio Burn", 
            "30 min • Medium", 
            "Interval training that pushes your heart rate to the limit. Great for burning fat and improving endurance.",
            "Burpees",
            "https://images.unsplash.com/photo-1517963879433-6ad2b056d712?w=400",
            15, 4
        ));

        binding.cardWorkout4.setOnClickListener(v -> openWorkoutDetail(
            "Zen Yoga Flow", 
            "15 min • Easy", 
            "Connect your breath with movement in this calming yoga sequence. Perfect for recovery and mental clarity.",
            "Sun Salutation",
            "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400",
            5, 3
        ));

        binding.cardWorkout5.setOnClickListener(v -> openWorkoutDetail(
            "Core Crusher", 
            "10 min • Medium", 
            "A quick and intense workout focused entirely on strengthening your abs and lower back.",
            "Plank",
            "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400",
            1, 4 
        ));

        binding.btnFilter.setOnClickListener(this::showFilterMenu);

        binding.btnMenu.setOnClickListener(v -> 
            Toast.makeText(this, "Menu drawer opening...", Toast.LENGTH_SHORT).show());

        binding.btnBell.setOnClickListener(v -> 
            Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show());
    }

    private void showFilterMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("All Levels");
        popup.getMenu().add("Easy");
        popup.getMenu().add("Medium");
        popup.getMenu().add("Hard");

        popup.setOnMenuItemClickListener(item -> {
            currentDifficulty = item.getTitle().toString();
            applyFilters();
            Toast.makeText(this, "Filtered by " + currentDifficulty, Toast.LENGTH_SHORT).show();
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
        if (tabText.equals("All")) currentCategory = "All";
        else if (tabText.equals("Strength")) currentCategory = "Strength";
        else if (tabText.equals("Cardio")) currentCategory = "Cardio";
        else if (tabText.contains("Yoga")) currentCategory = "Yoga";

        applyFilters();
    }

    private void applyFilters() {
        boolean show1 = isMatch("Cardio", "Easy");
        boolean show2 = isMatch("Strength", "Hard");
        boolean show3 = isMatch("Cardio", "Medium");
        boolean show4 = isMatch("Yoga", "Easy");
        boolean show5 = isMatch("Strength", "Medium");

        binding.cardWorkout1.setVisibility(show1 ? View.VISIBLE : View.GONE);
        binding.cardWorkout2.setVisibility(show2 ? View.VISIBLE : View.GONE);
        binding.cardWorkout3.setVisibility(show3 ? View.VISIBLE : View.GONE);
        binding.cardWorkout4.setVisibility(show4 ? View.VISIBLE : View.GONE);
        binding.cardWorkout5.setVisibility(show5 ? View.VISIBLE : View.GONE);
        
        if (!show1 && !show2 && !show3 && !show4 && !show5) {
             Toast.makeText(this, "No workouts match current filters", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMatch(String category, String difficulty) {
        boolean categoryMatch = currentCategory.equals("All") || currentCategory.equals(category);
        boolean difficultyMatch = currentDifficulty.equals("All Levels") || currentDifficulty.equals(difficulty);
        return categoryMatch && difficultyMatch;
    }

    private void openWorkoutDetail(String title, String subtitle, String desc, String exercise, String imageUrl, int reps, int sets) {
        Intent intent = new Intent(this, WorkoutDetailActivity.class);
        intent.putExtra("workout_title", title);
        intent.putExtra("workout_subtitle", subtitle);
        intent.putExtra("workout_description", desc);
        intent.putExtra("workout_exercise", exercise);
        intent.putExtra("workout_image", imageUrl);
        intent.putExtra("workout_reps", reps);
        intent.putExtra("workout_sets", sets);
        startActivity(intent);
    }

    private void loadImages() {
        // Workout list images
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1538805060514-97d9cc17730c?w=400") 
            .placeholder(R.drawable.ic_workout)
            .into(binding.ivWorkout1);

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400") 
            .placeholder(R.drawable.ic_workout)
            .into(binding.ivWorkout2);

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1517963879433-6ad2b056d712?w=400") 
            .placeholder(R.drawable.ic_workout)
            .into(binding.ivWorkout3);

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400") 
            .placeholder(R.drawable.ic_workout)
            .into(binding.ivWorkout4);

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400") 
            .placeholder(R.drawable.ic_workout)
            .into(binding.ivWorkout5);
    }
}