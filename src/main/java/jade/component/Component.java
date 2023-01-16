package jade.component;

import jade.GameObject;

public abstract class Component {

    protected GameObject gameObject;

    public abstract void update(float deltaTime);

    public void start() {}

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
