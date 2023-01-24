package util;

import jade.component.Spritesheet;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static final Map<String, Shader> SHADERS = new HashMap<>();
    private static final Map<String, Texture> TEXTURES = new HashMap<>();
    private static final Map<String, Spritesheet> SPRITE_SHEETS = new HashMap<>();

    public static Shader getShader(String resourceName) {
        var file = new File(resourceName);
        if (SHADERS.containsKey(file.getAbsolutePath())) {
            return SHADERS.get(file.getAbsolutePath());
        } else {
            var shader = new Shader(resourceName);
            SHADERS.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        var file = new File(resourceName);
        if (TEXTURES.containsKey(file.getAbsolutePath())) {
            return TEXTURES.get(file.getAbsolutePath());
        } else {
            var texture = new Texture();
            texture.init(resourceName);
            TEXTURES.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, Spritesheet spriteSheet) {
        SPRITE_SHEETS.put(resourceName, spriteSheet);
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        Spritesheet value = SPRITE_SHEETS.get(resourceName);
        if (value == null) {
            throw new IllegalStateException(
                    "Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to asset pool");
        }
        return value;
    }

}
