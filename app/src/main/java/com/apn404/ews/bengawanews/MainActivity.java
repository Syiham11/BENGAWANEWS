package com.apn404.ews.bengawanews;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.apn404.ews.bengawanews.fragment.FragmentAkun;
import com.apn404.ews.bengawanews.fragment.FragmentBeranda;
import com.apn404.ews.bengawanews.fragment.FragmentHistory;
import com.apn404.ews.bengawanews.fragment.FragmentMaps;
import com.apn404.ews.bengawanews.fragment.FragmentNotifikasi;

public class MainActivity extends AppCompatActivity {

    //private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.bottom_navigation_items);
        fragmentManager = getSupportFragmentManager();

        //untuk inisialisasi fragment pertama kali
        fragmentManager.beginTransaction().replace(R.id.main_container,new FragmentBeranda()).commit();
        //memberikan listener saat menu item di bottom diklik
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.menu_maps:
                        fragment = new FragmentMaps();
                        break;
                    case R.id.menu_history:
                        fragment = new FragmentHistory();
                        break;
                    case R.id.menu_home:
                        fragment = new FragmentBeranda();
                        break;
                    case R.id.menu_notif:
                        fragment = new FragmentNotifikasi();
                        break;
                    case R.id.menu_akun:
                        fragment = new FragmentAkun();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container,fragment).commit();

                return true;
            }
        });

    }
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Keluar Aplikasi ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

}
