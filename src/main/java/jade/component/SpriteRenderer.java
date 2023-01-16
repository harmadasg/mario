package jade.component;

import jade.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private static final Vector4f WHITE = new Vector4f(1, 1, 1, 1);

    private Vector4f color;
    private Sprite sprite;
    private Transform lastTransform;
    private boolean isDirty;

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = WHITE;
        this.isDirty = true;
    }

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
        this.isDirty = true;
    }

    @Override
    public void update(float deltaTime) {
        if (!lastTransform.equals(gameObject.getTransform())) {
            gameObject.getTransform().copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void start() {
        lastTransform = gameObject.getTransform();
    }

    public boolean hasTexture() {
        return sprite.hasTexture();
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.color = color;
            this.isDirty = true;
        }
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public Vector2f[] getTextCoordinates() {
        return sprite.getTextureCoordinates();
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void clean() {
        isDirty = false;
    }
}
