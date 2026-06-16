package com.example.fittrack;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.fittrack.databinding.ActivityActiveWorkoutBinding;
import java.util.Locale;

public class ActiveWorkoutActivity extends AppCompatActivity {

    private ActivityActiveWorkoutBinding binding;
    private CountDownTimer countDownTimer;
    private boolean isPaused = false;
    private long initialTimeInMillis = 45000;
    private long timeLeftInMillis = 45000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActiveWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String exerciseName = getIntent().getStringExtra("exercise_name");
        if (exerciseName != null) {
            binding.tvExerciseName.setText(exerciseName);
        }

        int durationSeconds = getIntent().getIntExtra("duration_seconds", 45);
        initialTimeInMillis = durationSeconds * 1000L;
        timeLeftInMillis = initialTimeInMillis;

        updateCountDownText();
        startTimer();
        loadImages();

        binding.btnPause.setOnClickListener(v -> {
            if (isPaused) {
                resumeTimer();
            } else {
                pauseTimer();
            }
        });

        binding.btnComplete.setOnClickListener(v -> {
            Toast.makeText(this, "Set complete!", Toast.LENGTH_SHORT).show();
            resetTimer();
        });

        binding.btnBack.setOnClickListener(v -> showExitDialog());
    }

    private void loadImages() {
        // High-quality workout demo
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800")
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
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        binding.tvTimer.setText(timeLeftFormatted);
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
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}