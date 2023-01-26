package jade;

import jade.component.Component;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GameObject {

    private final String name;
    @Getter
    private final Transform transform;
    @Getter
    private final int zIndex;
    private final List<Component> components = new ArrayList<>();

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

    public void renderImgui() {
        components.forEach(Component::renderImgui);
    }

    public void update(float deltaTime) {
        components.forEach(c -> c.update(deltaTime));
    }

    public void start() {
        components.forEach(Component::start);
    }

}
