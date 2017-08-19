package com.bondhan.research.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bondhan.research.dao.ApduModeEnum;
import com.bondhan.research.dao.DaoSession;
import com.bondhan.research.dao.GlobalPlatformCard;
import com.bondhan.research.dao.GlobalPlatformCardDao;
import com.bondhan.research.dao.GpApp;
import com.bondhan.research.dao.KeyDerivationModeEnum;
import com.bondhan.research.dao.ScpModeEnum;
import com.bondhan.research.gp.GpUtil;
import com.bondhan.research.gp.Iso7816;
import com.example.android.common.logger.Log;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gp.lab.bondhan.com.lazyglobalplatform.R;

public class SetupGpSubFragment extends Fragment {

    private String TAG = "GPSF:";

    private GlobalPlatformCardDao GpCardDao;

    /** Adapters for our spinners */
    ArrayAdapter<String> scpModeDataAdapter;
    ArrayAdapter<String> apduModeDataAdapter;
    ArrayAdapter<String> kdvModeDataAdapter;

    private EditText cardNameEditText;
    private EditText cardManagerAidEditText;
    private EditText kmcAcEditText;
    private EditText kmcMacEditText;
    private EditText kmcDekEditText;
    private Spinner kdvModeSpinner;
    private Spinner scpModeSpinner;
    private Spinner apduModeSpinner;
    private Spinner listOfCardsSpinner;

    private ICommuncation mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sub_fragment_setup_gp, container, false);
    }

    private void updateCardProfileList(boolean selectTheLatest)
    {
        // get the GP Dao
        DaoSession daoSes =  ((GpApp) getActivity().getApplication()).getDaoSession();
        GpCardDao = daoSes.getGlobalPlatformCardDao();

        //query all notes, sorted a-z by their text
        Query<GlobalPlatformCard> gpCardQuery = GpCardDao.queryBuilder().orderAsc(GlobalPlatformCardDao.Properties.Last_modified_date).build();
        List<GlobalPlatformCard> cardProfiles = gpCardQuery.list();

        List<String> cardList = new ArrayList<String>();
        for (GlobalPlatformCard card: cardProfiles)
        {
            cardList.add(card.getCardName());
        }

        ArrayAdapter<String> cardListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cardList);
        cardListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfCardsSpinner.setAdapter(cardListAdapter);

        //Let us load the last selected value or data with the latest modified date
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = cardListAdapter.getCount()-1;
        int index = sharedPref.getInt(getString(R.string.last_selected_data), defaultValue);

        //Load either the last one selected or the latest on modified
        listOfCardsSpinner.setSelection(selectTheLatest ? cardListAdapter.getCount()-1 : index);
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
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        cardNameEditText = (EditText) view.findViewById(R.id.CardNameTextEdit);
        kmcAcEditText = (EditText) view.findViewById(R.id.kmcAcTextEdit);
        kmcMacEditText = (EditText) view.findViewById(R.id.kmcMacTextEdit);
        kmcDekEditText = (EditText) view.findViewById(R.id.kmcDekTextEdit);
        cardManagerAidEditText = (EditText) view.findViewById(R.id.cmAidEditText);

        //populate the spinner for SCP Mode
        kdvModeSpinner = (Spinner) view.findViewById(R.id.kdvModeSpinner);
        List<String> kdvModeList = new ArrayList<String>();
        for (KeyDerivationModeEnum s:KeyDerivationModeEnum.values())
        {
            kdvModeList.add(KeyDerivationModeEnum.getStringValue(s));
        }
        kdvModeDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,kdvModeList);
        kdvModeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kdvModeSpinner.setAdapter(kdvModeDataAdapter);

        //populate the spinner for SCP Mode
        scpModeSpinner = (Spinner) view.findViewById(R.id.scpModeSpinner);
        List<String> scpModeList = new ArrayList<String>();
        for (ScpModeEnum s:ScpModeEnum.values())
        {
            scpModeList.add(ScpModeEnum.getStringValue(s));
        }

        scpModeDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,scpModeList);
        scpModeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scpModeSpinner.setAdapter(scpModeDataAdapter);

        //populate the spinner APDU Mode
        apduModeSpinner = (Spinner) view.findViewById(R.id.apduModeSpinner);
        List<String> apduModeList = new ArrayList<String>();
        for (ApduModeEnum s:ApduModeEnum.values())
        {
            apduModeList.add(ApduModeEnum.getStringValue(s));
        }

        apduModeDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, apduModeList);
        apduModeDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        apduModeSpinner.setAdapter(apduModeDataAdapter);

        //handle note save as button
        Button saveAsButton = (Button) view.findViewById(R.id.saveAsBtn);
        saveAsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveAsButtonClick(v);
            }
        });

        //handle note save as button
        Button deleteButton = (Button) view.findViewById(R.id.deleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteButtonClick(v);
            }
        });

        //handle update a card profile
        Button updateButton = (Button) view.findViewById(R.id.updateBtn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateButtonClick(v);
            }
        });

        listOfCardsSpinner = (Spinner)view.findViewById(R.id.cardProfileListSpinner);
        listOfCardsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = parent.getItemAtPosition(position).toString();

                if (selected != null && !selected.isEmpty())
                {
                    List<GlobalPlatformCard> cardList = GpCardDao.queryBuilder().where(GlobalPlatformCardDao.Properties.CardName.eq(selected)).list();
                    if (!cardList.isEmpty())
                    {
                        GlobalPlatformCard cardSelected = cardList.get(0);

                        cardNameEditText.setText(cardSelected.getCardName());
                        cardManagerAidEditText.setText(cardSelected.getCMAid());
                        kmcAcEditText.setText(cardSelected.getKeyKmcAC());
                        kmcMacEditText.setText(cardSelected.getKeyKmcMac());
                        kmcDekEditText.setText(cardSelected.getKeyKmcDek());

                        apduModeSpinner.setSelection(apduModeDataAdapter.getPosition(ApduModeEnum.getStringValue(cardSelected.getApdu_mode())));
                        scpModeSpinner.setSelection(scpModeDataAdapter.getPosition(ScpModeEnum.getStringValue(cardSelected.getScp_mode())));
                        kdvModeSpinner.setSelection(kdvModeDataAdapter.getPosition(KeyDerivationModeEnum.getStringValue(cardSelected.getKeyDerivation_mode())));

                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(getString(R.string.last_selected_data), position);
                        editor.apply();

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Button searchCmBtn = (Button) getActivity().findViewById(R.id.searchCmBtn);
        searchCmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Iso7816.Tag IsoDepCard = ((MainActivity)getActivity()).getIsodepCard();

                Iso7816.Response resp = null;
                String cmd = null;
                if (IsoDepCard != null && IsoDepCard.isConnected())
                {
                    cmd = "00A4040000";
                    resp = IsoDepCard.transceive(cmd);

                    if (resp != null) {
                        String cmAid = GpUtil.parseSelectCM(resp.getBytes());

                        final EditText et = (EditText) getView().findViewById(R.id.cmAidEditText);

                        Log.i(TAG, "CMAidEditText is null");
                    }
                }

                if (cmd == null || resp == null)
                    Toast.makeText(getActivity(), "Search Failed, Not Connected", Toast.LENGTH_SHORT).show();
                else
                    mCallback.apduCommLog(cmd, resp.toString());
            }
        });

        updateCardProfileList(false);
    }


    public void onSaveAsButtonClick(View view) {
        addCardProfile();
    }

    public void onDeleteButtonClick(View view) {
        deleteACardProfile();
    }

    public void onUpdateButtonClick(View view) {
        updateACardProfile();}


    private void updateACardProfile()
    {
        if (listOfCardsSpinner.getSelectedItem() != null) {
            String selected = listOfCardsSpinner.getSelectedItem().toString();
            List<GlobalPlatformCard> cardList = GpCardDao.queryBuilder().where(GlobalPlatformCardDao.Properties.CardName.eq(selected)).list();

            if (cardList != null && !cardList.isEmpty()) {
                GlobalPlatformCard gpc = cardList.get(0);
                gpc.setKeyKmcAC(kmcAcEditText.getText().toString());
                gpc.setKeyKmcMac(kmcMacEditText.getText().toString());
                gpc.setKeyKmcDek(kmcDekEditText.getText().toString());
                gpc.setCMAid(cardManagerAidEditText.getText().toString());
                gpc.setApdu_mode(ApduModeEnum.values()[apduModeSpinner.getSelectedItemPosition()]);
                gpc.setScp_mode(ScpModeEnum.values()[scpModeSpinner.getSelectedItemPosition()]);
                gpc.setKeyDerivation_mode(KeyDerivationModeEnum.values()[kdvModeSpinner.getSelectedItemPosition()]);
                gpc.setLast_modified_date(new Date());

                GpCardDao.update(gpc);

                Toast.makeText(getActivity(), "Card profile was succesfully updated", Toast.LENGTH_SHORT).show();

                updateCardProfileList(false);
            }
        }
    }

    /**
     * Delete a card profile from database based on the selected card profile name
     */
    private void deleteACardProfile()
    {
        if (listOfCardsSpinner.getSelectedItem() != null) {
            String selected = listOfCardsSpinner.getSelectedItem().toString();
            List<GlobalPlatformCard> cardList = GpCardDao.queryBuilder().where(GlobalPlatformCardDao.Properties.CardName.eq(selected)).list();

            if (cardList != null && !cardList.isEmpty()) {
                GpCardDao.delete(cardList.get(0));

                Toast.makeText(getActivity(), "A Card profile was deleted", Toast.LENGTH_SHORT).show();

                updateCardProfileList(true);
            }
        }
    }

    /**
     * Insert the card profile data into the database
     */
    private void addCardProfile() {
        GlobalPlatformCard gpc = new GlobalPlatformCard();
        gpc.setCardName(cardNameEditText.getText().toString());

        if (cardNameEditText.getText().toString().isEmpty() || cardNameEditText.getText().toString() == "") {
            if (cardNameEditText.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(cardNameEditText, InputMethodManager.SHOW_IMPLICIT);

                Toast.makeText(getActivity(), "Profile name must not be empty!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        gpc.setKeyKmcAC(kmcAcEditText.getText().toString());
        gpc.setKeyKmcMac(kmcMacEditText.getText().toString());
        gpc.setKeyKmcDek(kmcDekEditText.getText().toString());
        gpc.setCMAid(cardManagerAidEditText.getText().toString());
        gpc.setApdu_mode(ApduModeEnum.values()[apduModeSpinner.getSelectedItemPosition()]);
        gpc.setScp_mode(ScpModeEnum.values()[scpModeSpinner.getSelectedItemPosition()]);
        gpc.setKeyDerivation_mode(KeyDerivationModeEnum.values()[kdvModeSpinner.getSelectedItemPosition()]);
        gpc.setLast_modified_date(new Date());

        try {
            GpCardDao.insert(gpc);
        } catch (Exception | Error e) {
            Toast.makeText(getActivity(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        updateCardProfileList(true);

    }
}
