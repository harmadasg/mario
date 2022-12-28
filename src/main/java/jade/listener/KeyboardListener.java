package jade.listener;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardListener {

    private static final int SUPPORTED_KEY_BINDINGS = GLFW_KEY_LAST + 1;
    private static KeyboardListener INSTANCE;

    private final boolean[] keyPressed;

    private KeyboardListener()  {
        keyPressed = new boolean[SUPPORTED_KEY_BINDINGS];
    }

    public static KeyboardListener get() {
        if (INSTANCE == null) {
            INSTANCE = new KeyboardListener();
        }
        return INSTANCE;
    }

    public void obKeyboardAction(long window, int key, int scanCode, int action, int mods) {
        if (GLFW_PRESS == action) {
            keyPressed[key] = true;
        } else if (GLFW_RELEASE == action) {
            keyPressed[key] = false;
        }
    }

    public boolean isKeyPressed(int key) {
        return keyPressed[key];
    }
}
