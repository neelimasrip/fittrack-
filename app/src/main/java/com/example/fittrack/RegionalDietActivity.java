package com.example.fittrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityRegionalDietBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class RegionalDietActivity extends BaseActivity {

    private ActivityRegionalDietBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegionalDietBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_diet);
        loadImages();

        binding.btnBack.setOnClickListener(v -> finish());
        
        binding.btnViewSouth.setOnClickListener(v -> showPlanDetails("South Indian"));
        binding.btnViewNorth.setOnClickListener(v -> showPlanDetails("North Indian"));
    }

    private void showPlanDetails(String region) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_regional_plan, null);
        
        TextView title = view.findViewById(R.id.tv_plan_title);
        TextView breakfast = view.findViewById(R.id.tv_breakfast_desc);
        TextView lunch = view.findViewById(R.id.tv_lunch_desc);
        TextView dinner = view.findViewById(R.id.tv_dinner_desc);
        View btnApply = view.findViewById(R.id.btn_apply_plan);

        if (region.equals("South Indian")) {
            title.setText("South Indian Diet Plan");
            breakfast.setText("Oats Idli with Sambhar and Coconut Chutney (280 kcal)");
            lunch.setText("Brown Rice with Rasam, Beans Poriyal and Curd (420 kcal)");
            dinner.setText("Ragi Dosa with Peanut Chutney (310 kcal)");
        } else {
            title.setText("North Indian Diet Plan");
            breakfast.setText("Stuffed Paratha with Low-fat Curd (350 kcal)");
            lunch.setText("Whole Wheat Roti with Paneer Tikka and Salad (450 kcal)");
            dinner.setText("Dal Tadka with Jeera Rice (380 kcal)");
        }

        btnApply.setOnClickListener(v -> {
            if (region.equals("South Indian")) {
                preferenceManager.saveMealPlan(
                    "Oats Idli with Sambhar", 280, "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=400",
                    "Brown Rice bowl", 420, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400",
                    "Ragi Dosa", 310, "https://images.unsplash.com/photo-1666001128241-7448a044c525?w=400",
                    "Fresh Fruits", 120, "https://images.unsplash.com/photo-1490818387583-1baba5e638af?w=400"
                );
            } else {
                preferenceManager.saveMealPlan(
                    "Stuffed Paratha", 350, "https://images.unsplash.com/photo-1604152135912-04a002e75696?w=400",
                    "Whole Wheat Roti", 450, "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=400",
                    "Dal Tadka with Rice", 380, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400",
                    "Mixed Dry Fruits", 150, "https://images.unsplash.com/photo-1536627242493-2059aa0926dd?w=400"
                );
            }
            dialog.dismiss();
            Toast.makeText(this, region + " plan applied to your daily log!", Toast.LENGTH_LONG).show();
            finish(); // Go back to main diet screen
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void loadImages() {
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1516714435131-44d6b64dc6a2?w=400") // South Indian thali
            .placeholder(R.drawable.ic_diet)
            .into(binding.ivRegionalSouth);

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=400") // North Indian plate
            .placeholder(R.drawable.ic_diet)
            .into(binding.ivRegionalNorth);
    }
}