package jade.scene;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene implements Scene {

    private static final String VERTEX_SHADER_SOURCE = "    #version 330 core\n" +
            "    layout (location=0) in vec3 aPos;\n" +
            "    layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "    out vec4 fColor;\n" +
            "\n" +
            "    void main()\n" +
            "    {\n" +
            "        fColor = aColor;\n" +
            "        gl_Position = vec4(aPos, 1.0);\n" +
            "    }";
    private static final String FRAGMENT_SHADER_SOURCE = "    #version 330 core\n" +
            "\n" +
            "    in vec4 fColor;\n" +
            "\n" +
            "    out vec4 color;\n" +
            "\n" +
            "    void main()\n" +
            "    {\n" +
            "        color = fColor;\n" +
            "    }";

    private int vertexId, fragmentId, shaderProgram;
    private int vaoId, vboId, eboId;

    private float[] vertexArray = {
        // position             // color
        0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
        -0.5f, 0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
        0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, // Top right    2
        -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f // Bottom left   3
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
        // ============================================================
        // Compile and link shaders
        // ============================================================

        // First load and compile the vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexId, VERTEX_SHADER_SOURCE);
        glCompileShader(vertexId);

        // Check for errors in compilation
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (GL_FALSE == success) {
            int length = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            String infoLog = glGetShaderInfoLog(vertexId, length);
            throw new IllegalStateException(
                    "ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.\n" + infoLog);
        }

        // First load and compile the vertex shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentId, FRAGMENT_SHADER_SOURCE);
        glCompileShader(fragmentId);

        // Check for errors in compilation
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (GL_FALSE == success) {
            int length = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            String infoLog = glGetShaderInfoLog(fragmentId, length);
            throw new IllegalStateException(
                    "ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed.\n" + infoLog);
        }

        // Link shaders and  check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexId);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);

        // Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
            if (GL_FALSE == success) {
                int length = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
                String infoLog = glGetProgramInfoLog(shaderProgram, length);
                throw new IllegalStateException(
                        "ERROR: 'defaultShader.glsl'\n\tLinking of shaders failed.\n" + infoLog);
        }

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
        glUseProgram(shaderProgram);
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

        glUseProgram(0);
    }
}
