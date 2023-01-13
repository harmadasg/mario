package jade.component;

import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private static final Vector4f WHITE = new Vector4f(1, 1, 1, 1);

    private final Vector4f color;
    private final Sprite sprite;

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = WHITE;
    }

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void start() {
    }

    public boolean hasTexture() {
        return sprite.hasTexture();
    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTextCoordinates() {
        return sprite.getTextureCoordinates();
    }
}
