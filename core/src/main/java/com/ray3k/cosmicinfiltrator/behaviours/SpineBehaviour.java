package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;
import com.ray3k.cosmicinfiltrator.Utils;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;

import static com.ray3k.cosmicinfiltrator.Core.*;

public class SpineBehaviour extends BehaviourAdapter {
    public Skeleton skeleton;
    public AnimationState animationState;
    public SkeletonBounds skeletonBounds;

    public SpineBehaviour(GameObject gameObject, SkeletonData skeletonData, AnimationStateData animationStateData) {
        super(gameObject);

        skeleton = new Skeleton(skeletonData);
        go.skeleton = skeleton;

        animationState = new AnimationState(animationStateData);
        go.animationState = animationState;

        skeletonBounds = new SkeletonBounds();
    }

    @Override
    public void awake() {
        skeleton.setPosition(go.body.getPosition().x, go.body.getPosition().y);
        animationState.update(0);
        animationState.apply(skeleton);
        skeleton.updateWorldTransform();
        skeletonBounds.update(skeleton, true);
    }

    @Override
    public void start() {

    }

    @Override
    public void fixedUpdate() {
        skeleton.setPosition(go.body.getPosition().x, go.body.getPosition().y);
        skeleton.getRootBone().setRotation(go.body.getAngle() * MathUtils.radDeg);
    }

    @Override
    public void update(float delta) {
        animationState.update(delta);
        skeleton.updateWorldTransform();
        animationState.apply(skeleton);
        skeletonBounds.update(skeleton, true);
    }

    @Override
    public void render(Batch batch) {
        skeletonRenderer.draw(batch, skeleton);
    }

    public void createChainShapeFixture(String slotName, FixtureDef fixtureDef) {
        createChainShapeFixture(skeleton.findSlot(slotName), fixtureDef);
    }

    public void createChainShapeFixture(Slot slot, FixtureDef fixtureDef) {
        var bbox = (BoundingBoxAttachment) slot.getAttachment();
        var worldVertices = new float[bbox.getWorldVerticesLength()];
        bbox.computeWorldVertices(slot, 0, bbox.getWorldVerticesLength(),  worldVertices, 0, 2);

        if (Utils.isClockwise(worldVertices)) worldVertices = Utils.reverseVertecies(worldVertices);

        new CreateChainShapeFixtureBehaviour(worldVertices, null, fixtureDef, go);
    }

    public void createPolygonFixture(String slotName, FixtureDef fixtureDef) {
        createPolygonFixture(skeleton.findSlot(slotName), fixtureDef);
    }

    public void createPolygonFixture(Slot slot, FixtureDef fixtureDef) {
        var bbox = (BoundingBoxAttachment) slot.getAttachment();
        var worldVertices = new float[bbox.getWorldVerticesLength()];
        bbox.computeWorldVertices(slot, 0, bbox.getWorldVerticesLength(),  worldVertices, 0, 2);

        if (Utils.isClockwise(worldVertices)) worldVertices = Utils.reverseVertecies(worldVertices);

        new CreatePolygonShapeFixtureBehaviour(worldVertices, null, fixtureDef, go);
    }
}
