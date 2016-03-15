import com.google.gson.JsonObject;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GetSettingsRunnable implements Runnable {

    private int port;

    public GetSettingsRunnable(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                handleGetSettings(ss.accept());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGetSettings(final Socket socket) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Send the config
                PrintWriter writer = null;
                try {
                    JsonObject jsonObject = Utils.mapToJsonObject(Classifier.getInstance().getGestureCommandMap());
                    writer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
                    writer.print(jsonObject.toString());
                    writer.flush();
                    System.out.println("Sent the gestures map");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Utils.closeQuietly(writer);
                }
            }
        };

        Utils.startThreadWithName(runnable, "handle-get-settings");
    }
}
