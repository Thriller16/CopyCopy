package com.lawrene.falcon.copycopy;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    Button button;
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
        button = (Button)findViewById(R.id.buttin);

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
                final String postImageOne = dataSnapshot.child("url1").getValue().toString();
                final String postImageTwo = dataSnapshot.child("url2").getValue().toString();
                final String postImageThree = dataSnapshot.child("url3").getValue().toString();
                final String postImageFour = dataSnapshot.child("url4").getValue().toString();
                final String postImageFive = dataSnapshot.child("url5").getValue().toString();
                final String postImageSix = dataSnapshot.child("url6").getValue().toString();

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

                full_image_one.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Boolean result=isDownloadManagerAvailable(getApplicationContext());
                        if (result)
                            downloadFile(postImageOne);
                        return true;
                    }
                });

                full_image_two.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Boolean result=isDownloadManagerAvailable(getApplicationContext());
                        if (result)
                            downloadFile(postImageTwo);
                        return true;
                    }
                });

                full_image_three.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Boolean result=isDownloadManagerAvailable(getApplicationContext());
                        if (result)
                            downloadFile(postImageThree);
                        return true;
                    }
                });

                full_image_four.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Boolean result=isDownloadManagerAvailable(getApplicationContext());
                        if (result)
                            downloadFile(postImageFour);
                        return true;
                    }
                });

                full_image_five.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Boolean result=isDownloadManagerAvailable(getApplicationContext());
                        if (result)
                            downloadFile(postImageFive);
                        return true;
                    }
                });

                full_image_six.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Boolean result=isDownloadManagerAvailable(getApplicationContext());
                        if (result)
                            downloadFile(postImageSix);
                        return true;
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void downloadFile(String url){
        String DownloadUrl = url;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request.setDescription("Your file is now being downloaded");   //appears the same in Notification bar while downloading
        request.setTitle("New File.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalFilesDir(getApplicationContext(),null, "sample.pdf");

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
    }

    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui","com.android.providers.downloads.ui.DownloadList");
//            List <resolveinfo> list = context.getPackageManager().queryIntentActivities(intent,
//                    PackageManager.MATCH_DEFAULT_ONLY);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
//    //Check if internet is present or not
//    private boolean isConnectingToInternet() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager
//                .getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected())
//            return true;
//        else
//            return false;
//    }
}
