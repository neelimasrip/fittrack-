package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fittrack.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        try {
                            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                    .getResult(ApiException.class);
                            if (account != null) {
                                firebaseAuthWithGoogle(account);
                            }
                        } catch (ApiException e) {
                            Toast.makeText(this, "Google sign up failed: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

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
            if (!java.util.Objects.equals(password, confirmPassword)) {
                binding.etConfirmPassword.setError("Passwords do not match");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                            saveUserToFirestore(mAuth.getCurrentUser().getUid(), name, email);
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                            Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        binding.btnGoogleSignup.setOnClickListener(v -> {
            googleSignInClient.signOut().addOnCompleteListener(this, task -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(signInIntent);
            });
        });

        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        binding.ivToggle.setOnClickListener(v -> {
            if (binding.etPassword.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                binding.etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        saveUserToFirestore(mAuth.getCurrentUser().getUid(), account.getDisplayName(), account.getEmail());
                    } else {
                        Toast.makeText(SignUpActivity.this, "Google auth failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToFirestore(String uid, String name, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        db.collection("users").document(uid).set(user)
                .addOnSuccessListener(unused -> {
                    preferenceManager.setLoggedIn(true);
                    // SAVE NAME TO PREFERENCES FOR IMMEDIATE USE ON DASHBOARD
                    preferenceManager.saveProfile(name, email, "", 0, 0); 

                    Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, OnboardingActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void updatePasswordStrength(String password) {
        if (password.length() < 4) {
            binding.barWeak.setBackgroundColor(getColor(android.R.color.holo_red_dark));
            binding.barMedium.setBackgroundColor(getColor(android.R.color.darker_gray));
            binding.barStrong.setBackgroundColor(getColor(android.R.color.darker_gray));
            binding.tvStrength.setText(R.string.weak_strength);
            binding.tvStrength.setTextColor(getColor(android.R.color.holo_red_dark));
        } else if (password.length() < 8) {
            binding.barWeak.setBackgroundColor(getColor(android.R.color.holo_orange_dark));
            binding.barMedium.setBackgroundColor(getColor(android.R.color.holo_orange_dark));
            binding.barStrong.setBackgroundColor(getColor(android.R.color.darker_gray));
            binding.tvStrength.setText(R.string.medium_strength);
            binding.tvStrength.setTextColor(getColor(android.R.color.holo_orange_dark));
        } else {
            binding.barWeak.setBackgroundColor(getColor(android.R.color.holo_green_dark));
            binding.barMedium.setBackgroundColor(getColor(android.R.color.holo_green_dark));
            binding.barStrong.setBackgroundColor(getColor(android.R.color.holo_green_dark));
            binding.tvStrength.setText(R.string.strong_strength);
            binding.tvStrength.setTextColor(getColor(android.R.color.holo_green_dark));
        }
    }
}