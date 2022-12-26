package jade;

import static org.lwjgl.glfw.GLFW.*;

class MouseListener {

    private static final int SUPPORTED_MOUSE_BUTTONS = 3;
    private static MouseListener INSTANCE;

    private double scrollX, scrollY;
    private double xPosition, yPosition, lastX, lastY;
    private boolean isDragging;
    private final boolean[] mouseButtonPressed;

    private MouseListener()  {
        mouseButtonPressed = new boolean[SUPPORTED_MOUSE_BUTTONS];
    }

    static MouseListener get() {
        if (INSTANCE == null) {
            INSTANCE = new MouseListener();
        }
        return INSTANCE;
    }

    void onMousePositionChanged(long window, double xPosition, double yPosition) {
        this.lastX = xPosition;
        this.lastY = yPosition;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.isDragging = isAnyButtonPressed();
    }

    void onMouseButtonAction(long window, int button, int action, int mods) {
        if (isMouseButtonSupported(button)) {
            if (GLFW_PRESS == action) {
                mouseButtonPressed[button] = true;
            } else if (GLFW_RELEASE == action) {
                mouseButtonPressed[button] = false;
                isDragging = false;
            }
        }
    }

    void onMouseScrollAction(long window, double xOffset, double yOffset) {
        scrollX = xOffset;
        scrollY = yOffset;
    }

    void onEndFrame() {
        scrollX = 0;
        scrollY = 0;
        lastX = xPosition;
        lastY = yPosition;
    }

    double getX() {
        return xPosition;
    }

    double getY() {
        return yPosition;
    }

    double getDx() {
        return lastX - xPosition;
    }

    double getDy() {
        return lastY - yPosition;
    }

    double getScrollX() {
        return scrollX;
    }

    double getScrollY() {
        return scrollY;
    }

    boolean isDragging() {
        return isDragging;
    }

    boolean isMouseButtonDown(int button) {
        if (isMouseButtonSupported(button)) {
            return mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    private boolean isMouseButtonSupported(int button) {
        return button < SUPPORTED_MOUSE_BUTTONS;
    }

    private boolean isAnyButtonPressed() {
        return mouseButtonPressed[0] || mouseButtonPressed[1] || mouseButtonPressed[2];
    }
}
