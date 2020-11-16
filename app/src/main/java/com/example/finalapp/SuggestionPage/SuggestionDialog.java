package com.example.finalapp.SuggestionPage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestionDialog extends AppCompatDialogFragment {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private CircleImageView s_cImgView;
    private TextView s_Name;
    private TextView s_Date;
    private EditText s_suggest;
    private final String s_likes = "0";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference SuggestionRef = db.collection("Suggestions");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    StorageReference imgRef = storage.getReference().child("Users").child(uid+".jpg");

    private StorageReference storageReference;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog, null);
        AlertDialog.Builder addsuggest = new AlertDialog.Builder(getActivity());

        storageReference = FirebaseStorage.getInstance().getReference("suggestions");

        s_cImgView = view.findViewById(R.id.dialog_Profile);
        s_Name = view.findViewById(R.id.dialog_fullname);
        s_Date = view.findViewById(R.id.dialog_datepost);
        s_suggest = view.findViewById(R.id.dialog_SuggestionText);

        DateSetter();

        addsuggest.setView(view)
                .setTitle("Suggestion Box")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //way sud or way gamit
                    }
                })
                .setPositiveButton("Suggest!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user.isEmailVerified()){
                            Toast.makeText(getActivity(),"Suggested!",Toast.LENGTH_LONG).show();
                            addSuggest();
                        }
                        else {
                            Toast.makeText(getActivity(),"Your Email is not Verified\nGo to your Account Settings to Verify",Toast.LENGTH_LONG).show();
                        }

                    }
                });

        return addsuggest.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null){
                    Picasso.get().load(uri.toString())
                            .placeholder(R.drawable.download)
                            .into(s_cImgView);
                }
                else{
                    Toast.makeText(getActivity(),"Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

        String currentEmail = mAuth.getCurrentUser().getEmail();
        DocumentReference reference = firestore.collection("Users").document(currentEmail)
                .collection("User Info").document(currentEmail);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("fullname");
                    s_Name.setText(name);
                }
                else {
                    Toast.makeText(getActivity(),"Document Doesn't Exist",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addSuggest() {

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null){

                    Uri downloadUrl = uri;

                    String name = s_Name.getText().toString();
                    String date = s_Date.getText().toString();
                    String suggest = s_suggest.getText().toString();

                    SuggestionRef.add(new SuggestUpload(downloadUrl.toString(), name, date, suggest, s_likes));

                }
            }
        });

//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        StorageReference prof_img = storageReference.child(uid + ".jpg");
//
//        s_cImgView.setDrawingCacheEnabled(true);
//        s_cImgView.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) s_cImgView.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = prof_img.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                DateSetter();
//
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while ((!uriTask.isSuccessful())) ;
//                Uri downloadUrl = uriTask.getResult();
//
//                String name = s_Name.getText().toString();
//                String date = s_Date.getText().toString();
//                String suggest = s_suggest.getText().toString();
//
//                SuggestionRef.add(new SuggestUpload(downloadUrl.toString(), name, date, suggest, s_likes));
//            }
//        });
    }

    public void DateSetter() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM dd, EEEE hh:mm a");
        Date CurrentDate = new Date();
        String currentDate = simpleDateFormat.format(CurrentDate);
        s_Date.setText(currentDate);
    }
}
