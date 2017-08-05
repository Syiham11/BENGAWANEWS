package com.apn404.ews.bengawanews.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.adapter.FragmentAdapterClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotifikasi extends Fragment {

    View view;
    TabLayout tabLayout ;
    ViewPager viewPager ;
    FragmentAdapterClass fragmentAdapter ;

    public FragmentNotifikasi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notifikasi, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) view.findViewById(R.id.pager1);

        tabLayout.addTab(tabLayout.newTab().setText("List Lokasi"));
        tabLayout.addTab(tabLayout.newTab().setText("Notifikasi Aktif"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        fragmentAdapter = new FragmentAdapterClass(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {
                viewPager.setCurrentItem(LayoutTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {
                viewPager.setCurrentItem(LayoutTab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {
                viewPager.setCurrentItem(LayoutTab.getPosition());
            }
        });

        return view;
    }

}
