package jade;

public abstract class Component {

    protected GameObject gameObject;

    public abstract void update(float deltaTime);

    public void start() {}

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
