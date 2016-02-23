package com.gesture.recog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SensorActivity extends Activity implements HoldButton.HoldListener {

    public static final String SERVER_IP = "SERVER_IP";

    private static final int PORT = 8888;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private float xAcceleration;

    private float yAcceleration;

    private float zAcceleration;

    private float xRotation;

    private float yRotation;

    private float zRotation;

    private String mServerAddress;

    private Sender mSender;

    private TextView mText;

    private HoldButton mHoldButton;

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

        mServerAddress = getIntent().getStringExtra(SERVER_IP);

        mText.setText("Connected to " + mServerAddress);
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

        mSender.cancel();
        mSender = null;
    }

    @Override
    public void onHoldDown() {
        mSender = new Sender();
        mExecutorService.submit(mSender);
    }

    @Override
    public void onRelease() {
        mSender.cancel();
        mSender = null;
    }

    private class Sender implements Runnable {

        private Socket socket;

        private boolean cancel;

        public void cancel() {
            cancel = true;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(mServerAddress, PORT);
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                while (!cancel) {
                    out.printf(" %.2f", xAcceleration);
                    out.printf(" %.2f", yAcceleration);
                    out.printf(" %.2f", zAcceleration);
                    out.printf(" %.2f", xRotation);
                    out.printf(" %.2f", yRotation);
                    out.printf(" %.2f\n", zRotation);
                    out.flush();

                    Thread.sleep(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Utils.closeQuietly(socket);
            }
        }
    }
}
