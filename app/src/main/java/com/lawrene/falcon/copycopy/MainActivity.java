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
    DatabaseReference mUserDatabase;
    DatabaseReference mPostDatabase;

    StorageReference mFilesStore;
    FirebaseAuth mFireAuth;
    Toolbar mToolbar;
    FirebaseUser mCurrentUser;

    String mUserSchool;
    String mUserFaculty;
    String mUserDepartment;
    String mUserLevel;
    RecyclerView mUserlist;

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFireAuth = FirebaseAuth.getInstance();
        mFilesStore = FirebaseStorage.getInstance().getReference();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mCurrentUser = mFireAuth.getCurrentUser();

        searchView = (SearchView)findViewById(R.id.app_bar_search);
        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("CopyCopy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mUserlist = (RecyclerView)findViewById(R.id.main_recycler_view);
        mUserlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        mUserlist.setLayoutManager(linearLayoutManager);

//
//        //Tabs
//        mViewPager = (ViewPager)findViewById(R.id.main_pager);
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        mTabLayout = (TabLayout)findViewById(R.id.main_tabs);
//        mTabLayout.setupWithViewPager(mViewPager);
//

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView= (NavigationView)findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("TAG", "Key: " + key + " Value: " + value);
            }
        }












    }

    private void loaduserdata() {
        mUserDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserSchool = dataSnapshot.child("school").getValue().toString();
                mUserFaculty = dataSnapshot.child("faculty").getValue().toString();
                mUserDepartment = dataSnapshot.child("department").getValue().toString();
                mUserLevel = dataSnapshot.child("level").getValue().toString();

                //------------------------Getting all the relevant posts depending on the user--------------
                mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child("Schools")
                        .child(mUserSchool).child(mUserFaculty).child(mUserDepartment).child(mUserLevel);

                FirebaseRecyclerAdapter<Solution, MainViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Solution, MainViewHolder>(
                        Solution.class,
                        R.layout.list_item,
                        MainViewHolder.class,
                        mPostDatabase

                ) {
                    @Override
                    protected void populateViewHolder(final MainViewHolder viewHolder, final Solution model, int position) {
                        viewHolder.setTitle(model.getTitle());
                    viewHolder.setImage(model.getThumb_image(), getApplicationContext());

                        final String postkey = getRef(position).getKey();

                        //Changing the date from server time to normal time
                        mPostDatabase.child(postkey).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String postdate = dataSnapshot.child("date").getValue().toString();
//
                                GetTimeAgo getTimeAgo = new GetTimeAgo();
                                long poostdate = Long.parseLong(postdate);
                                String convertedtime = getTimeAgo.getTimeAgo(poostdate, MainActivity.this);
                                viewHolder.setDate(convertedtime);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent profile_intent = new Intent(MainActivity.this, DetailsActivity.class);
                            profile_intent.putExtra("post_school", mUserSchool);
                            profile_intent.putExtra("post_faculty", mUserFaculty);
                            profile_intent.putExtra("post_department", mUserDepartment);
                            profile_intent.putExtra("post_level", mUserLevel);
                            profile_intent.putExtra("post_key", postkey);
                            startActivity(profile_intent);
                        }
                    });
                    }
                };

                mUserlist.setAdapter(firebaseRecyclerAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mFireAuth.getCurrentUser();
        if(currentUser == null){
            sendToStart();
        }

        else{
            loaduserdata();
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
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                // [END subscribe_topics]

                // Log and toast
                String msg = getString(R.string.msg_subscribed);
                Log.i("TAG", msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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
