package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fittrack.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSignup.setOnClickListener(v -> {

            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                binding.etName.setError("Name is required");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Email is required");
                return;
            }

            if (password.length() < 6) {
                binding.etPassword.setError("Password must be at least 6 characters");
                return;
            }

            if (!password.equals(confirmPassword)) {
                binding.etConfirmPassword.setError("Passwords do not match");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            String uid = mAuth.getCurrentUser().getUid();

                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);

                            db.collection("users")
                                    .document(uid)
                                    .set(user)
                                    .addOnSuccessListener(unused -> {

                                        preferenceManager.setLoggedIn(true);

                                        Toast.makeText(
                                                SignUpActivity.this,
                                                "Account Created Successfully",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        startActivity(
                                                new Intent(
                                                        SignUpActivity.this,
                                                        OnboardingActivity.class
                                                )
                                        );

                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(
                                                    SignUpActivity.this,
                                                    e.getMessage(),
                                                    Toast.LENGTH_LONG
                                            ).show());

                        } else {

                            Toast.makeText(
                                    SignUpActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });

        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updatePasswordStrength(String password) {

        if (password.length() < 4) {

            binding.barWeak.setBackgroundColor(
                    getColor(android.R.color.holo_red_dark));

            binding.barMedium.setBackgroundColor(
                    getColor(android.R.color.darker_gray));

            binding.barStrong.setBackgroundColor(
                    getColor(android.R.color.darker_gray));

            binding.tvStrength.setText(R.string.weak_strength);

            binding.tvStrength.setTextColor(
                    getColor(android.R.color.holo_red_dark));

        } else if (password.length() < 8) {

            binding.barWeak.setBackgroundColor(
                    getColor(android.R.color.holo_orange_dark));

            binding.barMedium.setBackgroundColor(
                    getColor(android.R.color.holo_orange_dark));

            binding.barStrong.setBackgroundColor(
                    getColor(android.R.color.darker_gray));

            binding.tvStrength.setText(R.string.medium_strength);

            binding.tvStrength.setTextColor(
                    getColor(android.R.color.holo_orange_dark));

        } else {

            binding.barWeak.setBackgroundColor(
                    getColor(android.R.color.holo_green_dark));

            binding.barMedium.setBackgroundColor(
                    getColor(android.R.color.holo_green_dark));

            binding.barStrong.setBackgroundColor(
                    getColor(android.R.color.holo_green_dark));

            binding.tvStrength.setText(R.string.strong_strength);

            binding.tvStrength.setTextColor(
                    getColor(android.R.color.holo_green_dark));
        }
    }
}