package com.bondhan.research.main;

import android.content.Context;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bondhan.research.common.Util;
import com.bondhan.research.gp.GpUtil;
import com.bondhan.research.gp.Iso7816;
import com.example.android.common.logger.Log;

import gp.lab.bondhan.com.lazyglobalplatform.R;


public class CardInfoSubFragment extends Fragment {

    public static final String TAG = "Card Info:";

    private ICommuncation mCallback;
    private EditText info1, info2, info3, info4, info5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sub_fragment_card_info, container, false);
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

        info1 = (EditText) view.findViewById(R.id.info1);
        info2 = (EditText) view.findViewById(R.id.info2);
        info3 = (EditText) view.findViewById(R.id.info3);
        info4 = (EditText) view.findViewById(R.id.info4);
        info5 = (EditText) view.findViewById(R.id.info5);
    }

    public void updateInfo(Iso7816.Tag IsoDepCard, IsoDep isodep)
    {
        String cmd = null;
        if (IsoDepCard != null && IsoDepCard.isConnected())
        {
            byte[] value = null;
            if ((value = isodep.getHistoricalBytes()) != null)
            {
                info1.setText("Type A Card!");
                info2.setText("UID = " + Util.htos(isodep.getTag().getId()));
                info3.setText("Historical bytes: " + ((value == null) ? "None" : Util.htos(value)));

                NfcA nfcaCard = NfcA.get(isodep.getTag());

                info4.setText("ATQA Info: " + Util.htos(nfcaCard.getAtqa()));
                info5.setText("SAK: " + nfcaCard.getSak());
            }
            else if ((value = isodep.getHiLayerResponse()) == null)
            {
                info1.setText("Type B Card!");
                info2.setText("UID = " + Util.htos(isodep.getTag().getId()));
                info3.setText("Hi Layer bytes: " + ((value == null) ? "None" : Util.htos(value)));

                NfcB nfcbCard = NfcB.get(isodep.getTag());

                info4.setText("Protocol Info: " + Util.htos(nfcbCard.getProtocolInfo()));
                info5.setText("Application Data: " + nfcbCard.getApplicationData());
            }

        }
    }

}
