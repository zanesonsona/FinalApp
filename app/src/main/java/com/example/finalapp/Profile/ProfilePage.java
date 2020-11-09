package com.example.finalapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.example.finalapp.SuggestionPage.SuggestionDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePage extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Toolbar toolbar;

    CircleImageView profile_img;

    private TextView Email_Verify;
    private TextView fname,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profile_img = findViewById(R.id.profImg);
        fname = findViewById(R.id.profName);
        email = findViewById(R.id.profemail);

        Email_Verify = findViewById(R.id.prof_email_verify);
        Email_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifier = Email_Verify.getText().toString();

                if(verifier.equals("Email has been Verified!")){
                    openverified();
                }
                else{
                    openverifier();
                }

            }
        });

        TextView changeImg = findViewById(R.id.prof_settings);
        changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this,ProfileChange.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.pToolbar);
        setSupportActionBar(toolbar);

    }

    public void openverifier() {
        EmailVerifier emailVerifier = new EmailVerifier();
        emailVerifier.show(getSupportFragmentManager(), "Email Verifier");
    }

    public void openverified() {
        EmailVerified emailVerified = new EmailVerified();
        emailVerified.show(getSupportFragmentManager(), "Email Verified!");
    }

    @Override
    protected void onStart() {
        super.onStart();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference imgRef = storage.getReference().child("Users").child(uid+".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null){
                    Picasso.get().load(uri.toString())
                            .placeholder(R.drawable.download)
                            .into(profile_img);
                }
                else{
                    Toast.makeText(ProfilePage.this,"Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

        String currentEmail = mAuth.getCurrentUser().getEmail();
        email.setText(currentEmail);
        DocumentReference reference = firestore.collection("Users").document(currentEmail)
                .collection("User Info").document(currentEmail);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("fullname");
                    fname.setText(name);
                }
                else {
                    Toast.makeText(ProfilePage.this,"Document Doesn't Exist",Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        if(user.isEmailVerified()){
            Email_Verify.setText("Email has been Verified!");
            Email_Verify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_verified,0,0,0);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}