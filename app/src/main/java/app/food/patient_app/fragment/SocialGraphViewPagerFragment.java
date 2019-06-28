package app.food.patient_app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.github.florent37.bubbletab.BubbleTab;

import app.food.patient_app.R;
import app.food.patient_app.adapter.ViewPagerAdapter;
import app.food.patient_app.util.Constant;


public class SocialGraphViewPagerFragment extends Fragment {
    View mView;

    private Toolbar toolbar;
    private BubbleTab tabLayout;

    private static final String TAG = "SocialGraphViewPagerFra";
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
        viewPager.setOffscreenPageLimit(1);
        setupViewPager(viewPager);

        tabLayout = mView.findViewById(R.id.tabs);


        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity());
        adapter.addFragment(new SocialGraphFragment(), "Week");
        adapter.addFragment(new SocialGraphTwoFragment(), "Month");
        adapter.addFragment(new SocialGraphThreeFragment(), "3 Month");
        viewPager.setAdapter(adapter);
    }

}
