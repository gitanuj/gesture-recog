package com.gesture.recog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class OptionActivity extends Activity implements OnClickListener {

    private Button mCreateGestureButton;

    private Button mUseGestureButton;

    private String mServerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        mCreateGestureButton = (Button) findViewById(R.id.btn_create_gestures);
        mUseGestureButton = (Button) findViewById(R.id.btn_use_gestures);

        mCreateGestureButton.setOnClickListener(this);
        mUseGestureButton.setOnClickListener(this);

        mServerAddress = getIntent().getStringExtra(SensorActivity.SERVER_IP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_gestures:
                createGestures();
                break;
            case R.id.btn_use_gestures:
                useGestures();
                break;
        }
    }

    private void createGestures() {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_gesture_info, null);
        new AlertDialog.Builder(this).setView(dialogView).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = ((EditText) dialogView.findViewById(R.id.et_gesture_name)).getText().toString();
                String command = ((EditText) dialogView.findViewById(R.id.et_gesture_command)).getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString(SensorActivity.SERVER_IP, mServerAddress);
                bundle.putString(SensorActivity.GESTURE_NAME, name);
                bundle.putString(SensorActivity.GESTURE_COMMAND, command);
                Utils.launchActivity(OptionActivity.this, SensorActivity.class, bundle);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private void useGestures() {
        Bundle bundle = new Bundle();
        bundle.putString(SensorActivity.SERVER_IP, mServerAddress);
        Utils.launchActivity(OptionActivity.this, SensorActivity.class, bundle);
    }
}
