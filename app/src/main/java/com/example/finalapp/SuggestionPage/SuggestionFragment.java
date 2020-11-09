package com.example.finalapp.SuggestionPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SuggestionFragment extends Fragment {

    FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference SuggestionRef = db.collection("Suggestions");

    private SuggestionStoreAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);
        floatingActionButton = view.findViewById(R.id.addSuggest);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        recyclerView = view.findViewById(R.id.s_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView(){
        Query query = SuggestionRef.orderBy("s_Date",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<SuggestUpload> options = new FirestoreRecyclerOptions.Builder<SuggestUpload>()
                .setQuery(query, SuggestUpload.class)
                .build();

        adapter = new SuggestionStoreAdapter(options);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void openDialog() {
        SuggestionDialog suggestionDialog = new SuggestionDialog();
        if (getFragmentManager() == null) throw new AssertionError();
        suggestionDialog.show(getFragmentManager(), "Example Dialog!");
    }
}
