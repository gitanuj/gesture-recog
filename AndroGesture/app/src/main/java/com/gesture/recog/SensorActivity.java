package com.gesture.recog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SensorActivity extends Activity implements HoldButton.HoldListener, View.OnClickListener {

    public static final String SERVER_IP = "SERVER_IP";

    public static final String GESTURE_NAME = "GESTURE_NAME";

    public static final String GESTURE_COMMAND = "GESTURE_COMMAND";

    private static final int PORT = 8888;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private float xAcceleration;

    private float yAcceleration;

    private float zAcceleration;

    private float xRotation;

    private float yRotation;

    private float zRotation;

    private String mServerAddress;

    private String mGestureName;

    private String mGestureCommand;

    private Sender mSender;

    private TextView mText;

    private HoldButton mHoldButton;

    private Button mKeyboardButton;

    private Button mCompleteTraining;

    private StringBuilder mRecordedValues;

    private AccelerationListener mAccelerationListener = new AccelerationListener() {
        @Override
        public void onAccelerationChanged(float x, float y, float z) {
            xAcceleration = x;
            yAcceleration = y;
            zAcceleration = z;
        }
    };

    private RotationListener mRotationListener = new RotationListener() {
        @Override
        public void onRotationChanged(float x, float y, float z) {
            xRotation = x;
            yRotation = y;
            zRotation = z;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mText = (TextView) findViewById(R.id.tv_text);
        mHoldButton = (HoldButton) findViewById(R.id.hb_hold_button);
        mKeyboardButton = (Button) findViewById(R.id.btn_keyboard);
        mCompleteTraining = (Button) findViewById(R.id.btn_complete_training);

        mServerAddress = getIntent().getStringExtra(SERVER_IP);
        mGestureName = getIntent().getStringExtra(GESTURE_NAME);
        mGestureCommand = getIntent().getStringExtra(GESTURE_COMMAND);

        mText.setText("Connected to " + mServerAddress);
        mHoldButton.setHoldListener(this);
        mKeyboardButton.setOnClickListener(this);
        mCompleteTraining.setOnClickListener(this);

        mRecordedValues = new StringBuilder();

        if (mGestureName == null) {
            onGestureUseMode();
        } else {
            onTrainingMode();
        }
    }

    private void onTrainingMode() {
        mKeyboardButton.setVisibility(View.GONE);
        mCompleteTraining.setVisibility(View.VISIBLE);
    }

    private void onGestureUseMode() {
        mKeyboardButton.setVisibility(View.VISIBLE);
        mCompleteTraining.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_keyboard:
                Bundle bundle = new Bundle();
                bundle.putString(SERVER_IP, mServerAddress);
                Utils.launchActivity(this, KeyboardActivity.class, bundle);
                break;
            case R.id.btn_complete_training:
                sendData(false);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AccelerationMonitor.getInstance().register(mAccelerationListener);
        RotationMonitor.getInstance().register(mRotationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AccelerationMonitor.getInstance().unregister(mAccelerationListener);
        RotationMonitor.getInstance().unregister(mRotationListener);

        releaseSender();
    }

    @Override
    public void onHoldDown() {
        mSender = new Sender();
        mExecutorService.submit(mSender);
    }

    @Override
    public void onRelease() {
        releaseSender();
    }

    private void releaseSender() {
        if (mSender != null) {
            mSender.cancel();
            mSender = null;
        }
    }

    private void sendData(boolean sync) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket(mServerAddress, PORT);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("values", mRecordedValues.toString());
                    jsonObject.put("name", mGestureName);
                    jsonObject.put("command", mGestureCommand);

                    mRecordedValues = new StringBuilder();

                    PrintWriter writer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
                    writer.print(jsonObject.toString());
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Utils.closeQuietly(socket);
                }
            }
        };

        if (sync) {
            runnable.run();
        } else {
            mExecutorService.submit(runnable);
        }
    }

    private class Sender implements Runnable {

        private boolean cancel;

        public void cancel() {
            cancel = true;
        }

        @Override
        public void run() {
            try {
                while (!cancel) {
                    String val = String.format("%f %f %f %f %f %f,", xAcceleration, yAcceleration, zAcceleration, xRotation, yRotation, zRotation);
                    mRecordedValues.append(val);
                    Thread.sleep(2);
                }
                mRecordedValues.append("\n");

                if (mGestureName == null) {
                    sendData(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
