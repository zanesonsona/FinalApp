package com.example.finalapp.Profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.finalapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerifier extends AppCompatDialogFragment {
    private static final String TAG = "EmailVerifier";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String user = mAuth.getCurrentUser().getEmail();

        builder.setTitle("Verify Email")
                .setMessage("Send Email Verification to \n"+user)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Email Verification has been Sent!\nPlease check your Email to get verified!",Toast.LENGTH_SHORT).show();
                        FirebaseUser user1 = mAuth.getCurrentUser();
                        user1.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG , e.toString());
                            }
                        });
                    }
                });

        return builder.create();
    }
}
