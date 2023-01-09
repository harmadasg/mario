package renderer;

import jade.Window;
import jade.component.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static util.AssetPool.getShader;

public class RenderBatch {
    // Vertex
    // ========
    // Pos              Color
    // float, float,    float, float, float, float

    private static final int POSITION_SIZE = 2;
    private static final int COLOR_SIZE = 4;
    private static final int POSITION_OFFSET = 0;
    private static final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
    private static final int VERTEX_SIZE = 6;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private final SpriteRenderer[] sprites;
    private final float[] vertices;

    private int numberOfSprites;
    private boolean hasRoom;
    private int vaoId, vboId;
    private final int maxBatchSize;
    private final Shader shader;

    public RenderBatch(int maxBatchSize) {
        this.shader = getShader("assets/shaders/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices quads
        this. vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numberOfSprites = 0;
        this.hasRoom = true;
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Allocate space for vertices
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboId = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POSITION_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void render() {
        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.upload("uProjection", Window.get().getScene().getCamera().getProjectionMatrix());
        shader.upload("uView", Window.get().getScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, numberOfSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    public void addSprite(SpriteRenderer spriteRenderer) {
        // Get index and add renderObject
        int index = numberOfSprites;
        sprites[index] = spriteRenderer;
        numberOfSprites++;

        // Add properties to local vertices array
        loadVertexProperties(index);

        if (numberOfSprites >= maxBatchSize) {
            hasRoom = false;
        }
    }

    public boolean isHasRoom() {
        return hasRoom;
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArranIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArranIndex] = offset + 3;
        elements[offsetArranIndex + 1] = offset + 2;
        elements[offsetArranIndex + 2] = offset;

        // Triangle 2
        elements[offsetArranIndex + 3] = offset;
        elements[offsetArranIndex + 4] = offset + 2;
        elements[offsetArranIndex + 5] = offset + 1;
    }

    private void loadVertexProperties(int index) {
        var sprite = sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // Add vertices with the appropriate properties
        // *    *
        // *    *
        float[][] positions = {
                {1.f, 1.f},
                {1.f, 0.f},
                {0.f, 0.f},
                {0.f, 1.f}
            };

        for (int i = 0; i < 4; offset += VERTEX_SIZE, i++) {
            loadPosition(sprite, offset, positions[i]);
            loadColor(offset, color);
        }
    }

    private void loadPosition(SpriteRenderer sprite, int offset, float[] position) {
        Vector2f spritePosition = sprite.getGameObject().getTransform().getPosition();
        Vector2f spriteScale = sprite.getGameObject().getTransform().getScale();

        vertices[offset] = spritePosition.x + (position[0] * spriteScale.x);
        vertices[offset + 1] = spritePosition.y + (position[1] * spriteScale.y);
    }

    private void loadColor(int offset, Vector4f color) {
        vertices[offset + 2] = color.x;
        vertices[offset + 3] = color.y;
        vertices[offset + 4] = color.z;
        vertices[offset + 5] = color.w;
    }
}
