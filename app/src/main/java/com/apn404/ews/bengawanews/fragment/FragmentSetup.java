package com.apn404.ews.bengawanews.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apn404.ews.bengawanews.LoginActivity;
import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.helper.SessionManager;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSetup extends Fragment {
    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;
    View view;
    public FragmentSetup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setup, container, false);

        //list tab
        simpleFrameLayout = (FrameLayout) view.findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) view.findViewById(R.id.simpleTabLayout);
// Create a new Tab named "First"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Notifikasi Aktif"); // set the Text for the first Tab
        //firstTab.setIcon(R.drawable.ic_action_home); // set an icon for the
// first tab
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
// Create a new Tab named "Second"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("List Lokasi"); // set the Text for the second Tab
        //secondTab.setIcon(R.drawable.ic_action_notif); // set an icon for the second tab
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout

        // perform setOnTabSelectedListener event on TabLayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new FragmentSetting();
                        break;
                    case 1:
                        fragment = new FragmentNotif();
                        break;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
// end tab

        return view;
    }//end create

}
