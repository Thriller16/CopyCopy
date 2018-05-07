package com.lawrene.falcon.copycopy;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentsFragment extends Fragment {
    View mView;
    RecyclerView mUserlist;
    FirebaseAuth mFireAuth;
    DatabaseReference mUserDatabase;
    FirebaseUser mCurrentUser;
    StorageReference mFilesStore;
    DatabaseReference mPostDatabase;

    String mUserSchool;
    String mUserFaculty;
    String mUserDepartment;
    String mUserLevel;
    FirebaseRecyclerAdapter<Solution, MainViewHolder> firebaseRecyclerAdapter;


    public RecentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recents, container, false);

        mFireAuth = FirebaseAuth.getInstance();
        mFilesStore = FirebaseStorage.getInstance().getReference();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mCurrentUser = mFireAuth.getCurrentUser();


        mUserlist = (RecyclerView) mView.findViewById(R.id.main_recycler_view);
        mUserlist.setHasFixedSize(true);
        FloatingActionButton floatingActionButton = (FloatingActionButton) mView.findViewById(R.id.uploadFab);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        mUserlist.setLayoutManager(linearLayoutManager);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsers();
            }
        });

        return mView;
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

                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Solution, MainViewHolder>(
                        Solution.class,
                        R.layout.list_item_main,
                        MainViewHolder.class,
                        mPostDatabase.child("").child("")

                ) {


                    @Override
                    protected void populateViewHolder(final MainViewHolder viewHolder, final Solution model, int position) {
                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setImage(model.getThumb_image(), getContext());

                        final String postkey = getRef(position).getKey();

                        //Changing the date from server time to normal time
                        mPostDatabase.child(postkey).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String postdate = dataSnapshot.child("date").getValue().toString();
//
                                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                                    long poostdate = Long.parseLong(postdate);
                                    String convertedtime = getTimeAgo.getTimeAgo(poostdate, getContext());
                                    viewHolder.setDate(convertedtime);
//                                firebaseRecyclerAdapter.notifyDataSetChanged();
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent profile_intent = new Intent(getContext(), DetailsActivity.class);
                                profile_intent.putExtra("post_school", mUserSchool);
                                profile_intent.putExtra("post_faculty", mUserFaculty);
                                profile_intent.putExtra("post_department", mUserDepartment);
                                profile_intent.putExtra("post_level", mUserLevel);
                                profile_intent.putExtra("post_key", postkey);
                                startActivity(profile_intent);
                            }
                        });
//                        Toast.makeText(getContext(), "" + postkey, Toast.LENGTH_SHORT).show();
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
    public void onStart() {
        super.onStart();
        loaduserdata();
    }

    public void checkUsers() {
        mUserDatabase.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String can_upload = dataSnapshot.child("can_upload").getValue().toString();
                Toast.makeText(getContext(), "" + can_upload, Toast.LENGTH_SHORT).show();

                if (can_upload.equals("true")) {
                    Intent uploadIntent = new Intent(getContext(), AdminActivity.class);
                    uploadIntent.putExtra("user_id", mCurrentUser.getUid());
                    startActivity(uploadIntent);
                }
//
                else {
                    startActivity(new Intent(getContext(), BecomeEligible.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
