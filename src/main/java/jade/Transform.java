package jade;

import org.joml.Vector2f;

import java.util.Objects;

public class Transform {

    private final Vector2f position;
    private final Vector2f scale;

    public Transform() {
        this(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position) {
        this(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform(Transform transform) {
        this.position = transform.position;
        this.scale = transform.scale;
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transform transform = (Transform) o;
        return position.equals(transform.position) && scale.equals(transform.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, scale);
    }
}
