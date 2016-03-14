import java.awt.*;

public class Keyboard {

    private static final Keyboard INSTANCE = new Keyboard();

    private Robot ROBOT;

    public static Keyboard getInstance() {
        return INSTANCE;
    }

    private Keyboard() {
        try {
            ROBOT = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void type(int... keyEvents) {
        for (int i = 0; i < keyEvents.length; ++i) {
            ROBOT.keyPress(keyEvents[i]);
        }

        for (int i = keyEvents.length - 1; i >= 0; --i) {
            ROBOT.keyRelease(keyEvents[i]);
        }
    }
}
