package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityYogaHomeBinding;

public class YogaHomeActivity extends BaseActivity {

    private ActivityYogaHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYogaHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_workout);
        loadImages();

        binding.btnBack.setOnClickListener(v -> finish());
        
        binding.btnInfo.setOnClickListener(v -> 
            Toast.makeText(this, "Yoga & Pranayama Info", Toast.LENGTH_SHORT).show());

        binding.cardStressRelief.setOnClickListener(v -> startYogaSession("Stress Relief Yoga"));
        binding.cardMorningYoga.setOnClickListener(v -> startYogaSession("Morning Yoga Flow"));
        binding.cardAnulomVilom.setOnClickListener(v -> startYogaSession("Anulom Vilom Pranayama"));
        binding.cardKapalbhati.setOnClickListener(v -> startYogaSession("Kapalbhati Pranayama"));
    }

    private void startYogaSession(String name) {
        Intent intent = new Intent(this, ActiveWorkoutActivity.class);
        intent.putExtra("exercise_name", name);
        intent.putExtra("duration_seconds", 120);
        startActivity(intent);
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400") // Calm Yoga
            .into(binding.ivYogaPromo);
    }
}