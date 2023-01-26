package renderer;

import lombok.RequiredArgsConstructor;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

@RequiredArgsConstructor
class UniformUploader {

    private final Shader shader;

    void upload(String varName, Object value) {
        if (value instanceof Matrix4f)
            uploadMat4f(varName, (Matrix4f) value);
        else if (value instanceof Matrix3f)
            uploadMat3f(varName, (Matrix3f) value);
        else if (value instanceof Vector4f)
            uploadVec4f(varName, (Vector4f) value);
        else if (value instanceof Vector3f)
            uploadVec3f(varName, (Vector3f) value);
        else if (value instanceof Vector2f)
            uploadVec2f(varName, (Vector2f) value);
        else if (value instanceof Float)
            uploadFloat(varName, (Float) value);
        else if (value instanceof Integer)
            uploadInt(varName, (Integer) value);
        else if (value instanceof int[])
            uploadIntArray(varName, (int[]) value);
    }

    private void uploadMat4f(String varName, Matrix4f mat4) {
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(getLocation(varName), false, matBuffer);
    }

    private void uploadMat3f(String varName, Matrix3f mat3) {
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(getLocation(varName), false, matBuffer);
    }

    private void uploadVec4f(String varName, Vector4f vec) {
        glUniform4f(getLocation(varName), vec.x, vec.y, vec.z, vec.w);
    }

    private void uploadVec3f(String varName, Vector3f vec) {
        glUniform3f(getLocation(varName), vec.x, vec.y, vec.z);
    }

    private void uploadVec2f(String varName, Vector2f vec) {
        glUniform2f(getLocation(varName), vec.x, vec.y);
    }

    private void uploadFloat(String varName, float val) {
        glUniform1f(getLocation(varName), val);
    }

    private void uploadInt(String varName, int val) {
        glUniform1i(getLocation(varName), val);
    }

    private void uploadIntArray(String varName, int[] val) {
        glUniform1iv(getLocation(varName), val);
    }

    private int getLocation(String varName) {
        shader.use();
        return glGetUniformLocation(shader.getShaderProgramId(), varName);
    }
}
