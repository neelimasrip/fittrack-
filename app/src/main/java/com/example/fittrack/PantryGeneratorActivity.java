package com.example.fittrack;

import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;
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
                android.widget.Toast.makeText(this, "Please add some ingredients first", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            generateMeal();
        });

        binding.btnApplyPantryMeal.setOnClickListener(v -> {
            if (!currentRecipeName.isEmpty()) {
                // Show a simple choice for which meal time to apply to
                String[] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snacks"};
                new android.app.AlertDialog.Builder(this)
                    .setTitle("Add to which meal?")
                    .setItems(mealTypes, (dialog, which) -> {
                        String selectedType = mealTypes[which];
                        preferenceManager.saveMeal(selectedType, currentRecipeName, currentRecipeKcal, currentRecipeImg);
                        android.widget.Toast.makeText(this, currentRecipeName + " added to " + selectedType, android.widget.Toast.LENGTH_SHORT).show();
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

        // Simulate AI processing delay
        new android.os.Handler().postDelayed(() -> {
            binding.pantryLoading.setVisibility(View.GONE);
            binding.btnGenerateMeal.setEnabled(true);
            
            // Simple mock AI logic
            StringBuilder allIngredients = new StringBuilder();
            for (int i = 0; i < binding.pantryChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) binding.pantryChipGroup.getChildAt(i);
                allIngredients.append(chip.getText().toString().toLowerCase()).append(" ");
            }
            String ingredients = allIngredients.toString();

            if (ingredients.contains("dal") && ingredients.contains("tomato")) {
                currentRecipeName = "Tomato Dal Tadka";
                currentRecipeKcal = 180;
                currentRecipeImg = "https://images.unsplash.com/photo-1601303584126-269c824d4e51?w=400";
                binding.tvRecipeTitle.setText(currentRecipeName);
                binding.tvRecipeDesc.setText("Protein-rich dal cooked with tangy tomatoes and aromatic spices.");
            } else if (ingredients.contains("egg") && (ingredients.contains("tomato") || ingredients.contains("onion") || ingredients.contains("bread"))) {
                currentRecipeName = "Egg Masala / Bhurji";
                currentRecipeKcal = 240;
                currentRecipeImg = "https://images.unsplash.com/photo-1551326844-4df70f78d0e9?w=400"; // Real egg bhurji/masala
                binding.tvRecipeTitle.setText(currentRecipeName);
                binding.tvRecipeDesc.setText("Spicy scrambled or curried eggs with onions and tomatoes.");
            } else if (ingredients.contains("oat") && (ingredients.contains("milk") || ingredients.contains("fruit") || ingredients.contains("egg"))) {
                currentRecipeName = "Oatmeal Protein Bowl";
                currentRecipeKcal = 320;
                currentRecipeImg = "https://images.unsplash.com/photo-1517673132405-a56a62b18caf?w=400";
                binding.tvRecipeTitle.setText(currentRecipeName);
                binding.tvRecipeDesc.setText("Combine oats with milk or eggs for a fiber-rich, high-protein start.");
            } else if (ingredients.contains("paneer")) {
                currentRecipeName = "Paneer Tikka Salad";
                currentRecipeKcal = 280;
                currentRecipeImg = "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=400";
                binding.tvRecipeTitle.setText(currentRecipeName);
                binding.tvRecipeDesc.setText("Grilled paneer cubes served over a bed of fresh garden greens.");
            } else if (ingredients.contains("dal") || ingredients.contains("lentil")) {
                currentRecipeName = "Classic Yellow Dal";
                currentRecipeKcal = 150;
                currentRecipeImg = "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?w=400"; 
                binding.tvRecipeTitle.setText(currentRecipeName);
                binding.tvRecipeDesc.setText("Simple yellow lentils tempered with cumin, garlic, and ghee.");
                binding.tvRecipeTitle.setText(currentRecipeName);
                binding.tvRecipeDesc.setText("Simple yellow lentils tempered with cumin, garlic, and ghee.");
            } else if (ingredients.contains("egg")) {
                currentRecipeName = "Boiled Eggs & Toast";
                currentRecipeKcal = 210;
                currentRecipeImg = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=400";
                binding.tvRecipeTitle.setText(currentRecipeName);
                binding.tvRecipeDesc.setText("Perfectly boiled eggs with a side of whole wheat toast.");
            } else {
                currentRecipeName = "Healthy Veggie Bowl";
                currentRecipeKcal = 250;
                currentRecipeImg = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400";
                binding.tvRecipeTitle.setText(currentRecipeName);
                binding.tvRecipeDesc.setText("A balanced mix of your available vegetables with a dash of olive oil.");
            }

            binding.resultCard.setVisibility(View.VISIBLE);
            Glide.with(this).load(currentRecipeImg).into(binding.ivPantryResult);
        }, 1500);
    }
}