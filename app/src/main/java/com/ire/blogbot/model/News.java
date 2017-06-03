package com.ire.blogbot.model;

/**
 * Created by ire on 5/24/17.
 */

public class News {
    private String mNews;
    private String mTime;
//    private String mImageUrl;

    /*public News(String news, String time, String imageUrl){
        mNews = news;
        mTime = time;
        mImageUrl = imageUrl;
    }*/

    public News(String news, String time){
        mNews = news;
        mTime = time;
    }

    public String getNews() {
        return mNews;
    }

    public String getTime() {
        return mTime;
    }

//    public String getImageUrl() {
//        return mImageUrl;
//    }

//    COMPLETED: Remove fake model
}
