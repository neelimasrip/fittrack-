package com.example.fittrack;

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
                .requestIdToken("524815681692-pv6kgfm8243kae87h1j5f3tfq54ttca6.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK) {

                        try {

                            GoogleSignInAccount account =
                                    GoogleSignIn.getSignedInAccountFromIntent(
                                                    result.getData())
                                            .getResult(ApiException.class);

                            firebaseAuthWithGoogle(account);

                        } catch (ApiException e) {

                            Toast.makeText(
                                    this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
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

                            preferenceManager.setLoggedIn(true);

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            loginSuccess();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    task.getException() != null
                                            ? task.getException().getMessage()
                                            : "Login Failed",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        });

        binding.tvSignup.setOnClickListener(v -> {
            startActivity(
                    new Intent(
                            LoginActivity.this,
                            SignUpActivity.class
                    )
            );
        });

        binding.tvForgotPassword.setOnClickListener(v -> {

            String email = binding.etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                binding.etEmail.setError("Enter your email first");
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Password reset email sent",
                                    Toast.LENGTH_SHORT
                            ).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    LoginActivity.this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show());
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
            intent = new Intent(
                    LoginActivity.this,
                    OnboardingActivity.class
            );
        } else {
            intent = new Intent(
                    LoginActivity.this,
                    DashboardActivity.class
            );
        }

        startActivity(intent);
        finish();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(
                        account.getIdToken(),
                        null
                );

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        String uid = mAuth.getCurrentUser().getUid();

                        java.util.HashMap<String, Object> user =
                                new java.util.HashMap<>();

                        user.put("name", account.getDisplayName());
                        user.put("email", account.getEmail());

                        db.collection("users")
                                .document(uid)
                                .set(user);

                        preferenceManager.setLoggedIn(true);

                        Toast.makeText(
                                LoginActivity.this,
                                "Google Sign-In Successful",
                                Toast.LENGTH_SHORT
                        ).show();

                        loginSuccess();

                    } else {

                        Toast.makeText(
                                LoginActivity.this,
                                "Google Sign-In Failed",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

}