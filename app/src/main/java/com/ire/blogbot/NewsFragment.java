package com.ire.blogbot;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NewsFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mErrorMessage;
    //    private ProgressBar mLoadingIndicator;
    ArrayList<News> news = null;
    NetworkInfo info;
    String source;
    //    The Loader takes in a bundle
    Bundle sourceBundle = new Bundle();

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String ENTERTAINMENT_NEWS_SOURCE = "entertainment-weekly";
    private static final int ENTERTAINMENT_NEWS_LOADER = 21;

    private RecyclerView mRecyclerView;


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mErrorMessage = (TextView) view.findViewById(R.id.tv_error_message);
//        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);



//        COMPLETED: Change to real data
        updateUI();
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(CONNECTIVITY_SERVICE);

        info = cm.getActiveNetworkInfo();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v(LOG_TAG, "Refreshing");
                updateUI();
//                getSupportLoaderManager().restartLoader(TECH_NEWS_LOADER, sourceBundle, new NewsDataLoader());
            }
        });

        return view;
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
//                    URL techNewsUrl = TechNetworkUtils.buildUrl(NEWS_SOURCE);
                    URL entertainmentNewsUrl = NetworkUtils.buildUrl();
                    sourceBundle.putString("source", entertainmentNewsUrl.toString());

                    getLoaderManager().initLoader(ENTERTAINMENT_NEWS_LOADER, sourceBundle, new NewsDataLoader());
                }
                mSwipeRefreshLayout.setRefreshing(false);
                Log.v(LOG_TAG, "Finished refreshing");

                mErrorMessage.setVisibility(View.VISIBLE);
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                mErrorMessage.setText(getString(R.string.internet_error));
            }
        }, 5000);

    }


    public class NewsDataLoader implements LoaderManager.LoaderCallbacks<ArrayList<News>> {
        private NewsAdapter mNewsAdapter;

        @Override
        public Loader<ArrayList<News>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<ArrayList<News>>(getActivity()) {
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
