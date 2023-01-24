package jade.scene;

import jade.Camera;
import jade.GameObject;
import jade.Transform;
import jade.component.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static util.AssetPool.*;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene() {
        loadResources();

        camera = new Camera(new Vector2f(-250, 0));

        if (isLevelLoaded) return;

        sprites = getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)), 0);
        obj1.addComponent(new SpriteRenderer(new Vector4f(1, 0, 0, 1)));
        addGameObjectToScene(obj1);
        activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 1);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                getTexture("assets/images/blendImage2.png")
        )));
        addGameObjectToScene(obj2);
    }

    private void loadResources() {
        String spriteSheetPath = "assets/images/spritesheet.png";
        var spriteSheet = new Spritesheet(getTexture(spriteSheetPath), 16, 16, 26, 0);
        addSpriteSheet(spriteSheetPath, spriteSheet);
        getShader("assets/shaders/default.glsl");


    }

    @Override
    public void update(float deltaTime) {
        gameObjects.forEach(g -> g.update(deltaTime));
        renderer.render();
    }
}
