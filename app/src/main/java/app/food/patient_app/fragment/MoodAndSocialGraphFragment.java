package app.food.patient_app.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import app.food.patient_app.R;
import app.food.patient_app.util.Constant;

public class MoodAndSocialGraphFragment extends Fragment {
    View mView;
    private WebView webViewGraph;
    private static final String TAG = "MoodAndSocialGraphFragm";
    private Button btn_Seven_days, btn_Thirty_days, btn_Ninty_days;
    int Day;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mood_and_social_graph, container, false);

        Constant.setSession(getActivity());
        initiliaze();
        Clicked();
        webView(7);
        btn_Seven_days.setBackgroundResource(R.drawable.graph_button_pressed_drawable);
        return mView;
    }


    private void initiliaze() {
        btn_Seven_days = mView.findViewById(R.id.btn_seven_day);
        btn_Thirty_days = mView.findViewById(R.id.btn_thirty_day);
        btn_Ninty_days = mView.findViewById(R.id.btn_ninty_day);
        progressBar = mView.findViewById(R.id.progress_bar_moods_social);
    }

    private void Clicked() {

        btn_Seven_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Seven_days.setBackgroundResource(R.drawable.graph_button_pressed_drawable);
                btn_Thirty_days.setBackgroundResource(R.drawable.days_button_design);
                btn_Ninty_days.setBackgroundResource(R.drawable.days_button_design);
                Day = 7;
                webView(Day);
            }
        });

        btn_Thirty_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Thirty_days.setBackgroundResource(R.drawable.graph_button_pressed_drawable);
                btn_Seven_days.setBackgroundResource(R.drawable.days_button_design);
                btn_Ninty_days.setBackgroundResource(R.drawable.days_button_design);
                Day = 30;
                webView(Day);
            }
        });
        btn_Ninty_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Ninty_days.setBackgroundResource(R.drawable.graph_button_pressed_drawable);
                btn_Seven_days.setBackgroundResource(R.drawable.days_button_design);
                btn_Thirty_days.setBackgroundResource(R.drawable.days_button_design);
                Day = 90;
                webView(Day);
            }
        });
    }

    private void webView(int day) {
        webViewGraph = mView.findViewById(R.id.webViewGraph_mood_social);

        webViewGraph.getSettings().setJavaScriptEnabled(true);
        webViewGraph.getSettings().setLoadWithOverviewMode(true);
        webViewGraph.getSettings().setUseWideViewPort(true);

        webViewGraph.getSettings().setDomStorageEnabled(true);
        webViewGraph.getSettings().setBuiltInZoomControls(true);
        webViewGraph.getSettings().setPluginState(WebSettings.PluginState.ON);
        webViewGraph.getSettings().setBuiltInZoomControls(false);
        webViewGraph.getSettings().setDisplayZoomControls(false);

        webViewGraph.setVerticalScrollBarEnabled(false);
        webViewGraph.setHorizontalScrollBarEnabled(false);
        webViewGraph.setWebViewClient(new HelloWebViewClient());
        Log.e(TAG, "webview Mood and Social Graph URL:- " + Constant.BASE_URL + "get_moods_social?id=" + Constant.mUserId + "&date=" + Constant.currentDate() + "&day=7");
        webViewGraph.loadUrl(Constant.BASE_URL + "get_moods_social?id=" + Constant.mUserId + "&date=" + Constant.currentDate() + "&day=" + day);
    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //Page load finished
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}