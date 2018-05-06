package com.lawrene.falcon.copycopy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lawrene on 4/26/18.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RecentsFragment recentsFragment = new RecentsFragment();
                return recentsFragment;

            case 1:
                FavoritesFragment favoritesFragment = new FavoritesFragment();
                return favoritesFragment;

            case 2:
                MyUploadsFragment myUploadsFragment = new MyUploadsFragment();
                return myUploadsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "RECENT";

            case 1:
                return "FAVORITES";

            case 2:
                return "MY UPLOADS";

            default:
                return null;
        }
    }
}
