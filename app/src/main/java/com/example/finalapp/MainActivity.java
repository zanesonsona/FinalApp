package com.example.finalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.finalapp.Fragments.DashboardFragment;
import com.example.finalapp.Fragments.HomeFragment;
import com.example.finalapp.Fragments.InquiresFragment;
import com.example.finalapp.Profile.ProfilePage;
import com.example.finalapp.SuggestionPage.SuggestionFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    CircleImageView head_img;
    TextView head_name,head_mail;

    DrawerLayout drawerLayout;
    NavigationView drawerNav;
    Toolbar tbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ------------------------Drawer Navigation---------------

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerNav = findViewById(R.id.draw_nav_view);
        tbar = findViewById(R.id.toolbar);

        View header = drawerNav.getHeaderView(0);
        head_img = header.findViewById(R.id.header_img);
        head_name = header.findViewById(R.id.header_Name);
        head_mail = header.findViewById(R.id.header_email);



        setSupportActionBar(tbar);

        drawerNav.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, tbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        drawerNav.setNavigationItemSelectedListener(this);

        drawerNav.setCheckedItem(R.id.draw_home);


//        --------------------Bottom Navigation View------------
        BottomNavigationView navigationView = findViewById(R.id.btm_Nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.Home) {
                    HomeFragment fragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Frame_Layout, fragment);
                    fragmentTransaction.commit();
                    drawerNav.setCheckedItem(R.id.draw_home);
                }

                if (id == R.id.Suggestion) {

                    SuggestionFragment fragment = new SuggestionFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Frame_Layout, fragment);
                    fragmentTransaction.commit();
                }

                if (id == R.id.Inquires) {

                    InquiresFragment fragment = new InquiresFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Frame_Layout, fragment);
                    fragmentTransaction.commit();
                }

                if (id == R.id.Dashboard) {

                    DashboardFragment fragment = new DashboardFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Frame_Layout, fragment);
                    fragmentTransaction.commit();
                }

                return true;
            }
        });

        navigationView.setSelectedItemId(R.id.Home);
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
                            .into(head_img);

                }
                else{
                    Toast.makeText(MainActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

        String currentEmail = mAuth.getCurrentUser().getEmail();
        head_mail.setText(currentEmail);
        DocumentReference reference = firestore.collection("Users").document(currentEmail)
                .collection("User Info").document(currentEmail);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("fullname");
                    head_name.setText(name);
                }
                else {
                    Toast.makeText(MainActivity.this,"Document Doesn't Exist",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        BottomNavigationView navigationView = findViewById(R.id.btm_Nav);

        switch (item.getItemId()){
            case R.id.draw_home:
                navigationView.setSelectedItemId(R.id.Home);
                HomeFragment hf = new HomeFragment();
                FragmentTransaction hft = getSupportFragmentManager().beginTransaction();
                hft.replace(R.id.Frame_Layout, hf);
                hft.commit();
                break;

            case R.id.draw_settings:
                Intent intent = new Intent(this, ProfilePage.class);
                startActivity(intent);
                break;

            case R.id.logout:
                startActivity(new Intent(MainActivity.this, LoginPage.class));
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
