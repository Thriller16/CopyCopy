package com.lawrene.falcon.copycopy;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DetailsActivity extends AppCompatActivity {

    DatabaseReference mPostDatabase;
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
        setContentView(R.layout.activity_details);

        mToolbar = (Toolbar) findViewById(R.id.details_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleText = (TextView) findViewById(R.id.ttiittllee);
        dateText = (TextView) findViewById(R.id.ddaattee);
        full_image_one = (ImageView) findViewById(R.id.img_one);
        full_image_two = (ImageView) findViewById(R.id.img_two);
        full_image_three = (ImageView) findViewById(R.id.img_three);
        full_image_four = (ImageView) findViewById(R.id.img_four);
        full_image_five = (ImageView) findViewById(R.id.img_five);
        full_image_six = (ImageView) findViewById(R.id.img_six);

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

    private void loadpostdetails() {

        mPostDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String postTitle = dataSnapshot.child("title").getValue().toString();
                String postDate = dataSnapshot.child("date").getValue().toString();
                String postImageOne = dataSnapshot.child("url1").getValue().toString();
                String postImageTwo = dataSnapshot.child("url2").getValue().toString();
                String postImageThree = dataSnapshot.child("url3").getValue().toString();
                String postImageFour = dataSnapshot.child("url4").getValue().toString();
                String postImageFive = dataSnapshot.child("url5").getValue().toString();
                String postImageSix = dataSnapshot.child("url6").getValue().toString();

                titleText.setText(postTitle);

                GetTimeAgo getTimeAgo = new GetTimeAgo();
                long poostdate = Long.parseLong(postDate);
                String convertedtime = getTimeAgo.getTimeAgo(poostdate, DetailsActivity.this);
                dateText.setText(convertedtime);


//               ------------------------------ Loading all the images into the view----------------------------
                Picasso.get().load(postImageOne).into(full_image_one);
                if (!postImageTwo.equals("")) {
                    Picasso.get().load(postImageTwo).into(full_image_two);

                    if (!postImageThree.equals("")) {
                        Picasso.get().load(postImageThree).into(full_image_three);

                        if(!postImageFour.equals("")){
                            Picasso.get().load(postImageFour).into(full_image_four);

                            if(!postImageFive.equals("")){
                                Picasso.get().load(postImageFive).into(full_image_five);

                                if(!postImageSix.equals("")){
                                    Picasso.get().load(postImageSix).into(full_image_six);
                                }
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Open downloaded folder
    private void openDownloadedFolder() {
        //First check if SD Card is present or not
        if (new CheckForSDCard().isSDCardPresent()) {

            //Get Download Directory File
            File apkStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + "CopyCopy Downloads");

            //If file is not present then display Toast
            if (!apkStorage.exists())
                Toast.makeText(DetailsActivity.this, "Right now there is no directory. Please download some file first.", Toast.LENGTH_SHORT).show();

            else {

                //If directory is present Open Folder

                /** Note: Directory will open only if there is a app to open directory like File Manager, etc.  **/

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                        + "/" + "CopyCopy Downloads");
                intent.setDataAndType(uri, "file/*");
                startActivity(Intent.createChooser(intent, "Open Download Folder"));
            }

        } else
            Toast.makeText(DetailsActivity.this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

    }
}
