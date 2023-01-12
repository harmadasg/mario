package jade.scene;

import jade.Camera;
import jade.GameObject;
import jade.Transform;
import jade.component.SpriteRenderer;
import org.joml.Vector2f;

import static util.AssetPool.getShader;
import static util.AssetPool.getTexture;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {
        this.camera = new Camera(new Vector2f(-250, 0));

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(getTexture("assets/images/testImage.png")));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(getTexture("assets/images/testImage2.png")));
        this.addGameObjectToScene(obj2);

        loadResources();
    }

    private void loadResources() {
        getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float deltaTime) {
        gameObjects.forEach(g -> g.update(deltaTime));
        renderer.render();
    }
}
