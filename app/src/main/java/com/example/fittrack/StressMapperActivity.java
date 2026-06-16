package com.example.fittrack;

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
                binding.tvStressValue.setText(String.format(Locale.getDefault(), "%d/10", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.btnStartYoga.setOnClickListener(v -> 
            startActivity(new Intent(this, YogaHomeActivity.class))
        );

        View.OnClickListener moodListener = v -> {
            Toast.makeText(this, "Mood logged. We recommend specialized breathing exercises.", Toast.LENGTH_SHORT).show();
        };

        binding.btnMood1.setOnClickListener(moodListener);
        binding.btnMood2.setOnClickListener(moodListener);
        binding.btnMood3.setOnClickListener(moodListener);
        binding.btnMood4.setOnClickListener(moodListener);
        binding.btnMood5.setOnClickListener(moodListener);
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800") // Meditation
            .into(binding.ivStressHeader);
    }
}