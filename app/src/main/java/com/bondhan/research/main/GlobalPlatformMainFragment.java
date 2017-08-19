package com.bondhan.research.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

import gp.lab.bondhan.com.lazyglobalplatform.R;

public class GlobalPlatformMainFragment extends Fragment {

    private String TAG = "GPMF:";
    private GlobalPlatformMainPagerAdapter adapter;
    private TextView logView;
    private ScrollView scrollView;
    private ICommuncation mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "On onCreateView was called");
        return inflater.inflate(R.layout.main_global_platform_fragment, container, false);
    }


    public GlobalPlatformMainPagerAdapter getGpAdapter() {
        return adapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (ICommuncation) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ICommuncation");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        Log.d(TAG, "On onViewCreated was called");

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2_title));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new GlobalPlatformMainPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        scrollView = (ScrollView) getView().findViewById(R.id.log_scrollView);
        logView = (TextView) view.findViewById(R.id.log_textview);
        logView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "On start was called");
        initializeLogging();
    }

    private void initializeLogging()
    {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = new LogFragment();
        msgFilter.setNext(logFragment.getLogView());

    }

    public void doAppendLog(String text)
    {
        if (logView != null) {
            logView.append(text + "\n");
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        } else
            Log.d(TAG, "logView is null");
    }

    public String getAllLogMsg()
    {
        String msg = null;

        if (logView != null) {
            msg = logView.getText().toString();
        } else
            Log.d(TAG, "logView is null");

        return msg;
    }

    public void loadHistory(String msg)
    {
        if (logView != null)
        {
            logView.setText(msg);
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

}
