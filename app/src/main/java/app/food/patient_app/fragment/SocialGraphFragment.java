package app.food.patient_app.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.food.patient_app.R;
import app.food.patient_app.util.Constant;


public class SocialGraphFragment extends Fragment {
    View mView;
    WebView webViewGraph;
    private static final String TAG = "SocialGraphFragment";
    boolean Flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_social_graph, container, false);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialization();
    }

    private void initialization() {
        webViewGraph = mView.findViewById(R.id.webViewSoicalGraph);
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

        Log.e(TAG, "webview Graph URL:- " + Constant.BASE_URL + "barchart?id=" + Constant.mUserId + "&date=" + Constant.currentDate() + "&day=7");
        webViewGraph.loadUrl(Constant.BASE_URL + "barchart?id=" + Constant.mUserId + "&date=" + Constant.currentDate() + "&day=7");
        Flag = false;
    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

        }
    }

}
