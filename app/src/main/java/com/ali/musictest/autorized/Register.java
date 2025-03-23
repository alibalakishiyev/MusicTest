package com.ali.musictest.autorized;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ali.musictest.R;
import com.ali.musictest.splash.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText mName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // UI elementləri tapın
        mName = findViewById(R.id.mname);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password); // password EditText-in id-si
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.register);
        mLoginBtn = findViewById(R.id.createText);
        progressBar = findViewById(R.id.progressBar);

        // Firebase Auth instance
        fAuth = FirebaseAuth.getInstance();

        // Əgər istifadəçi artıq daxil olubsa, Login səhifəsinə yönləndir
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        // Qeydiyyat düyməsinə klik hadisəsi
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // Validasiya
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                // ProgressBar-i göstər
                progressBar.setVisibility(View.VISIBLE);

                // Firebase ilə istifadəçi qeydiyyatı
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE); // ProgressBar-i gizlət
                    }
                });
            }
        });

        // Login səhifəsinə keçid
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}