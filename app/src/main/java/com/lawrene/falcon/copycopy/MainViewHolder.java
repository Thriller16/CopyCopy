package com.lawrene.falcon.copycopy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by lawrene on 4/19/18.
 */

class MainViewHolder extends RecyclerView.ViewHolder {

    View mview;

    public MainViewHolder(View itemView) {
        super(itemView);

        mview = itemView;
    }


    public void allowClick(final Context ctx, final int position){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "Item " + position + " has been clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setTitle(String title) {
        TextView usernametextview = (TextView) mview.findViewById(R.id.post_title);
        usernametextview.setText(title);
    }

    public void setDate(String date) {
        TextView statustextview = (TextView) mview.findViewById(R.id.post_date);
        statustextview.setText(date);
    }

    public void setImage(String thumb_image
//            , Context applicationContext
    ) {
        ImageView userimageview = (ImageView) mview.findViewById(R.id.post_thumb);
        Picasso.get().load(thumb_image).into(userimageview);
    }

    public void changeChecked(final Solution solution) {
        final ImageView imageView = (ImageView) mview.findViewById(R.id.addToFav);

        if(solution.getIsInFav().equals("true")){
            imageView.setImageResource(R.drawable.ic_favorite_black_24dp);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SolutionAdapter solutionAdapter = new SolutionAdapter();
                    solutionAdapter.removeFromav(solution, imageView);
                }
            });
        }


        else if(solution.getIsInFav().equals("false")) {
            imageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SolutionAdapter solutionAdapter = new SolutionAdapter();
                    solutionAdapter.addToFav(solution, imageView);
                }
            });
        }
    }

}
