package jade.scene;

import jade.Camera;
import jade.GameObject;
import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;

    protected final List<GameObject> gameObjects;

    protected Renderer renderer;
    private boolean isRunning;

    protected Scene() {
        gameObjects = new ArrayList<>();
        renderer = new Renderer();
    }

    public void start() {
        gameObjects.stream()
                .peek(GameObject::start)
                .forEach(renderer::add);
        isRunning = true;
    }

    public abstract void update(float deltaTime);

    protected void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (isRunning) {
            gameObject.start();
            renderer.add(gameObject);
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
