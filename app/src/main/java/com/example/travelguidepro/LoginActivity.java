package com.example.travelguidepro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email, password;
    private TextView signupRedirectText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if (!emailText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    if (!passwordText.isEmpty()) {
                        auth.signInWithEmailAndPassword(emailText, passwordText)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Usuario logueado correctamente", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        password.setError("La contraseña no puede estar vacía ");
                    }
                } else if (emailText.isEmpty()) {
                    email.setError("El correo electrónico no puede estar vacío");
                } else {
                    email.setError("El correo electrónico no es válido");
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
