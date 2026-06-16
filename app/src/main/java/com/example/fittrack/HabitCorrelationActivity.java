package com.example.fittrack;

import android.os.Bundle;
import com.example.fittrack.databinding.ActivityHabitCorrelationBinding;

public class HabitCorrelationActivity extends BaseActivity {

    private ActivityHabitCorrelationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitCorrelationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_progress);

        binding.btnBack.setOnClickListener(v -> finish());
    }
}