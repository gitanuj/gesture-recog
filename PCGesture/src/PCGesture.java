public class PCGesture {

    private static final int GESTURE_PORT = 8888;

    private static final int KEYBOARD_PORT = 8800;
    
    private static final int SETTINGS_PORT = 9001;

    public static void main(String[] args) {
        try {
            Classifier.getInstance().init();
        } catch (Exception e) {
            System.out.println("Failed to init classifier");
            e.printStackTrace();
        }

        Utils.startThreadWithName(new KeyboardInputRunnable(KEYBOARD_PORT), "keyboard-input");
        Utils.startThreadWithName(new GestureInputRunnable(GESTURE_PORT), "gesture-input");
        Utils.startThreadWithName(new SettingsRunnable(SETTINGS_PORT), "settings-output");
    }
}
