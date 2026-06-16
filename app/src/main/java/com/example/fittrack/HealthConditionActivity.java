package com.example.fittrack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fittrack.databinding.ActivityHealthConditionBinding;
import java.util.ArrayList;
import java.util.List;

public class HealthConditionActivity extends AppCompatActivity {
    ActivityHealthConditionBinding binding;

    List<TextView> allChips = new ArrayList<>();
    List<TextView> selectedChips = new ArrayList<>();
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthConditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        allChips.add(binding.chipKnee);
        allChips.add(binding.chipDiabetes);
        allChips.add(binding.chipHypertension);
        allChips.add(binding.chipPcos);
        allChips.add(binding.chipLowerBack);
        allChips.add(binding.chipHeart);
        allChips.add(binding.chipAsthma);
        allChips.add(binding.chipShoulder);

        for (TextView chip : allChips) {
            chip.setOnClickListener(v -> toggleChip(chip));
        }

        binding.chipNone.setOnClickListener(v -> {
            for (TextView c : allChips) {
                c.setBackgroundResource(R.drawable.chip_unselected);
                c.setTextColor(Color.parseColor("#1A56A0"));
            }
            selectedChips.clear();
            
            binding.chipNone.setBackgroundResource(R.drawable.chip_selected);
            binding.chipNone.setTextColor(Color.WHITE);
            selectedChips.add(binding.chipNone);
        });

        binding.btnBack.setOnClickListener(v -> finish());

        binding.tvSkip.setOnClickListener(v -> finishOnboarding());

        binding.btnContinue.setOnClickListener(v -> finishOnboarding());
    }

    private void finishOnboarding() {
        // Save selected health conditions
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedChips.size(); i++) {
            sb.append(selectedChips.get(i).getText().toString());
            if (i < selectedChips.size() - 1) sb.append(", ");
        }
        preferenceManager.saveHealthConditions(sb.toString());

        // Mark onboarding as complete
        preferenceManager.setFirstRun(false);

        // Navigate to Dashboard
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    void toggleChip(TextView chip) {
        if (selectedChips.contains(chip)) {
            chip.setBackgroundResource(R.drawable.chip_unselected);
            chip.setTextColor(Color.parseColor("#1A56A0"));
            selectedChips.remove(chip);
        } else {
            chip.setBackgroundResource(R.drawable.chip_selected);
            chip.setTextColor(Color.WHITE);
            selectedChips.add(chip);
            
            // Deselect 'None' if a specific condition is selected
            binding.chipNone.setBackgroundResource(R.drawable.chip_unselected);
            binding.chipNone.setTextColor(Color.parseColor("#1A56A0"));
            selectedChips.remove(binding.chipNone);
        }
    }
}