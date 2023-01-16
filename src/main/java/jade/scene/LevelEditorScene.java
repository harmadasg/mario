package jade.scene;

import jade.Camera;
import jade.GameObject;
import jade.Transform;
import jade.component.SpriteRenderer;
import jade.component.Spritesheet;
import org.joml.Vector2f;

import static util.AssetPool.*;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    public LevelEditorScene() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = getSpritesheet("assets/images/spritesheet.png");
        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(15)));
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
        spriteFlipTimeLeft -= deltaTime;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 4) {
                spriteIndex = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }

        gameObjects.forEach(g -> g.update(deltaTime));
        renderer.render();
    }
}
