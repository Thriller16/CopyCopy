package com.lawrene.falcon.copycopy;

import android.app.Application;
import android.content.Context;

/**
 * Created by lawrene on 4/22/18.
 */

public class GetTimeAgo extends Application {
    private static final int SECOND_MILLS = 1000;
    private static final int MINUTE_MILLS = 60 * SECOND_MILLS;
    private static final int HOUR_MILLS = 60 * MINUTE_MILLS;
    private static final int DAY_MILLS = 24 * HOUR_MILLS;

    public String getTimeAgo(long time, Context ctx){
        if(time < 1000000000000L){
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if(time > now || time <= 0){
            return null;
        }

        //TODO: localize
        final long diff = now - time;
        if(diff < MINUTE_MILLS){
            return "just now";
        } else if (diff < 2 * MINUTE_MILLS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLS) {
            return diff / MINUTE_MILLS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLS) {
            return "an hour a go";
        } else if (diff < 24 * HOUR_MILLS) {
            return diff / HOUR_MILLS + " hours ago";
        } else if (diff < 48 * HOUR_MILLS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLS + " days ago";
        }
    }
}
