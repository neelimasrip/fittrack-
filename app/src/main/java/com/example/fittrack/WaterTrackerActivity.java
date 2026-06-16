package com.example.fittrack;

import android.os.Bundle;
import android.widget.Toast;
import com.example.fittrack.databinding.ActivityWaterTrackerBinding;

public class WaterTrackerActivity extends BaseActivity {

    private ActivityWaterTrackerBinding binding;
    private int glasses = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaterTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_diet);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnAddWater.setOnClickListener(v -> {
            if (glasses < 8) {
                glasses++;
                updateWaterUI();
            } else {
                Toast.makeText(this, "Daily goal reached! Great job!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWaterUI() {
        binding.tvGlassesCount.setText(String.valueOf(glasses));
        // Simple height calculation for fill level
        int maxHeight = binding.waterFillBg.getHeight();
        if (maxHeight == 0) maxHeight = 600; // fallback if view not measured
        binding.waterFillLevel.getLayoutParams().height = (maxHeight * glasses) / 8;
        binding.waterFillLevel.requestLayout();
    }
}