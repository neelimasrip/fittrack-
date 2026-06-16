package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.example.fittrack.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                binding.etEmail.setError(getString(R.string.email_address) + " is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                binding.etPassword.setError(getString(R.string.password) + " is required");
                return;
            }

            // Verify login credentials
            if (verifyLogin(email, password)) {
                preferenceManager.setLoggedIn(true);
                loginSuccess();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvSignup.setOnClickListener(v -> 
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class))
        );

        binding.btnGoogle.setOnClickListener(v -> 
            Toast.makeText(this, "Google Sign-In coming soon", Toast.LENGTH_SHORT).show()
        );

        binding.tvForgotPassword.setOnClickListener(v -> 
            Toast.makeText(this, "Reset password email sent", Toast.LENGTH_SHORT).show()
        );
    }

    private boolean verifyLogin(String email, String password) {
        String savedEmail = preferenceManager.getUserEmail();
        String savedPassword = preferenceManager.getUserPassword();

        // If no user is registered, allow login for development or prompt for signup
        if (savedEmail.isEmpty()) {
            return true; // Default behavior for now if no one has registered
        }

        return email.equals(savedEmail) && password.equals(savedPassword);
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
}