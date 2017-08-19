package com.bondhan.research.main;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bondhan.research.common.Util;
import com.bondhan.research.gp.Iso7816;
import com.example.android.common.logger.Log;

import gp.lab.bondhan.com.lazyglobalplatform.R;

//It will implements fragment because we are using fragmentation
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICommuncation {

    public static final String TAG = "MA:";
    private String history;

    public void setmLogShown(boolean mLogShown) {
        this.mLogShown = mLogShown;
    }

    public boolean ismLogShown() {
        return mLogShown;
    }

    private boolean mLogShown = false;

    private SharedPreferences mPrefs;
    static final String HISTORY_LOG = "history";
    static final String LOG_SHOWN = "shown";


    public static String[][] TECHLISTS;
    public static IntentFilter[] FILTERS;

    private static Tag tag = null;

    private static IsoDep isodep = null;

    private static Iso7816.Tag isodepCard = null;
    private static NfcAdapter mNfcAdapter = null;
    private static PendingIntent pendingIntent = null;

    private Dialog dialog;
    Fragment fragment = null;
    private Fragment GpFragment;
    private Fragment OtherMenuFragment;

    private boolean isSaved = false;
    private Boolean exit = false;

    public static Tag getTag() {
        return tag;
    }
    public static IsoDep getIsodep() {
        return isodep;
    }
    public static Iso7816.Tag getIsodepCard() {
        return isodepCard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        GpFragment = fragmentManager.findFragmentById(R.id.MainFragmentGpContent);

        //Check if the application was loaded before, if not null then we will resume
        if (GpFragment == null) {
            try {
                GpFragment = new GlobalPlatformMainFragment();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fragmentManager.beginTransaction().replace(R.id.MainFragmentGpContent, GpFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TECHLISTS = new String[][]{{IsoDep.class.getName()},};
        try {
            FILTERS = new IntentFilter[]{new IntentFilter(
                    NfcAdapter.ACTION_TECH_DISCOVERED, "*/*")};
        } catch (IntentFilter.MalformedMimeTypeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        checkNfcAdapter();

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        mPrefs = getPreferences(Context.MODE_PRIVATE);
        history = mPrefs.getString(HISTORY_LOG, "");
        mLogShown = mPrefs.getBoolean(LOG_SHOWN, false);

        Log.d(TAG, "On Create Main Activity was called");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (exit) {

                //clean up the log
                history = "";
                mLogShown = false;
                LinearLayout output = (LinearLayout) findViewById(R.id.log_window_layout);

                if (output != null)
                    output.setVisibility(LinearLayout.GONE);

                mPrefs = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = mPrefs.edit();
                ed.putBoolean(LOG_SHOWN, mLogShown);
                ed.putString(HISTORY_LOG, history);
                ed.apply();

                if (GpFragment != null) {
                    ((GlobalPlatformMainFragment) GpFragment).loadHistory(history);
                }

                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        LinearLayout output = (LinearLayout) findViewById(R.id.log_window_layout);

        if (output != null)
            if (mLogShown) {
                output.setVisibility(LinearLayout.VISIBLE);
            } else {
                output.setVisibility(LinearLayout.GONE);
            }

        MenuItem logToggle = menu.findItem(R.id.action_toggle_log);
        logToggle.setTitle(mLogShown ? R.string.hide_log_action : R.string.show_log_action);

        if ((fragment instanceof  GlobalPlatformMainFragment) && isSaved == true) {
            ((GlobalPlatformMainFragment) GpFragment).loadHistory(history);
            isSaved = false;
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_toggle_log)
        {
            showOutputLog(false);

            supportInvalidateOptionsMenu();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showOutputLog(boolean show) {
        LinearLayout output = (LinearLayout) findViewById(R.id.log_window_layout);

        mLogShown = !mLogShown;

        if (mLogShown || show) {

            output.setVisibility(LinearLayout.VISIBLE);
        } else {

            output.setVisibility(LinearLayout.GONE);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Log.d(TAG, "On onNavigationItemSelected was called");

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Class fragmentClass = null;

        if (id == R.id.nav_main)
        {
            fragmentClass = GlobalPlatformMainFragment.class;
            fragment = GpFragment;
        }
        else if (id == R.id.nav_setup)
        {
            fragmentClass = SetupScreenMainFragment.class;
            fragment = OtherMenuFragment;
        }
        else if (id == R.id.nav_share)
        {
            fragmentClass = GlobalPlatformMainFragment.class;
            fragment = GpFragment;
        }

        try {
            if (fragment == null) {
                fragment = (Fragment) fragmentClass.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if ((fragment instanceof  GlobalPlatformMainFragment) == false && isSaved == false) {
            history = ((GlobalPlatformMainFragment) GpFragment).getAllLogMsg();
            isSaved = true;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.MainFragmentGpContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /* Setup the NFC adapter */
    private void checkNfcAdapter()
    {
        if (dialog == null)
            dialog = new Dialog(this);

        // get the nfc adapter (if supported)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Warning");

        final TextView infoTextView = (TextView) dialog.findViewById(R.id.infoTextView);
        Button btnSettings          = (Button) dialog.findViewById(R.id.settingsBtnInfo);
        Button btnCancel        = (Button) dialog.findViewById(R.id.cancelInfoBtn);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_NFC_SETTINGS), 0);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (mNfcAdapter == null) {//if phone does not support NFC
            infoTextView.setText("NFC not supported!");
            dialog.show();
            return;
        } else if (!mNfcAdapter.isEnabled()) { //
            infoTextView.setText("NFC not enabled!");
            dialog.show();

            if (mNfcAdapter.isEnabled())
                dialog.dismiss();
        } else {
            Toast.makeText(this, "Put a card close to the back of your phone!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        setIntent(intent); //save and return back the intent to the main application
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (GpFragment != null) {
            ((GlobalPlatformMainFragment) GpFragment).loadHistory(history);
        }

        doOnNewIntent(getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG));

        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    FILTERS, TECHLISTS);
        }

        Log.d(TAG, "onResume was called, history = " + history);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null && mNfcAdapter.isEnabled())
            if (dialog != null) {
                dialog.dismiss();
            }

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);

        if (GpFragment != null)
            history = ((GlobalPlatformMainFragment) GpFragment).getAllLogMsg();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(HISTORY_LOG, history);
        ed.putBoolean(LOG_SHOWN, mLogShown);
        ed.apply();

        Log.d(TAG, "On Pause was called");
    }


    //    public void doOnNewIntent(IsoDep isodep, Iso7816.Tag isodepCard, Tag tag, Parcelable p )
    public void doOnNewIntent(Parcelable p )
    {

        if (isodepCard != null && isodepCard.isConnected()) {
            isodepCard.close();

            Log.w(TAG, "New Card found, close previous card");
        }

        if (p == null)    // not parcelable
            return;

        tag = (Tag) p;
        isodep = IsoDep.get(tag);

        if (isodep == null)    // not isodep
        {
            Log.w(TAG, "Not isodep card");
            return;
        }

        isodepCard = new Iso7816.Tag(isodep);
        isodepCard.connect();

        if (isodep.isConnected()) {
            Log.e(TAG, "Connected");
            if (GpFragment != null ) {
                CardInfoSubFragment cisf = (CardInfoSubFragment) ((GlobalPlatformMainFragment) GpFragment).getGpAdapter().getItem(0);
                if (cisf != null)
                    cisf.updateInfo(isodepCard, isodep);
            }
        }
        else
            Log.e(TAG, "Not Connected");
    }

    @Override
    public void appendLogMessage(String msg) {
        Log.d(TAG, msg);
        if ( GpFragment != null)
            ((GlobalPlatformMainFragment)GpFragment).doAppendLog(msg);
        else
            Log.d(TAG, "GpFragment is NULL");
    }

    @Override
    public void apduCommLog(String cmd, String resp) {
        if ( GpFragment != null)
        {
            ((GlobalPlatformMainFragment) GpFragment).doAppendLog("Term->Card:" + cmd);
            ((GlobalPlatformMainFragment) GpFragment).doAppendLog("Card->Term:" + resp.toString());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        appendLogMessage("On Destroy was called");

    }


}
