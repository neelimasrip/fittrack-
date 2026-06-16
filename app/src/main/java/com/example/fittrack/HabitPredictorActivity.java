package com.example.fittrack;

import android.os.Bundle;
import com.example.fittrack.databinding.ActivityHabitPredictorBinding;

public class HabitPredictorActivity extends BaseActivity {

    private ActivityHabitPredictorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitPredictorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_progress);

        binding.btnBack.setOnClickListener(v -> finish());
    }
}