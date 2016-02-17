package com.gesture.recog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashSet;
import java.util.Set;

public class RotationMonitor implements SensorEventListener {

    private static final RotationMonitor INSTANCE = new RotationMonitor();

    private Set<RotationListener> mListeners = new HashSet<>();

    private SensorManager mSensorManager;

    private Sensor mSensor;

    public static RotationMonitor getInstance() {
        return INSTANCE;
    }

    public RotationMonitor() {
        mSensorManager = (SensorManager) GestureApplication.getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void register(RotationListener listener) {
        mListeners.add(listener);

        if (mListeners.size() == 1) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void unregister(RotationListener listener) {
        mListeners.remove(listener);

        if (mListeners.isEmpty()) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for (RotationListener listener : mListeners) {
            listener.onRotationChanged(event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
