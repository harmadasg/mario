package jade.listener;

import lombok.Getter;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    private static final int SUPPORTED_MOUSE_BUTTONS = 3;
    private static MouseListener INSTANCE;

    @Getter
    private double scrollX;
    @Getter
    private double scrollY;
    @Getter
    private double xPosition;
    @Getter
    private double yPosition;
    private double lastX;
    private double lastY;
    @Getter
    private boolean isDragging;
    private final boolean[] mouseButtonPressed;

    private MouseListener()  {
        mouseButtonPressed = new boolean[SUPPORTED_MOUSE_BUTTONS];
    }

    public static MouseListener get() {
        if (INSTANCE == null) {
            INSTANCE = new MouseListener();
        }
        return INSTANCE;
    }

    public void onMousePositionChanged(long window, double xPosition, double yPosition) {
        this.lastX = xPosition;
        this.lastY = yPosition;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.isDragging = isAnyButtonPressed();
    }

    public void onMouseButtonAction(long window, int button, int action, int mods) {
        if (isMouseButtonSupported(button)) {
            if (GLFW_PRESS == action) {
                mouseButtonPressed[button] = true;
            } else if (GLFW_RELEASE == action) {
                mouseButtonPressed[button] = false;
                isDragging = false;
            }
        }
    }

    public void onMouseScrollAction(long window, double xOffset, double yOffset) {
        scrollX = xOffset;
        scrollY = yOffset;
    }

    public void onEndFrame() {
        scrollX = 0;
        scrollY = 0;
        lastX = xPosition;
        lastY = yPosition;
    }

    public double getDx() {
        return lastX - xPosition;
    }

    public double getDy() {
        return lastY - yPosition;
    }

    public boolean isMouseButtonDown(int button) {
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
