package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.os.Bundle;
import android.widget.Toast;
import com.example.fittrack.databinding.ActivityWaterTrackerBinding;

public class WaterTrackerActivity extends BaseActivity {

    private ActivityWaterTrackerBinding binding;
    private PreferenceManager preferenceManager;
    private int glasses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaterTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_diet);

        glasses = preferenceManager.getWaterGlasses();
        
        // Use post to ensure view is measured before UI update
        binding.getRoot().post(this::updateWaterUI);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnAddWater.setOnClickListener(v -> {
            if (glasses < 8) {
                glasses++;
                preferenceManager.setWaterGlasses(glasses);
                updateWaterUI();
            } else {
                Toast.makeText(this, "Daily goal reached! Great job!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWaterUI() {
        binding.tvGlassesCount.setText(String.valueOf(glasses));
        
        int remaining = 8 - glasses;
        if (remaining > 0) {
            binding.tvRemainingInfo.setText(String.format(java.util.Locale.getDefault(), "Drink %d more glasses to reach your goal", remaining));
        } else {
            binding.tvRemainingInfo.setText("Daily goal reached! Great job!");
        }

        // Simple height calculation for fill level
        int maxHeight = binding.waterFillBg.getHeight();
        if (maxHeight == 0) maxHeight = 600; // fallback if view not measured
        binding.waterFillLevel.getLayoutParams().height = (maxHeight * glasses) / 8;
        binding.waterFillLevel.requestLayout();
    }
}