package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityOnboardingBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OnboardingActivity extends AppCompatActivity {
    ActivityOnboardingBinding binding;
    int primarySelected = 0; // 0=WeightLoss 1=MuscleGain 2=StayFit 3=ImproveHealth
    int secondarySelected = 1;
    String selectedGender = "Male"; // Default

    String[] activityLevels = {"Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extra Active"};
    int selectedActivityIndex = 2; // Default to Moderately Active

    CardView[] primaryCards;
    ImageView[] primaryChecks;
    CardView[] secondaryCards;
    ImageView[] secondaryChecks;
    PreferenceManager preferenceManager;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        primaryCards = new CardView[]{
                binding.cardP1, binding.cardP2,
                binding.cardP3, binding.cardP4};
        primaryChecks = new ImageView[]{
                binding.checkP1, binding.checkP2,
                binding.checkP3, binding.checkP4};
        secondaryCards = new CardView[]{
                binding.cardS1, binding.cardS2,
                binding.cardS3, binding.cardS4};
        secondaryChecks = new ImageView[]{
                binding.checkS1, binding.checkS2,
                binding.checkS3, binding.checkS4};

        updatePrimaryUI();
        updateSecondaryUI();
        loadGoalImages();

        for (int k = 0; k < 4; k++) {
            final int idx = k;
            primaryCards[k].setOnClickListener(v -> {
                primarySelected = idx;
                updatePrimaryUI();
            });
            secondaryCards[k].setOnClickListener(v -> {
                secondarySelected = idx;
                updateSecondaryUI();
            });
        }

        binding.btnMale.setOnClickListener(v -> {
            selectedGender = "Male";
            binding.btnMale.setBackgroundColor(Color.parseColor("#1A56A0"));
            binding.btnMale.setTextColor(Color.WHITE);
            binding.btnFemale.setBackgroundResource(R.drawable.gender_unselected);
            binding.btnFemale.setTextColor(Color.parseColor("#1A56A0"));
            binding.btnOther.setBackgroundResource(R.drawable.gender_unselected);
            binding.btnOther.setTextColor(Color.parseColor("#1A56A0"));
        });

        binding.btnFemale.setOnClickListener(v -> {
            selectedGender = "Female";
            binding.btnFemale.setBackgroundColor(Color.parseColor("#1A56A0"));
            binding.btnFemale.setTextColor(Color.WHITE);
            binding.btnMale.setBackgroundResource(R.drawable.gender_unselected);
            binding.btnMale.setTextColor(Color.parseColor("#1A56A0"));
            binding.btnOther.setBackgroundResource(R.drawable.gender_unselected);
            binding.btnOther.setTextColor(Color.parseColor("#1A56A0"));
        });

        binding.btnOther.setOnClickListener(v -> {
            selectedGender = "Other";
            binding.btnOther.setBackgroundColor(Color.parseColor("#1A56A0"));
            binding.btnOther.setTextColor(Color.WHITE);
            binding.btnMale.setBackgroundResource(R.drawable.gender_unselected);
            binding.btnMale.setTextColor(Color.parseColor("#1A56A0"));
            binding.btnFemale.setBackgroundResource(R.drawable.gender_unselected);
            binding.btnFemale.setTextColor(Color.parseColor("#1A56A0"));
        });

        binding.btnBack.setOnClickListener(v -> finish());

        binding.containerActivity.setOnClickListener(v -> 
            new AlertDialog.Builder(this)
                    .setTitle("Select Activity Level")
                    .setSingleChoiceItems(activityLevels, selectedActivityIndex, (dialog, which) -> {
                        selectedActivityIndex = which;
                        binding.tvActivity.setText(activityLevels[which]);
                        dialog.dismiss();
                    })
                    .show()
        );

        binding.btnNext.setOnClickListener(v -> {
            if (primarySelected == secondarySelected) {
                Toast.makeText(this,
                        R.string.goal_error,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse height and weight
            float weight = 75.0f;
            float height = 180.0f;
            try {
                if (!binding.etWeight.getText().toString().trim().isEmpty()) {
                    weight = Float.parseFloat(binding.etWeight.getText().toString().trim());
                }
                if (!binding.etHeight.getText().toString().trim().isEmpty()) {
                    height = Float.parseFloat(binding.etHeight.getText().toString().trim());
                }
            } catch (NumberFormatException ignored) {}

            // Save onboarding data locally
            preferenceManager.saveOnboardingData(
                selectedGender, 
                primarySelected, 
                secondarySelected, 
                activityLevels[selectedActivityIndex]
            );
            
            // Save physical attributes
            preferenceManager.setHeight(height);
            preferenceManager.saveWeight(weight, new java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(new java.util.Date()));

            // Sync with Firestore
            if (mAuth.getCurrentUser() != null) {
                Map<String, Object> onboarding = new HashMap<>();
                onboarding.put("gender", selectedGender);
                onboarding.put("primaryGoal", primarySelected);
                onboarding.put("secondaryGoal", secondarySelected);
                onboarding.put("activityLevel", activityLevels[selectedActivityIndex]);
                onboarding.put("currentWeight", weight);
                onboarding.put("userHeight", height);
                
                db.collection("users").document(mAuth.getCurrentUser().getUid())
                    .update(onboarding);
            }

            startActivity(new Intent(this, HealthConditionActivity.class));
        });
    }

    private void loadGoalImages() {
        // High-quality goal-oriented imagery
        String weightLoss = "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?w=600";
        String muscleGain = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=600";
        String stayFit = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=600";
        String improveHealth = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=600";

        // Using .centerCrop() and setting alpha for better visibility and text contrast
        loadGoalImage(weightLoss, binding.ivP1, R.drawable.ic_scale);
        loadGoalImage(muscleGain, binding.ivP2, R.drawable.ic_dumbbell);
        loadGoalImage(stayFit, binding.ivP3, R.drawable.ic_heart);
        loadGoalImage(improveHealth, binding.ivP4, R.drawable.ic_shield);

        loadGoalImage(weightLoss, binding.ivS1, R.drawable.ic_scale);
        loadGoalImage(muscleGain, binding.ivS2, R.drawable.ic_dumbbell);
        loadGoalImage(stayFit, binding.ivS3, R.drawable.ic_heart);
        loadGoalImage(improveHealth, binding.ivS4, R.drawable.ic_shield);
    }

    private void loadGoalImage(String url, ImageView imageView, int placeholder) {
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(placeholder)
                .into(imageView);
        imageView.setAlpha(0.5f);
    }

    void updatePrimaryUI() {
        for (int i = 0; i < 4; i++) {
            if (i == primarySelected) {
                primaryCards[i].setCardBackgroundColor(Color.parseColor("#EEF4FB"));
                primaryChecks[i].setVisibility(View.VISIBLE);
                if (primaryCards[i] instanceof com.google.android.material.card.MaterialCardView) {
                    ((com.google.android.material.card.MaterialCardView) primaryCards[i]).setStrokeWidth((int)(2 * getResources().getDisplayMetrics().density));
                }
            } else {
                primaryCards[i].setCardBackgroundColor(Color.WHITE);
                primaryChecks[i].setVisibility(View.GONE);
                if (primaryCards[i] instanceof com.google.android.material.card.MaterialCardView) {
                    ((com.google.android.material.card.MaterialCardView) primaryCards[i]).setStrokeWidth(0);
                }
            }
        }
    }

    void updateSecondaryUI() {
        for (int i = 0; i < 4; i++) {
            if (i == secondarySelected) {
                secondaryCards[i].setCardBackgroundColor(Color.parseColor("#EEF4FB"));
                secondaryChecks[i].setVisibility(View.VISIBLE);
                if (secondaryCards[i] instanceof com.google.android.material.card.MaterialCardView) {
                    ((com.google.android.material.card.MaterialCardView) secondaryCards[i]).setStrokeWidth((int)(2 * getResources().getDisplayMetrics().density));
                }
            } else {
                secondaryCards[i].setCardBackgroundColor(Color.WHITE);
                secondaryChecks[i].setVisibility(View.GONE);
                if (secondaryCards[i] instanceof com.google.android.material.card.MaterialCardView) {
                    ((com.google.android.material.card.MaterialCardView) secondaryCards[i]).setStrokeWidth(0);
                }
            }
        }
    }
}