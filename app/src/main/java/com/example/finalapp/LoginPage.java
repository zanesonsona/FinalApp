package com.example.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    CheckBox keep_signin;

    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String keep_check = preferences.getString("keep","");

        if(keep_check.equals("true")){
            startActivity(new Intent(LoginPage.this,MainActivity.class));
        }else if(keep_check.equals("false")){
            Toast.makeText(this,"Please Sign-in!",Toast.LENGTH_SHORT).show();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        user = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        keep_signin = findViewById(R.id.keepme);

        loginButton = findViewById(R.id.login);
        register = findViewById(R.id.signup);

        register.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        keep_signin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("keep","true");
                    editor.apply();
                    Toast.makeText(LoginPage.this,"Keep me sign-in",Toast.LENGTH_SHORT).show();
                }
                else if(!buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("keep","false");
                    editor.apply();
                    Toast.makeText(LoginPage.this,"Unchecked!",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
