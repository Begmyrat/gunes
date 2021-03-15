package com.fabrika.gunes;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private ArrayList<Fragment> fragmentList;

    public PagerAdapter(Context context, FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        mContext = context;
        this.fragmentList = fragmentList;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    // This determines the title for each tab
//    @Override
//    public CharSequence getPageTitle(int position) {
//        // Generate title based on item position
//        switch (position) {
//            case 0:
//                return "1";
//            case 1:
//                return "2";
//            case 2:
//                return "2";
//            case 3:
//                return "3";
//            default:
//                return null;
//        }
//    }
}
