package com.example.fittrack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkoutData {

    public static class Workout {
        public String title;
        public String subtitle;
        public String description;
        public String exercise;
        public String imageUrl;
        public int reps;
        public int sets;
        public String category;
        public String difficulty;

        public Workout(String title, String subtitle, String description, String exercise, String imageUrl, int reps, int sets, String category, String difficulty) {
            this.title = title;
            this.subtitle = subtitle;
            this.description = description;
            this.exercise = exercise;
            this.imageUrl = imageUrl;
            this.reps = reps;
            this.sets = sets;
            this.category = category;
            this.difficulty = difficulty;
        }
    }

    public static List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        workouts.add(new Workout("Morning Cardio Blitz", "20 min • Easy", "High-energy cardio session.", "Jumping Jacks", "https://images.unsplash.com/photo-1538805060514-97d9cc17730c?w=400", 20, 3, "Cardio", "Easy"));
        workouts.add(new Workout("Advanced Power Lift", "60 min • Hard", "Heavy compound movements.", "Deadlift", "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400", 8, 5, "Strength", "Hard"));
        workouts.add(new Workout("HIIT Cardio Burn", "30 min • Medium", "Interval training for fat burn.", "Burpees", "https://images.unsplash.com/photo-1517963879433-6ad2b056d712?w=400", 15, 4, "Cardio", "Medium"));
        workouts.add(new Workout("Zen Yoga Flow", "15 min • Easy", "Calming yoga sequence.", "Sun Salutation", "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400", 5, 3, "Yoga", "Easy"));
        workouts.add(new Workout("Core Crusher", "10 min • Medium", "Intense ab workout.", "Plank", "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400", 1, 4, "Strength", "Medium"));
        workouts.add(new Workout("Full Body Strength", "45 min • Medium", "Comprehensive muscle building.", "Dumbbell Press", "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=400", 12, 3, "Strength", "Medium"));
        return workouts;
    }

    public static Workout getWorkoutOfTheDay() {
        List<Workout> all = getAllWorkouts();
        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return all.get(dayOfYear % all.size());
    }
}