package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
        
        checkDailyReset();
        setupClickListeners();
    }

    private void checkDailyReset() {
        if (preferenceManager.shouldResetMeals() || preferenceManager.getMealName("Breakfast").isEmpty()) {
            DietData.DayPlan today = DietData.getPlanOfTheDay();
            preferenceManager.saveMealPlan(
                today.breakfast.name, today.breakfast.kcal, today.breakfast.imageUrl,
                today.lunch.name, today.lunch.kcal, today.lunch.imageUrl,
                today.dinner.name, today.dinner.kcal, today.dinner.imageUrl,
                today.snacks.name, today.snacks.kcal, today.snacks.imageUrl
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDailyReset();
        loadMealData();
        loadMealImages();
        updateCalorieProgress();
    }

    private void loadMealData() {
        binding.tvMealBreakfastName.setText(preferenceManager.getMealName("Breakfast"));
        binding.tvMealBreakfastKcal.setText(String.format(java.util.Locale.getDefault(), "%d kcal", preferenceManager.getMealKcal("Breakfast")));
        binding.ivCheckBreakfast.setAlpha(preferenceManager.isMealDone("Breakfast") ? 1.0f : 0.4f);

        binding.tvMealLunchName.setText(preferenceManager.getMealName("Lunch"));
        binding.tvMealLunchKcal.setText(String.format(java.util.Locale.getDefault(), "%d kcal", preferenceManager.getMealKcal("Lunch")));
        binding.ivCheckLunch.setAlpha(preferenceManager.isMealDone("Lunch") ? 1.0f : 0.4f);

        binding.tvMealDinnerName.setText(preferenceManager.getMealName("Dinner"));
        binding.tvMealDinnerKcal.setText(String.format(java.util.Locale.getDefault(), "%d kcal", preferenceManager.getMealKcal("Dinner")));
        updateAddButtonStatus(binding.btnAddDinner, "Dinner");

        binding.tvMealSnacksName.setText(preferenceManager.getMealName("Snacks"));
        binding.tvMealSnacksKcal.setText(String.format(java.util.Locale.getDefault(), "%d kcal", preferenceManager.getMealKcal("Snacks")));
        updateAddButtonStatus(binding.btnAddSnacks, "Snacks");
    }

    private void updateAddButtonStatus(com.google.android.material.button.MaterialButton btn, String type) {
        if (preferenceManager.isMealDone(type)) {
            btn.setText("Logged");
            btn.setEnabled(false);
            btn.setAlpha(0.5f);
        } else {
            btn.setText("Add");
            btn.setEnabled(true);
            btn.setAlpha(1.0f);
        }
    }

    private void updateCalorieProgress() {
        int consumed = 0;
        if (preferenceManager.isMealDone("Breakfast")) consumed += preferenceManager.getMealKcal("Breakfast");
        if (preferenceManager.isMealDone("Lunch")) consumed += preferenceManager.getMealKcal("Lunch");
        if (preferenceManager.isMealDone("Dinner")) consumed += preferenceManager.getMealKcal("Dinner");
        if (preferenceManager.isMealDone("Snacks")) consumed += preferenceManager.getMealKcal("Snacks");
                       
        binding.dietProgress.setMax(2000);
        binding.dietProgress.setProgress(consumed);
        binding.tvCalories.setText(String.valueOf(consumed));
        binding.tvTargetLabel.setText("OF 2000");
        int rem = 2000 - consumed;
        binding.tvRemainingCalories.setText(String.format(java.util.Locale.getDefault(), "Remaining %d kcal", Math.max(0, rem)));
    }

    private void setupClickListeners() {
        binding.btnInfo.setOnClickListener(v -> Toast.makeText(this, "Mark meals as completed to track your calories!", Toast.LENGTH_SHORT).show());
        binding.fabAddMeal.setOnClickListener(v -> showAddMealDialog());
        binding.ivCheckBreakfast.setOnClickListener(v -> toggleMealStatus(v, "Breakfast"));
        binding.ivCheckLunch.setOnClickListener(v -> toggleMealStatus(v, "Lunch"));

        binding.btnAddDinner.setOnClickListener(v -> {
            preferenceManager.setMealDone("Dinner", true);
            Toast.makeText(this, "Dinner Logged Successfully", Toast.LENGTH_SHORT).show();
            updateAddButtonStatus(binding.btnAddDinner, "Dinner");
            updateCalorieProgress();
        });

        binding.btnAddSnacks.setOnClickListener(v -> {
            preferenceManager.setMealDone("Snacks", true);
            Toast.makeText(this, "Snack Logged Successfully", Toast.LENGTH_SHORT).show();
            updateAddButtonStatus(binding.btnAddSnacks, "Snacks");
            updateCalorieProgress();
        });
    }

    private void toggleMealStatus(View v, String mealType) {
        boolean newState = !preferenceManager.isMealDone(mealType);
        preferenceManager.setMealDone(mealType, newState);
        v.setAlpha(newState ? 1.0f : 0.4f);
        Toast.makeText(this, mealType + (newState ? " completed" : " pending"), Toast.LENGTH_SHORT).show();
        updateCalorieProgress();
    }

    private void showAddMealDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        view.findViewById(R.id.option_regional).setOnClickListener(v -> { dialog.dismiss(); startActivity(new Intent(this, RegionalDietActivity.class)); });
        view.findViewById(R.id.option_quick_add).setOnClickListener(v -> { dialog.dismiss(); startActivity(new Intent(this, QuickLogActivity.class)); });
        view.findViewById(R.id.option_pantry).setOnClickListener(v -> { dialog.dismiss(); startActivity(new Intent(this, PantryGeneratorActivity.class)); });
        dialog.setContentView(view);
        dialog.show();
    }

    private void loadMealImages() {
        Glide.with(this).load(preferenceManager.getMealImage("Breakfast")).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_diet).centerCrop().into(binding.ivMealBreakfast);
        Glide.with(this).load(preferenceManager.getMealImage("Lunch")).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_diet).centerCrop().into(binding.ivMealLunch);
        Glide.with(this).load(preferenceManager.getMealImage("Dinner")).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_diet).centerCrop().into(binding.ivMealDinner);
        Glide.with(this).load(preferenceManager.getMealImage("Snacks")).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_diet).centerCrop().into(binding.ivMealSnacks);
    }
}