package com.lawrene.falcon.copycopy;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFireAuth = FirebaseAuth.getInstance();
        mFilesStore = FirebaseStorage.getInstance().getReference();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mCurrentUser = mFireAuth.getCurrentUser();

        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("CopyCopy");

//        List<Solution> newlist = new ArrayList<>();
//        newlist.add(new Solution("21", "New title", R.drawable.ic_search_black_24dp));
//        newlist.add(new Solution("21", "New title", R.drawable.ic_search_black_24dp));
//        newlist.add(new Solution("21", "New title", R.drawable.ic_search_black_24dp));
//        newlist.add(new Solution("21", "New title", R.drawable.ic_search_black_24dp));
//        newlist.add(new Solution("21", "New title", R.drawable.ic_search_black_24dp));
//        newlist.add(new Solution("21", "New title", R.drawable.ic_search_black_24dp));
//        newlist.add(new Solution("21", "New title", R.drawable.ic_search_black_24dp));
//        newlist.add(new Solution("21", "New title", R.drawable.ic_search_black_24dp));

//        --------Firebase references----------
//        mCurrentUser = mFireAuth.getCurrentUser();
//        mUserDatabase = FirebaseDatabase.getInstance().getReference();
//        mFilesStore = FirebaseStorage.getInstance().getReference();
//        loaduserdata();
//        loadallposts();


//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.main_recycler_view);
//        SolutionAdapter solutionAdapter = new SolutionAdapter(this, newlist);
//        recyclerView.setAdapter(solutionAdapter);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//
//
//
//

        mUserlist = (RecyclerView)findViewById(R.id.main_recycler_view);
        mUserlist.setHasFixedSize(true);
        mUserlist.setLayoutManager(new LinearLayoutManager(this));

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
        }

        return super.onOptionsItemSelected(item);
    }
}
