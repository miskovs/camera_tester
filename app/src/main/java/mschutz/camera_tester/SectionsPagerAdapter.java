package mschutz.camera_tester;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private int count;

    public SectionsPagerAdapter(FragmentManager fm, int pagesCount) {
        super(fm);
        count = pagesCount;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new LogicalCameraFragment();
        Bundle args = new Bundle();
        args.putInt("cameraId", i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }
}