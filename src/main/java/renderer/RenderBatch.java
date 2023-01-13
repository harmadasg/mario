package renderer;

import jade.Window;
import jade.component.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static util.AssetPool.getShader;

public class RenderBatch {

    // Vertices with the appropriate properties
    // *    *
    // *    *
    private static final float[][] POSITIONS = new float[][]{
            {1.f, 1.f},
            {1.f, 0.f},
            {0.f, 0.f},
            {0.f, 1.f}
    };
    // Vertex
    // ========
    // Pos              Color                           tex coords      tex id
    // float, float,    float, float, float, float      float, float    float

    private static final int POSITION_SIZE = 2;
    private static final int COLOR_SIZE = 4;
    private static final int TEXTURE_COORDINATE_SIZE = 2;
    private static final int TEXTURE_ID_SIZE = 1;
    private static final int POSITION_OFFSET = 0;
    private static final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
    private static final int TEXTURE_COORDINATE_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private static final int TEXTURE_ID_OFFSET = TEXTURE_COORDINATE_OFFSET + TEXTURE_COORDINATE_SIZE * Float.BYTES;
    private static final int VERTEX_SIZE = 9;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private static final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private static final int MAX_TEXTURE_SIZE = 8;

    private final SpriteRenderer[] sprites;
    private final List<Texture> textures;
    private final float[] vertices;

    private int numberOfSprites;
    private boolean hasRoom;
    private int vaoId, vboId;
    private final int maxBatchSize;
    private final Shader shader;

    public RenderBatch(int maxBatchSize) {
        this.shader = getShader("assets/shaders/default.glsl");
        this.textures = new ArrayList<>();
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

        enableBufferAttributePointers();
    }

    public void render() {
        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.upload("uProjection", Window.get().getScene().getCamera().getProjectionMatrix());
        shader.upload("uView", Window.get().getScene().getCamera().getViewMatrix());

        // Bind textures
        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.upload("uTextures", textureSlots);


        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, numberOfSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        textures.forEach(Texture::unbind);
        shader.detach();
    }

    public void addSprite(SpriteRenderer spriteRenderer) {
        // Get index and add renderObject
        int index = numberOfSprites;
        sprites[index] = spriteRenderer;
        numberOfSprites++;

        if (spriteRenderer.hasTexture()) {
            addTexture(spriteRenderer);
        }

        // Add properties to local vertices array
        loadVertexProperties(index);

        if (numberOfSprites >= maxBatchSize) {
            hasRoom = false;
        }
    }

    public boolean canSpriteBeAdded(SpriteRenderer spriteRenderer) {
        var texture = spriteRenderer.getTexture();
        return hasRoom
                || texture == null
                || textures.contains(texture)
                || textures.size() < MAX_TEXTURE_SIZE;
    }

    private void enableBufferAttributePointers() {
        enableBufferAttributePointer(0, POSITION_SIZE, POSITION_OFFSET);
        enableBufferAttributePointer(1, COLOR_SIZE, COLOR_OFFSET);
        enableBufferAttributePointer(2, TEXTURE_COORDINATE_SIZE, TEXTURE_COORDINATE_OFFSET);
        enableBufferAttributePointer(3, TEXTURE_ID_SIZE, TEXTURE_ID_OFFSET);
    }

    private void enableBufferAttributePointer(int index, int positionSize, int offset) {
        glVertexAttribPointer(index, positionSize, GL_FLOAT, false, VERTEX_SIZE_BYTES, offset);
        glEnableVertexAttribArray(index);
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

    private void addTexture(SpriteRenderer spriteRenderer) {
        var texture = spriteRenderer.getTexture();
        if (!textures.contains(texture)) {
            textures.add(texture);
        }
    }

    private void loadVertexProperties(int index) {
        var sprite = sprites[index];

        int textureId = getTextureId(sprite);
        Vector4f color = sprite.getColor();
        Vector2f[] textCoordinates = sprite.getTextCoordinates();

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;
        for (int i = 0; i < 4; offset += VERTEX_SIZE, i++) {
            loadPosition(sprite, offset, POSITIONS[i]);
            loadColor(offset, color);
            loadTextureCoordinates(offset, textCoordinates[i]);
            loadTextureId(offset, textureId);
        }
    }

    private int getTextureId(SpriteRenderer sprite) {
        // [0, tex, tex, tex, tex]
        int textureId = 0;
        if (sprite.hasTexture()) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == sprite.getTexture()) {
                    textureId = i + 1;
                }
            }
        }
        return textureId;
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

    private void loadTextureCoordinates(int offset, Vector2f coordinate) {
        vertices[offset + 6] = coordinate.x;
        vertices[offset + 7] = coordinate.y;
    }

    private void loadTextureId(int offset, int id) {
        vertices[offset + 8] = id;
    }
}
