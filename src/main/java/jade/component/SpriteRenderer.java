package jade.component;

import imgui.ImGui;
import jade.Transform;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private static final Vector4f WHITE = new Vector4f(1, 1, 1, 1);

    @Getter
    private Vector4f color;
    private Sprite sprite;

    private transient Transform lastTransform;
    @Getter
    private transient boolean isDirty;

    public SpriteRenderer() {
        this.color = WHITE;
        this.sprite = new Sprite(null);
        this.isDirty = true;

    }

    public SpriteRenderer(Sprite sprite) {
        this();
        this.sprite = sprite;
    }

    public SpriteRenderer(Vector4f color) {
        this();
        this.color = color;
    }

    @Override
    public void renderImgui() {
        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color Picker: ", imColor)) {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            isDirty = true;
        }

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

    public void clean() {
        isDirty = false;
    }
}
