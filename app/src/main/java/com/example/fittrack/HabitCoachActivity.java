package com.example.fittrack;

import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityHabitCoachBinding;

public class HabitCoachActivity extends BaseActivity {

    private ActivityHabitCoachBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHabitCoachBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_progress);
        loadImages();

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200") // Sarah
            .into(binding.ivCoachAvatar);
    }
}