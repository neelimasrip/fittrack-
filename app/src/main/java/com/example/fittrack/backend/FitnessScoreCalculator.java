package com.example.fittrack.backend;

import android.content.Context;
import android.graphics.Color;

public class FitnessScoreCalculator {
    public static class ScoreResult {
        public int finalScore;
        public String status;
        public int color;

        public ScoreResult(int finalScore, String status, int color) {
            this.finalScore = finalScore;
            this.status = status;
            this.color = color;
        }
    }

    public static ScoreResult calculateScore(Context context) {
        PreferenceManager pm = new PreferenceManager(context);
        
        int water = pm.getWaterGlasses();
        int calories = 0;
        if (pm.isMealDone("Breakfast")) calories += pm.getMealKcal("Breakfast");
        if (pm.isMealDone("Lunch")) calories += pm.getMealKcal("Lunch");
        if (pm.isMealDone("Dinner")) calories += pm.getMealKcal("Dinner");
        if (pm.isMealDone("Snacks")) calories += pm.getMealKcal("Snacks");

        int workouts = pm.getTotalWorkouts();

        float waterScore = (Math.min(water, 8) / 8.0f) * 25;
        float ds = 0;
        if (calories > 0) {
            float ratio = calories / 2000.0f;
            if (ratio > 1.0f) ratio = 2.0f - ratio;
            ds = Math.max(0, ratio * 25);
        }
        float workoutScore = (Math.min(workouts, 10) / 10.0f) * 50;

        int finalScore = Math.round(waterScore + ds + workoutScore);
        if (finalScore < 10) finalScore = 10;

        String status;
        int color;

        if (finalScore >= 80) {
            status = "Excellent";
            color = Color.parseColor("#22C55E"); // Green
        } else if (finalScore >= 60) {
            status = "Good";
            color = Color.parseColor("#22C55E"); // Green
        } else if (finalScore >= 40) {
            status = "Average";
            color = Color.parseColor("#F59E0B"); // Orange
        } else {
            status = "Needs Focus";
            color = Color.RED; // Red
        }

        return new ScoreResult(finalScore, status, color);
    }
}
