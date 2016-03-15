import java.net.ServerSocket;
import java.net.Socket;

public class SaveSettingsRunnable implements Runnable {

    private int port;

    public SaveSettingsRunnable(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                handleSaveSettings(ss.accept());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSaveSettings(final Socket socket) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Save the config
                try {
                    String jsonString = Utils.readFully(socket.getInputStream());
                    Classifier.getInstance().updateGestureCommandMap(Utils.jsonStringToMap(jsonString));
                    System.out.println("Updated the gestures map");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Utils.closeQuietly(socket);
                }
            }
        };

        Utils.startThreadWithName(runnable, "handle-save-settings");
    }
}
