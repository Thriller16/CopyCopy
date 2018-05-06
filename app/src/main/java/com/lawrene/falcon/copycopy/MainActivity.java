package com.lawrene.falcon.copycopy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //
    SearchView searchView;
    FirebaseAuth mFireAuth;
    Toolbar mToolbar;
    FirebaseUser mCurrentUser;
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    TabLayout mTabLayout;
    DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFireAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFireAuth.getCurrentUser();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        searchView = (SearchView) findViewById(R.id.app_bar_search);
        mToolbar = (Toolbar) findViewById(R.id.admin_approve);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Approve Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        //Setting up navigationbar
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.user_upload:
                        mUserDatabase.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String can_upload = dataSnapshot.child("can_upload").getValue().toString();
                                Toast.makeText(MainActivity.this, "" + can_upload, Toast.LENGTH_SHORT).show();

                                if (can_upload.equals("true")) {
                                    Intent uploadIntent = new Intent(MainActivity.this, AdminActivity.class);
                                    uploadIntent.putExtra("user_id", mCurrentUser.getUid());
                                    startActivity(uploadIntent);
                                }
//
                                else {
                                    startActivity(new Intent(MainActivity.this, BecomeEligible.class));
                                }
                            }

                        @Override
                        public void onCancelled (DatabaseError databaseError){

                    }
                });
                Intent uploadIntent = new Intent(MainActivity.this, AdminActivity.class);
                uploadIntent.putExtra("user_id", mCurrentUser.getUid());
                startActivity(uploadIntent);
                break;
            }

            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }
    });
}

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mFireAuth.getCurrentUser();
        if (currentUser == null) {
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mFireAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;

//                ---------------------Test subscribe to topics------------
            case R.id.about:
//                // [START subscribe_topics]
//                FirebaseMessaging.getInstance().subscribeToTopic("news");
//                // [END subscribe_topics]
//
//                // Log and toast
//                String msg = getString(R.string.msg_subscribed);
//                Log.i("TAG", msg);
//                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                break;

            case R.id.settings:
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msgg = getString(R.string.msg_token_fmt, token);
                Log.i("TAGGER", msgg);
                Toast.makeText(MainActivity.this, msgg, Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
