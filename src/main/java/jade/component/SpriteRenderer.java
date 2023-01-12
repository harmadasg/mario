package jade.component;

import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private static final Vector4f WHITE = new Vector4f(1, 1, 1, 1);
    private static final Vector2f[] DUMMY = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    private final Vector4f color;
    private final Texture texture;
    private Vector2f[] textCoordinates;
    // (0, 1)
    // (0, 0)
    // (1, 1)
    // (1, 0)

    public SpriteRenderer(Texture texture) {
        this.texture = texture;
        this.color = WHITE;
    }

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.texture = null;
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void start() {
    }

    public boolean hasTexture() {
        return texture != null;
    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextCoordinates() {
        return DUMMY; // uv coordinates, only temporary
    }
}
