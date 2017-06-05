package com.ire.blogbot;

import android.content.Context;
import android.os.Bundle;

import com.ire.blogbot.model.News;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ire on 5/31/17.
 */

public class NewsLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<News>> {
//    The Loader takes in a bundle
    Bundle sourceBundle = new Bundle();
    private static final String NEWS_SOURCE = "entertainment-weekly";

    public NewsLoader(Context context, final Bundle args){
        super(context);
        sourceBundle = args;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        ArrayList<News> news = null;
        try {
            news = EntertainmentNetworkUtils.parseJSON(NEWS_SOURCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }
}
