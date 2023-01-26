package jade;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private static final float GRID_TILE_SIZE = 32.0f;

    @Getter
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();
    private final Vector2f position;

    public Camera(Vector2f position) {
        this.position = position;
        adjustProjection();
    }

    public Matrix4f getViewMatrix() {
        var cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        var cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.setLookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);
        return viewMatrix;
    }

    private void adjustProjection() {
        projectionMatrix.setOrtho(0.0f, GRID_TILE_SIZE * 40.0f, 0.0f, GRID_TILE_SIZE * 21.0f, 0.0f, 100.0f);
    }
}
