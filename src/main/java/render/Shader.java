package render;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private static final String PATTERN = "(#type)( )+([a-zA-Z]+)";

    private final String filePath;

    private int shaderProgramId;
    private String vertexSource;
    private String fragmentSource;

    public Shader(String filePath) {
        this.filePath = filePath;
        extractSources(readFile(filePath));
    }

    public void compileAndLink() {
        int vertexId = compileShader(GL_VERTEX_SHADER, vertexSource);
        int fragmentId = compileShader(GL_FRAGMENT_SHADER, fragmentSource);
        linkShaders(vertexId, fragmentId);
    }

    public void use() {
        glUseProgram(shaderProgramId);
    }

    public void detach() {
        glUseProgram(0);
    }

    private String readFile(String filePath) {
        String source;
        try {
            source = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error: Could not open file for shader: " + filePath);
        }
        return source;
    }

    private void extractSources(String source) {
        // Only works if vertex source is before fragment source!
        String[] splitString = source.split(PATTERN);
        vertexSource = splitString[1];
        fragmentSource = splitString[2];
    }

    private int compileShader(int type, String source) {
        int id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);
        validateCompilation(id);
        return id;
    }

    private void validateCompilation(int id) {
        int success = glGetShaderi(id, GL_COMPILE_STATUS);
        if (GL_FALSE == success) {
            int length = glGetShaderi(id, GL_INFO_LOG_LENGTH);
            String infoLog = glGetShaderInfoLog(id, length);
            throw new IllegalStateException(
                    "ERROR: '" + filePath + "'\n\tShader compilation failed.\n" + infoLog);
        }
    }

    private void linkShaders(int vertexId, int fragmentId) {
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);
        validateLinking(shaderProgramId);
    }

    private void validateLinking(int id) {
        int success = glGetProgrami(id, GL_LINK_STATUS);
        if (GL_FALSE == success) {
            int length = glGetProgrami(id, GL_INFO_LOG_LENGTH);
            String infoLog = glGetProgramInfoLog(id, length);
            throw new IllegalStateException(
                    "ERROR: '" + filePath + "'\n\tLinking of shaders failed.\n" + infoLog);
        }
    }
}
