package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jade.GameObject;
import jade.component.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SerializerHelper {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Component.class, new ComponentAdapter())
            .registerTypeAdapter(GameObject.class, new GameObjectAdapter())
            .create();
    private static final String FILE_NAME = "level.json";

    public static void serialize(List<GameObject> gameObjects) {
        try (var writer = new FileWriter(FILE_NAME)) {
            GSON.toJson(gameObjects, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameObject[] deserialize() {
        String json = "";
        try {
            json = Files.readString(Paths.get(FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return GSON.fromJson(json, GameObject[].class);
    }
}
