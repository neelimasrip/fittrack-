package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityYogaHomeBinding;

public class YogaHomeActivity extends BaseActivity {

    private ActivityYogaHomeBinding binding;
    private String p1Name = "Anulom Vilom";
    private String p2Name = "Kapalbhati";
    private int p1Dur = 600;
    private int p2Dur = 900;
    private int p1Sets = 3;
    private int p2Sets = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYogaHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_workout);
        
        int stressLevel = getIntent().getIntExtra("stress_level", 5);
        String mood = getIntent().getStringExtra("mood");
        
        updateDynamicRecommendations(stressLevel, mood);
        loadImages(stressLevel);

        binding.btnBack.setOnClickListener(v -> finish());
        
        binding.btnInfo.setOnClickListener(v -> showYogaInfoDialog());

        binding.cardStressRelief.setOnClickListener(v -> startYogaSession("Stress Relief Yoga", 900, 1, 1));
        binding.cardMorningYoga.setOnClickListener(v -> startYogaSession("Morning Yoga Flow", 600, 1, 1));
        
        binding.cardAnulomVilom.setOnClickListener(v -> startYogaSession(p1Name, p1Dur, 10, p1Sets));
        binding.cardKapalbhati.setOnClickListener(v -> startYogaSession(p2Name, p2Dur, 20, p2Sets));
    }

    private void showYogaInfoDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Yoga & Pranayama")
            .setMessage("Yoga and Pranayama help in reducing stress, improving focus, and balancing your body's energy. Practicing daily for 15-20 minutes can significantly improve your mental and physical well-being.")
            .setPositiveButton("Got It", null)
            .show();
    }

    private void updateDynamicRecommendations(int stress, String mood) {
        if (stress >= 7) {
            binding.tvRecommendation.setText("High stress detected. Focus on calming breaths.");
            p1Name = "Bhramari (Bee Breath)";
            p1Dur = 300;
            p1Sets = 5;
            p2Name = "Anulom Vilom";
            p2Dur = 1200;
            p2Sets = 4;
            binding.cardStressRelief.setCardBackgroundColor(android.graphics.Color.parseColor("#EEF4FB"));
        } else if (stress >= 4) {
            binding.tvRecommendation.setText("Moderate stress. A balanced session will help.");
            p1Name = "Anulom Vilom";
            p1Dur = 600;
            p1Sets = 3;
            p2Name = "Ujjayi Breath";
            p2Dur = 600;
            p2Sets = 3;
        } else {
            binding.tvRecommendation.setText("Low stress! Great time for energizing techniques.");
            p1Name = "Kapalbhati";
            p1Dur = 900;
            p1Sets = 3;
            p2Name = "Surya Bhedana";
            p2Dur = 600;
            p2Sets = 3;
            binding.cardMorningYoga.setCardBackgroundColor(android.graphics.Color.parseColor("#EEF4FB"));
        }
        
        binding.tvPranayama1Title.setText(p1Name);
        binding.tvPranayama1Duration.setText((p1Dur / 60) + " min");
        binding.tvPranayama2Title.setText(p2Name);
        binding.tvPranayama2Duration.setText((p2Dur / 60) + " min");
    }

    private void startYogaSession(String name, int duration, int reps, int sets) {
        Intent intent = new Intent(this, ActiveWorkoutActivity.class);
        intent.putExtra("exercise_name", name);
        intent.putExtra("duration_seconds", duration);
        intent.putExtra("reps", reps);
        intent.putExtra("total_sets", sets);
        intent.putExtra("exercise_image", "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800");
        startActivity(intent);
    }

    private void loadImages(int stress) {
        String url = (stress >= 7) 
            ? "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800" 
            : "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800";

        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_workout)
            .centerCrop()
            .into(binding.ivYogaPromo);
    }
}