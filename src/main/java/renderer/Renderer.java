package renderer;

import jade.GameObject;
import jade.component.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private static final int MAX_BATCH_SIZE = 1000;
    private final List<RenderBatch> batches;

    public Renderer() {
        batches = new ArrayList<>();
    }

    public void render() {
        batches.forEach(RenderBatch::render);
    }

    public void add(GameObject gameObject) {
        var spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        add(spriteRenderer);
    }

    private void add(SpriteRenderer sprite) {
        var batch = batches.stream().filter(RenderBatch::isHasRoom).findFirst();
        batch.ifPresentOrElse(b -> b.addSprite(sprite),
                () -> createBatchAndAdd(sprite));
    }

    private void createBatchAndAdd(SpriteRenderer sprite) {
        var newBatch = new RenderBatch(MAX_BATCH_SIZE);
        newBatch.start();
        batches.add(newBatch);
        newBatch.addSprite(sprite);
    }

}
