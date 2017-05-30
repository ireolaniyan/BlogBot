package com.ire.blogbot;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mNewsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

//    private static final String TECH_NEWS_REQUEST_URL = "https://newsapi.org/v1/articles?source=techcrunch&sortBy=latest&apiKey=3431d57e51a04c1d967e2eb96c99fd1a";

    ArrayList<News> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TODO: SwipeRefresh layout
//        TODO: View pager - tech and gist
//        TODO: Open link after list click event

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

//        TODO: Change to real data
//        news = News.newsList(20);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        updateUI();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI();
                Log.v(LOG_TAG, "Refreshing");
            }
        });

//        COMPLETED: set the adapter on the recycler view
        mNewsAdapter = new NewsAdapter(news);
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
    }

    public void updateUI(){
        URL techNewsUrl = NetworkUtils.buildUrl("techcrunch");
        GetNews getNews = new GetNews();
        getNews.execute(techNewsUrl);
    }

    private class GetNews extends AsyncTask<URL, Void, String>{

        private final String LOG_TAG = MainActivity.class.getSimpleName();

        @Override
        protected String doInBackground(URL... urls) {

            URL techUrl = urls[0];
            String techNewsResults = null;

            try{
                techNewsResults = NetworkUtils.getResponseFromHttpUrl(techUrl);
            }catch (IOException e){
                Log.e(LOG_TAG, "Error fetching data", e);
            }

            return techNewsResults;
        }

        @Override
        protected void onPostExecute(String techNewsResults) {
            if (techNewsResults != null){
                try {
                    NetworkUtils.parseJSON(techNewsResults);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
