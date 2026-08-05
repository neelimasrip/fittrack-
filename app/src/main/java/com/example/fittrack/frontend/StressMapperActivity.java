package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityStressMapperBinding;
import java.util.Locale;

public class StressMapperActivity extends BaseActivity {

    private ActivityStressMapperBinding binding;

    private int currentStress = 1;
    private String currentMood = "Okay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStressMapperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_workout);
        loadImages();

        binding.btnBack.setOnClickListener(v -> finish());

        binding.seekStress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentStress = progress;
                binding.tvStressValue.setText(String.format(Locale.getDefault(), "%d/10", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.btnStartYoga.setOnClickListener(v -> {
            Intent intent = new Intent(this, YogaHomeActivity.class);
            intent.putExtra("stress_level", currentStress);
            intent.putExtra("mood", currentMood);
            startActivity(intent);
        });

        binding.btnMood1.setOnClickListener(v -> selectMood("Terrible", v));
        binding.btnMood2.setOnClickListener(v -> selectMood("Bad", v));
        binding.btnMood3.setOnClickListener(v -> selectMood("Okay", v));
        binding.btnMood4.setOnClickListener(v -> selectMood("Good", v));
        binding.btnMood5.setOnClickListener(v -> selectMood("Great", v));
    }

    private void selectMood(String mood, View v) {
        currentMood = mood;
        // Reset all mood alphas
        binding.btnMood1.setAlpha(0.5f);
        binding.btnMood2.setAlpha(0.5f);
        binding.btnMood3.setAlpha(0.5f);
        binding.btnMood4.setAlpha(0.5f);
        binding.btnMood5.setAlpha(0.5f);
        
        v.setAlpha(1.0f);
        Toast.makeText(this, "Mood set to: " + mood, Toast.LENGTH_SHORT).show();
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800") // Meditation
            .placeholder(R.drawable.ic_mood)
            .into(binding.ivStressHeader);
    }
}