package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.ray3k.cosmicinfiltrator.Utils;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class EnemyJetBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    private SpineBehaviour sb2;
    private float delay = 1f;
    private Array<Target> targets;

    public EnemyJetBehaviour(GameObject gameObject) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, enemyJetSkeletonData, enemyJetAnimationStateData);
        sb.setRenderOrder(DEPTH_ENEMY_SKY);
        sb2 = new SpineBehaviour(go, shadowSkeletonData, shadowAnimationStateData);
        sb2.setRenderOrder(DEPTH_PARTICLES);
        setRenderOrder(DEPTH_PARTICLES);
    }

    @Override
    public void start() {
        var playerX = PlayerBehaviour.playerBehaviour.go.body.getPosition().x;
        var playerY = PlayerBehaviour.playerBehaviour.go.body.getPosition().y;

        go.body.setTransform(playerX, -10, MathUtils.degRad * 90);
        go.body.setLinearVelocity(0, 0f);

        targets = new Array<>();
        targets.add(new Target(playerX, playerY), new Target(playerX, playerY + 1), new Target(playerX, playerY - 1));

        for (var target : targets) {
            var crosshair = new GameObject(unBox);
            new CrossHairBehaviour(crosshair) {
                @Override
                public void start() {
                    super.start();
                    crosshair.body.setTransform(target.x, target.y, 0);
                }
            };
        }
    }

    @Override
    public void fixedUpdate() {
        if (go.body.getPosition().y > gameViewport.getWorldHeight() + 1) go.destroy();

        var playerX = PlayerBehaviour.playerBehaviour.go.body.getPosition().x;
        var playerY = PlayerBehaviour.playerBehaviour.go.body.getPosition().y;

        var iter = targets.iterator();
        while (iter.hasNext()) {
            var target = iter.next();
            if (go.body.getPosition().y > target.y) {
                sndExplosion.play();
                iter.remove();
                var explosion = new GameObject(unBox);
                var eb = new ExplosionBehaviour(explosion);
                eb.targetX = target.x;
                eb.targetY = target.y;

                if (Utils.pointDistance(target.x, target.y, playerX, playerY) < 1) PlayerBehaviour.playerBehaviour.die();

                for (var behaviour : unBox.findBehaviours(CivillianBehaviour.class)) {
                    if (behaviour.go.body == null) continue;
                    if (Utils.pointDistance(target.x, target.y, behaviour.go.body.getPosition().x, behaviour.go.body.getPosition().y) < 1) behaviour.die();
                }

                for (var behaviour : unBox.findBehaviours(EnemyCarBehaviour.class)) {
                    if (behaviour.go.body == null) continue;
                    if (Utils.pointDistance(target.x, target.y, behaviour.go.body.getPosition().x, behaviour.go.body.getPosition().y) < 1) behaviour.die();
                }

                for (var behaviour : unBox.findBehaviours(EnemyTankBehaviour.class)) {
                    if (behaviour.go.body == null) continue;
                    if (Utils.pointDistance(target.x, target.y, behaviour.go.body.getPosition().x, behaviour.go.body.getPosition().y) < 1) behaviour.die();
                }

                for (var behaviour : unBox.findBehaviours(EnemyBikeBehaviour.class)) {
                    if (behaviour.go.body == null) continue;
                    if (Utils.pointDistance(target.x, target.y, behaviour.go.body.getPosition().x, behaviour.go.body.getPosition().y) < 1) behaviour.die();
                }
            }
        }

        sb2.skeleton.findBone("bone").setRotation(-MathUtils.radDeg * go.body.getAngle());
    }

    @Override
    public void update(float delta) {
        if (delay > 0) {
            delay -= delta;
            if (delay <= 0) {
                go.body.setLinearVelocity(0, 10f);
            }
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
