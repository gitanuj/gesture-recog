package com.gesture.recog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GestureActivity extends SensorActivity implements HoldButton.HoldListener, View.OnClickListener {

    private static final int CREATE_GESTURE_REQUEST_CODE = 1;

    private Button mKeyboardBtn;

    private HoldButton mHoldButton;

    private FloatingActionButton mFAB, mSettingsFAB;

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
        mSettingsFAB = (FloatingActionButton) findViewById(R.id.settings_fab);
        mSettingsFAB.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.btn_keyboard:
                Bundle bundle = new Bundle();
                bundle.putString(SensorActivity.SERVER_IP, mServerAddress);
                Intent intent = Utils.buildLaunchIntent(this, KeyboardActivity.class, bundle);
                startActivity(intent);
                break;
            case R.id.fab:
                trainGestures();
                break;
            case R.id.settings_fab:
                Bundle bundle1 = new Bundle();
                bundle1.putString(SensorActivity.SERVER_IP, mServerAddress);
                Intent intent1 = Utils.buildLaunchIntent(this, SettingsActivity.class, bundle1);
                startActivity(intent1);
        }
    }

    private void trainGestures() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_gesture_info, null);
        final EditText gestureName = (EditText) dialogView.findViewById(R.id.et_gesture_name);
        final EditText gestureCmd = (EditText) dialogView.findViewById(R.id.et_gesture_command);
        new AlertDialog.Builder(this).setTitle("Create gesture").setView(dialogView).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = gestureName.getText().toString();
                String cmd = gestureCmd.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString(SensorActivity.SERVER_IP, mServerAddress);
                bundle.putString(CreateGestureActivity.GESTURE_NAME, name);
                bundle.putString(CreateGestureActivity.GESTURE_COMMAND, cmd);
                Intent intent = Utils.buildLaunchIntent(GestureActivity.this, CreateGestureActivity.class, bundle);
                GestureActivity.this.startActivityForResult(intent, CREATE_GESTURE_REQUEST_CODE);
            }
        }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Snackbar.make(mFAB, "Recorded new gesture", Snackbar.LENGTH_LONG).show();
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
