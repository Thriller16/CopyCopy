package com.lawrene.falcon.copycopy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lawrene on 5/27/18.
 */

public class SolutionAdapter extends RecyclerView.Adapter<MainViewHolder> implements Filterable {

    private Context mContext;
    private List<Solution> solutionList;
    private List<Solution> filteredSolutionList;


    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    DatabaseReference mFavDatabase;
    DatabaseReference mPostDatabase;
    DatabaseReference mUserDatabase;


    public SolutionAdapter(Context mContext, List<Solution> solutionList) {
        this.mContext = mContext;
        this.solutionList = solutionList;
    }

    public SolutionAdapter() {
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_main, parent, false);

        return new MainViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Solution solution = solutionList.get(position);

        holder.setTitle(solution.getTitle());
        GetTimeAgo getTimeAgo = new GetTimeAgo();
        long poostdate = Long.parseLong(String.valueOf(solution.getDate()));
        String convertedtime = getTimeAgo.getTimeAgo(poostdate, mContext);
        holder.setDate(convertedtime);
        holder.setImage(solution.getThumb_image());
        holder.changeChecked(solution);
    }


    @Override
    public int getItemCount() {
        return solutionList.size();
    }

    public void addToFav(final Solution solution, final ImageView imageView) {

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        final String user_id = mCurrentUser.getUid().toString();
        mFavDatabase = FirebaseDatabase.getInstance().getReference().child("Favorites");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);


        final HashMap<String, Object> favHashMap = new HashMap<>();
        favHashMap.put("title", solution.getTitle());
        favHashMap.put("date", ServerValue.TIMESTAMP);
        favHashMap.put("url1", solution.getUrl1());
        favHashMap.put("url2", solution.getUrl2());
        favHashMap.put("url3", solution.getUrl3());
        favHashMap.put("url4", solution.getUrl4());
        favHashMap.put("url5", solution.getUrl5());
        favHashMap.put("url6", solution.getUrl6());
        favHashMap.put("thumb_image", solution.getThumb_image());
        favHashMap.put("posted_by", solution.getPosted_by());
        favHashMap.put("image_uid", solution.postkey);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userSchool = dataSnapshot.child("school").getValue().toString();
                String userFaculty = dataSnapshot.child("faculty").getValue().toString();
                String userDepartment = dataSnapshot.child("department").getValue().toString();
                String userLevel = dataSnapshot.child("level").getValue().toString();


                mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child("Schools").
                        child(userSchool).child(userFaculty).child(userDepartment).child(userLevel).child(solution.getPostkey()).child("isInFav");
                mPostDatabase.setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mFavDatabase.child(user_id).child(solution.getPostkey()).setValue(favHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                imageView.setImageResource(R.drawable.ic_favorite_black_24dp);
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void removeFromav(final Solution solution, final ImageView imageView) {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        final String user_id = mCurrentUser.getUid().toString();
        mFavDatabase = FirebaseDatabase.getInstance().getReference().child("Favorites");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userSchool = dataSnapshot.child("school").getValue().toString();
                String userFaculty = dataSnapshot.child("faculty").getValue().toString();
                String userDepartment = dataSnapshot.child("department").getValue().toString();
                String userLevel = dataSnapshot.child("level").getValue().toString();

                mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child("Schools").
                        child(userSchool).child(userFaculty).child(userDepartment).child(userLevel).child(solution.getPostkey()).child("isInFav");
                mPostDatabase.setValue("false").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mFavDatabase.child(user_id).child(solution.getPostkey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    filteredSolutionList = solutionList;
                } else {

                    ArrayList<Solution> filteredList = new ArrayList<>();

                    for (Solution solution : solutionList) {
                        filteredList.add(solution);
                    }

                    filteredSolutionList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSolutionList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredSolutionList = (ArrayList<Solution>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
