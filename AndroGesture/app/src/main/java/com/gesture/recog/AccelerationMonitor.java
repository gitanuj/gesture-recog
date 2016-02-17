package com.gesture.recog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashSet;
import java.util.Set;

public class AccelerationMonitor implements SensorEventListener {

    private static final AccelerationMonitor INSTANCE = new AccelerationMonitor();

    private Set<AccelerationListener> mListeners = new HashSet<>();

    private SensorManager mSensorManager;

    private Sensor mSensor;

    public static AccelerationMonitor getInstance() {
        return INSTANCE;
    }

    public AccelerationMonitor() {
        mSensorManager = (SensorManager) GestureApplication.getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void register(AccelerationListener listener) {
        mListeners.add(listener);

        if (mListeners.size() == 1) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void unregister(AccelerationListener listener) {
        mListeners.remove(listener);

        if (mListeners.isEmpty()) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for (AccelerationListener listener : mListeners) {
            listener.onAccelerationChanged(event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
