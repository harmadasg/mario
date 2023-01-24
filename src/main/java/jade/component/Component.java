package jade.component;

import jade.GameObject;

public abstract class Component {

    protected transient GameObject gameObject;

    public void update(float deltaTime) {
    }

    public void renderImgui() {
    }

    public void start() {}

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
