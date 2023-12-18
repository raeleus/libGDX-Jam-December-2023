package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class CivillianBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    public float speed;
    public float spawnX;
    public float spawnWidth;
    private Array<Fixture> createDustFixtures = new Array<>();
    private ParticleEffect dustEffect = new ParticleEffect();
    public float stunned;
    public static final float STUN_INTERVAL = .05f;
    public static final float STUN_VELOCITY = 5f;

    public CivillianBehaviour(GameObject gameObject, float speed, float spawnX, float spawnWidth) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, civilianSkeletonData, civilianAnimationStateData);
        sb.setRenderOrder(DEPTH_ENEMY);
        setRenderOrder(DEPTH_PARTICLES_UNDER);

        this.speed = speed;
        this.spawnX = spawnX;
        this.spawnWidth = spawnWidth;
    }

    @Override
    public void start() {
        dustEffect.load(Gdx.files.internal("particles/dust.p"), Gdx.files.internal("particles"));
        dustEffect.scaleEffect(1 / PPM);

        switch (MathUtils.random(2)) {
            case 0:
                sb.skeleton.setSkin(civilianSkeletonData.findSkin("car1"));
                break;
            case 1:
                sb.skeleton.setSkin(civilianSkeletonData.findSkin("car2"));
                break;
            case 2:
                sb.skeleton.setSkin(civilianSkeletonData.findSkin("car3"));
                break;
        }

        var slot = sb.skeleton.findSlot("car");
        slot.getColor().set(MathUtils.random(1f), MathUtils.random(1f), MathUtils.random(1f), 1f);

        sb.createPolygonFixture("bbox", new FixtureDef());

        go.body.setTransform(MathUtils.random(spawnX, spawnX + spawnWidth), gameViewport.getWorldHeight() + 1, MathUtils.degRad * 90);
        speed -= MathUtils.random(2f);
        go.body.setLinearVelocity(0, speed);
    }

    @Override
    public void fixedUpdate() {
        if (go.body.getPosition().y < - 1) go.destroy();
    }

    @Override
    public void update(float delta) {
        if (createDustFixtures.size > 0) {
            for (var emitter : dustEffect.getEmitters()) {
                emitter.setContinuous(true);
                emitter.getVelocity().setHigh(PlayerBehaviour.speed / 10);
            }
        } else {
            for (var emitter : dustEffect.getEmitters()) {
                emitter.setContinuous(false);
            }
        }

        dustEffect.update(delta);
        dustEffect.setPosition(go.body.getPosition().x, go.body.getPosition().y);

        if (stunned <= 0) go.body.setLinearVelocity(go.body.getLinearVelocity().x * .9f, -PlayerBehaviour.speed / PlayerBehaviour.MAX_SPEED * 10.5f);
        else {
            stunned -= delta;
        }
    }

    @Override
    public void render(Batch batch) {
        dustEffect.draw(batch);
    }

    @Override
    public boolean onCollisionPreSolve(Behaviour other, Contact contact, Manifold oldManifold) {
        return false;
    }

    @Override
    public void onCollisionEnter(Behaviour other, Contact contact) {
        if (other.go.getBehaviour(WallBehaviour.class) != null) {
            die();
        }

        if (other.go.getBehaviour(DustSensorBehaviour.class) != null) {
            var fixture = contact.getFixtureA().getBody() == other.go.body ? contact.getFixtureA() : contact.getFixtureB();
            createDustFixtures.add(fixture);
        }
    }

    public void die() {
        var explosion = new GameObject(unBox);
        var eb = new ExplosionBehaviour(explosion);
        eb.targetX = go.body.getPosition().x;
        eb.targetY = go.body.getPosition().y;

        go.destroy();
        sndExplosion.play();
    }

    @Override
    public void onCollisionStay(Behaviour other) {
        if (other.go.getBehaviour(DustSensorBehaviour.class) != null) {

        }
    }

    @Override
    public void onCollisionExit(Behaviour other, Contact contact) {
        if (other.go.getBehaviour(DustSensorBehaviour.class) != null) {
            var fixture = contact.getFixtureA().getBody() == other.go.body ? contact.getFixtureA() : contact.getFixtureB();
            createDustFixtures.removeValue(fixture, true);
        }
    }
}
