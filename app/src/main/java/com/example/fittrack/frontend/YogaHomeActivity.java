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
        
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnInfo.setOnClickListener(v -> showYogaInfoDialog());

        // Update cards dynamically based on Stress Level
        setupStressBasedExercises(stressLevel, mood);
    }

    private void showYogaInfoDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Yoga & Pranayama Guidance")
            .setMessage("Daily Yoga and Pranayama practice reduces stress hormones, improves oxygen saturation, and enhances mental clarity. Tap any exercise card for step-by-step guidance!")
            .setPositiveButton("Got It", null)
            .show();
    }

    private void setupStressBasedExercises(int stress, String mood) {
        if (stress >= 7) {
            // HIGH STRESS (7 - 10): Deep Calming & Anxiety Reduction
            binding.tvRecommendation.setText("🚨 High Stress Detected (" + stress + "/10). Focus on Deep Calming Pranayamas & Anxiety Relief.");

            setupCard(
                binding.cardStressRelief,
                binding.tvPranayama1Title,
                binding.tvPranayama1Duration,
                "Bhramari (Humming Bee Breath)",
                "15 min • Calming",
                900,
                "Instantly calms agitated mind, alleviates tension headaches & anxiety.",
                new String[]{
                    "Sit comfortably with eyes closed and spine straight.",
                    "Place index fingers gently on ear cartilage (tragus).",
                    "Take a deep breath in through your nose.",
                    "Exhale making a smooth, deep humming bee sound ('Mmmmm') feeling vibration in skull.",
                    "Repeat 7 to 10 rounds."
                },
                "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800"
            );

            setupCard(
                binding.cardMorningYoga,
                null,
                null,
                "Sheetali (Cooling Breath)",
                "10 min • Heat & Stress Release",
                600,
                "Cools body temperature, lowers heart rate and calms autonomic nervous system.",
                new String[]{
                    "Sit in Sukhasana with hands resting on knees.",
                    "Roll your tongue into a 'tube' shape extending slightly past lips.",
                    "Inhale deeply through the rolled tongue feeling cool air flow.",
                    "Draw tongue in, close mouth, and exhale smoothly through nose.",
                    "Repeat 10 to 15 rounds."
                },
                "https://images.unsplash.com/photo-1552196563-55cd4e45efb3?w=800"
            );

            setupCard(
                binding.cardAnulomVilom,
                null,
                null,
                "Anulom Vilom (Alternate Nostril)",
                "20 min • Mind Balance",
                1200,
                "Balances left & right brain, reduces cortisol & restores emotional peace.",
                new String[]{
                    "Sit erect in Padmasana with eyes closed.",
                    "Close right nostril with thumb, inhale deeply through left nostril for 4s.",
                    "Close left nostril, open right and exhale completely for 6s.",
                    "Inhale through right, close right, and exhale through left nostril.",
                    "Continue rhythmically for 15-20 minutes."
                },
                "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800"
            );

            setupCard(
                binding.cardKapalbhati,
                binding.tvPranayama2Title,
                binding.tvPranayama2Duration,
                "Shavasana & Deep Body Scan",
                "15 min • Total Relaxation",
                900,
                "Completely relaxes nervous system, releases deep physical and mental tension.",
                new String[]{
                    "Lie flat on your back with legs comfortably apart and arms by sides.",
                    "Close eyes and bring full attention to your natural breath.",
                    "Mentally scan your body from toes to head, consciously releasing muscle tension.",
                    "Remain completely still for 15 minutes."
                },
                "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800"
            );

            Glide.with(this).load("https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800").centerCrop().into(binding.ivYogaPromo);

        } else if (stress >= 4) {
            // MODERATE STRESS (4 - 6): Balance & Gentle Restoration
            binding.tvRecommendation.setText("⚖️ Moderate Stress (" + stress + "/10). Balanced Pranayama & Gentle Stretch Flow Recommended.");

            setupCard(
                binding.cardStressRelief,
                binding.tvPranayama1Title,
                binding.tvPranayama1Duration,
                "Nadi Shodhana Pranayama",
                "12 min • Harmony",
                720,
                "Clears energy channels, balances nervous system, improves mental focus.",
                new String[]{
                    "Sit in a comfortable upright posture.",
                    "Place Vishnu Mudra on right hand.",
                    "Alternate breathing between left and right nostrils with gentle pauses.",
                    "Perform 10 to 12 cycles."
                },
                "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800"
            );

            setupCard(
                binding.cardMorningYoga,
                null,
                null,
                "Balasana (Child's Pose Flow)",
                "10 min • Restorative Stretch",
                600,
                "Gently stretches hips, lower back, and calms central nervous system.",
                new String[]{
                    "Kneel on floor with big toes touching.",
                    "Lower torso between thighs and extend arms forward on mat.",
                    "Rest forehead on floor and breathe deeply into lower back.",
                    "Hold for 3 to 5 minutes."
                },
                "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800"
            );

            setupCard(
                binding.cardAnulomVilom,
                null,
                null,
                "Marjaryasana (Cat-Cow Stretch)",
                "15 min • Spine Alignment",
                900,
                "Flexes spine, relieves back tightness & improves posture.",
                new String[]{
                    "Start on hands and knees in tabletop position.",
                    "Inhale, drop belly toward floor and lift head into Cow Pose.",
                    "Exhale, arch spine upward toward ceiling into Cat Pose.",
                    "Repeat rhythmically for 10-15 cycles."
                },
                "https://images.unsplash.com/photo-1552196563-55cd4e45efb3?w=800"
            );

            setupCard(
                binding.cardKapalbhati,
                binding.tvPranayama2Title,
                binding.tvPranayama2Duration,
                "Vrikshasana (Tree Pose Balance)",
                "10 min • Focus & Balance",
                600,
                "Enhances physical balance, strengthens legs & stills a scattered mind.",
                new String[]{
                    "Stand straight in Tadasana.",
                    "Shift weight onto left foot and place right sole on inner left thigh.",
                    "Join palms at chest in Namaste or extend arms overhead.",
                    "Hold steady gaze at one fixed point for 1-2 minutes per side."
                },
                "https://images.unsplash.com/photo-1510894347713-fc3ed6fdf539?w=800"
            );

            Glide.with(this).load("https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800").centerCrop().into(binding.ivYogaPromo);

        } else {
            // LOW STRESS (1 - 3): Energizing & High Vitality
            binding.tvRecommendation.setText("⚡ Low Stress (" + stress + "/10)! Great Energy State for Kapalbhati & Surya Namaskar Flow.");

            setupCard(
                binding.cardStressRelief,
                binding.tvPranayama1Title,
                binding.tvPranayama1Duration,
                "Kapalbhati (Skull-Shining)",
                "15 min • Detox & Vitality",
                900,
                "Detoxifies lungs, boosts metabolism, energizes brain cells & core muscles.",
                new String[]{
                    "Sit straight with palms on knees.",
                    "Inhale deeply through nostrils.",
                    "Forcefully contract abdomen to pump air out through nostrils.",
                    "Allow passive inhalation between exhalations.",
                    "Perform 3 rounds of 30 pumps each."
                },
                "https://images.unsplash.com/photo-1510894347713-fc3ed6fdf539?w=800"
            );

            setupCard(
                binding.cardMorningYoga,
                null,
                null,
                "Surya Namaskar (Sun Flow)",
                "15 min • Full Body Vitality",
                900,
                "Full-body cardiovascular workout, flexes spine & tones core muscles.",
                new String[]{
                    "Stand erect in Pranamasana with hands folded.",
                    "Inhale into Hastauttanasana, exhale into Padahastasana.",
                    "Flow through Lunge, Plank, Cobra & Downward Dog poses.",
                    "Complete 12 continuous rounds."
                },
                "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800"
            );

            setupCard(
                binding.cardAnulomVilom,
                null,
                null,
                "Bhastrika (Bellows Breath)",
                "10 min • High Energy Boost",
                600,
                "Increases oxygenation, revitalizes full body & boosts immune function.",
                new String[]{
                    "Sit in Vajrasana or Sukhasana with fists near shoulders.",
                    "Inhale forcefully raising arms up.",
                    "Exhale forcefully pulling arms down with fists near shoulders.",
                    "Perform 20 quick breaths per round."
                },
                "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800"
            );

            setupCard(
                binding.cardKapalbhati,
                binding.tvPranayama2Title,
                binding.tvPranayama2Duration,
                "Virabhadrasana (Warrior Flow)",
                "20 min • Strength & Confidence",
                1200,
                "Builds leg strength, opens chest & boosts mental resilience.",
                new String[]{
                    "Step feet wide apart on mat.",
                    "Turn right foot out 90 degrees and bend right knee over ankle.",
                    "Stretch arms parallel to floor looking past right fingertips.",
                    "Hold Warrior I & II for 5 breaths each side."
                },
                "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800"
            );

            Glide.with(this).load("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800").centerCrop().into(binding.ivYogaPromo);
        }
    }

    private void setupCard(View cardView, TextView titleView, TextView durView, String name, String category, int durationSecs, String benefits, String[] steps, String imageUrl) {
        if (titleView != null) titleView.setText(name);
        if (durView != null) durView.setText((durationSecs / 60) + " min");

        cardView.setOnClickListener(v -> showExerciseGuideDialog(
            name, category, durationSecs, benefits, steps, imageUrl
        ));
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
}