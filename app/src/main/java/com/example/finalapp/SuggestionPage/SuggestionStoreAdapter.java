package com.example.finalapp.SuggestionPage;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalapp.MainActivity;
import com.example.finalapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestionStoreAdapter extends FirestoreRecyclerAdapter<SuggestUpload,SuggestionStoreAdapter.SuggestStoreHolder> {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public SuggestionStoreAdapter(@NonNull FirestoreRecyclerOptions<SuggestUpload> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SuggestStoreHolder holder, int position, @NonNull SuggestUpload model) {

//        --------------------Try Removing img in firestore----------------
//        ----------------------Clear all Suggestions first------------------

//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        StorageReference imgRef = storage.getReference().child("Users").child(uid+".jpg");
//
//        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                if (uri != null){
//                    Picasso.get().load(uri.toString())
//                            .placeholder(R.drawable.kam_map)
//                            .into(head_img);
//
//                }
//            }
//        });

        Picasso.get().load(model.getS_cImgView())
                .placeholder(R.drawable.kam_map)
                .fit()
                .centerCrop()
                .into(holder.s_cImgView);
        holder.s_Name.setText(model.getS_Name());
        holder.s_Date.setText(model.getS_Date());
        holder.s_suggest.setText(model.getS_suggest());
        holder.s_likes.setText(model.getS_likes());

    }

    @NonNull
    @Override
    public SuggestStoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_layout,parent,false);

        return new SuggestStoreHolder(v);
    }

    class SuggestStoreHolder extends RecyclerView.ViewHolder{

        public CircleImageView s_cImgView;
        public TextView s_Name;
        public TextView s_Date;
        public TextView s_suggest;
        public TextView s_likes;

        public SuggestStoreHolder(@NonNull View itemView) {
            super(itemView);
            s_cImgView = itemView.findViewById(R.id.s_Profile);
            s_Name = itemView.findViewById(R.id.s_name);
            s_Date = itemView.findViewById(R.id.s_datepost);
            s_suggest = itemView.findViewById(R.id.s_suggested);
            s_likes = itemView.findViewById(R.id.s_likes);
        }
    }
}
