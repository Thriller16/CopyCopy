package com.lawrene.falcon.copycopy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminApprove extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mPostlist;
    DatabaseReference mUserPost;
    DatabaseReference mPosts;
    DatabaseReference mNotificationsDatabase;
    DatabaseReference mUsersDatabase;
    FirebaseRecyclerAdapter<Solution, ApproveViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve);
        mUserPost = FirebaseDatabase.getInstance().getReference().child("PostsByUsers");
        mPosts = FirebaseDatabase.getInstance().getReference().child("Posts").child("Schools");
        mNotificationsDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = (Toolbar) findViewById(R.id.admin_approve);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setTitle("Approve posts");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPostlist = (RecyclerView) findViewById(R.id.approve_recycler_view);
        mPostlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPostlist.setLayoutManager(linearLayoutManager);
    }


    private void loaduserdata() {
        //------------------------Getting all the relevant posts depending on the user--------------
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Solution, ApproveViewHolder>(

                Solution.class,
                R.layout.list_item_admin_approve,
                ApproveViewHolder.class,
                mUserPost
        )

        {

            @Override
            protected void populateViewHolder(final ApproveViewHolder viewHolder, Solution model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(model.getThumb_image(), AdminApprove.this);
                final String postkey = getRef(position).getKey();

                viewHolder.changeChecked(postkey);
                mUserPost.child(postkey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String postdate = dataSnapshot.child("date").getValue().toString();
                            final String mUserSchool = dataSnapshot.child("school").getValue().toString();
                            final String mUserFaculty = dataSnapshot.child("faculty").getValue().toString();
                            final String mUserDepartment = dataSnapshot.child("department").getValue().toString();
                            final String mUserLevel = dataSnapshot.child("level").getValue().toString();

                            GetTimeAgo getTimeAgo = new GetTimeAgo();
                            long poostdate = Long.parseLong(postdate);
                            String convertedtime = getTimeAgo.getTimeAgo(poostdate, AdminApprove.this);
                            viewHolder.setDate(convertedtime);


                            viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent profile_intent = new Intent(AdminApprove.this, PostApproveDetails.class);
                                    profile_intent.putExtra("post_school", mUserSchool);
                                    profile_intent.putExtra("post_faculty", mUserFaculty);
                                    profile_intent.putExtra("post_department", mUserDepartment);
                                    profile_intent.putExtra("post_level", mUserLevel);
                                    profile_intent.putExtra("post_key", postkey);
                                    startActivity(profile_intent);
//                                    Toast.makeText(AdminApprove.this, "Item clicked" + mUserSchool+ mUserFaculty+mUserDepartment+mUserLevel, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                Toast.makeText(AdminApprove.this, "" + postkey, Toast.LENGTH_SHORT).show();
            }
        };

        mPostlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ApproveViewHolder extends RecyclerView.ViewHolder {

        View mview;

        public ApproveViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTitle(String title) {
            TextView usernametextview = (TextView) mview.findViewById(R.id.post_title_approve);
            usernametextview.setText(title);
        }

        public void setDate(String date) {
            TextView statustextview = (TextView) mview.findViewById(R.id.post_date_approve);
            statustextview.setText(date);
        }

        public void setImage(String thumb_image, Context applicationContext) {
            ImageView userimageview = (ImageView) mview.findViewById(R.id.post_thumb_approve);
            Picasso.get().load(thumb_image).into(userimageview);
        }

        public void changeChecked(final String key) {
            final ImageView imageView = (ImageView) mview.findViewById(R.id.approveCheck);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    approvePost(key);
                    imageView.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                }
            });
        }


        private void approvePost(final String postkey) {
            final DatabaseReference mUserPost = FirebaseDatabase.getInstance().getReference().child("PostsByUsers");
            final DatabaseReference mPosts = FirebaseDatabase.getInstance().getReference().child("Posts").child("Schools");
            final DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference().child("Users");

            mUserPost.child(postkey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        final String mUserSchool = dataSnapshot.child("school").getValue().toString();
                        final String mUserFaculty = dataSnapshot.child("faculty").getValue().toString();
                        final String mUserDepartment = dataSnapshot.child("department").getValue().toString();
                        final String mUserLevel = dataSnapshot.child("level").getValue().toString();
                        String mImageUid = dataSnapshot.child("image_uid").getValue().toString();
                        final String mPostedBy = dataSnapshot.child("posted_by").getValue().toString();
                        String mThumbImage = dataSnapshot.child("thumb_image").getValue().toString();
                        final String mTitle = dataSnapshot.child("title").getValue().toString();
                        String mUrl1 = dataSnapshot.child("url1").getValue().toString();
                        String mUrl2 = dataSnapshot.child("level").getValue().toString();
                        String mUrl3 = dataSnapshot.child("level").getValue().toString();
                        String mUrl4 = dataSnapshot.child("level").getValue().toString();
                        String mUrl5 = dataSnapshot.child("level").getValue().toString();
                        String mUrl6 = dataSnapshot.child("level").getValue().toString();

                        HashMap<String, Object> postMap = new HashMap<>();
                        postMap.put("date", ServerValue.TIMESTAMP);
                        postMap.put("image_uid", mImageUid);
                        postMap.put("posted_by", mPostedBy);
                        postMap.put("thumb_image", mThumbImage);
                        postMap.put("title", mTitle);
                        postMap.put("url1", mUrl1);
                        postMap.put("url2", mUrl2);
                        postMap.put("url3", mUrl3);
                        postMap.put("url4", mUrl4);
                        postMap.put("url5", mUrl5);
                        postMap.put("url6", mUrl6);

                        mPosts.child(mUserSchool).child(mUserFaculty).child(mUserDepartment).child(mUserLevel).child(postkey).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                mUserPost.child(postkey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//
//                                        mUsers.child(mPostedBy).child("earnings").setValue("5").addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
                                        sendRequiredUsersNotifications(mTitle, mUserSchool, mUserFaculty, mUserDepartment, mUserLevel);
//                                            }
//                                        });
                                    }
                                });
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void sendRequiredUsersNotifications(final String title, final String userSchool, final String userFaculty, final String userDepartment, final String userLevel) {

            final DatabaseReference mNotificationsDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
            DatabaseReference mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String key = ds.getKey();
                        String school = ds.child("school").getValue().toString();
                        String faculty = ds.child("faculty").getValue().toString();
                        String department = ds.child("department").getValue().toString();
                        String level = ds.child("level").getValue().toString();

                        HashMap<String, Object> notificationHashmap = new HashMap<>();
                        notificationHashmap.put("title", title);
                        notificationHashmap.put("message", "Click here to see");
                        notificationHashmap.put("time", ServerValue.TIMESTAMP);

                        if (school.equals(userSchool) && faculty.equals(userFaculty) && department.equals(userDepartment) && level.equals(userLevel)) {
                            mNotificationsDatabase.child(key).push().setValue(notificationHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i("TAG", "Notification has been sent to the selected users");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        }
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loaduserdata();
    }
}
