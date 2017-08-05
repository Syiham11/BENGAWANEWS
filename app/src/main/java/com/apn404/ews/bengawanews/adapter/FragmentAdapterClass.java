package com.apn404.ews.bengawanews.adapter;

/**
 * Created by agungeks on 05/08/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.apn404.ews.bengawanews.fragment.FragmentNotif;
import com.apn404.ews.bengawanews.fragment.FragmentSetting;

public class FragmentAdapterClass extends FragmentStatePagerAdapter {

    int TabCount;

    public FragmentAdapterClass(FragmentManager fragmentManager, int CountTabs) {

        super(fragmentManager);

        this.TabCount = CountTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentNotif tab1 = new FragmentNotif();
                return tab1;

            case 1:
                FragmentSetting tab2 = new FragmentSetting();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}