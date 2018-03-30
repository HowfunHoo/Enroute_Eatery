package com.enroute.enroute;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class log_in extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailLogin;
    private EditText passLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // EditTexts
        emailLogin = findViewById(R.id.loginEmail);
        passLogin  = findViewById(R.id.loginPass);

        // Buttons
        Button loginBtn = findViewById(R.id.loginBtn);                 //Log in button
        Button signUpScreenBtn = findViewById(R.id.signUpScreenBtn);   // SignUp screen button

        mAuth = FirebaseAuth.getInstance();

        signUpScreenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                startActivity(new Intent(log_in.this,
                        sign_up.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (mAuth.signInWithEmailAndPassword(emailLogin.getText().toString(), passLogin.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(log_in.this,
                                            "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(log_in.this,
                                            MapsActivity.class));
                                } else {
                                    Toast.makeText(log_in.this,
                                            "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
            }
        });
    }
}

