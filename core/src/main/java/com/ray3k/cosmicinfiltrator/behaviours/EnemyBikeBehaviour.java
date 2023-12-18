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

public class EnemyBikeBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;
    public float speed;
    public float spawnX;
    public float spawnWidth;
    private Array<Fixture> createDustFixtures = new Array<>();
    private ParticleEffect dustEffect = new ParticleEffect();
    public float stunned;
    public static final float STUN_INTERVAL = .05f;
    public static final float STUN_VELOCITY = 10f;
    public static final float UP_ACCELERATION = .5f;
    public static final float DOWN_ACCELERATION = .09f;
    public static final float VERTICAL_VELOCITY_MAX = 5f;
    public static final float VERTICAL_ACCELERATION_HELP = .3f;
    public static final float HORIZONTAL_SPEED = .3f;
    public int health = 1;
    private float charge;

    public EnemyBikeBehaviour(GameObject gameObject, float speed, float spawnX, float spawnWidth) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, enemyBikeSkeletonData, enemyBikeAnimationStateData);
        sb.setRenderOrder(DEPTH_ENEMY);
        setRenderOrder(DEPTH_PARTICLES);

        this.speed = speed;
        this.spawnX = spawnX;
        this.spawnWidth = spawnWidth;
    }

    @Override
    public void start() {
        dustEffect.load(Gdx.files.internal("particles/dust.p"), Gdx.files.internal("particles"));
        dustEffect.scaleEffect(1 / PPM);

        sb.animationState.setAnimation(0, "charge", false);
        sb.animationState.getCurrent(0).setTimeScale(0);
        sb.animationState.apply(sb.skeleton);
        sb.createPolygonFixture("bbox", new FixtureDef());

        go.body.setTransform(MathUtils.random(spawnX, spawnX + spawnWidth), MathUtils.randomBoolean() ? gameViewport.getWorldHeight() + 1 : -.5f, MathUtils.degRad * 90);
        speed -= MathUtils.random(2f);
        go.body.setLinearVelocity(0, speed);
    }

    @Override
    public void fixedUpdate() {
        if (go.body.getPosition().y < - 2) go.destroy();
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
        var bone = go.skeleton.findBone("dust");
        dustEffect.setPosition(bone.getWorldX(), bone.getWorldY());

        if (stunned <= 0) {
            var deltaY = go.body.getLinearVelocity().y;
            var carX = go.body.getPosition().x;
            var carY = go.body.getPosition().y;
            var playerX = PlayerBehaviour.playerBehaviour.go.body.getPosition().x;
            var playerY = PlayerBehaviour.playerBehaviour.go.body.getPosition().y -1f;
            var timestep = unBox.getOptions().getTimeStep();

            if (carY < playerY) {
                deltaY += UP_ACCELERATION;
                deltaY = MathUtils.clamp(deltaY, -VERTICAL_VELOCITY_MAX, VERTICAL_VELOCITY_MAX);
                deltaY *= (PlayerBehaviour.MAX_SPEED - PlayerBehaviour.speed) / PlayerBehaviour.MAX_SPEED + VERTICAL_ACCELERATION_HELP;
                if (carY + deltaY * timestep >= playerY) deltaY = 0;
            } else if (go.body.getPosition().y > playerY) {
                deltaY -= DOWN_ACCELERATION;
                deltaY = MathUtils.clamp(deltaY, -VERTICAL_VELOCITY_MAX, VERTICAL_VELOCITY_MAX);
                if (carY + deltaY * timestep <= playerY) deltaY = 0;
            }

            if (deltaY <= -.1f) {
                go.body.setLinearVelocity(go.body.getLinearVelocity().x * .9f, deltaY);
            } else {
                var distance = Math.abs(carX - playerX);
                if (distance > 0) {
                    var deltaX = carX < playerX ? HORIZONTAL_SPEED : -HORIZONTAL_SPEED;

                    if (Math.signum(deltaX) > 0 && carX + deltaX * timestep >= playerX) deltaX = 0;
                    else if (Math.signum(deltaX) < 0 && carX + deltaX * timestep <= playerX) deltaX = 0;


                    if (Math.abs(carY - playerY) < .2f) {
                        if (charge == 1) {
                            if (!sb.animationState.getCurrent(0).getAnimation().getName().equals("blast")){
                                sb.animationState.setAnimation(0, "blast", false);
                                sndBeam.play();
                            }
                            sb.animationState.getCurrent(0).setTimeScale(1f);
                            PlayerBehaviour.playerBehaviour.die();
                        } else {
                            if (Math.abs(carX - playerX) < .2f) {
                                charge += delta * 1f;
                            } else {
                                charge -= delta * 2f;
                            }
                            charge = MathUtils.clamp(charge, 0, 1);

                            sb.animationState.getCurrent(0).setTrackTime(charge);
                        }
                    }

                    go.body.setLinearVelocity(go.body.getLinearVelocity().x * .9f + deltaX, deltaY);
                }
            }
        }
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
        var civilianBehaviour = other.go.getBehaviour(CivillianBehaviour.class);
        if (civilianBehaviour != null) {
            if (contact.getWorldManifold().getNormal().y > .5 || contact.getWorldManifold().getNormal().y < -.5) {
                civilianBehaviour.die();
                die();
            }
            else {
                go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
                civilianBehaviour.stunned = CivillianBehaviour.STUN_INTERVAL;

                civilianBehaviour.go.body.setLinearVelocity(
                    CivillianBehaviour.STUN_VELOCITY * Math.signum(contact.getWorldManifold().getNormal().x),
                        civilianBehaviour.go.body.getLinearVelocity().y);
            }
        }

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
