import com.fastdtw.timeseries.TimeSeries;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class GestureInputRunnable implements Runnable {

    private final int port;

    public GestureInputRunnable(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                handleGestureInput(ss.accept());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGestureInput(final Socket socket) throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handleGestureData(new BufferedReader(new InputStreamReader(socket.getInputStream())));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Utils.closeQuietly(socket);
                }
            }
        };

        Utils.startThreadWithName(runnable, "handle-gesture-input");
    }

    private void handleGestureData(Reader reader) throws Exception {
        GestureData gestureData = new Gson().fromJson(reader, GestureData.class);

        if (gestureData.getName() == null) {
            classify(gestureData);
        } else {
            train(gestureData);
        }
    }

    private void classify(GestureData gestureData) throws Exception {
        TimeSeries timeSeries = Utils.dataToTimeSeries(gestureData.getValues());
        Gesture gesture = Classifier.getInstance().knn(1, timeSeries);
        System.out.println(gesture);
        Keyboard.getInstance().type(gesture.getCommand());
    }

    private void train(GestureData gestureData) throws Exception {
        Classifier.getInstance().addGesture(gestureData.getName(), gestureData.getCommand(), gestureData.getValues());
    }
}
