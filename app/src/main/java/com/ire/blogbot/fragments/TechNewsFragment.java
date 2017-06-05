package com.ire.blogbot.fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.ire.blogbot.activity.MainActivity;
import com.ire.blogbot.model.News;
import com.ire.blogbot.NewsAdapter;
import com.ire.blogbot.R;
import com.ire.blogbot.TechNetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class TechNewsFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mErrorMessage;
    private NewsAdapter mNewsAdapter;
    ArrayList<News> news;
    NetworkInfo info;
    //    The Loader takes in a bundle
    Bundle sourceBundle = new Bundle();

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String TECH_NEWS_QUERY_URL = "query";
    private static final String TECH_NEWS_SOURCE = "techcrunch";
    private static final int TECH_NEWS_LOADER = 22;

    private RecyclerView mRecyclerView;


    public TechNewsFragment() {
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


//        COMPLETED: Change to real data/

        getActivity().getSupportLoaderManager().initLoader(TECH_NEWS_LOADER, null, new NewsDataLoader());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v(LOG_TAG, "Refreshing");
                restartLoader();
                mSwipeRefreshLayout.setColorSchemeResources(
                        R.color.colorPrimary,
                        R.color.colorPrimaryDark);
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

    private int anyRandomInt(Random random) {
        return random.nextInt();
    }

    private void restartLoader() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                URL techNewsUrl = TechNetworkUtils.buildUrl(TECH_NEWS_SOURCE);
                sourceBundle.putString(TECH_NEWS_QUERY_URL, techNewsUrl.toString());

                Random random = new Random();
                int uniqueId = anyRandomInt(random); //Generates a new ID for each loader call;

//                    sourceBundle.putString("query", s);]

                LoaderManager loaderManager = getActivity().getSupportLoaderManager();

                if (loaderManager.getLoader(TECH_NEWS_LOADER) == null) {
                    loaderManager.initLoader(uniqueId, sourceBundle, new NewsDataLoader());
                } else {
                    loaderManager.restartLoader(TECH_NEWS_LOADER, sourceBundle, new
                            NewsDataLoader());
                }
            }
        }, 5000);
        mSwipeRefreshLayout.setRefreshing(false);
        Log.v(LOG_TAG, "Finished refreshing");
    }

    private void showErrorScreen(){
        mErrorMessage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(getString(R.string.internet_error));
    }

    public class NewsDataLoader implements LoaderManager.LoaderCallbacks<ArrayList<News>> {


        @Override
        public Loader<ArrayList<News>> onCreateLoader(int id, final Bundle args) {
            if (isConnected()){
                mErrorMessage.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                return new AsyncTaskLoader<ArrayList<News>>(getActivity()) {
                    ArrayList<News> mNewsData = null;

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        if (mNewsData != null){
                            deliverResult(mNewsData);
                        }else{
                            forceLoad();
                            mSwipeRefreshLayout.setRefreshing(true);
                        }
                    }

                    @Override
                    public ArrayList<News> loadInBackground() {
                        String techNewsUrlString = args.getString(TECH_NEWS_QUERY_URL);
                        if (techNewsUrlString == null){
                            return null;
                        }
                        try {
//                            URL techNewsUrl = new URL(techNewsUrlString);
                            return TechNetworkUtils.parseJSON(TECH_NEWS_SOURCE);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    public void deliverResult(ArrayList<News> data) {
                        mNewsData = data;
                        super.deliverResult(data);
                    }
                };
            }else{
                showErrorScreen();
                return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> data) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (null == data) {
                showErrorScreen();
            } else {
                mErrorMessage.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                if (news != null) {
                    news.clear();
                    news.addAll(data);
                    mNewsAdapter = new NewsAdapter(news);
                    mRecyclerView.setAdapter(mNewsAdapter);
                } else {
                    news = data;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<News>> loader) {
//            loader.forceLoad();
        }

    }

}
