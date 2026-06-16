package com.example.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityDietHomeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DietHomeActivity extends BaseActivity {

    private ActivityDietHomeBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDietHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_diet);
        
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMealData();
        loadMealImages();
    }

    private void loadMealData() {
        binding.tvMealBreakfastName.setText(preferenceManager.getMealName("Breakfast"));
        binding.tvMealBreakfastKcal.setText(preferenceManager.getMealKcal("Breakfast") + " kcal");

        binding.tvMealLunchName.setText(preferenceManager.getMealName("Lunch"));
        binding.tvMealLunchKcal.setText(preferenceManager.getMealKcal("Lunch") + " kcal");

        binding.tvMealDinnerName.setText(preferenceManager.getMealName("Dinner"));
        binding.tvMealDinnerKcal.setText(preferenceManager.getMealKcal("Dinner") + " kcal");

        binding.tvMealSnacksName.setText(preferenceManager.getMealName("Snacks"));
        binding.tvMealSnacksKcal.setText(preferenceManager.getMealKcal("Snacks") + " kcal");
    }

    private void setupClickListeners() {
        binding.btnInfo.setOnClickListener(v -> 
            Toast.makeText(this, "Daily Goal: 2000 kcal. You have 600 kcal left.", Toast.LENGTH_SHORT).show());

        binding.fabAddMeal.setOnClickListener(v -> showAddMealDialog());

        binding.ivCheckBreakfast.setOnClickListener(v -> toggleMealStatus(v, "Breakfast"));
        binding.ivCheckLunch.setOnClickListener(v -> toggleMealStatus(v, "Lunch"));

        binding.btnAddDinner.setOnClickListener(v -> {
            Toast.makeText(this, "Dinner Logged Successfully", Toast.LENGTH_SHORT).show();
            binding.btnAddDinner.setText("Logged");
            binding.btnAddDinner.setEnabled(false);
            binding.btnAddDinner.setAlpha(0.5f);
        });

        binding.btnAddSnacks.setOnClickListener(v -> {
            Toast.makeText(this, "Snack Logged Successfully", Toast.LENGTH_SHORT).show();
            binding.btnAddSnacks.setText("Logged");
            binding.btnAddSnacks.setEnabled(false);
            binding.btnAddSnacks.setAlpha(0.5f);
        });
    }

    private void showAddMealDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        
        LinearLayout optionRegional = view.findViewById(R.id.option_regional);
        LinearLayout optionQuick = view.findViewById(R.id.option_quick_add);
        LinearLayout optionPantry = view.findViewById(R.id.option_pantry);

        optionRegional.setOnClickListener(v -> {
            dialog.dismiss();
            android.content.Intent intent = new android.content.Intent(this, RegionalDietActivity.class);
            startActivity(intent);
        });

        optionQuick.setOnClickListener(v -> {
            dialog.dismiss();
            android.content.Intent intent = new android.content.Intent(this, QuickLogActivity.class);
            startActivity(intent);
        });

        optionPantry.setOnClickListener(v -> {
            dialog.dismiss();
            android.content.Intent intent = new android.content.Intent(this, PantryGeneratorActivity.class);
            startActivity(intent);
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void toggleMealStatus(View v, String mealName) {
        if (v.getAlpha() < 1.0f) {
            v.setAlpha(1.0f);
            Toast.makeText(this, mealName + " marked as completed", Toast.LENGTH_SHORT).show();
        } else {
            v.setAlpha(0.4f);
            Toast.makeText(this, mealName + " marked as pending", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMealImages() {
        Glide.with(this)
            .load(preferenceManager.getMealImage("Breakfast"))
            .placeholder(R.drawable.ic_diet)
            .into(binding.ivMealBreakfast);

        Glide.with(this)
            .load(preferenceManager.getMealImage("Lunch"))
            .placeholder(R.drawable.ic_diet)
            .into(binding.ivMealLunch);

        Glide.with(this)
            .load(preferenceManager.getMealImage("Dinner"))
            .placeholder(R.drawable.ic_diet)
            .into(binding.ivMealDinner);

        Glide.with(this)
            .load(preferenceManager.getMealImage("Snacks"))
            .placeholder(R.drawable.ic_diet)
            .into(binding.ivMealSnacks);
    }
}