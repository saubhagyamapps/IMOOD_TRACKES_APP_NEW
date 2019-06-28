package app.food.patient_app.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import app.food.patient_app.R;
import app.food.patient_app.adapter.StepDisplayAdepter;
import app.food.patient_app.model.StepDisplayModel;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepCountGraphFragment extends Fragment {

    private static final String TAG = "StepCountGraphFragment";
    View mView;
    WebView webViewGraph;
    RecyclerView recyclerViewStepDisplay;
    StepDisplayAdepter stepDisplayAdepter;
    RecyclerView.LayoutManager layoutManager;
    TextView txtTotalStep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_step_count_graph, container, false);
        Constant.setSession(getActivity());
        initialization();
        return mView;
    }

    private void initialization() {
        recyclerViewStepDisplay = mView.findViewById(R.id.recyclerViewStepDisplay);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewStepDisplay.setLayoutManager(layoutManager);
        webViewGraph = mView.findViewById(R.id.webViewStepGraph);
        txtTotalStep = mView.findViewById(R.id.txtTotalStep);
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
        Log.e(TAG, "webview Graph URL:- " + Constant.BASE_URL + "stepchart?id=" + Constant.mUserId + "&date=" + Constant.currentDate()+"&day=7");
        webViewGraph.loadUrl(Constant.BASE_URL + "stepchart?id=" + Constant.mUserId + "&date=" + Constant.currentDate()+"&day=7");

        stepDataSetAPICALL();
    }

    private void stepDataSetAPICALL() {
        Call<StepDisplayModel> modelCall = Constant.apiService.setStepData(Constant.mUserId, Constant.currentDate());
        modelCall.enqueue(new Callback<StepDisplayModel>() {
            @Override
            public void onResponse(Call<StepDisplayModel> call, Response<StepDisplayModel> response) {
                Log.e(TAG, "onResponse: " );
                if (response.body().getStatus() == 0) {
                    txtTotalStep.setText(String.valueOf(response.body().getTotal_foot_step())+" steps");
                    stepDisplayAdepter = new StepDisplayAdepter(getActivity(), response.body().getFoot_step_data());
                    recyclerViewStepDisplay.setHasFixedSize(true);
                    stepDisplayAdepter.notifyDataSetChanged();
                    recyclerViewStepDisplay.setAdapter(stepDisplayAdepter);
                }

            }

            @Override
            public void onFailure(Call<StepDisplayModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " );

            }
        });
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
            //Page load finished
            super.onPageFinished(view, url);

        }
    }
}