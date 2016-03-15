public class PCGesture {

    private static final int GESTURE_PORT = 8888;

    private static final int KEYBOARD_PORT = 8800;

    private static final int GET_SETTINGS_PORT = 8801;

    private static final int SAVE_SETTINGS_PORT = 8802;

    public static void main(String[] args) {
        try {
            Classifier.getInstance().init();
        } catch (Exception e) {
            System.out.println("Failed to init classifier");
            e.printStackTrace();
        }

        Utils.startThreadWithName(new KeyboardInputRunnable(KEYBOARD_PORT), "keyboard-input");
        Utils.startThreadWithName(new GestureInputRunnable(GESTURE_PORT), "gesture-input");
        Utils.startThreadWithName(new GetSettingsRunnable(GET_SETTINGS_PORT), "get-settings");
        Utils.startThreadWithName(new SaveSettingsRunnable(SAVE_SETTINGS_PORT), "save-settings");
    }
}
