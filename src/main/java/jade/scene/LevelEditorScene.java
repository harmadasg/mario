package jade.scene;

import jade.Camera;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import render.Shader;
import util.Time;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene implements Scene {

    private int vaoId, vboId, eboId;
    private final Shader shader;
    private final Camera camera;

    private float[] vertexArray = {
        // position             // color
        100.5f, 0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
        0.5f, 100.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
        100.5f, 100.5f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f, // Top right    2
        0.5f, 0.5f, 0.0f,        1.0f, 1.0f, 0.0f, 1.0f // Bottom left   3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
        /*
                x           x



                x           x
         */
            2, 1, 0, // Top right triangle
            0, 1, 3 // Bottom left triangle
    };

    public LevelEditorScene() {
        camera = new Camera(new Vector2f());
        shader = new Shader("assets/shaders/default.glsl");
        shader.compileAndLink();

        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create a float buffer of vertices
        var vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        var elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (positionSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float deltaTime) {
        // Bind shader program
        shader.use();
        shader.upload("uProjection", camera.getProjectionMatrix());
        shader.upload("uView", camera.getViewMatrix());
        shader.upload("uTime", Time.getTime());
        // Bind the VAO that we're using
        glBindVertexArray(vaoId);

        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        shader.detach();
    }
}
