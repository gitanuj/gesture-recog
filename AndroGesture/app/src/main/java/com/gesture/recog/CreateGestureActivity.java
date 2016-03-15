package com.gesture.recog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

public class CreateGestureActivity extends SensorActivity implements HoldButton.HoldListener {

    public static final String GESTURE_NAME = "GESTURE_NAME";

    public static final String GESTURE_COMMAND = "GESTURE_COMMAND";

    private static final int RECORDED_COUNT = 5;

    private TextView mText;

    private TextView mRecordedCounter;

    private HoldButton mHoldButton;

    private String mGestureName;

    private String mGestureCmd;

    private int mCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gesture);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mText = (TextView) findViewById(R.id.tv_text);
        mText.setText(getString(R.string.connected_to, mServerAddress));

        mRecordedCounter = (TextView) findViewById(R.id.tv_recorded_counter);

        mGestureName = getIntent().getStringExtra(GESTURE_NAME);
        mGestureCmd = getIntent().getStringExtra(GESTURE_COMMAND);

        mHoldButton = ((HoldButton) findViewById(R.id.hb_hold_button));
        mHoldButton.setHoldListener(this);

        setCounter(mCounter);
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

    private void setCounter(int count) {
        mRecordedCounter.setText(getString(R.string.recorded_counter_text, count, RECORDED_COUNT, mGestureName));

        if (count >= RECORDED_COUNT) {
            // Done
            sendData(mGestureName, mGestureCmd);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public void onRecordingFinished() {
        setCounter(++mCounter);
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
