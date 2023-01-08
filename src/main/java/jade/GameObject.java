package jade;

import jade.component.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private final String name;
    private Transform transform;
    private final List<Component> components;


    public GameObject(String name) {
        this(name, new Transform());
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return components.stream()
                .filter(c -> componentClass.isAssignableFrom(c.getClass()))
                .findFirst()
                .map(componentClass::cast)
                .orElseThrow();
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.removeIf(c -> componentClass.isAssignableFrom(c.getClass()));
    }

    public void addComponent(Component component) {
        components.add(component);
        component.setGameObject(this);
    }

    public void update(float deltaTime) {
        components.forEach(c -> c.update(deltaTime));
    }

    public void start() {
        components.forEach(Component::start);
    }

    public Transform getTransform() {
        return transform;
    }
}
