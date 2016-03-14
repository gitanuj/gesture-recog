import com.fastdtw.timeseries.TimeSeries;

import java.awt.event.KeyEvent;
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
                    String data = Utils.readFully(socket.getInputStream());
                    TimeSeries timeSeries = Utils.dataToTimeSeries(data);
                    long start = System.currentTimeMillis();
                    Gesture g = Classifier.getInstance().knn(1, timeSeries);
                    System.out.println(g);
                    System.out.println("Time taken: " + (System.currentTimeMillis() - start));
                    performAction(g);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Utils.closeQuietly(socket);
                }
            }
        };

        Utils.startThreadWithName(runnable, "handle-gesture-input");
    }

    private void performAction(Gesture g) {
        String name = g.getName();

        if (name.startsWith("flip")) {
            Keyboard.getInstance().type(KeyEvent.VK_META, KeyEvent.VK_SPACE);
        } else if (name.startsWith("right_left")) {
            Keyboard.getInstance().type(KeyEvent.VK_RIGHT);
        } else if (name.startsWith("top_down")) {
            Keyboard.getInstance().type(KeyEvent.VK_SPACE);
        }
    }
}
