package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.ActivityYogaHomeBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;

public class YogaHomeActivity extends BaseActivity {

    private ActivityYogaHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYogaHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNav(R.id.nav_workout);
        
        int stressLevel = getIntent().getIntExtra("stress_level", 5);
        String mood = getIntent().getStringExtra("mood");
        
        updateDynamicRecommendations(stressLevel, mood);
        loadImages(stressLevel);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnInfo.setOnClickListener(v -> showYogaInfoDialog());

        // Guided Step-by-Step Exercise Click Listeners
        binding.cardStressRelief.setOnClickListener(v -> showExerciseGuideDialog(
            "Stress Relief & Deep Breathing",
            "Pranayama • High Stress",
            900,
            "Calms central nervous system, lowers cortisol & anxiety, restores emotional harmony.",
            new String[]{
                "Sit comfortably in a quiet space with eyes closed and spine straight.",
                "Inhale deeply through your nose into your abdomen for 4 seconds.",
                "Hold your breath gently for 4 seconds.",
                "Exhale smoothly through your mouth for 6 seconds, releasing tension.",
                "Repeat continuously for 10-15 minutes."
            },
            "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800"
        ));

        binding.cardMorningYoga.setOnClickListener(v -> showExerciseGuideDialog(
            "Surya Namaskar (Sun Salutations)",
            "Yoga Asana • Energy Flow",
            600,
            "Improves full-body flexibility, spine health, muscle tone & blood circulation.",
            new String[]{
                "Stand erect at the front of your mat in Pranamasana with hands folded.",
                "Inhale, stretch arms up and arch slightly backward into Hastauttanasana.",
                "Exhale and bend forward from waist to touch palms to floor in Padahastasana.",
                "Step right leg back into Ashwa Sanchalanasana, then flow to Plank and Cobra pose.",
                "Complete 6 to 12 continuous rounds."
            },
            "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800"
        ));

        binding.cardAnulomVilom.setOnClickListener(v -> showExerciseGuideDialog(
            "Anulom Vilom (Alternate Nostril)",
            "Pranayama • Mind Balance",
            600,
            "Balances left and right brain hemispheres, reduces anxiety, improves lung capacity.",
            new String[]{
                "Sit erect in Padmasana or Sukhasana with eyes closed.",
                "Close right nostril with right thumb and inhale slowly through left nostril.",
                "Close left nostril with ring finger and exhale completely through right nostril.",
                "Inhale through right nostril, close right, and exhale through left nostril.",
                "Perform rhythmically for 10 minutes."
            },
            "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800"
        ));

        binding.cardKapalbhati.setOnClickListener(v -> showExerciseGuideDialog(
            "Kapalbhati (Skull-Shining Breath)",
            "Pranayama • Detox & Focus",
            900,
            "Detoxifies respiratory system, boosts metabolism, energizes brain cells.",
            new String[]{
                "Sit in a comfortable posture with palms resting on knees.",
                "Inhale deeply through both nostrils.",
                "Forcefully contract abdominal muscles to pump air out through nostrils.",
                "Allow passive inhalation between active exhalations.",
                "Perform 3 rounds of 20 pumps each."
            },
            "https://images.unsplash.com/photo-1510894347713-fc3ed6fdf539?w=800"
        ));
    }

    private void showYogaInfoDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Yoga & Pranayama Guidance")
            .setMessage("Daily Yoga and Pranayama practice reduces stress hormones, improves oxygen saturation, and enhances mental clarity. Tap any exercise card for step-by-step guidance!")
            .setPositiveButton("Got It", null)
            .show();
    }

    private void updateDynamicRecommendations(int stress, String mood) {
        if (stress >= 7) {
            binding.tvRecommendation.setText("High stress detected! Recommended: Anulom Vilom & Deep Breathing.");
        } else if (stress >= 4) {
            binding.tvRecommendation.setText("Moderate stress. Recommended: Balanced Pranayama & Gentle Stretch.");
        } else {
            binding.tvRecommendation.setText("Low stress! Recommended: Surya Namaskar & Kapalbhati.");
        }
    }

    private void showExerciseGuideDialog(String name, String category, int durationSecs, String benefits, String[] steps, String imageUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_exercise_guide, null);
        
        ImageView ivGuide = view.findViewById(R.id.iv_guide_img);
        TextView tvTitle = view.findViewById(R.id.tv_guide_title);
        TextView tvCategory = view.findViewById(R.id.tv_guide_category);
        TextView tvBenefits = view.findViewById(R.id.tv_guide_benefits);
        TextView tvSteps = view.findViewById(R.id.tv_guide_steps);
        Button btnStart = view.findViewById(R.id.btn_start_guided_session);

        tvTitle.setText(name);
        tvCategory.setText(category + " • " + (durationSecs / 60) + " Mins");
        tvBenefits.setText("💡 Benefits: " + benefits);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps.length; i++) {
            sb.append((i + 1)).append(". ").append(steps[i]).append("\n\n");
        }
        tvSteps.setText(sb.toString().trim());

        Glide.with(this).load(imageUrl).centerCrop().into(ivGuide);

        AlertDialog dialog = builder.setView(view).create();
        btnStart.setOnClickListener(v -> {
            dialog.dismiss();
            startYogaSession(name, durationSecs, 10, 3);
        });
        dialog.show();
    }

    private void startYogaSession(String name, int duration, int reps, int sets) {
        Intent intent = new Intent(this, ActiveWorkoutActivity.class);
        intent.putExtra("exercise_name", name);
        intent.putExtra("duration_seconds", duration);
        intent.putExtra("reps", reps);
        intent.putExtra("total_sets", sets);
        intent.putExtra("exercise_image", "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800");
        startActivity(intent);
    }

    private void loadImages(int stress) {
        String url = (stress >= 7) 
            ? "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800" 
            : "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800";

        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_workout)
            .centerCrop()
            .into(binding.ivYogaPromo);
    }
}