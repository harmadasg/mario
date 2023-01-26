package jade.component;

import jade.GameObject;
import lombok.Getter;
import lombok.Setter;

public abstract class Component {

    @Getter
    @Setter
    protected transient GameObject gameObject;

    public void update(float deltaTime) {
    }

    public void renderImgui() {
    }

    public void start() {}
}
