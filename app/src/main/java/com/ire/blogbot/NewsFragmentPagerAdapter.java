package com.ire.blogbot;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public NewsFragmentPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NewsFragment();
            case 1:
                return new TechNewsFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.entertainmet_page_title);
            case 1:
                return mContext.getString(R.string.tech_page_title);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}