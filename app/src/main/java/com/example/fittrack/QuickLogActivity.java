package com.example.fittrack;

import android.os.Bundle;
import android.widget.Toast;
import com.example.fittrack.databinding.ActivityQuickLogBinding;
import com.google.android.material.chip.Chip;

public class QuickLogActivity extends BaseActivity {

    private ActivityQuickLogBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuickLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_diet);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSaveMeal.setOnClickListener(v -> {
            String name = binding.etMealName.getText().toString().trim();
            String kcalStr = binding.etCalories.getText().toString().trim();
            
            int checkedId = binding.chipGroupMealType.getCheckedChipId();
            if (name.isEmpty() || kcalStr.isEmpty() || checkedId == -1) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int kcal = Integer.parseInt(kcalStr);
            String type = ((Chip) findViewById(checkedId)).getText().toString();

            String defaultImg = "https://images.unsplash.com/photo-1604152135912-04a002e75696?w=400";
            if (type.equals("Lunch")) defaultImg = "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=400";
            if (type.equals("Dinner")) defaultImg = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400";
            if (type.equals("Snacks")) defaultImg = "https://images.unsplash.com/photo-1536627242493-2059aa0926dd?w=400";

            preferenceManager.saveMeal(type, name, kcal, defaultImg);
            
            Toast.makeText(this, "Meal logged successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
