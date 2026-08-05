package com.example.fittrack.backend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DietData {

    public static class Meal {
        public String name;
        public int kcal;
        public String imageUrl;

        public Meal(String name, int kcal, String imageUrl) {
            this.name = name;
            this.kcal = kcal;
            this.imageUrl = imageUrl;
        }
    }

    public static class DayPlan {
        public Meal breakfast;
        public Meal lunch;
        public Meal dinner;
        public Meal snacks;

        public DayPlan(Meal b, Meal l, Meal d, Meal s) {
            this.breakfast = b;
            this.lunch = l;
            this.dinner = d;
            this.snacks = s;
        }
    }

    public static List<DayPlan> getRotationalPlans() {
        List<DayPlan> plans = new ArrayList<>();
        
        // Day 1: High Protein
        plans.add(new DayPlan(
            new Meal("Oats Idli with Sambhar", 280, "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=400"),
            new Meal("Grilled Chicken Salad", 420, "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400"),
            new Meal("Ragi Dosa with Chutney", 310, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800"),
            new Meal("Mixed Nuts & Seeds", 150, "https://images.unsplash.com/photo-1511067007398-7e4b90cfa4bc?w=400")
        ));

        // Day 2: Low Carb
        plans.add(new DayPlan(
            new Meal("Stuffed Paratha (Small)", 350, "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=800"),
            new Meal("Paneer Tikka Bowl", 450, "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=400"),
            new Meal("Lentil Soup with Veggies", 320, "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400"),
            new Meal("Fresh Apple Slices", 120, "https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=400")
        ));

        // Day 3: Balanced Mix
        plans.add(new DayPlan(
            new Meal("Peanut Butter Toast", 310, "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=400"),
            new Meal("Brown Rice & Dal Tadka", 380, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400"),
            new Meal("Steamed Fish/Tofu", 340, "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=400"),
            new Meal("Greek Yogurt Bowl", 180, "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400")
        ));

        return plans;
    }

    public static DayPlan getPlanOfTheDay() {
        List<DayPlan> plans = getRotationalPlans();
        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return plans.get(dayOfYear % plans.size());
    }
}