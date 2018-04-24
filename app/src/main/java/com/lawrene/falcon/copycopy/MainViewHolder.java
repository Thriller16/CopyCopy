package com.lawrene.falcon.copycopy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    public void setTitle(String title) {
        TextView usernametextview = (TextView)mview.findViewById(R.id.post_title);
        usernametextview.setText(title);
    }

    public void setDate(String date) {
        TextView statustextview = (TextView)mview.findViewById(R.id.post_date);
        statustextview.setText(date);
    }

    public void setImage(String thumb_image, Context applicationContext) {
        ImageView userimageview = (ImageView) mview.findViewById(R.id.post_thumb);
        Picasso.get().load(thumb_image).into(userimageview);
    }
}
