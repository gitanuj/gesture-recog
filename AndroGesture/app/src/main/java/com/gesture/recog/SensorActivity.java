package com.gesture.recog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class SensorActivity extends AppCompatActivity {

    public static final String SERVER_IP = "SERVER_IP";

    private static final int PORT = 8888;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private float xAcceleration;

    private float yAcceleration;

    private float zAcceleration;

    private float xRotation;

    private float yRotation;

    private float zRotation;

    private Sender mSender;

    private StringBuilder mRecordedValues = new StringBuilder();

    protected String mServerAddress;

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

        mServerAddress = getIntent().getStringExtra(SERVER_IP);
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

        stopRecording();
    }

    public void startRecording() {
        mSender = new Sender();
        mExecutorService.submit(mSender);
    }

    public void stopRecording() {
        if (mSender != null) {
            mSender.cancel();
            mSender = null;
        }
    }

    public abstract void onRecordingFinished();

    public void sendData(final String gestureName, final String gestureCmd) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket(mServerAddress, PORT);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("values", mRecordedValues.toString());
                    jsonObject.put("name", gestureName);
                    jsonObject.put("command", gestureCmd);

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

        mExecutorService.submit(runnable);
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onRecordingFinished();
                }
            });
        }
    }
}
