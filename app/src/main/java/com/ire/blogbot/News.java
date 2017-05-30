package com.ire.blogbot;

import java.util.ArrayList;

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

/*//    TODO: Remove fake model
    private static int past = 1;

    public static ArrayList<News> newsList(int numberToDisplay){
        ArrayList<News> news = new ArrayList<>();

        for(int i = 0; i < numberToDisplay; i++){
            news.add(new News("News " + past++, "Time " + 1));
        }
        return news;
    }*/
}
