package com.example.dell.letschat;

import android.app.DownloadManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by DELL on 11/11/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:

            case 1:

            case 2: Friends friends=new Friends();
                return friends;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    public CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0:


            case 1:


            case 2:
                return "FRIENDS";

                default:
                return null;
        }
    }
}
