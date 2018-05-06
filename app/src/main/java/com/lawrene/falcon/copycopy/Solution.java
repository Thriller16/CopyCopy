package com.lawrene.falcon.copycopy;

/**
 * Created by lawrene on 4/19/18.
 */

public class Solution {
    long date;
    String title;
    String url1;
    String posted_by;
    String thumb_image;

    public String getUrl1() {
        return url1;
    }


    public String getThumb_image() {
        return thumb_image;
    }

    public Solution(){}

    public long getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
