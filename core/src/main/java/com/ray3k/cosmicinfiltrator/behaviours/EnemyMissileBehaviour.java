package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ray3k.cosmicinfiltrator.Utils;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class EnemyMissileBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    private float startX, startY, targetX, targetY;
    private static final Vector2 temp = new Vector2();

    public EnemyMissileBehaviour(GameObject gameObject, float startX, float startY, float targetX, float targetY) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, missileSkeletonData, missileAnimationStateData);
        sb.setRenderOrder(DEPTH_BULLET);

        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void start() {
        var angle = MathUtils.degRad * Utils.pointDirection(startX, startY, targetX, targetY);
        go.body.setTransform(startX, startY, angle);
    }

    @Override
    public void fixedUpdate() {
        var angle = MathUtils.degRad * Utils.pointDirection(startX, startY, targetX, targetY);
        temp.set(20f, 0);
        temp.rotateRad(angle);
        go.body.applyForceToCenter(temp, true);

        if (Utils.pointDistance(targetX, targetY, go.body.getPosition().x, go.body.getPosition().y) < .1f) {
            sndExplosion.play();
            go.destroy();

            var explosion = new GameObject(unBox);
            var eb = new ExplosionBehaviour(explosion);
            eb.targetX = targetX;
            eb.targetY = targetY;

            var playerX = PlayerBehaviour.playerBehaviour.go.body.getPosition().x;
            var playerY = PlayerBehaviour.playerBehaviour.go.body.getPosition().y;
            if (Utils.pointDistance(targetX, targetY, playerX, playerY) < 1) PlayerBehaviour.playerBehaviour.die();

            for (var behaviour : unBox.findBehaviours(CivillianBehaviour.class)) {
                if (behaviour.go.body == null) continue;
                if (Utils.pointDistance(targetX, targetY, behaviour.go.body.getPosition().x, behaviour.go.body.getPosition().y) < 1) behaviour.die();
            }

            for (var behaviour : unBox.findBehaviours(EnemyCarBehaviour.class)) {
                if (behaviour.go.body == null) continue;
                if (Utils.pointDistance(targetX, targetY, behaviour.go.body.getPosition().x, behaviour.go.body.getPosition().y) < 1) behaviour.die();
            }

            for (var behaviour : unBox.findBehaviours(EnemyTankBehaviour.class)) {
                if (behaviour.go.body == null) continue;
                if (Utils.pointDistance(targetX, targetY, behaviour.go.body.getPosition().x, behaviour.go.body.getPosition().y) < 1) behaviour.die();
            }

            for (var behaviour : unBox.findBehaviours(EnemyBikeBehaviour.class)) {
                if (behaviour.go.body == null) continue;
                if (Utils.pointDistance(targetX, targetY, behaviour.go.body.getPosition().x, behaviour.go.body.getPosition().y) < 1) behaviour.die();
            }
        }
    }
}
