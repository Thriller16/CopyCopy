package com.lawrene.falcon.copycopy;

/**
 * Created by lawrene on 4/19/18.
 */

public class Solution {
    long date;
    String title;
    String url1;
    String url2;
    String url3;
    String url4;
    String url5;
    String url6;
    String posted_by;
    String thumb_image;
    String postkey;
    String isInFav;

    public String getIsInFav() {
        return isInFav;
    }

    public Solution(long date, String title, String url1, String url2, String url3, String url4, String url5, String url6, String posted_by, String thumb_image, String postkey, String isInFav) {

        this.date = date;
        this.title = title;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.url4 = url4;
        this.url5 = url5;
        this.url6 = url6;
        this.posted_by = posted_by;
        this.thumb_image = thumb_image;
        this.postkey = postkey;
        this.isInFav = isInFav;
    }

    public Solution(){}

    public String getUrl1() {
        return url1;
    }

    public String getPostkey() {
        return postkey;
    }

    public String getUrl2() {
        return url2;
    }

    public String getUrl3() {
        return url3;
    }

    public String getUrl4() {
        return url4;
    }

    public String getUrl5() {
        return url5;
    }

    public String getUrl6() {
        return url6;
    }

    public String getThumb_image() {
        return thumb_image;
    }
//
//    public Solution(){}

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
