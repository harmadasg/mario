package jade.scene;

import jade.Camera;
import jade.GameObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;

    protected final List<GameObject> gameObjects;

    private boolean isRunning;

    protected Scene() {
        gameObjects = new ArrayList<>();
    }

    public void start() {
        gameObjects.forEach(GameObject::start);
        isRunning = true;
    }

    public abstract void update(float deltaTime);

    protected void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (isRunning) {
            gameObject.start();
        }
    }
}
