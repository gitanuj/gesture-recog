package com.gesture.recog;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SensorActivity extends Activity implements SensorEventListener {

    public static final String SERVER_IP = "SERVER_IP";

    private static final int PORT = 8888;

    private SensorManager mSensorManager;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private Sensor mSensor;

    private float x;

    private float y;

    private float z;

    private String mServerAddress;

    private Sender mSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mServerAddress = getIntent().getStringExtra(SERVER_IP);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            onLinearAccelerationChanged(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void onLinearAccelerationChanged(SensorEvent event) {
        float[] values = event.values;
        x = values[0];
        y = values[1];
        z = values[2];
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);

        mSender = new Sender();
        mExecutorService.submit(mSender);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

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
                    out.printf("%10.2f", x);
                    out.printf("%10.2f", y);
                    out.printf("%10.2f\n", z);
                    out.flush();

                    Thread.sleep(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
