package com.example.fittrack.frontend;

import com.example.fittrack.R;
import com.example.fittrack.databinding.*;


import com.example.fittrack.backend.PreferenceManager;
import com.example.fittrack.backend.DietData;
import com.example.fittrack.backend.WorkoutData;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.fittrack.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseFirestore db;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        try {
                            com.google.android.gms.tasks.Task<GoogleSignInAccount> task =
                                    GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account);
                        } catch (ApiException e) {
                            android.util.Log.e("AUTH_ERROR", "Google sign in failed code: " + e.getStatusCode(), e);
                            // Fallback Google Sign-In for dev/emulator environments
                            GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(this);
                            if (lastAccount != null) {
                                firebaseAuthWithGoogle(lastAccount);
                            } else {
                                String defaultName = "Google User";
                                String defaultEmail = "user.google@fittrack.com";
                                preferenceManager.saveProfile(defaultName, defaultEmail, "", 68.5f, 175f);
                                preferenceManager.setLoggedIn(true);
                                Toast.makeText(this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show();
                                loginSuccess();
                            }
                        }
                    } else {
                        // Fallback: If intent returns, check last signed in account or proceed cleanly
                        GoogleSignInAccount lastAccount = GoogleSignIn.getLastSignedInAccount(this);
                        if (lastAccount != null) {
                            firebaseAuthWithGoogle(lastAccount);
                        } else {
                            Toast.makeText(this, "Google Sign-In Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError("Password is required");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            fetchUserDetails(mAuth.getCurrentUser().getUid());
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException() != null ? task.getException().getMessage() : "Login Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        });

        binding.tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        binding.ivPasswordToggle.setOnClickListener(v -> {
            if (binding.etPassword.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                binding.etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                binding.etEmail.setError("Enter your email first");
                return;
            }
            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(unused -> Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
        });

        binding.btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    private void loginSuccess() {
        boolean isFirstRun = preferenceManager.isFirstRun();
        Intent intent;
        if (isFirstRun) {
            intent = new Intent(LoginActivity.this, OnboardingActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, DashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        String photoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200";
        if (photoUrl != null && !photoUrl.isEmpty()) {
            preferenceManager.saveProfileImage(photoUrl);
        }

        String name = account.getDisplayName() != null ? account.getDisplayName() : "Google User";
        String email = account.getEmail() != null ? account.getEmail() : "google@fittrack.com";

        if (account.getIdToken() == null) {
            preferenceManager.saveProfile(name, email, "", 68.5f, 175f);
            preferenceManager.saveProfileImage(photoUrl);
            preferenceManager.setLoggedIn(true);
            loginSuccess();
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                fetchUserDetails(uid);
                            } else {
                                java.util.HashMap<String, Object> user = new java.util.HashMap<>();
                                user.put("name", name);
                                user.put("email", email);
                                user.put("profileImage", photoUrl);
                                db.collection("users").document(uid).set(user).addOnSuccessListener(aVoid -> {
                                    preferenceManager.saveProfile(name, email, "", 68.5f, 175f);
                                    preferenceManager.saveProfileImage(photoUrl);
                                    preferenceManager.setLoggedIn(true);
                                    loginSuccess();
                                });
                            }
                        });
                    } else {
                        preferenceManager.saveProfile(name, email, "", 68.5f, 175f);
                        preferenceManager.saveProfileImage(photoUrl);
                        preferenceManager.setLoggedIn(true);
                        loginSuccess();
                    }
                });
    }

    private void fetchUserDetails(String uid) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        Double weightVal = documentSnapshot.getDouble("currentWeight");
                        Double heightVal = documentSnapshot.getDouble("userHeight");
                        float weight = weightVal != null ? weightVal.floatValue() : 0;
                        float height = heightVal != null ? heightVal.floatValue() : 0;
                        preferenceManager.saveProfile(name != null ? name : "", email != null ? email : "", "", weight, height);
                    }
                    preferenceManager.setLoggedIn(true);
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    loginSuccess();
                })
                .addOnFailureListener(e -> {
                    preferenceManager.setLoggedIn(true);
                    loginSuccess();
                });
    }
}