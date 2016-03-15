package com.gesture.recog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GestureActivity extends SensorActivity implements HoldButton.HoldListener, View.OnClickListener {

    private Button mKeyboardBtn;

    private HoldButton mHoldButton;

    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle(getString(R.string.connected_to, mServerAddress));

        mHoldButton = ((HoldButton) findViewById(R.id.hb_hold_button));
        mHoldButton.setHoldListener(this);
        mKeyboardBtn = (Button) findViewById(R.id.btn_keyboard);
        mKeyboardBtn.setOnClickListener(this);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = null;
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btn_keyboard:
                bundle = new Bundle();
                bundle.putString(SensorActivity.SERVER_IP, mServerAddress);
                intent = Utils.buildLaunchIntent(this, KeyboardActivity.class, bundle);
                startActivity(intent);
                break;
            case R.id.fab:
                bundle = new Bundle();
                bundle.putString(SensorActivity.SERVER_IP, mServerAddress);
                intent = Utils.buildLaunchIntent(this, GestureListActivity.class, bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRecordingFinished() {
        sendData(null, null);
    }

    @Override
    public void onHoldDown() {
        startRecording();
    }

    @Override
    public void onRelease() {
        stopRecording();
    }
}
