package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.ray3k.cosmicinfiltrator.Utils;
import com.ray3k.cosmicinfiltrator.screens.CreditsScreen;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class UfoBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    private SpineBehaviour sb2;

    public UfoBehaviour(GameObject gameObject) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, enemyMothershipSkeletonData, enemyMothershipAnimationStateData);
        sb.setRenderOrder(DEPTH_ENEMY_SKY);
        sb2 = new SpineBehaviour(go, shadowSkeletonData, shadowAnimationStateData);
        sb2.setRenderOrder(DEPTH_PARTICLES);
        setRenderOrder(DEPTH_PARTICLES);
    }

    @Override
    public void start() {
        var playerX = PlayerBehaviour.playerBehaviour.go.body.getPosition().x;
        var playerY = PlayerBehaviour.playerBehaviour.go.body.getPosition().y;

        go.body.setTransform(gameViewport.getWorldWidth() / 2, -10, MathUtils.degRad * 90);
        go.body.setLinearVelocity(0, 5f);
    }

    @Override
    public void fixedUpdate() {
        if (go.body.getPosition().y > gameViewport.getWorldHeight() + 1) {
            core.setScreen(new CreditsScreen());
        }
        sb2.skeleton.findBone("bone").setRotation(-MathUtils.radDeg * go.body.getAngle());
    }
}
