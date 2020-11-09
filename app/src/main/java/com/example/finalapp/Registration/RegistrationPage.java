package com.example.finalapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalapp.LoginPage;
import com.example.finalapp.Profile.ProfileChange;
import com.example.finalapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    EditText reg_email, reg_pass, reg_re_pass;
    EditText reg_fname,reg_age,reg_birthday,reg_address;

    RadioGroup gender;
    RadioButton gender_btn;

    CircleImageView reg_male,reg_female;

    TextView signupButton,cancelBtn;
    private ProgressBar progressBar;

    int progress = 0;
    private StorageTask UploadTask;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    CollectionReference RegisterRef = db.collection("Users");

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("Users");

        reg_email = findViewById(R.id.Email_Field);
        reg_pass = findViewById(R.id.Password_Field);
        reg_re_pass = findViewById(R.id.Retype_Password_Field);
        progressBar = findViewById(R.id.reg_prog_bar);

        reg_fname = findViewById(R.id.reg_full_name);
        reg_age = findViewById(R.id.reg_age);
        reg_birthday = findViewById(R.id.reg_birthday);
        reg_address = findViewById(R.id.reg_address);
        gender = findViewById(R.id.gender);

        reg_male = findViewById(R.id.reg_male_img);
        reg_female = findViewById(R.id.reg_female_img);

        signupButton = findViewById(R.id.reg_signup);
        cancelBtn = findViewById(R.id.reg_cancel);

        signupButton.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void registerUser() {
        final String Email = reg_email.getText().toString().trim();
        String Password = reg_pass.getText().toString().trim();
        String RetypePassword = reg_re_pass.getText().toString().trim();

        if (Email.isEmpty()) {
            reg_email.setError("Email is Required");
            reg_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            reg_email.setError("Invalid Email");
            reg_email.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            reg_pass.setError("Password is Required");
            reg_pass.requestFocus();
            return;
        }
        if (RetypePassword.isEmpty()) {
            reg_re_pass.setError("Please Confirm Password");
            reg_re_pass.requestFocus();
            return;
        }
        if (Password.length() < 6) {
            reg_pass.setError("Password must contain atleast 6 characters");
            reg_pass.requestFocus();
            return;
        }
        if (!Password.equals(RetypePassword)) {
            reg_re_pass.setError("Password is not Confirmed!");
            reg_re_pass.requestFocus();
            return;
        }
        if (Password.equals(RetypePassword)) {
            final String name = reg_fname.getText().toString().trim();
            final String age = reg_age.getText().toString().trim();
            final String birthday = reg_birthday.getText().toString().trim();
            final String address = reg_address.getText().toString().trim();

            if (name.isEmpty()) {
                reg_fname.setError("Fullname is required");
                reg_fname.requestFocus();
                return;
            }if (age.isEmpty()) {
                reg_age.setError("Age is required");
                reg_age.requestFocus();
                return;
            }if (birthday.isEmpty()) {
                reg_birthday.setError("Birthday is required");
                reg_birthday.requestFocus();
                return;
            }if (address.isEmpty()) {
                reg_address.setError("Address is required");
                reg_address.requestFocus();
                return;
            }

            int gender_picker = gender.getCheckedRadioButtonId();
            gender_btn = findViewById(gender_picker);

            final String gender = gender_btn.getText().toString().trim();


            if(gender.equals("Male")){
                signupButton.setTextColor(Color.GRAY);
                signupButton.setClickable(false);
                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    StorageReference prof_img = storageReference.child(uid+".jpg");
                                    reg_male.setDrawingCacheEnabled(true);
                                    reg_male.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) reg_male.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    UploadTask = prof_img.putBytes(data).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                        }}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while ((!uriTask.isSuccessful())) ;
                                            Uri downloadUrl = uriTask.getResult();

                                            RegisterUser registerRef = new RegisterUser(name,gender,age,birthday,address,Email,downloadUrl.toString());
                                            db.collection("Users")
                                                    .document(Email)
                                                    .collection("User Info")
                                                    .document(Email)
                                                    .set(registerRef);
                                        }
                                    });

                                    final Timer timer = new Timer();
                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            progress++;
                                            progressBar.setProgress(progress);
                                            if(progress == 100){
                                                timer.cancel();
                                            }
                                        }
                                    }; timer.schedule(timerTask,0,10);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegistrationPage.this,"Successfully Rregistered!",Toast.LENGTH_SHORT).show();
                                            progressBar.setProgress(0);
                                            ClearFields();
                                        }
                                    },1500);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(RegistrationPage.this, LoginPage.class));
                                        }
                                    },4000);
                                }
                                else {
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                        reg_email.setError("Email is already Registered");
                                        reg_email.requestFocus();
                                    }
                                    else {
                                        Toast.makeText(RegistrationPage.this,
                                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });


            }
            else if(gender.equals("Female")){
                signupButton.setTextColor(Color.GRAY);
                signupButton.setClickable(false);
                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    StorageReference prof_img = storageReference.child(uid+".jpg");

                                    reg_female.setDrawingCacheEnabled(true);
                                    reg_female.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) reg_female.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    UploadTask uploadTask = prof_img.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while ((!uriTask.isSuccessful())) ;
                                            Uri downloadUrl = uriTask.getResult();

                                            RegisterUser registerRef = new RegisterUser(name,gender,age,birthday,address,Email,downloadUrl.toString());
                                            db.collection("Users")
                                                    .document(Email)
                                                    .collection("User Info")
                                                    .document(Email)
                                                    .set(registerRef);

                                        }
                                    });
                                    final Timer timer = new Timer();
                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            progress++;
                                            progressBar.setProgress(progress);

                                            if(progress == 100){
                                                timer.cancel();
                                            }
                                        }
                                    }; timer.schedule(timerTask,0,10);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegistrationPage.this,"Successfully Rregistered!",Toast.LENGTH_SHORT).show();
                                            progressBar.setProgress(0);
                                            ClearFields();
                                        }
                                    },1500);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(RegistrationPage.this, LoginPage.class));
                                        }
                                    },4000);
                                }
                                else {
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                        reg_email.setError("Email is already Registered");
                                        reg_email.requestFocus();
                                    }
                                    else {
                                        Toast.makeText(RegistrationPage.this,
                                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        }
    }

    private void ClearFields(){
        reg_email.setText("");
        reg_pass.setText("");
        reg_re_pass.setText("");
        reg_fname.setText("");
        reg_age.setText("");
        reg_birthday.setText("");
        reg_address.setText("");
    }

    @Override
    public void onClick(View v) {

        if (v == signupButton) {
            if(UploadTask != null && UploadTask.isInProgress()){
                Toast.makeText(RegistrationPage.this,"Processing Registration...", Toast.LENGTH_SHORT).show();
                signupButton.setTextColor(Color.GRAY);
                signupButton.setClickable(false);
            }else{
                registerUser();
            }
        }
        if(v == cancelBtn){
            startActivity(new Intent(RegistrationPage.this, LoginPage.class));
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.signOut();
        finish();
    }
}
