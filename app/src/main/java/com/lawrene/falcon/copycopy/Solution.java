package com.lawrene.falcon.copycopy;

/**
 * Created by lawrene on 4/19/18.
 */

public class Solution {
    long date;
    String title;
    String image;
    String thumb_image;
    String recent;

    public String getThumb_image() {
        return thumb_image;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public Solution(){}

    public long getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getimage() {
        return image;
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

    public void setImage(String image) {
        this.image = image;
    }
}