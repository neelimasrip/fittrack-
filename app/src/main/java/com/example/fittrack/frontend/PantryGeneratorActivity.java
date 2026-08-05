package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.databinding.ActivityPantryGeneratorBinding;
import com.google.android.material.chip.Chip;

public class PantryGeneratorActivity extends BaseActivity {

    private ActivityPantryGeneratorBinding binding;
    private PreferenceManager preferenceManager;
    private String currentRecipeName = "";
    private int currentRecipeKcal = 0;
    private String currentRecipeImg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPantryGeneratorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_diet);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnAddChip.setOnClickListener(v -> {
            String input = binding.etIngredient.getText().toString().trim();
            if (!input.isEmpty()) {
                String[] parts = input.split("[,\\s]+");
                for (String part : parts) {
                    if (!part.trim().isEmpty()) {
                        addChip(part.trim());
                    }
                }
                binding.etIngredient.setText("");
            }
        });

        binding.btnGenerateMeal.setOnClickListener(v -> {
            if (binding.pantryChipGroup.getChildCount() == 0) {
                Toast.makeText(this, "Please add some ingredients first", Toast.LENGTH_SHORT).show();
                return;
            }
            generateMeal();
        });

        binding.btnApplyPantryMeal.setOnClickListener(v -> {
            if (!currentRecipeName.isEmpty()) {
                String[] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snacks"};
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Add to which meal?")
                    .setItems(mealTypes, (dialog, which) -> {
                        String selectedType = mealTypes[which];
                        preferenceManager.saveMeal(selectedType, currentRecipeName, currentRecipeKcal, currentRecipeImg);
                        Toast.makeText(this, currentRecipeName + " added to " + selectedType, Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .show();
            }
        });
    }

    private void addChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> binding.pantryChipGroup.removeView(chip));
        binding.pantryChipGroup.addView(chip);
    }

    private void generateMeal() {
        binding.btnGenerateMeal.setEnabled(false);
        binding.resultCard.setVisibility(View.GONE);
        binding.pantryLoading.setVisibility(View.VISIBLE);

        new android.os.Handler().postDelayed(() -> {
            if (isFinishing()) return;
            
            binding.pantryLoading.setVisibility(View.GONE);
            binding.btnGenerateMeal.setEnabled(true);
            
            StringBuilder allIngredients = new StringBuilder();
            for (int i = 0; i < binding.pantryChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) binding.pantryChipGroup.getChildAt(i);
                allIngredients.append(chip.getText().toString().toLowerCase()).append(" ");
            }
            String ingredients = allIngredients.toString();

            if (ingredients.contains("chicken") || ingredients.contains("meat")) {
                currentRecipeName = "Grilled Chicken Salad";
                currentRecipeKcal = 380;
                currentRecipeImg = "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=800";
            } else if (ingredients.contains("egg")) {
                currentRecipeName = "Spicy Egg Scramble";
                currentRecipeKcal = 240;
                currentRecipeImg = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=800";
            } else if (ingredients.contains("oat") || ingredients.contains("milk") || ingredients.contains("banana")) {
                currentRecipeName = "Creamy Oatmeal Bowl";
                currentRecipeKcal = 320;
                currentRecipeImg = "https://images.unsplash.com/photo-1517673132405-a56a62b18caf?w=800";
            } else if (ingredients.contains("paneer") || ingredients.contains("cheese")) {
                currentRecipeName = "Paneer Stir-fry";
                currentRecipeKcal = 410;
                currentRecipeImg = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800";
            } else if (ingredients.contains("rice") || ingredients.contains("dal")) {
                currentRecipeName = "Classic Dal & Rice";
                currentRecipeKcal = 350;
                currentRecipeImg = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800";
            } else if (ingredients.contains("potato") || ingredients.contains("bread")) {
                currentRecipeName = "Hearty Veggie Sandwich";
                currentRecipeKcal = 290;
                currentRecipeImg = "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=800";
            } else {
                currentRecipeName = "Nutritious Veggie Bowl";
                currentRecipeKcal = 220;
                currentRecipeImg = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800";
            }

            binding.tvRecipeTitle.setText(currentRecipeName);
            binding.resultCard.setVisibility(View.VISIBLE);
            
            Glide.with(PantryGeneratorActivity.this)
                .load(currentRecipeImg)
                .placeholder(R.drawable.ic_diet)
                .centerCrop()
                .into(binding.ivPantryResult);

        }, 1500);
    }
}