package com.ire.blogbot.fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ire.blogbot.NewsLoader;
import com.ire.blogbot.activity.MainActivity;
import com.ire.blogbot.NetworkUtils;
import com.ire.blogbot.model.News;
import com.ire.blogbot.NewsAdapter;
import com.ire.blogbot.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class EntertainmentNewsFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mErrorMessage;
    ArrayList<News> news = null;
    NetworkInfo info;
    //    The Loader takes in a bundle
    Bundle sourceBundle = new Bundle();

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String ENTERTAINMENT_NEWS_SOURCE = "entertainment-weekly";
    private static final int ENTERTAINMENT_NEWS_LOADER = 21;

    private RecyclerView mRecyclerView;

    public EntertainmentNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mErrorMessage = (TextView) view.findViewById(R.id.tv_error_message);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

//        COMPLETED: Change to real data

        getLoaderManager().initLoader(ENTERTAINMENT_NEWS_LOADER, sourceBundle, new NewsDataLoader());

        updateUI();

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

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(CONNECTIVITY_SERVICE);

        info = cm.getActiveNetworkInfo();

        return info != null && info.isConnectedOrConnecting();
    }

    public void updateUI() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        URL entertainmentNewsUrl = NetworkUtils.buildUrl(ENTERTAINMENT_NEWS_SOURCE);
        sourceBundle.putString("source", entertainmentNewsUrl.toString());

        mSwipeRefreshLayout.setRefreshing(false);
        Log.v(LOG_TAG, "Finished refreshing");
    }

    private void showErrorScreen(){
        mErrorMessage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(getString(R.string.internet_error));
    }

    public class NewsDataLoader implements LoaderManager.LoaderCallbacks<ArrayList<News>> {
        private NewsAdapter mNewsAdapter;

        @Override
        public Loader<ArrayList<News>> onCreateLoader(int id, final Bundle args) {
            if (isConnected()){
                updateUI();
                return new NewsLoader(getActivity(), args);
            }
            showErrorScreen();
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
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
                showErrorScreen();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<News>> loader) {
            loader.forceLoad();
        }

    }

}
