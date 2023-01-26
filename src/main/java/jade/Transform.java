package jade;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.joml.Vector2f;

@Value
@RequiredArgsConstructor
public class Transform {

    Vector2f position;
    Vector2f scale;

    public Transform(Transform transform) {
        this.position = transform.position;
        this.scale = transform.scale;
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }
}
