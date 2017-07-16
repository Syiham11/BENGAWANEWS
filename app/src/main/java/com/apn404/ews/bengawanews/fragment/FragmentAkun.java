package com.apn404.ews.bengawanews.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.helper.SessionManager;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAkun extends Fragment {

    Button logout;
    SessionManager session;
    String nama_user;
    View view;

    public FragmentAkun() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_akun, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        nama_user = user.get(SessionManager.KEY_NAMA_USER);

        TextView status = (TextView) view.findViewById(R.id.status);
        status.setText(Html.fromHtml("Welcome,<b>" + nama_user + "</b>  "));

        logout = (Button) view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                session.logoutUser();
                getActivity().finish();
            }
        });

        return view;
    }
}