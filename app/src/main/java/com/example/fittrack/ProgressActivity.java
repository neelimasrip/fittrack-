package com.example.fittrack;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityProgressBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressActivity extends BaseActivity {

    private ActivityProgressBinding binding;
    private List<TextView> tabViews = new ArrayList<>();
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        setupBottomNav(R.id.nav_progress);
        loadImages();

        binding.btnBack.setOnClickListener(v -> finish());

        // Initialize Tabs
        tabViews.add(binding.tabWeight);
        tabViews.add(binding.tabBmi);
        tabViews.add(binding.tabWorkouts);
        tabViews.add(binding.tabReports);

        for (TextView tab : tabViews) {
            tab.setOnClickListener(v -> selectTab(tab));
        }

        binding.btnShare.setOnClickListener(v -> exportProgressReport());
        binding.btnDownloadReport.setOnClickListener(v -> exportProgressReport());
            
        binding.btnCalendar.setOnClickListener(v -> showCalendarHistory());
            
        binding.btnAddMeasurement.setOnClickListener(v -> showAddWeightDialog());
        binding.cardGoal.setOnClickListener(v -> showEditGoalDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        String userName = preferenceManager.getUserName();
        if (userName.isEmpty()) userName = "Arjun";
        binding.tvProgressGreeting.setText(getString(R.string.progress_title, userName));

        loadWeightData();
    }

    private void exportProgressReport() {
        String history = preferenceManager.getWeightHistory();
        float current = preferenceManager.getCurrentWeight();
        float start = preferenceManager.getStartWeight();
        float goal = preferenceManager.getGoalWeight();
        float change = current - start;

        StringBuilder report = new StringBuilder();
        report.append("--- FitTrack Progress Report ---\n\n");
        report.append("Summary:\n");
        report.append("• Start Weight: ").append(start).append(" kg\n");
        report.append("• Current Weight: ").append(current).append(" kg\n");
        report.append("• Total Change: ").append(String.format(Locale.getDefault(), "%.1f", change)).append(" kg\n");
        report.append("• Goal Weight: ").append(goal).append(" kg\n\n");

        if (!history.isEmpty()) {
            report.append("Recent History:\n");
            String[] entries = history.split("\\|");
            int count = Math.min(entries.length, 5); // Share last 5 entries
            for (int i = 0; i < count; i++) {
                String[] parts = entries[i].split(":");
                if (parts.length == 2) {
                    report.append("- ").append(parts[0]).append(": ").append(parts[1]).append(" kg\n");
                }
            }
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My FitTrack Progress Report");
        shareIntent.putExtra(Intent.EXTRA_TEXT, report.toString());
        startActivity(Intent.createChooser(shareIntent, "Export Report via"));
    }

    private void showEditGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_weight, null);
        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        EditText etGoal = view.findViewById(R.id.et_weight_input);
        
        if (tvTitle != null) tvTitle.setText("Set Weight Goal (kg)");
        etGoal.setHint("e.g. 70.0");
        etGoal.setText(String.valueOf(preferenceManager.getGoalWeight()));

        builder.setView(view)
               .setTitle("Edit Goal")
               .setPositiveButton("Save", (dialog, which) -> {
                   String goalStr = etGoal.getText().toString();
                   if (!goalStr.isEmpty()) {
                       float goal = Float.parseFloat(goalStr);
                       preferenceManager.setGoalWeight(goal);
                       loadWeightData();
                       Toast.makeText(this, "Goal updated!", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void showCalendarHistory() {
        View view = getLayoutInflater().inflate(R.layout.dialog_calendar_history, null);
        android.widget.CalendarView calendarView = view.findViewById(R.id.calendar_view);
        TextView tvWeight = view.findViewById(R.id.tv_selected_date_weight);

        String history = preferenceManager.getWeightHistory();
        
        calendarView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
            // Format: MMM dd (matches what we save)
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, dayOfMonth);
            String selectedDate = sdf.format(cal.getTime());
            
            boolean found = false;
            if (!history.isEmpty()) {
                String[] entries = history.split("\\|");
                for (String entry : entries) {
                    if (entry.startsWith(selectedDate)) {
                        String weight = entry.split(":")[1];
                        tvWeight.setText("Weight on " + selectedDate + ": " + weight + " kg");
                        found = true;
                        break;
                    }
                }
            }
            
            if (!found) {
                tvWeight.setText("No entry for " + selectedDate);
            }
        });

        new AlertDialog.Builder(this)
            .setTitle("History Calendar")
            .setView(view)
            .setPositiveButton("Close", null)
            .show();
    }

    private void loadWeightData() {
        updateRecentEntries();
        updateWeightStats();
    }

    private void updateWeightStats() {
        float current = preferenceManager.getCurrentWeight();
        float start = preferenceManager.getStartWeight();
        float goal = preferenceManager.getGoalWeight();
        float change = current - start;

        binding.tvCurrentWeight.setText(String.format(Locale.getDefault(), "%.1f kg", current));
        binding.tvStartWeight.setText(String.format(Locale.getDefault(), "%.1f kg", start));
        binding.tvGoalWeight.setText(String.format(Locale.getDefault(), "%.1f kg", goal));
        
        String changeStr = String.format(Locale.getDefault(), "%.1f kg", change);
        if (change > 0) {
            changeStr = "+" + changeStr;
            binding.tvWeightChange.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        } else if (change < 0) {
            binding.tvWeightChange.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        }
        binding.tvWeightChange.setText(changeStr);
    }

    private void updateRecentEntries() {
        binding.containerRecentEntries.removeAllViews();
        String history = preferenceManager.getWeightHistory();

        if (history.isEmpty()) {
            binding.tvNoEntries.setVisibility(View.VISIBLE);
            return;
        }

        binding.tvNoEntries.setVisibility(View.GONE);
        String[] entries = history.split("\\|");
        
        for (int i = 0; i < entries.length; i++) {
            String[] parts = entries[i].split(":");
            if (parts.length == 2) {
                addEntryToView(parts[0], parts[1] + " kg", i);
            }
        }
    }

    private void addEntryToView(String date, String weight, int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_weight_entry, binding.containerRecentEntries, false);
        TextView tvDate = view.findViewById(R.id.tv_entry_date);
        TextView tvWeight = view.findViewById(R.id.tv_entry_weight);
        View btnDelete = view.findViewById(R.id.btn_delete_entry);
        
        tvDate.setText(date);
        tvWeight.setText(weight);
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this weight entry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    preferenceManager.deleteWeightEntry(index);
                    loadWeightData();
                    Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
        
        binding.containerRecentEntries.addView(view);
    }

    private void showAddWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_weight, null);
        EditText etWeight = view.findViewById(R.id.et_weight_input);
        
        builder.setView(view)
               .setTitle("Add Weight")
               .setPositiveButton("Save", (dialog, which) -> {
                   String weightStr = etWeight.getText().toString();
                   if (!weightStr.isEmpty()) {
                       float weight = Float.parseFloat(weightStr);
                       String date = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date());
                       preferenceManager.saveWeight(weight, date);
                       loadWeightData();
                       Toast.makeText(this, "Weight updated!", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void loadImages() {
        // Motivational progress header
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1476480862126-209bfaa8edc8?w=800")
            .into(binding.ivProgressHeader);
    }

    private void selectTab(TextView selectedTab) {
        for (TextView tab : tabViews) {
            if (tab == selectedTab) {
                tab.setTextColor(ContextCompat.getColor(this, R.color.primary));
                tab.setTypeface(null, Typeface.BOLD);
                tab.setBackgroundResource(R.drawable.tab_selected);
            } else {
                tab.setTextColor(ContextCompat.getColor(this, R.color.grey_text));
                tab.setTypeface(null, Typeface.NORMAL);
                tab.setBackground(null);
            }
        }

        // Toggle content visibility
        binding.layoutWeightContent.setVisibility(selectedTab == binding.tabWeight ? android.view.View.VISIBLE : android.view.View.GONE);
        binding.layoutBmiContent.setVisibility(selectedTab == binding.tabBmi ? android.view.View.VISIBLE : android.view.View.GONE);
        binding.layoutWorkoutsContent.setVisibility(selectedTab == binding.tabWorkouts ? android.view.View.VISIBLE : android.view.View.GONE);
        binding.layoutReportsContent.setVisibility(selectedTab == binding.tabReports ? android.view.View.VISIBLE : android.view.View.GONE);
    }
}