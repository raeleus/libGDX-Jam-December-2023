package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.ray3k.cosmicinfiltrator.Utils;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;

import static com.ray3k.cosmicinfiltrator.Core.gameViewport;

public class DustSensorBehaviour extends BehaviourAdapter {
    public Skeleton skeleton;
    private GameObject parent;

    public DustSensorBehaviour(GameObject gameObject, SkeletonData skeletonData, GameObject parent) {
        super(gameObject);
        this.parent = parent;

        skeleton = new Skeleton(skeletonData);
        go.skeleton = skeleton;
    }

    @Override
    public void awake() {
        skeleton.setPosition(go.body.getPosition().x, go.body.getPosition().y);
        skeleton.updateWorldTransform();
    }

    @Override
    public void start() {
        go.body.setLinearVelocity(0, -PlayerBehaviour.speed);

        var fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        var slot = go.skeleton.findSlot("bbox-dust-left");
        if (slot != null) createChainShapeFixture(slot, fixtureDef);

        slot = go.skeleton.findSlot("bbox-dust-middle-left");
        if (slot != null) createChainShapeFixture(slot, fixtureDef);

        slot = go.skeleton.findSlot("bbox-dust-middle-right");
        if (slot != null) createChainShapeFixture(slot, fixtureDef);

        slot = go.skeleton.findSlot("bbox-dust-right");
        if (slot != null) createChainShapeFixture(slot, fixtureDef);

        if (parent == null) return;
        var yPosition = parent.body.getPosition().y + gameViewport.getWorldHeight();
        go.body.setTransform(0, yPosition, 0);
    }

    @Override
    public void fixedUpdate() {
        go.body.setLinearVelocity(0, -PlayerBehaviour.speed);
        if (go.body.getPosition().y < -gameViewport.getWorldHeight() - StreetBehaviour.BORDER) go.destroy();
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(Batch batch) {

    }

    public void createChainShapeFixture(String slotName, FixtureDef fixtureDef) {
        createChainShapeFixture(skeleton.findSlot(slotName), fixtureDef);
    }

    public void createChainShapeFixture(Slot slot, FixtureDef fixtureDef) {
        var bbox = (BoundingBoxAttachment) slot.getAttachment();
        var worldVertices = new float[bbox.getWorldVerticesLength()];
        bbox.computeWorldVertices(slot, 0, bbox.getWorldVerticesLength(),  worldVertices, 0, 2);

        if (Utils.isClockwise(worldVertices)) worldVertices = Utils.reverseVertecies(worldVertices);

        new CreateEdgeShapeFixtureBehaviour(worldVertices, null, fixtureDef, go);
    }
}
