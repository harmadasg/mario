package jade.scene;

import jade.Camera;
import jade.GameObject;
import jade.Transform;
import jade.component.Sprite;
import jade.component.SpriteRenderer;
import jade.component.Spritesheet;
import org.joml.Vector2f;

import static util.AssetPool.*;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)), 0);
        obj1.addComponent(new SpriteRenderer(new Sprite(
                getTexture("assets/images/blendImage1.png")
        )));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 1);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                getTexture("assets/images/blendImage2.png")
        )));
        this.addGameObjectToScene(obj2);
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
