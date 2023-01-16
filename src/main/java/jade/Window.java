package jade;

import jade.listener.KeyboardListener;
import jade.listener.MouseListener;
import jade.scene.LevelEditorScene;
import jade.scene.Scene;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static final float RED = 1.0f;
    private static final float GREEN = 1.0f;
    private static final float BLUE = 1.0f;
    private static final float ALPHA = 1.0f;
    private static Window WINDOW;

    private final int width;
    private final int height;
    private final String title;

    private long glfwWindow;
    private Scene currentScene;

    private Window() {
        width = 1920;
        height = 1080;
        title = "Mario";
    }

    public static Window get() {
        if(WINDOW == null) {
            WINDOW = new Window();
        }
        return WINDOW;
    }

    public void run() {
        // The following code is more or less the same as on https://www.lwjgl.org/guide
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public Scene getScene() {
        return currentScene;
    }

    public void setScene(Scene scene) {
        currentScene = scene;
        currentScene.start();
    }

    private void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (NULL == glfwWindow) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener.get()::onMousePositionChanged);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener.get()::onMouseButtonAction);
        glfwSetScrollCallback(glfwWindow, MouseListener.get()::onMouseScrollAction);
        glfwSetKeyCallback(glfwWindow, KeyboardListener.get()::obKeyboardAction);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        setScene(new LevelEditorScene());
    }

    private void loop() {
        float deltaTime = 0.0f;
        float endTime, beginTime = (float) glfwGetTime();

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            glClearColor(RED, GREEN, BLUE, ALPHA);
            glClear(GL_COLOR_BUFFER_BIT);

            currentScene.update(deltaTime);

            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
