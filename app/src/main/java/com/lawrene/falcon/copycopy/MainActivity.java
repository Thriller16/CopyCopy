package com.lawrene.falcon.copycopy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    android.support.v7.widget.SearchView searchView;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("School Planner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting up navigationbar
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navView);
<<<<<<< HEAD

//        updateUserEarnings();

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
                                    Intent uploadIntent = new Intent(MainActivity.this, PostActivity.class);
                                    uploadIntent.putExtra("user_id", mCurrentUser.getUid());
                                    startActivity(uploadIntent);
                                }
//
                                else {
                                    startActivity(new Intent(MainActivity.this, BecomeEligible.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;
                    case R.id.logout_nav:
                        mFireAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        break;
=======
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.reminder){
                    startActivity(new Intent(MainActivity.this, ReminderActivity.class));
>>>>>>> a8e3a2135d6986e4ede79f2204c98676cb29777d
                }
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
<<<<<<< HEAD

        updateUserDept();
    }
=======
>>>>>>> a8e3a2135d6986e4ede79f2204c98676cb29777d

    }

<<<<<<< HEAD
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(startIntent);
        finish();
    }
//
//    public void updateUserEarnings(){
//
//        mUserDatabase.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    String total_earnings = dataSnapshot.child("earnings").getValue().toString();
//                    View headerView = navigationView.getHeaderView(0);
//                    TextView navUsername = (TextView) headerView.findViewById(R.id.total_earnings);
//                    navUsername.setText("₦" + total_earnings + ".00");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//

//    }

    public void updateUserDept() {
        mUserDatabase.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                View headerView = navigationView.getHeaderView(0);
                TextView navDept = (TextView) headerView.findViewById(R.id.userDept);

                String userDept = dataSnapshot.child("department").getValue().toString();
                String userFaculty = dataSnapshot.child("faculty").getValue().toString();

                String firstStringFaculty = String.valueOf(userFaculty.charAt(0));
                String firstStringDept = String.valueOf(userDept.charAt(0));

                navDept.setText("" + firstStringDept + firstStringFaculty);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
=======
    long previousTime;
>>>>>>> a8e3a2135d6986e4ede79f2204c98676cb29777d

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onBackPressed() {
//        searchView = (SearchView) findViewById(R.id.app_bar_search);
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//            return;
//        } else {
//            if (2000 + previousTime > (previousTime = System.currentTimeMillis())) {
//                MainActivity.this.finish();
//                moveTaskToBack(true);
//
//            } else {
//                Toast.makeText(getBaseContext(), "Touch again to exit", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
