package renderer;

import jade.GameObject;
import jade.component.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparingInt;

public class Renderer {

    private static final int MAX_BATCH_SIZE = 1000;
    private final List<RenderBatch> batches = new ArrayList<>();

    public void render() {
        batches.forEach(RenderBatch::render);
    }

    public void add(GameObject gameObject) {
        var spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        var batch = batches.stream()
                .filter(b -> b.canSpriteBeAdded(spriteRenderer))
                .findFirst();
        batch.ifPresentOrElse(b -> b.addSprite(spriteRenderer),
                () -> createBatchAndAdd(spriteRenderer));
    }

    private void createBatchAndAdd(SpriteRenderer sprite) {
        var newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.getGameObject().getZIndex());
        newBatch.start();
        batches.add(newBatch);
        newBatch.addSprite(sprite);
        batches.sort(comparingInt(RenderBatch::getZIndex));
    }

}
