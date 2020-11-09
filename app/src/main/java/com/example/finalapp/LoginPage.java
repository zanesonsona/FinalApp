package com.example.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.Registration.RegistrationPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;

    Button loginButton;
    TextView register;

    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebaseAuth = FirebaseAuth.getInstance();

        user = findViewById(R.id.username);
        pass = findViewById(R.id.pass);

        loginButton = findViewById(R.id.login);
        register = findViewById(R.id.signup);


        register.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    private void userLogin() {
        String email = user.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (email.isEmpty()) {
            user.setError("Fill out Email");
            user.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            user.setError("Email doesn't exist");
            user.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pass.setError("Fill out Password");
            pass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            pass.setError("Password contain atleast 6 characters");
            pass.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginPage.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginPage.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onClick(View v) {

        if (v == register) {
            startActivity(new Intent(this, RegistrationPage.class));
        }

        if (v == loginButton) {
            userLogin();
//            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
