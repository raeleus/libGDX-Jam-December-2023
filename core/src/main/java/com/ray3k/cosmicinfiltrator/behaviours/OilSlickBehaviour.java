package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.math.MathUtils;
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

public class OilSlickBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    private float startX, startY, startAngle;
    private static final Vector2 temp = new Vector2();
    private boolean enabled = true;

    public OilSlickBehaviour(GameObject gameObject, float startX, float startY, float startAngle) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, oilSlickSkeletonData, oilSlickAnimationStateData);
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

        go.body.setTransform(startX, startY, MathUtils.degRad * MathUtils.random(360));
        temp.set(0, -PlayerBehaviour.speed);
        go.body.setLinearVelocity(temp);
    }

    @Override
    public void fixedUpdate() {
        if (go.body.getPosition().y < -1) go.destroy();
        temp.set(0, -PlayerBehaviour.speed / 2);
        go.body.setLinearVelocity(temp);
    }

    @Override
    public void onCollisionEnter(Behaviour other, Contact contact) {
        if (!enabled) return;

        var civillianBehaviour = other.go.getBehaviour(CivillianBehaviour.class);
        if (civillianBehaviour != null) {
            civillianBehaviour.die();
            enabled = false;
        }

        var enemyCarBehaviour = other.go.getBehaviour(EnemyCarBehaviour.class);
        if (enemyCarBehaviour != null) {
            enemyCarBehaviour.stunned = EnemyCarBehaviour.STUN_INTERVAL * 3;
            enemyCarBehaviour.go.body.setLinearVelocity(EnemyCarBehaviour.STUN_VELOCITY * .75f * (enemyCarBehaviour.go.body.getPosition().x < gameViewport.getWorldWidth() / 2 ? -1f : 1f), enemyCarBehaviour.go.body.getLinearVelocity().y);
            go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
            enabled = false;
        }

        var enemyTankBehaviour = other.go.getBehaviour(EnemyTankBehaviour.class);
        if (enemyTankBehaviour != null) {
            enemyTankBehaviour.stunned = EnemyTankBehaviour.STUN_INTERVAL * 3;
            enemyTankBehaviour.go.body.setLinearVelocity(EnemyTankBehaviour.STUN_VELOCITY * .75f * (enemyTankBehaviour.go.body.getPosition().x < gameViewport.getWorldWidth() / 2 ? -1f : 1f), enemyTankBehaviour.go.body.getLinearVelocity().y);
            go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
            enabled = false;
        }

        var enemyBikeBehaviour = other.go.getBehaviour(EnemyBikeBehaviour.class);
        if (enemyBikeBehaviour != null) {
            enemyBikeBehaviour.stunned = EnemyBikeBehaviour.STUN_INTERVAL * 3;
            enemyBikeBehaviour.go.body.setLinearVelocity(EnemyBikeBehaviour.STUN_VELOCITY * .75f * (enemyBikeBehaviour.go.body.getPosition().x < gameViewport.getWorldWidth() / 2 ? -1f : 1f), enemyBikeBehaviour.go.body.getLinearVelocity().y);
            go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
            enabled = false;
        }
    }
}
