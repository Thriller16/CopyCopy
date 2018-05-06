package com.lawrene.falcon.copycopy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminApprove extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mPostlist;
    DatabaseReference mUserPost;
    FirebaseRecyclerAdapter<Solution, ApproveViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve);
        mUserPost = FirebaseDatabase.getInstance().getReference().child("PostsByUsers");

        mToolbar = (Toolbar) findViewById(R.id.admin_approve);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Approve Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        mPostlist = (RecyclerView) findViewById(R.id.approve_recycler_view);
        mPostlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
        mPostlist.setLayoutManager(linearLayoutManager);
    }


    private void loaduserdata() {
//        mUserDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mUserSchool = dataSnapshot.child("school").getValue().toString();
//                mUserFaculty = dataSnapshot.child("faculty").getValue().toString();
//                mUserDepartment = dataSnapshot.child("department").getValue().toString();
//                mUserLevel = dataSnapshot.child("level").getValue().toString();

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

                mUserPost.child(postkey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String postdate = dataSnapshot.child("date").getValue().toString();
//
                        GetTimeAgo getTimeAgo = new GetTimeAgo();
                        long poostdate = Long.parseLong(postdate);
                        String convertedtime = getTimeAgo.getTimeAgo(poostdate, AdminApprove.this);
                        viewHolder.setDate(convertedtime);
                        firebaseRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent profile_intent = new Intent(UsersActivity.this, ProfileActivity.class);
//                        profile_intent.putExtra("user_id", user_id);
//                        startActivity(profile_intent);
//                    }
//                });
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
            TextView usernametextview = (TextView)mview.findViewById(R.id.post_title_approve);
            usernametextview.setText(title);
        }

        public void setDate(String date) {
            TextView statustextview = (TextView)mview.findViewById(R.id.post_date_approve);
            statustextview.setText(date);
        }

        public void setImage(String thumb_image, Context applicationContext) {
            ImageView userimageview = (ImageView) mview.findViewById(R.id.post_thumb_approve);
            Picasso.get().load(thumb_image).into(userimageview);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        loaduserdata();
    }
}
