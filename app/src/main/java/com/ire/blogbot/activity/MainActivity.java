package com.ire.blogbot.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ire.blogbot.R;
import com.ire.blogbot.fragments.NewsFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

//    private static final String TECH_NEWS_REQUEST_URL = "https://newsapi.org/v1/articles?source=techcrunch&sortBy=latest&apiKey=3431d57e51a04c1d967e2eb96c99fd1a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        COMPLETED: SwipeRefresh layout
//        COMPLETED: View pager - tech and gist
//        COMPLETED: Convert time to readable text
//        TODO: Change font of BlogBot and tab titles
//        TODO: Open link after list click
//        TODO: add additional urls
//        COMPLETED: add images of news
//        TODO: Show * notification on viewpager titles

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        NewsFragmentPagerAdapter newsFragmentPagerAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), this);



        viewPager.setAdapter(newsFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
