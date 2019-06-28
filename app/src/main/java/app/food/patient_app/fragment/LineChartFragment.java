package app.food.patient_app.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.model.GetWeeklyPercentageModel;
import app.food.patient_app.model.MoodTrackerBarChartModel;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.mikephil.charting.utils.ColorTemplate.LIBERTY_COLORS;
import static com.github.mikephil.charting.utils.ColorTemplate.createColors;


public class LineChartFragment extends Fragment {
    View mView;

    BarChart barChart;
    ArrayList<BarEntry> entries;
    WebView webViewGraph;
    private static final String TAG = "LineChartFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_chart_common, container, false);
        Constant.setSession(getActivity());
     //   Constant.progressDialog(getActivity());
        weview();
        return mView;
    }

    private void weview() {
        webViewGraph = (WebView) mView.findViewById(R.id.webViewGraph);
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
        Log.e(TAG, "webview Graph URL:- " + Constant.BASE_URL+"linechartmoodmonthly?id=" + Constant.mUserId + "&date=" + Constant.currentDate());
        webViewGraph.loadUrl(Constant.BASE_URL+"linechartmoodmonthly?id=" + Constant.mUserId + "&date=" + Constant.currentDate());

     //   Constant.progressBar.dismiss();

    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
           // Constant.progressDialog(getActivity());
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
         //   Constant.progressBar.dismiss();
        }
    }

}
