package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class BulletBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    private float startX, startY, startAngle;
    private static final Vector2 temp = new Vector2();

    public BulletBehaviour(GameObject gameObject, float startX, float startY, float startAngle) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, bulletSkeletonData, bulletAnimationStateData);
        sb.setRenderOrder(DEPTH_BULLET);

        this.startX = startX;
        this.startY = startY;
        this.startAngle = startAngle;
    }

    @Override
    public void start() {
        sb.animationState.setAnimation(0, "animation", false);
        sb.animationState.apply(sb.skeleton);
        var fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        sb.createPolygonFixture("bbox", fixtureDef);

        go.body.setTransform(startX, startY, startAngle);
        temp.set(10, 0);
        temp.rotateRad(startAngle);
        go.body.setLinearVelocity(temp);
    }

    @Override
    public void fixedUpdate() {
        if (go.body.getPosition().y > gameViewport.getWorldHeight() + 1) go.destroy();
    }

    @Override
    public void onCollisionEnter(Behaviour other, Contact contact) {
        var civillianBehaviour = other.go.getBehaviour(CivillianBehaviour.class);
        if (civillianBehaviour != null) {
            civillianBehaviour.die();
            go.destroy();
            sndSmallExplosion.play();
        }

        var enemyCarBehaviour = other.go.getBehaviour(EnemyCarBehaviour.class);
        if (enemyCarBehaviour != null) {
            var explosion = new GameObject(unBox);
            var eb = new SparkBehaviour(explosion);
            eb.targetX = go.body.getPosition().x;
            eb.targetY = go.body.getPosition().y;

            enemyCarBehaviour.health -= 1;
            if (enemyCarBehaviour.health <= 0) enemyCarBehaviour.die();
            go.destroy();
            sndSmallExplosion.play();
        }

        var enemyTankBehaviour = other.go.getBehaviour(EnemyTankBehaviour.class);
        if (enemyTankBehaviour != null) {
            var explosion = new GameObject(unBox);
            var eb = new SparkBehaviour(explosion);
            eb.targetX = go.body.getPosition().x;
            eb.targetY = go.body.getPosition().y;

            go.destroy();
            sndSmallExplosion.play();
        }

        var enemyBikeBehaviour = other.go.getBehaviour(EnemyBikeBehaviour.class);
        if (enemyBikeBehaviour != null) {
            var explosion = new GameObject(unBox);
            var eb = new SparkBehaviour(explosion);
            eb.targetX = go.body.getPosition().x;
            eb.targetY = go.body.getPosition().y;

            enemyBikeBehaviour.health -= 1;
            if (enemyBikeBehaviour.health <= 0) enemyBikeBehaviour.die();
            go.destroy();
            sndSmallExplosion.play();
        }

        if (other.go.getBehaviour(WallBehaviour.class) != null) {
            go.destroy();
        }
    }
}
