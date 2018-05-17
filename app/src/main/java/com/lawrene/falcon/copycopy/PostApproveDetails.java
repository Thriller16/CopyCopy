package com.lawrene.falcon.copycopy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostApproveDetails extends AppCompatActivity {

    DatabaseReference mUserPostDatabase;
    Toolbar mToolbar;
    TextView titleText, dateText;
    ImageView full_image_one;
    ImageView full_image_two;
    ImageView full_image_three;
    ImageView full_image_four;
    ImageView full_image_five;
    ImageView full_image_six;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_approve_details);


        mToolbar = (Toolbar) findViewById(R.id.approve_details_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        titleText = (TextView) findViewById(R.id.ttiittllee);
//        dateText = (TextView) findViewById(R.id.ddaattee);
//        full_image_one = (ImageView) findViewById(R.id.img_one);
//        full_image_two = (ImageView) findViewById(R.id.img_two);
//        full_image_three = (ImageView) findViewById(R.id.img_three);
//        full_image_four = (ImageView) findViewById(R.id.img_four);
//        full_image_five = (ImageView) findViewById(R.id.img_five);
//        full_image_six = (ImageView) findViewById(R.id.img_six);
        String postSchool = getIntent().getStringExtra("post_school");
        String postFaculty = getIntent().getStringExtra("post_faculty");
        String postDepartment = getIntent().getStringExtra("post_department");
        String postLevel = getIntent().getStringExtra("post_level");
        String postId = getIntent().getStringExtra("post_key");


        mUserPostDatabase = FirebaseDatabase.getInstance().getReference().child("PostByUsers").child(postId);
        loadpostdetails();

    }

    private void loadpostdetails() {
        mUserPostDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String postTitle = dataSnapshot.child("title").getValue().toString();
//                String postDate = dataSnapshot.child("date").getValue().toString();
//                final String postImageOne = dataSnapshot.child("url1").getValue().toString();
//                final String postImageTwo = dataSnapshot.child("url2").getValue().toString();
//                final String postImageThree = dataSnapshot.child("url3").getValue().toString();
//                final String postImageFour = dataSnapshot.child("url4").getValue().toString();
//                final String postImageFive = dataSnapshot.child("url5").getValue().toString();
//                final String postImageSix = dataSnapshot.child("url6").getValue().toString();
                Toast.makeText(PostApproveDetails.this, "The snapshot received is " + dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
