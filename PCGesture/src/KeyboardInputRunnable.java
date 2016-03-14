import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.net.ServerSocket;
import java.net.Socket;

public class KeyboardInputRunnable implements Runnable {

    private final int port;

    private Robot robot;

    private Clipboard clipboard;

    public KeyboardInputRunnable(int port) {
        this.port = port;
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                handleKeyboardInput(ss.accept());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleKeyboardInput(final Socket socket) throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String data = Utils.readFully(socket.getInputStream());
                    typeString(data);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Utils.closeQuietly(socket);
                }
            }
        };

        Utils.startThreadWithName(runnable, "handle-keyboard-input");
    }

    private void typeString(String data) throws Exception {
        clipboard.setContents(new StringSelection(data), null);

        robot.keyPress(KeyEvent.VK_META);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_META);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
}
