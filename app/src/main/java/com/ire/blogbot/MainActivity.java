package com.ire.blogbot;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

//    private static final String TECH_NEWS_REQUEST_URL = "https://newsapi.org/v1/articles?source=techcrunch&sortBy=latest&apiKey=3431d57e51a04c1d967e2eb96c99fd1a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TODO: SwipeRefresh layout
//        TODO: View pager - tech and gist
//        TODO: Open link after list click event
//        TODO: add images of news

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        NewsFragmentPagerAdapter newsFragmentPagerAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(newsFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
