package com.example.fittrack.backend;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "FitTrackPrefs";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_IS_REGISTERED = "isRegistered";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_FIRST_RUN = "isFirstRun";

    // Onboarding data
    private static final String KEY_GENDER = "userGender";
    private static final String KEY_PRIMARY_GOAL = "primaryGoal";
    private static final String KEY_SECONDARY_GOAL = "secondaryGoal";
    private static final String KEY_ACTIVITY_LEVEL = "activityLevel";
    private static final String KEY_HEALTH_CONDITIONS = "healthConditions";
    private static final String KEY_WEIGHT_HISTORY = "weightHistory";
    private static final String KEY_CURRENT_WEIGHT = "currentWeight";
    private static final String KEY_START_WEIGHT = "startWeight";
    private static final String KEY_GOAL_WEIGHT = "goalWeight";

    // User profile
    private static final String KEY_USER_PHONE = "userPhone";
    private static final String KEY_USER_HEIGHT = "userHeight";
    private static final String KEY_PROFILE_IMAGE = "profileImage";
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Workout Stats
    private static final String KEY_TOTAL_WORKOUTS = "totalWorkouts";
    private static final String KEY_TOTAL_CALORIES = "totalCalories";
    private static final String KEY_WATER_GLASSES = "waterGlasses";

    // Diet data
    private static final String KEY_MEAL_RESET_DATE = "mealResetDate";
    private static final String KEY_MEAL_BREAKFAST_NAME = "mealBreakfastName";
    private static final String KEY_MEAL_BREAKFAST_KCAL = "mealBreakfastKcal";
    private static final String KEY_MEAL_LUNCH_NAME = "mealLunchName";
    private static final String KEY_MEAL_LUNCH_KCAL = "mealLunchKcal";
    private static final String KEY_MEAL_DINNER_NAME = "mealDinnerName";
    private static final String KEY_MEAL_DINNER_KCAL = "mealDinnerKcal";
    private static final String KEY_MEAL_SNACKS_NAME = "mealSnacksName";
    private static final String KEY_MEAL_SNACKS_KCAL = "mealSnacksKcal";

    private static final String KEY_MEAL_BREAKFAST_IMG = "mealBreakfastImg";
    private static final String KEY_MEAL_LUNCH_IMG = "mealLunchImg";
    private static final String KEY_MEAL_DINNER_IMG = "mealDinnerImg";
    private static final String KEY_MEAL_SNACKS_IMG = "mealSnacksImg";

    private static final String KEY_BREAKFAST_DONE = "breakfastDone";
    private static final String KEY_LUNCH_DONE = "lunchDone";
    private static final String KEY_DINNER_DONE = "dinnerDone";
    private static final String KEY_SNACKS_DONE = "snacksDone";

    private final SharedPreferences prefs;

    public PreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveMealPlan(String bName, int bKcal, String bImg,
            String lName, int lKcal, String lImg,
            String dName, int dKcal, String dImg,
            String sName, int sKcal, String sImg) {
        prefs.edit()
                .putString(KEY_MEAL_BREAKFAST_NAME, bName)
                .putInt(KEY_MEAL_BREAKFAST_KCAL, bKcal)
                .putString(KEY_MEAL_BREAKFAST_IMG, bImg)
                .putString(KEY_MEAL_LUNCH_NAME, lName)
                .putInt(KEY_MEAL_LUNCH_KCAL, lKcal)
                .putString(KEY_MEAL_LUNCH_IMG, lImg)
                .putString(KEY_MEAL_DINNER_NAME, dName)
                .putInt(KEY_MEAL_DINNER_KCAL, dKcal)
                .putString(KEY_MEAL_DINNER_IMG, dImg)
                .putString(KEY_MEAL_SNACKS_NAME, sName)
                .putInt(KEY_MEAL_SNACKS_KCAL, sKcal)
                .putString(KEY_MEAL_SNACKS_IMG, sImg)
                .apply();
    }

    public void saveMeal(String type, String name, int kcal, String imgUrl) {
        SharedPreferences.Editor editor = prefs.edit();
        switch (type) {
            case "Breakfast":
                editor.putString(KEY_MEAL_BREAKFAST_NAME, name);
                editor.putInt(KEY_MEAL_BREAKFAST_KCAL, kcal);
                editor.putString(KEY_MEAL_BREAKFAST_IMG, imgUrl);
                break;
            case "Lunch":
                editor.putString(KEY_MEAL_LUNCH_NAME, name);
                editor.putInt(KEY_MEAL_LUNCH_KCAL, kcal);
                editor.putString(KEY_MEAL_LUNCH_IMG, imgUrl);
                break;
            case "Dinner":
                editor.putString(KEY_MEAL_DINNER_NAME, name);
                editor.putInt(KEY_MEAL_DINNER_KCAL, kcal);
                editor.putString(KEY_MEAL_DINNER_IMG, imgUrl);
                break;
            case "Snacks":
                editor.putString(KEY_MEAL_SNACKS_NAME, name);
                editor.putInt(KEY_MEAL_SNACKS_KCAL, kcal);
                editor.putString(KEY_MEAL_SNACKS_IMG, imgUrl);
                break;
        }
        editor.apply();
    }

    public String getMealImage(String type) {
        switch (type) {
            case "Breakfast":
                return prefs.getString(KEY_MEAL_BREAKFAST_IMG,
                        "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=800");
            case "Lunch":
                return prefs.getString(KEY_MEAL_LUNCH_IMG,
                        "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=400");
            case "Dinner":
                return prefs.getString(KEY_MEAL_DINNER_IMG,
                        "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800");
            default:
                return prefs.getString(KEY_MEAL_SNACKS_IMG,
                        "https://images.unsplash.com/photo-1536627242493-2059aa0926dd?w=400");
        }
    }

    public String getMealName(String type) {
        switch (type) {
            case "Breakfast":
                return prefs.getString(KEY_MEAL_BREAKFAST_NAME, "Breakfast Oats Idli");
            case "Lunch":
                return prefs.getString(KEY_MEAL_LUNCH_NAME, "Lunch Brown Rice");
            case "Dinner":
                return prefs.getString(KEY_MEAL_DINNER_NAME, "Dinner Ragi Dosa");
            default:
                return prefs.getString(KEY_MEAL_SNACKS_NAME, "Snacks Fruits");
        }
    }

    public int getMealKcal(String type) {
        switch (type) {
            case "Breakfast":
                return prefs.getInt(KEY_MEAL_BREAKFAST_KCAL, 280);
            case "Lunch":
                return prefs.getInt(KEY_MEAL_LUNCH_KCAL, 420);
            case "Dinner":
                return prefs.getInt(KEY_MEAL_DINNER_KCAL, 310);
            default:
                return prefs.getInt(KEY_MEAL_SNACKS_KCAL, 120);
        }
    }

    public void setNotificationsEnabled(boolean isEnabled) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, isEnabled).apply();
    }

    public boolean isNotificationsEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATIONS, true);
    }

    public void setDarkMode(boolean isEnabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, isEnabled).apply();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public void saveProfile(String name, String email, String phone, float weight, float height) {
        prefs.edit()
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_USER_PHONE, phone)
                .putFloat(KEY_CURRENT_WEIGHT, weight)
                .putFloat(KEY_USER_HEIGHT, height)
                .apply();
    }

    public String getUserPhone() {
        return prefs.getString(KEY_USER_PHONE, "");
    }

    public float getUserHeight() {
        return prefs.getFloat(KEY_USER_HEIGHT, 175.0f);
    }

    public void saveProfileImage(String uri) {
        prefs.edit().putString(KEY_PROFILE_IMAGE, uri).apply();
    }

    public String getProfileImage() {
        return prefs.getString(KEY_PROFILE_IMAGE, "");
    }

    public void saveWeight(float weight, String date) {
        float startWeight = prefs.getFloat(KEY_START_WEIGHT, 0.0f);
        if (startWeight == 0.0f) {
            prefs.edit().putFloat(KEY_START_WEIGHT, weight).apply();
        }

        String history = prefs.getString(KEY_WEIGHT_HISTORY, "");
        String newEntry = date + ":" + weight;
        if (history.isEmpty()) {
            history = newEntry;
        } else {
            history = newEntry + "|" + history; // Newest first
        }

        prefs.edit()
                .putString(KEY_WEIGHT_HISTORY, history)
                .putFloat(KEY_CURRENT_WEIGHT, weight)
                .apply();
    }

    public float getStartWeight() {
        return prefs.getFloat(KEY_START_WEIGHT, 0.0f);
    }

    public void setGoalWeight(float weight) {
        prefs.edit().putFloat(KEY_GOAL_WEIGHT, weight).apply();
    }

    public float getGoalWeight() {
        return prefs.getFloat(KEY_GOAL_WEIGHT, 70.0f); // Default 70
    }

    public String getWeightHistory() {
        return prefs.getString(KEY_WEIGHT_HISTORY, "");
    }

    public void deleteWeightEntry(int index) {
        String history = getWeightHistory();
        if (history.isEmpty())
            return;

        String[] entries = history.split("\\|");
        if (index < 0 || index >= entries.length)
            return;

        StringBuilder sb = new StringBuilder();
        float lastWeight = 0;
        for (int i = 0; i < entries.length; i++) {
            if (i == index)
                continue;
            if (sb.length() > 0)
                sb.append("|");
            sb.append(entries[i]);

            // Track the "newest" weight to update currentWeight
            if (lastWeight == 0) {
                String[] parts = entries[i].split(":");
                if (parts.length == 2)
                    lastWeight = Float.parseFloat(parts[1]);
            }
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_WEIGHT_HISTORY, sb.toString());
        editor.putFloat(KEY_CURRENT_WEIGHT, lastWeight);

        // If everything is deleted, reset start weight too
        if (sb.length() == 0) {
            editor.putFloat(KEY_START_WEIGHT, 0.0f);
        }

        editor.apply();
    }

    public float getCurrentWeight() {
        return prefs.getFloat(KEY_CURRENT_WEIGHT, 0.0f);
    }

    public void saveUser(String name, String email) {
        prefs.edit()
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_EMAIL, email)
                .putBoolean(KEY_IS_REGISTERED, true)
                .apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public void setLoggedIn(boolean isLoggedIn) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setFirstRun(boolean isFirstRun) {
        prefs.edit().putBoolean(KEY_IS_FIRST_RUN, isFirstRun).apply();
    }

    public boolean isFirstRun() {
        return prefs.getBoolean(KEY_IS_FIRST_RUN, true);
    }

    public void saveOnboardingData(String gender, int primaryGoal, int secondaryGoal, String activityLevel) {
        prefs.edit()
                .putString(KEY_GENDER, gender)
                .putInt(KEY_PRIMARY_GOAL, primaryGoal)
                .putInt(KEY_SECONDARY_GOAL, secondaryGoal)
                .putString(KEY_ACTIVITY_LEVEL, activityLevel)
                .apply();
    }

    public void saveHealthConditions(String conditions) {
        prefs.edit().putString(KEY_HEALTH_CONDITIONS, conditions).apply();
    }

    public void clearProfile() {
        prefs.edit().clear().apply();
    }

    public void incrementWorkoutCount(int calories) {
        int count = prefs.getInt(KEY_TOTAL_WORKOUTS, 0);
        int totalCals = prefs.getInt(KEY_TOTAL_CALORIES, 0);
        prefs.edit()
                .putInt(KEY_TOTAL_WORKOUTS, count + 1)
                .putInt(KEY_TOTAL_CALORIES, totalCals + calories)
                .apply();
    }

    public int getTotalWorkouts() {
        return prefs.getInt(KEY_TOTAL_WORKOUTS, 0);
    }

    public int getTotalCalories() {
        return prefs.getInt(KEY_TOTAL_CALORIES, 0);
    }

    public void setWaterGlasses(int count) {
        prefs.edit().putInt(KEY_WATER_GLASSES, count).apply();
    }

    public int getWaterGlasses() {
        return prefs.getInt(KEY_WATER_GLASSES, 0);
    }

    public boolean shouldResetMeals() {
        String lastDate = prefs.getString(KEY_MEAL_RESET_DATE, "");
        String currentDate = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
                .format(new java.util.Date());
        if (!lastDate.equals(currentDate)) {
            prefs.edit()
                    .putString(KEY_MEAL_RESET_DATE, currentDate)
                    .putBoolean(KEY_BREAKFAST_DONE, false)
                    .putBoolean(KEY_LUNCH_DONE, false)
                    .putBoolean(KEY_DINNER_DONE, false)
                    .putBoolean(KEY_SNACKS_DONE, false)
                    .apply();
            return true;
        }
        return false;
    }

    public void setMealDone(String type, boolean isDone) {
        String key;
        switch (type) {
            case "Breakfast":
                key = KEY_BREAKFAST_DONE;
                break;
            case "Lunch":
                key = KEY_LUNCH_DONE;
                break;
            case "Dinner":
                key = KEY_DINNER_DONE;
                break;
            default:
                key = KEY_SNACKS_DONE;
                break;
        }
        prefs.edit().putBoolean(key, isDone).apply();
    }

    public boolean isMealDone(String type) {
        switch (type) {
            case "Breakfast":
                return prefs.getBoolean(KEY_BREAKFAST_DONE, false);
            case "Lunch":
                return prefs.getBoolean(KEY_LUNCH_DONE, false);
            case "Dinner":
                return prefs.getBoolean(KEY_DINNER_DONE, false);
            default:
                return prefs.getBoolean(KEY_SNACKS_DONE, false);
        }
    }
}