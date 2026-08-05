package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.databinding.ActivityActiveWorkoutBinding;
import java.util.Locale;
import java.util.Objects;

public class ActiveWorkoutActivity extends AppCompatActivity {

    private ActivityActiveWorkoutBinding binding;
    private CountDownTimer countDownTimer;
    private PreferenceManager preferenceManager;
    private boolean isPaused = false;
    private long initialTimeInMillis = 45000;
    private long timeLeftInMillis = 45000;
    private int caloriesBurned = 300;
    private int currentSet = 1;
    private int totalSets = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActiveWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        String exerciseName = getIntent().getStringExtra("exercise_name");
        if (exerciseName != null) {
            binding.tvExerciseName.setText(exerciseName);
        }

        int durationSeconds = getIntent().getIntExtra("duration_seconds", 45);
        initialTimeInMillis = durationSeconds * 1000L;
        timeLeftInMillis = initialTimeInMillis;
        
        caloriesBurned = getIntent().getIntExtra("calories_burned", 250);
        int reps = getIntent().getIntExtra("reps", 12);
        totalSets = getIntent().getIntExtra("total_sets", 3);
        
        binding.tvReps.setText(String.valueOf(reps));
        updateCountDownText();
        startTimer();
        loadImages();
        updateSetInfo();

        binding.btnPause.setOnClickListener(v -> {
            if (isPaused) resumeTimer();
            else pauseTimer();
        });

        binding.btnComplete.setOnClickListener(v -> {
            if (currentSet < totalSets) {
                currentSet++;
                updateSetInfo();
                Toast.makeText(this, "Set " + (currentSet-1) + " complete!", Toast.LENGTH_SHORT).show();
                resetTimer();
            } else {
                pauseTimer();
                showWorkoutCompleteDialog();
            }
        });

        binding.btnBack.setOnClickListener(v -> showExitDialog());
    }

    private void updateSetInfo() {
        binding.tvSetInfo.setText(String.format(Locale.getDefault(), "Set %d of %d", currentSet, totalSets));
        binding.tvSets.setText(String.valueOf(totalSets));
    }

    private void showWorkoutCompleteDialog() {
        preferenceManager.incrementWorkoutCount(caloriesBurned);
        new AlertDialog.Builder(this)
                .setTitle("Workout Complete!")
                .setMessage("Great job! You burned approximately " + caloriesBurned + " kcal.\nProgress updated in your dashboard.")
                .setPositiveButton("Finish", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void loadImages() {
        String imageUrl = getIntent().getStringExtra("exercise_image");
        Glide.with(this)
            .load(Objects.requireNonNullElse(imageUrl, "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800"))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_workout)
            .centerCrop()
            .into(binding.ivWorkoutAnimation);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                binding.tvTimer.setText("00:00");
                Toast.makeText(ActiveWorkoutActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isPaused = true;
        binding.btnPause.setText("▶ Resume");
    }

    private void resumeTimer() {
        startTimer();
        isPaused = false;
        binding.btnPause.setText("⏸ Pause");
    }

    private void resetTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        timeLeftInMillis = initialTimeInMillis;
        updateCountDownText();
        startTimer();
        isPaused = false;
        binding.btnPause.setText("⏸ Pause");
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        binding.tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("End workout?")
                .setMessage("Are you sure you want to stop this session?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}