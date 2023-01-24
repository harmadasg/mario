package util;

import com.google.gson.*;
import jade.GameObject;
import jade.Transform;
import jade.component.Component;

import java.lang.reflect.Type;

public class GameObjectAdapter implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);
        var gameObject = new GameObject(name, transform, zIndex);
        JsonArray components = jsonObject.getAsJsonArray("components");
        components.forEach(c ->
                gameObject.addComponent(context.deserialize(c, Component.class)));
        return gameObject;
    }
}
