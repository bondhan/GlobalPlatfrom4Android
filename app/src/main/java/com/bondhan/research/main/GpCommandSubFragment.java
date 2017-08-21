package com.bondhan.research.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

        final EditText cla_editText = (EditText) view.findViewById(R.id.cla_editText);
        final EditText ins_editText = (EditText) view.findViewById(R.id.ins_editText);
        final EditText p1_editText = (EditText) view.findViewById(R.id.p1_editText);
        final EditText p2_editText = (EditText) view.findViewById(R.id.p2_editText);
        final EditText len_editText = (EditText) view.findViewById(R.id.len_editText);
        final EditText data_cmd = (EditText) view.findViewById(R.id.data_cmd);

        cla_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 2)
                    ins_editText.requestFocus();
            }
        });

        ins_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 2)
                    p1_editText.requestFocus();
            }
        });

        p1_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 2)
                    p2_editText.requestFocus();
            }
        });

        p2_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 2)
                    data_cmd.requestFocus();
            }
        });

        data_cmd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                len_editText.setText(String.format("%02X", s.length() / 2));
            }
        });

        data_cmd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    sendCommand();

                    return true;
                }
                return false;
            }
        });

        final Button sendBtn = (Button) getView().findViewById(R.id.sendCmdBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendCommand();
            }
        });

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

    public void sendCommand() {
        mCallback.appendLogMessage("Send Cmd");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        final MenuItem logToggle = menu.findItem(R.id.action_toggle_log);
        logToggle.setVisible(false);

        ((MainActivity) getActivity()).showOutputLog(true);
    }
}
