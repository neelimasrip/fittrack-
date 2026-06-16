package com.example.fittrack;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
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

        // Click Listeners
        binding.btnResume.setOnClickListener(v -> 
            Toast.makeText(this, "Resuming workout...", Toast.LENGTH_SHORT).show());

        binding.cardResumeToday.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActiveWorkoutActivity.class);
            intent.putExtra("exercise_name", "Morning Cardio Blitz");
            intent.putExtra("duration_seconds", 60);
            startActivity(intent);
        });

        binding.cardStressMapper.setOnClickListener(v -> 
            startActivity(new Intent(this, StressMapperActivity.class)));

        binding.cardMicroWorkout.setOnClickListener(v -> 
            startActivity(new Intent(this, MicroWorkoutActivity.class)));

        View.OnClickListener workoutDetailListener = v -> 
            startActivity(new Intent(this, WorkoutDetailActivity.class));

        binding.cardWorkout1.setOnClickListener(workoutDetailListener);
        binding.cardWorkout2.setOnClickListener(workoutDetailListener);

        binding.btnMenu.setOnClickListener(v -> 
            Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show());

        binding.btnBell.setOnClickListener(v -> 
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show());
    }

    private void loadImages() {
        // Hero Workout
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800") 
            .into(binding.ivHeroWorkout);

        // Morning Cardio Blitz
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1538805060514-97d9cc17730c?w=400") 
            .into(binding.ivWorkout1);

        // Advanced Power Lift
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1581009146145-b5ef03a7101b?w=400") 
            .into(binding.ivWorkout2);

        // HIIT Cardio Burn
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1434682881908-b43d0467b798?w=400") 
            .into(binding.ivWorkout3);
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
    }
}