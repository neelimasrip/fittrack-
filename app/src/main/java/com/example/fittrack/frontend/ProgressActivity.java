package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.databinding.ActivityProgressBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressActivity extends BaseActivity {

    private ActivityProgressBinding binding;
    private final List<TextView> tabViews = new ArrayList<>();
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
        binding.cardWorkoutsCount.setOnClickListener(v -> startActivity(new Intent(this, WorkoutHomeActivity.class)));
        binding.cardCaloriesBurned.setOnClickListener(v -> startActivity(new Intent(this, DietHomeActivity.class)));
        binding.cardWeeklyReport.setOnClickListener(v -> exportProgressReport());

        binding.rowGoalPredictor.setOnClickListener(v -> startActivity(new Intent(this, GoalPredictorActivity.class)));
        binding.rowHabitPredictor.setOnClickListener(v -> startActivity(new Intent(this, HabitPredictorActivity.class)));
        binding.rowHabitCoach.setOnClickListener(v -> startActivity(new Intent(this, HabitCoachActivity.class)));
        binding.rowHabitCorrelation.setOnClickListener(v -> startActivity(new Intent(this, HabitCorrelationActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        String name = preferenceManager.getUserName();
        String display = name.isEmpty() ? "User" : name.split(" ")[0];
        binding.tvProgressGreeting.setText(String.format(Locale.getDefault(), "Keep Going, %s!", display));

        loadWeightData();
        loadWorkoutStats();
    }

    private void exportProgressReport() {
        float current = preferenceManager.getCurrentWeight();
        float start = preferenceManager.getStartWeight();
        float goal = preferenceManager.getGoalWeight();
        
        StringBuilder report = new StringBuilder();
        report.append("--- FitTrack Progress Report ---\n\n");
        report.append("Summary:\n");
        report.append("• Start: ").append(start).append(" kg\n");
        report.append("• Current: ").append(current).append(" kg\n");
        report.append("• Goal: ").append(goal).append(" kg\n");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, report.toString());
        startActivity(Intent.createChooser(shareIntent, "Export Report"));
    }

    private void showEditGoalDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_weight, null);
        EditText etGoal = view.findViewById(R.id.et_weight_input);
        etGoal.setText(String.valueOf(preferenceManager.getGoalWeight()));

        new AlertDialog.Builder(this)
               .setTitle("Edit Goal (kg)")
               .setView(view)
               .setPositiveButton("Save", (dialog, which) -> {
                   String goalStr = etGoal.getText().toString();
                   if (!goalStr.isEmpty()) {
                       float newGoal = Float.parseFloat(goalStr);
                       preferenceManager.setGoalWeight(newGoal);
                       
                       // Sync with Firestore
                       com.google.firebase.auth.FirebaseAuth mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
                       if (mAuth.getCurrentUser() != null) {
                           java.util.Map<String, Object> updates = new java.util.HashMap<>();
                           updates.put("goalWeight", newGoal);
                           com.google.firebase.firestore.FirebaseFirestore.getInstance()
                               .collection("users").document(mAuth.getCurrentUser().getUid())
                               .update(updates);
                       }
                       
                       loadWeightData();
                   }
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void showCalendarHistory() {
        View view = getLayoutInflater().inflate(R.layout.dialog_calendar, null);
        android.widget.CalendarView calendarView = view.findViewById(R.id.calendar_view);
        
        AlertDialog dialog = new AlertDialog.Builder(this)
               .setTitle("Progress Calendar")
               .setView(view)
               .setPositiveButton("Close", null)
               .create();

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, dayOfMonth);
            String selectedDate = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(cal.getTime());
            dialog.dismiss();
            showAddWeightDialogForDate(selectedDate);
        });
        
        dialog.show();
    }

    private void showAddWeightDialogForDate(String dateStr) {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_weight, null);
        EditText etWeight = view.findViewById(R.id.et_weight_input);
        new AlertDialog.Builder(this)
               .setTitle("Add Weight (" + dateStr + ")")
               .setView(view)
               .setPositiveButton("Save", (dialog, which) -> {
                   String weightStr = etWeight.getText().toString();
                   if (!weightStr.isEmpty()) {
                       preferenceManager.saveWeight(Float.parseFloat(weightStr), dateStr);
                       loadWeightData();
                   }
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void loadWeightData() {
        float current = preferenceManager.getCurrentWeight();
        float start = preferenceManager.getStartWeight();
        float goal = preferenceManager.getGoalWeight();

        binding.tvCurrentWeight.setText(String.format(Locale.getDefault(), "%.1f kg", current));
        binding.tvStartWeight.setText(String.format(Locale.getDefault(), "%.1f kg", start));
        binding.tvGoalWeight.setText(String.format(Locale.getDefault(), "%.1f kg", goal));
        
        float diff = start - current;
        binding.tvWeightChange.setText(String.format(Locale.getDefault(), "%.1f kg", Math.abs(diff)));
        
        if (diff > 0) {
            binding.tvProgressSummary.setText(String.format(Locale.getDefault(), "You've lost %.1f kg recently.", diff));
        } else if (diff < 0) {
            binding.tvProgressSummary.setText(String.format(Locale.getDefault(), "You've gained %.1f kg recently.", Math.abs(diff)));
        } else {
            binding.tvProgressSummary.setText("You are maintaining your weight.");
        }
        
        updateRecentEntries();
    }

    private void loadWorkoutStats() {
        int count = preferenceManager.getTotalWorkouts();
        int calories = preferenceManager.getTotalCalories();
        binding.tvTotalWorkoutsCount.setText(count + " Workouts");
        binding.tvTotalCaloriesBurned.setText(String.format(Locale.getDefault(), "%,d kcal", calories));
    }

    private void updateRecentEntries() {
        binding.containerRecentEntries.removeAllViews();
        String history = preferenceManager.getWeightHistory();
        
        TextView[] dateLabels = {
            binding.tvChartDate0, binding.tvChartDate1,
            binding.tvChartDate2, binding.tvChartDate3,
            binding.tvChartDate4
        };
        for (TextView tv : dateLabels) {
            tv.setText("");
        }

        if (history.isEmpty()) {
            binding.tvNoEntries.setVisibility(View.VISIBLE);
            binding.chartView.setDataPoints(new ArrayList<>());
            return;
        }
        binding.tvNoEntries.setVisibility(View.GONE);
        String[] entries = history.split("\\|");
        
        List<Float> chartPoints = new ArrayList<>();
        List<String> chartDates = new ArrayList<>();
        
        int count = Math.min(entries.length, 5);
        for (int i = 0; i < entries.length; i++) {
            String[] parts = entries[i].split(":");
            if (parts.length == 2) {
                if (i < 5) { // Add to recent entries list view
                    addEntryToView(parts[0], parts[1] + " kg", i);
                }
                
                if (chartPoints.size() < 5) {
                    try {
                        chartPoints.add(Float.parseFloat(parts[1]));
                        chartDates.add(parts[0]);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        
        // History is newest first, we want oldest first for the chart
        Collections.reverse(chartPoints);
        Collections.reverse(chartDates);
        
        binding.chartView.setDataPoints(chartPoints);
        
        // Update date labels
        for (int i = 0; i < chartDates.size(); i++) {
            if (i < dateLabels.length) {
                if (i == chartDates.size() - 1) {
                    dateLabels[i].setText("Today");
                } else {
                    dateLabels[i].setText(chartDates.get(i));
                }
            }
        }
    }

    private void addEntryToView(String date, String weight, int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_weight_entry, binding.containerRecentEntries, false);
        ((TextView)view.findViewById(R.id.tv_entry_date)).setText(date);
        ((TextView)view.findViewById(R.id.tv_entry_weight)).setText(weight);
        view.findViewById(R.id.btn_delete_entry).setOnClickListener(v -> {
            preferenceManager.deleteWeightEntry(index);
            loadWeightData();
        });
        binding.containerRecentEntries.addView(view);
    }

    private void showAddWeightDialog() {
        String date = new SimpleDateFormat("MMM dd", Locale.getDefault()).format(new Date());
        showAddWeightDialogForDate(date);
    }

    private void loadImages() {
        Glide.with(this).load("https://images.unsplash.com/photo-1476480862126-209bfaa8edc8?w=800").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_progress_white).centerCrop().into(binding.ivProgressHeader);
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
        binding.layoutWeightContent.setVisibility(selectedTab == binding.tabWeight ? View.VISIBLE : View.GONE);
        binding.layoutBmiContent.setVisibility(selectedTab == binding.tabBmi ? View.VISIBLE : View.GONE);
        binding.layoutWorkoutsContent.setVisibility(selectedTab == binding.tabWorkouts ? View.VISIBLE : View.GONE);
        binding.layoutReportsContent.setVisibility(selectedTab == binding.tabReports ? View.VISIBLE : View.GONE);
    }
}