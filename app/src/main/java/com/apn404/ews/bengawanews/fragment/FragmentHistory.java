package com.apn404.ews.bengawanews.fragment;
import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apn404.ews.bengawanews.R;

public class FragmentHistory extends Fragment {
    WebView web;
    View view;

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
        web.loadUrl("http://banjir.mitigasi.com/banjir/dasbor/jejak");

        return view;
    }

/*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_history);


    }
*/

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