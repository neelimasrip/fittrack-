package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupBottomNav(int selectedId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        if (bottomNav == null) return;

        // Ensure the correct tab is highlighted
        bottomNav.setSelectedItemId(selectedId);
        
        // itemIconTint and itemTextColor are handled in XML by nav_selector
        
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == selectedId) return true;

            Intent intent = null;
            if (itemId == R.id.nav_home) {
                intent = new Intent(this, DashboardActivity.class);
            } else if (itemId == R.id.nav_workout) {
                intent = new Intent(this, WorkoutHomeActivity.class);
            } else if (itemId == R.id.nav_diet) {
                intent = new Intent(this, DietHomeActivity.class);
            } else if (itemId == R.id.nav_progress) {
                intent = new Intent(this, ProgressActivity.class);
            } else if (itemId == R.id.nav_profile) {
                intent = new Intent(this, ProfileActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}