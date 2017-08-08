package com.apn404.ews.bengawanews.fragment;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.apn404.ews.bengawanews.R;

public class FragmentHistory extends Fragment {
    WebView web;
    View view;
    TextView textView;

    public FragmentHistory() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_history, container, false);

        web = (WebView) view.findViewById(R.id.web);
        web.setWebViewClient(new FragmentHistory.myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("http://ews.apn404.com/TA/dasbor/jejakA");
        TextView textView = (TextView) view.findViewById(R.id.kuni);

        textView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return view;
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setTitle("Sekilas Info !");
        alertDialogBuilder
                .setMessage("Data diambil dari server pusat")
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
           super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }
    }
}