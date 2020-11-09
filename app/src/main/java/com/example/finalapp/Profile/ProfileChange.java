package com.example.finalapp.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.finalapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileChange extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private static final int Pick_Image_Request = 1;

    private CircleImageView profpic;
    private ProgressBar progressBar;

    private Uri imageURI;

    private StorageReference storageReference;

    private StorageTask UploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change);

        Button changeBtn = findViewById(R.id.prof_change_btn);
        Button save = findViewById(R.id.prof_save);
        profpic = findViewById(R.id.prof_pic);
        progressBar = findViewById(R.id.prof_prog_bar);

        storageReference = FirebaseStorage.getInstance().getReference("Users");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference imgRef = storage.getReference().child("Users").child(uid+".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null){
                    Picasso.get().load(uri.toString())
                            .placeholder(R.drawable.download)
                            .into(profpic);
                }
                else{
                    Toast.makeText(ProfileChange.this,"Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UploadTask != null && UploadTask.isInProgress()){
                    Toast.makeText(ProfileChange.this,"Upload in Progress", Toast.LENGTH_SHORT).show();
                }else{
                    uploadFile();
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.pToolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Pick_Image_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Pick_Image_Request && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();

            Picasso.get().load(imageURI).into(profpic);
        }
    }

    private void uploadFile() {
        if(imageURI != null){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference fireRef = storageReference.child(uid+".jpg");

            UploadTask = fireRef.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 1000);
                            Toast.makeText(ProfileChange.this,"Successfully Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileChange.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });
        }
        else {
            Toast.makeText(this,"No Image Selected!",Toast.LENGTH_SHORT).show();
        }
    }
}