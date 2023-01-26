package jade.scene;

import imgui.ImGui;
import jade.Camera;
import jade.GameObject;
import lombok.Getter;
import renderer.Renderer;
import util.SerializerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Scene {
    protected final List<GameObject> gameObjects;
    @Getter
    protected Camera camera;
    protected boolean isLevelLoaded;
    protected Renderer renderer;
    protected GameObject activeGameObject;
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

    public void renderImgui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.renderImgui();
            ImGui.end();
        }
    }

    public abstract void update(float deltaTime);

    public void saveExit() {
        SerializerHelper.serialize(gameObjects);
    }

    public void load() {
        var gameObjects = SerializerHelper.deserialize();
        Arrays.stream(gameObjects)
                .forEach(this::addGameObjectToScene);
        isLevelLoaded = true;
    }

    protected void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (isRunning) {
            gameObject.start();
            renderer.add(gameObject);
        }
    }

}
