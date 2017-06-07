package com.ire.blogbot.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ire.blogbot.R;

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
                return new EntertainmentFragment();
            case 1:
                return new TechFragment();
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