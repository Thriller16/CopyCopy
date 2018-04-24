package com.lawrene.falcon.copycopy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    DatabaseReference mPostDatabase;
    Toolbar mToolbar;
    TextView titleText, dateText;
    ImageView full_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mToolbar = (Toolbar)findViewById(R.id.details_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleText = (TextView)findViewById(R.id.ttiittllee);
        dateText = (TextView)findViewById(R.id.ddaattee);
        full_image = (ImageView)findViewById(R.id.imgg);

        String postSchool = getIntent().getStringExtra("post_school");
        String postFaculty = getIntent().getStringExtra("post_faculty");
        String postDepartment = getIntent().getStringExtra("post_department");
        String postLevel = getIntent().getStringExtra("post_level");
        String postId = getIntent().getStringExtra("post_key");

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child("Schools")
                .child(postSchool).child(postFaculty).child(postDepartment).child(postLevel)
        .child(postId);

        loadpostdetails();
    }

    private void loadpostdetails(){
        mPostDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String postTitle = dataSnapshot.child("title").getValue().toString();
                String postDate = dataSnapshot.child("date").getValue().toString();
                String postImage = dataSnapshot.child("image").getValue().toString();

                titleText.setText(postTitle);
                dateText.setText(postDate);
                Picasso.get().load(postImage).into(full_image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
