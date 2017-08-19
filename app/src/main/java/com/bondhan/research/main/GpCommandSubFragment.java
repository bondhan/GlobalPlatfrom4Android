package com.bondhan.research.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bondhan.research.gp.Iso7816;

import gp.lab.bondhan.com.lazyglobalplatform.R;

/**
 * Created by bondhan on 19/08/17.
 */

public class GpCommandSubFragment extends Fragment {

    public static final String TAG = "Card Content:";

    private ICommuncation mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sub_fragment_gp_command, container, false);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //hook the handler for the buttons
        final Button authBtn = (Button) getView().findViewById(R.id.extAuthBtn);
        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Iso7816.Tag IsoDepCard = ((MainActivity) getActivity()).getIsodepCard();

                Iso7816.Response resp = null;
                String cmd = null;

                mCallback.appendLogMessage("Auth Btn");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        final MenuItem logToggle = menu.findItem(R.id.action_toggle_log);
        logToggle.setVisible(false);

        ((MainActivity) getActivity()).showOutputLog(true);

//        LinearLayout output = (LinearLayout) getView().findViewById(R.id.log_window_layout);
//        output.setVisibility(LinearLayout.VISIBLE);
    }
}
