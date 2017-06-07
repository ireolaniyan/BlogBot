package com.ire.blogbot.model;

/**
 * Created by ire on 5/24/17.
 */

public class News {
    private String mNews, mTime, mUrl, mImageUrl;

    public News(String news, String time, String url){
        mNews = news;
        mTime = time;
        mUrl = url;
    }

    public String getNews() {
        return mNews;
    }

    public String getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }

//    public String getImageUrl() {
//        return mImageUrl;
//    }

//    COMPLETED: Remove fake model
}
