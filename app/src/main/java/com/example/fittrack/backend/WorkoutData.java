package com.example.fittrack.backend;

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
        public String[] steps;

        public Workout(String title, String subtitle, String description, String exercise, String imageUrl, int reps, int sets, String category, String difficulty, String[] steps) {
            this.title = title;
            this.subtitle = subtitle;
            this.description = description;
            this.exercise = exercise;
            this.imageUrl = imageUrl;
            this.reps = reps;
            this.sets = sets;
            this.category = category;
            this.difficulty = difficulty;
            this.steps = steps;
        }
    }

    public static List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        workouts.add(new Workout(
            "Morning Cardio Blitz", "20 min • Easy", "High-energy cardio session.", "Jumping Jacks", 
            "https://images.unsplash.com/photo-1538805060514-97d9cc17730c?w=400", 20, 3, "Cardio", "Easy",
            new String[]{
                "Warm up with 2 mins jumping jacks.",
                "Stand with feet together and arms at sides.",
                "Jump while spreading legs and clapping hands overhead.",
                "Jump back to starting position.",
                "Perform high knees for 45s, rest 15s and repeat."
            }
        ));
        workouts.add(new Workout(
            "Advanced Power Lift", "60 min • Hard", "Heavy compound movements.", "Deadlift", 
            "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400", 8, 5, "Strength", "Hard",
            new String[]{
                "Stand with feet hip-width apart, barbell over mid-foot.",
                "Bend at hips and knees, grab the bar.",
                "Lift by extending hips and knees, keeping back straight.",
                "Lower the bar under control.",
                "Bench Press 4x8, Barbell Squats 4x10."
            }
        ));
        workouts.add(new Workout(
            "HIIT Cardio Burn", "30 min • Medium", "Interval training for fat burn.", "Burpees", 
            "https://images.unsplash.com/photo-1517963879433-6ad2b056d712?w=400", 15, 4, "Cardio", "Medium",
            new String[]{
                "Start in a standing position.",
                "Drop into a squat and place hands on the floor.",
                "Kick feet back into a plank, then return to squat.",
                "Jump up explosively with hands overhead.",
                "Repeat continuously."
            }
        ));
        workouts.add(new Workout(
            "Zen Yoga Flow", "15 min • Easy", "Calming yoga sequence.", "Sun Salutation", 
            "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400", 5, 3, "Yoga", "Easy",
            new String[]{
                "Stand straight in Pranamasana (Prayer Pose).",
                "Inhale, stretch arms up into Hastauttanasana.",
                "Exhale into forward bend (Padahastasana).",
                "Flow through Lunge, Plank, Cobra & Downward Dog.",
                "Return to standing position with focused breathing."
            }
        ));
        workouts.add(new Workout(
            "Core Crusher", "10 min • Medium", "Intense ab workout.", "Plank", 
            "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400", 1, 4, "Strength", "Medium",
            new String[]{
                "Place forearms on the floor, elbows under shoulders.",
                "Extend legs back, balancing on toes.",
                "Keep body in a straight line from head to heels.",
                "Hold position while engaging core for 60s.",
                "Russian twists 20 reps, Bicycle crunches 30 reps."
            }
        ));
        workouts.add(new Workout(
            "Full Body Strength", "45 min • Medium", "Comprehensive muscle building.", "Dumbbell Press", 
            "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=400", 12, 3, "Strength", "Medium",
            new String[]{
                "Sit on a bench with back support.",
                "Hold a dumbbell in each hand at shoulder level.",
                "Press the weights upward until your arms are fully extended.",
                "Slowly lower the dumbbells back to shoulder level.",
                "Keep your core engaged throughout."
            }
        ));
        workouts.add(new Workout(
            "Vinyasa Flow", "20 min • Medium", "Dynamic yoga linking breath to movement.", "Sun Salutation B", 
            "https://images.unsplash.com/photo-1599901860904-17e6ed7083a0?w=400", 5, 2, "Yoga", "Medium",
            new String[]{
                "Start in Mountain Pose at the top of your mat.",
                "Inhale, sweep arms overhead.",
                "Exhale, fold forward.",
                "Inhale, lift halfway up.",
                "Exhale, step back to plank and lower through Chaturanga.",
                "Inhale to Upward-Facing Dog, exhale to Downward-Facing Dog."
            }
        ));
        workouts.add(new Workout(
            "Morning Pranayama", "10 min • Easy", "Breathing exercises for clarity and focus.", "Anulom Vilom", 
            "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=400", 1, 10, "Yoga", "Easy",
            new String[]{
                "Sit comfortably in a cross-legged position.",
                "Close your right nostril with your right thumb.",
                "Inhale deeply through your left nostril.",
                "Close left nostril, exhale through the right.",
                "Inhale through the right, exhale through the left."
            }
        ));
        return workouts;
    }

    public static Workout getWorkoutOfTheDay() {
        List<Workout> all = getAllWorkouts();
        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return all.get(dayOfYear % all.size());
    }
}