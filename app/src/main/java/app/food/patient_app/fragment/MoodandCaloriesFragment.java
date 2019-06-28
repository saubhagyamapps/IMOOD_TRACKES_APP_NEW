package app.food.patient_app.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import app.food.patient_app.R;
import app.food.patient_app.util.Constant;

public class MoodandCaloriesFragment extends Fragment {
    private static final String TAG = "MoodandCaloriesFragment";
    View mView;
    private WebView webViewGraph;
    private Button btn_Seven_Days, btn_Thirty_Days, btn_Ninty_Days;
    private int Day;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_moodand_calories, container, false);
        initialize();
        Clicked();
        webView(7);
        btn_Seven_Days.setBackgroundResource(R.drawable.graph_button_pressed_drawable);
        return mView;
    }

    private void initialize() {

        btn_Seven_Days = mView.findViewById(R.id.btn_seven_day_moods_calories);
        btn_Thirty_Days = mView.findViewById(R.id.btn_thirty_day_moods_calories);
        btn_Ninty_Days = mView.findViewById(R.id.btn_ninty_day_moods_calories);
        progressBar = mView.findViewById(R.id.progress_bar_moods_calories);
    }

    private void Clicked() {

        btn_Seven_Days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Seven_Days.setBackgroundResource(R.drawable.graph_button_pressed_drawable);
                btn_Ninty_Days.setBackgroundResource(R.drawable.days_button_design);
                btn_Thirty_Days.setBackgroundResource(R.drawable.days_button_design);
                Day = 7;
                webView(Day);
            }
        });
        btn_Thirty_Days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Thirty_Days.setBackgroundResource(R.drawable.graph_button_pressed_drawable);
                btn_Ninty_Days.setBackgroundResource(R.drawable.days_button_design);
                btn_Seven_Days.setBackgroundResource(R.drawable.days_button_design);
                Day = 30;
                webView(Day);
            }
        });

        btn_Ninty_Days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Ninty_Days.setBackgroundResource(R.drawable.graph_button_pressed_drawable);
                btn_Thirty_Days.setBackgroundResource(R.drawable.days_button_design);
                btn_Seven_Days.setBackgroundResource(R.drawable.days_button_design);
                Day = 90;
                webView(Day);
            }
        });
    }
    private void webView(int day) {
        webViewGraph = mView.findViewById(R.id.webViewGraph_moods_calories);

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
        Log.e(TAG, "webview Mood and Social Graph URL:- " + Constant.BASE_URL + "moodandcalories?id=" + Constant.mUserId + "&date=" + Constant.currentDate() + "&day=7");
        webViewGraph.loadUrl(Constant.BASE_URL + "moodandcalories?id=" + Constant.mUserId + "&date=" + Constant.currentDate() + "&day="+day);
    }
    //http://charmhdwallpapers.com/patient_app/moodandcalories?id=19&date=2019-05-16&day=30

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
