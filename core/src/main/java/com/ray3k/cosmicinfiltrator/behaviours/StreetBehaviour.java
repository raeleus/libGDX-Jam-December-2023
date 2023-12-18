package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ray3k.cosmicinfiltrator.TimeLine;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;

public class StreetBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    private boolean createdChild;
    private GameObject parent;
    public static final float BORDER = 1;

    public StreetBehaviour(GameObject gameObject, GameObject parent) {
        super(gameObject);
        this.parent = parent;

        new Box2dBehaviour(BodyDefType.KinematicBody, go);
        sb = TimeLine.getStreet(go);
        sb.setRenderOrder(DEPTH_STREET);
        new WallBehaviour(go);
    }

    @Override
    public void start() {
        var fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;

        var slot = go.skeleton.findSlot("bbox-collision-left");
        if (slot != null) sb.createChainShapeFixture(slot, fixtureDef);

        slot = go.skeleton.findSlot("bbox-collision-right");
        if (slot != null) sb.createChainShapeFixture(slot, fixtureDef);

        slot = go.skeleton.findSlot("bbox-collision-middle");
        if (slot != null) sb.createChainShapeFixture(slot, fixtureDef);

        var dust = new GameObject(unBox);
        new Box2dBehaviour(BodyDefType.KinematicBody, dust);
        new DustSensorBehaviour(dust, sb.skeleton.getData(), parent);

        if (parent == null) return;
        var yPosition = parent.body.getPosition().y + gameViewport.getWorldHeight();
        go.body.setTransform(0, yPosition, 0);
        go.body.setLinearVelocity(0, -PlayerBehaviour.speed);

    }

    @Override
    public void fixedUpdate() {
        go.body.setLinearVelocity(0, -PlayerBehaviour.speed);
        if (!createdChild && go.body.getPosition().y < BORDER) {
            createdChild = true;

            var street = new GameObject(unBox);
            new StreetBehaviour(street, go);
        }

        if (go.body.getPosition().y < -gameViewport.getWorldHeight() - BORDER) go.destroy();
    }
}
