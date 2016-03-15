import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Keyboard {

    private static final Keyboard INSTANCE = new Keyboard();

    private static final Map<String, Integer> KEY_MAP = new HashMap<String, Integer>();

    static {
        KEY_MAP.put("cmd", KeyEvent.VK_META);
        KEY_MAP.put("command", KeyEvent.VK_META);
        KEY_MAP.put("space", KeyEvent.VK_SPACE);
        KEY_MAP.put("up", KeyEvent.VK_UP);
        KEY_MAP.put("down", KeyEvent.VK_DOWN);
        KEY_MAP.put("left", KeyEvent.VK_LEFT);
        KEY_MAP.put("right", KeyEvent.VK_RIGHT);
        KEY_MAP.put("enter", KeyEvent.VK_ENTER);
        KEY_MAP.put("esc", KeyEvent.VK_ESCAPE);
        KEY_MAP.put("escape", KeyEvent.VK_ESCAPE);
        KEY_MAP.put("ctrl", KeyEvent.VK_CONTROL);
        KEY_MAP.put("control", KeyEvent.VK_CONTROL);
        KEY_MAP.put("alt", KeyEvent.VK_ALT);
        KEY_MAP.put("shift", KeyEvent.VK_SHIFT);
    }

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

    public void type(String command) {
        java.util.List<Integer> keyEvents = new ArrayList<Integer>();

        StringTokenizer stringTokenizer = new StringTokenizer(command, " ");
        while (stringTokenizer.hasMoreTokens()) {
            String str = stringTokenizer.nextToken();

            if (KEY_MAP.containsKey(str)) {
                keyEvents.add(KEY_MAP.get(str));
            } else {
                for (int i = 0; i < str.length(); ++i) {
                    keyEvents.add(getKeyEvent(str.charAt(i)));
                }
            }
        }

        int[] keycodes = new int[keyEvents.size()];
        for (int i = 0; i < keycodes.length; ++i) {
            keycodes[i] = keyEvents.get(i);
        }
        doType(keycodes);
    }

    private int getKeyEvent(char character) {
        switch (character) {
            case 'a':
                return (KeyEvent.VK_A);

            case 'b':
                return (KeyEvent.VK_B);

            case 'c':
                return (KeyEvent.VK_C);

            case 'd':
                return (KeyEvent.VK_D);

            case 'e':
                return (KeyEvent.VK_E);

            case 'f':
                return (KeyEvent.VK_F);

            case 'g':
                return (KeyEvent.VK_G);

            case 'h':
                return (KeyEvent.VK_H);

            case 'i':
                return (KeyEvent.VK_I);

            case 'j':
                return (KeyEvent.VK_J);

            case 'k':
                return (KeyEvent.VK_K);

            case 'l':
                return (KeyEvent.VK_L);

            case 'm':
                return (KeyEvent.VK_M);

            case 'n':
                return (KeyEvent.VK_N);

            case 'o':
                return (KeyEvent.VK_O);

            case 'p':
                return (KeyEvent.VK_P);

            case 'q':
                return (KeyEvent.VK_Q);

            case 'r':
                return (KeyEvent.VK_R);

            case 's':
                return (KeyEvent.VK_S);

            case 't':
                return (KeyEvent.VK_T);

            case 'u':
                return (KeyEvent.VK_U);

            case 'v':
                return (KeyEvent.VK_V);

            case 'w':
                return (KeyEvent.VK_W);

            case 'x':
                return (KeyEvent.VK_X);

            case 'y':
                return (KeyEvent.VK_Y);

            case 'z':
                return (KeyEvent.VK_Z);

            case '`':
                return (KeyEvent.VK_BACK_QUOTE);

            case '0':
                return (KeyEvent.VK_0);

            case '1':
                return (KeyEvent.VK_1);

            case '2':
                return (KeyEvent.VK_2);

            case '3':
                return (KeyEvent.VK_3);

            case '4':
                return (KeyEvent.VK_4);

            case '5':
                return (KeyEvent.VK_5);

            case '6':
                return (KeyEvent.VK_6);

            case '7':
                return (KeyEvent.VK_7);

            case '8':
                return (KeyEvent.VK_8);

            case '9':
                return (KeyEvent.VK_9);

            case '-':
                return (KeyEvent.VK_MINUS);

            case '=':
                return (KeyEvent.VK_EQUALS);

            case '!':
                return (KeyEvent.VK_EXCLAMATION_MARK);

            case '@':
                return (KeyEvent.VK_AT);

            case '#':
                return (KeyEvent.VK_NUMBER_SIGN);

            case '$':
                return (KeyEvent.VK_DOLLAR);

            case '^':
                return (KeyEvent.VK_CIRCUMFLEX);

            case '&':
                return (KeyEvent.VK_AMPERSAND);

            case '*':
                return (KeyEvent.VK_ASTERISK);

            case '(':
                return (KeyEvent.VK_LEFT_PARENTHESIS);

            case ')':
                return (KeyEvent.VK_RIGHT_PARENTHESIS);

            case '_':
                return (KeyEvent.VK_UNDERSCORE);

            case '+':
                return (KeyEvent.VK_PLUS);

            case '\t':
                return (KeyEvent.VK_TAB);

            case '\n':
                return (KeyEvent.VK_ENTER);

            case '[':
                return (KeyEvent.VK_OPEN_BRACKET);

            case ']':
                return (KeyEvent.VK_CLOSE_BRACKET);

            case '\\':
                return (KeyEvent.VK_BACK_SLASH);

            case ';':
                return (KeyEvent.VK_SEMICOLON);

            case ':':
                return (KeyEvent.VK_COLON);

            case '\'':
                return (KeyEvent.VK_QUOTE);

            case '"':
                return (KeyEvent.VK_QUOTEDBL);

            case ',':
                return (KeyEvent.VK_COMMA);

            case '.':
                return (KeyEvent.VK_PERIOD);

            case '/':
                return (KeyEvent.VK_SLASH);

            case ' ':
                return (KeyEvent.VK_SPACE);

            default:
                return -1;
        }
    }

    private void doType(int... keyCodes) {
        doType(keyCodes, 0, keyCodes.length);
    }

    private void doType(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }

        ROBOT.keyPress(keyCodes[offset]);
        doType(keyCodes, offset + 1, length - 1);
        ROBOT.keyRelease(keyCodes[offset]);
    }
}
