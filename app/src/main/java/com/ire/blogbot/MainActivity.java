package com.ire.blogbot;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mErrorMessage;
//    private ProgressBar mLoadingIndicator;
    ArrayList<News> news = null;
    NetworkInfo info;
    String source;
//    The Loader takes in a bundle
    Bundle sourceBundle = new Bundle();

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String NEWS_SOURCE = "techcrunch";
    private static final int TECH_NEWS_LOADER = 22;

    private RecyclerView mRecyclerView;

//    private static final String TECH_NEWS_REQUEST_URL = "https://newsapi.org/v1/articles?source=techcrunch&sortBy=latest&apiKey=3431d57e51a04c1d967e2eb96c99fd1a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TODO: SwipeRefresh layout
//        TODO: View pager - tech and gist
//        TODO: Open link after list click event
//        TODO: add images of news

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
//        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);

//        COMPLETED: Change to real data
        updateUI();
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(CONNECTIVITY_SERVICE);

        info = cm.getActiveNetworkInfo();

//            NewsLoader newsLoader = new NewsLoader(MainActivity.this, args);

//        return null;
//        getSupportLoaderManager().restartLoader(TECH_NEWS_LOADER, sourceBundle, new NewsDataLoader());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v(LOG_TAG, "Refreshing");
                updateUI();
//                getSupportLoaderManager().restartLoader(TECH_NEWS_LOADER, sourceBundle, new NewsDataLoader());
            }
        });
    }

    public void updateUI() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (info != null && info.isConnectedOrConnecting()) {
                    mErrorMessage.setVisibility(View.INVISIBLE);
//                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);

//                    source = "techcrunch";
//                    URL techNewsUrl = NetworkUtils.buildUrl(NEWS_SOURCE);
                    URL techNewsUrl = NetworkUtils.buildUrl();
                    sourceBundle.putString("source", techNewsUrl.toString());

                    getSupportLoaderManager().initLoader(TECH_NEWS_LOADER, sourceBundle, new NewsDataLoader());
                }
                mSwipeRefreshLayout.setRefreshing(false);
                Log.v(LOG_TAG, "Finished refreshing");

                mErrorMessage.setVisibility(View.VISIBLE);
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                mErrorMessage.setText(getString(R.string.internet_error));
            }
        }, 5000);

      /*  URL techNewsUrl = NetworkUtils.buildUrl(NEWS_SOURCE);
//        Log.i(LOG_TAG, "techNewsUrl: " + techNewsUrl.toString());
//        GetNews getNews = new GetNews();
//        getNews.execute(techNewsUrl);
        source = "techcrunch";
        sourceBundle.putString("source", source);
        if (techNewsUrl == null){
            getSupportLoaderManager().initLoader(TECH_NEWS_LOADER, sourceBundle, new NewsDataLoader());
        }else{
            getSupportLoaderManager().restartLoader(TECH_NEWS_LOADER, sourceBundle, new NewsDataLoader());
        }*/
    }

    public class NewsDataLoader implements LoaderManager.LoaderCallbacks<ArrayList<News>> {
        private NewsAdapter mNewsAdapter;

        @Override
        public Loader<ArrayList<News>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<ArrayList<News>>(MainActivity.this) {
                //    The Loader takes in a bundle
                Bundle sourceBundle = new Bundle();

                @Override
                protected void onStartLoading() {
                    forceLoad();
                }

                @Override
                public ArrayList<News> loadInBackground() {
                    ArrayList<News> news = null;
                    try {
                        news = NetworkUtils.parseJSON();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return news;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
//            mLoadingIndicator.setVisibility(View.GONE);
            if (data != null) {
                if (news != null) {
                    news.clear();
                    news.addAll(data);
                    if(mNewsAdapter != null) {
                        mNewsAdapter.notifyDataSetChanged();
                    }
                } else {
                    news = data;
                }
                mNewsAdapter = new NewsAdapter(news);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(loader.getContext()));
                mRecyclerView.setAdapter(mNewsAdapter);
            } else {
                mErrorMessage.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                mErrorMessage.setText(getString(R.string.internet_error));
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<News>> loader) {
            loader.forceLoad();
        }

    }

}
