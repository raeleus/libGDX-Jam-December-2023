package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.ray3k.cosmicinfiltrator.Utils;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class EnemyHeliBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    private SpineBehaviour sb2;
    private float delay = FIRING_RATE;
    private Array<Target> targets;
    private static final float FIRING_RATE = 2f;

    public EnemyHeliBehaviour(GameObject gameObject) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, enemyHeliSkeletonData, enemyHeliAnimationStateData);
        sb.setRenderOrder(DEPTH_ENEMY_SKY);
        sb2 = new SpineBehaviour(go, shadowSkeletonData, shadowAnimationStateData);
        sb2.setRenderOrder(DEPTH_PARTICLES);
        setRenderOrder(DEPTH_PARTICLES);
    }

    @Override
    public void start() {
        go.body.setTransform(gameViewport.getWorldWidth() + 1, 5, MathUtils.degRad * 90);
        go.body.setLinearVelocity(-1, 0f);

        targets = new Array<>();
    }

    @Override
    public void fixedUpdate() {
        if (go.body.getPosition().x < -1) go.destroy();

        var playerX = PlayerBehaviour.playerBehaviour.go.body.getPosition().x;
        var playerY = PlayerBehaviour.playerBehaviour.go.body.getPosition().y;
        var thisX = go.body.getPosition().x;
        var thisY = go.body.getPosition().y;

        sb2.skeleton.findBone("bone").setRotation(-MathUtils.radDeg * go.body.getAngle());

        go.body.setTransform(go.body.getPosition().x, go.body.getPosition().y, MathUtils.degRad * Utils.pointDirection(thisX, thisY, playerX, playerY));
    }

    @Override
    public void update(float delta) {
        delay -= delta;
        if (delay <= 0) {
            delay = FIRING_RATE;

            var playerX = PlayerBehaviour.playerBehaviour.go.body.getPosition().x;
            var playerY = PlayerBehaviour.playerBehaviour.go.body.getPosition().y;

            var iter = targets.iterator();
            while (iter.hasNext()) {
                var target = iter.next();
                iter.remove();

                var missile = new GameObject(unBox);
                new EnemyMissileBehaviour(missile, go.body.getPosition().x, go.body.getPosition().y, target.x, target.y);
                sndLaunch.play();
            }

            targets.add(new Target(playerX, playerY));
            var crosshair = new GameObject(unBox);
            new CrossHairBehaviour(crosshair) {
                @Override
                public void start() {
                    super.start();
                    crosshair.body.setTransform(playerX, playerY, 0);
                }
            };
        }
    }

    @Override
    public void render(Batch batch) {
    }

    private class Target {
        float x, y;

        public Target(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
