package app.food.patient_app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.github.florent37.bubbletab.BubbleTab;

import app.food.patient_app.R;
import app.food.patient_app.adapter.ViewPagerAdapter;
import app.food.patient_app.util.Constant;


public class MoodGraphFragment extends Fragment {
    View mView;

    private Toolbar toolbar;
    // private TabLayout tabLayout;
    private BubbleTab tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.moodgraph_fragment, container, false);
        Constant.setSession(getActivity());
        initialization();
        return mView;
    }

    private void initialization() {
        viewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = mView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(),getActivity());
        adapter.addFragment(new LineChartFragment(), "Week");
        adapter.addFragment(new LineChartFragment(), "Month");
        adapter.addFragment(new LineChartFragment(), "3 Month");
        viewPager.setAdapter(adapter);
    }

}
