package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.ray3k.cosmicinfiltrator.Controls;
import com.ray3k.cosmicinfiltrator.Utils;
import com.ray3k.cosmicinfiltrator.screens.CarScreen;
import com.ray3k.cosmicinfiltrator.screens.GameScreen;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.GameObjectState;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.badlogic.gdx.math.MathUtils.*;
import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class PlayerBehaviour extends BehaviourAdapter {
    public static PlayerBehaviour playerBehaviour;
    private SpineBehaviour sb;
    public static final float HORIZONTAL_SPEED = 25;
    public static final float HORIZONTAL_STOPPING_SPEED = 20;
    public static final float TURNING_ANGLE = 45;
    public static final float TURNING_SPEED = 200;
    public static final float TOP_POSITION = 2.5f;
    public static final float BOTTOM_POSITION = 1;
    public static final float MIN_POSITION = .5f;
    public static final float MIN_SPEED = 10f;
    public static final float MAX_SPEED = 20f;
    public static final float ACCELERATION = .2f;
    public static final float BRAKING = .3f;
    public static float speed;
    private Array<Fixture> createDustFixtures = new Array<>();
    private ParticleEffect dustEffect = new ParticleEffect();
    private ParticleEffect thrusterLeftEffect = new ParticleEffect();
    private ParticleEffect thrusterRightEffect = new ParticleEffect();
    private ParticleEffect skidLeftEffect = new ParticleEffect();
    private ParticleEffect skidRightEffect = new ParticleEffect();
    private Music music;
    private boolean playedMusic;
    private float thrustTimer = -1;
    private float skidTimer = -1;
    private static final float THRUST_TIME = .5f;
    private static final float SKID_TIME = .25f;
    private static final float MINIMUM_THRUST_SPEED = 15f;
    private float stunned;
    private float bulletTimer;
    private static final float BULLET_INTERVAL = .05f;
    private boolean shootLeft;
    private final static float AMMO_REGEN = 4f;
    private final static float AMMO_MAX = 10f;
    private float bulletAmmo = AMMO_MAX;
    private float slickAmmo = AMMO_MAX;
    private boolean ignition;
    private boolean playedTurnSound;

    public PlayerBehaviour(GameObject gameObject) {
        super(gameObject);
        playerBehaviour = this;
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, playerSkeletonData, playerAnimationStateData);
        sb.setRenderOrder(DEPTH_PLAYER);
        setRenderOrder(DEPTH_PARTICLES_UNDER);
    }

    @Override
    public void start() {
        dustEffect.load(Gdx.files.internal("particles/dust.p"), Gdx.files.internal("particles"));
        dustEffect.scaleEffect(1 / PPM);
        thrusterLeftEffect.load(Gdx.files.internal("particles/thruster.p"), Gdx.files.internal("particles"));
        thrusterLeftEffect.scaleEffect(1 / PPM);
        thrusterRightEffect.load(Gdx.files.internal("particles/thruster.p"), Gdx.files.internal("particles"));
        thrusterRightEffect.scaleEffect(1 / PPM);
        skidLeftEffect.load(Gdx.files.internal("particles/skid.p"), Gdx.files.internal("particles"));
        skidLeftEffect.scaleEffect(1 / PPM);
        skidRightEffect.load(Gdx.files.internal("particles/skid.p"), Gdx.files.internal("particles"));
        skidRightEffect.scaleEffect(1 / PPM);
        music = Gdx.audio.newMusic(Gdx.files.internal("bgm/bgm.mp3"));

        switch (CarScreen.carIndex) {
            case 0:
                sb.skeleton.setSkin(playerSkeletonData.findSkin("infiltrator"));
                break;
            case 1:
                sb.skeleton.setSkin(playerSkeletonData.findSkin("muscle"));
                break;
            case 2:
                sb.skeleton.setSkin(playerSkeletonData.findSkin("pursuit"));
                break;
        }

        sb.animationState.setAnimation(0, "animation", true);
        sb.animationState.setAnimation(1, "hide-guns", false);

        sb.createPolygonFixture("bbox", new FixtureDef());
        go.body.setTransform(1.5f, 0, degRad * 90);
    }

    @Override
    public void fixedUpdate() {
        if (stunned > 0) {
            return;
        }

        //vertical speed and position
        if (Controls.upPressed || ignition && speed < MIN_SPEED) {
            speed = Utils.approach(speed, MAX_SPEED, ACCELERATION);
            if (!playedMusic && speed >= MIN_SPEED) {
                music.play();
                playedMusic = true;
            } else if (!ignition) {
                ignition = true;
                sndBoost.play();
            }
            if (thrustTimer == -1 && speed >= MINIMUM_THRUST_SPEED) {
                thrustTimer = THRUST_TIME;
                sndBoost.play(.5f);
                skidTimer = -1;
            }
        } else if (Controls.downPressed) {
            if (speed > MIN_SPEED) speed = Utils.approach(speed, MIN_SPEED, BRAKING);
            thrustTimer = -1;
            if (skidTimer == -1) {
                skidTimer = SKID_TIME;
                sndScreech.play(.1f);
            }
        }

        var targetPosition = Math.max((speed - MIN_SPEED) / (MAX_SPEED - MIN_SPEED) * (TOP_POSITION - BOTTOM_POSITION) + BOTTOM_POSITION, MIN_POSITION);
        var position = go.body.getPosition().y;
        var positionDifference = targetPosition - position;
        go.body.setLinearVelocity(go.body.getLinearVelocity().x, positionDifference * 20);

        //horizontal speed and angle
        var slowSpeedMultiplier = Math.min(speed / MIN_SPEED, 1);

        var targetAngle = 90f;
        if (Controls.leftPressed) {
            targetAngle = 90 + TURNING_ANGLE;
            go.body.applyForceToCenter(-HORIZONTAL_SPEED * slowSpeedMultiplier, 0, true);
        } else if (Controls.rightPressed) {
            targetAngle = 90 - TURNING_ANGLE;
            go.body.applyForceToCenter(HORIZONTAL_SPEED * slowSpeedMultiplier, 0, true);
        } else {
            var hspeed = go.body.getLinearVelocity().x;
            if (isZero(hspeed, 1f)) go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
            else if (hspeed < 0) go.body.applyForceToCenter(HORIZONTAL_STOPPING_SPEED, 0, true);
            else if (hspeed > 0) go.body.applyForceToCenter(-HORIZONTAL_STOPPING_SPEED, 0, true);
        }

        var angle = radDeg * go.body.getAngle();
        var angleDifference = Utils.degDifference(angle, targetAngle);
        go.body.setAngularVelocity(degRad * TURNING_SPEED * Math.signum(angleDifference) * Interpolation.exp10Out.apply(Math.abs(angleDifference) / TURNING_ANGLE * slowSpeedMultiplier));
    }

    @Override
    public void update(float delta) {
        if (Controls.firePressed) {
            if (!sb.animationState.getCurrent(1).getAnimation().getName().equals("show-guns") && !sb.animationState.getCurrent(1).getAnimation().getName().equals("fire")) sb.animationState.setAnimation(1, "show-guns", false);
            if (bulletTimer <= 0 && bulletAmmo >= 1) {
                if (!sb.animationState.getCurrent(1).getAnimation().getName().equals("fire")) {
                    sb.animationState.setAnimation(1, "fire", false);
                    sb.animationState.addEmptyAnimation(1,0, 0);
                }
                bulletAmmo--;
                shootLeft = !shootLeft;

                var bone = sb.skeleton.findBone(shootLeft ? "turret-left" : "turret-right");
                var bullet = new GameObject(unBox);
                new BulletBehaviour(bullet, bone.getWorldX(), bone.getWorldY(), go.body.getAngle());
                bulletTimer = BULLET_INTERVAL;

                if (bulletAmmo < 1) sndClick.play(.5f);
                sndGun.play(.25f);
            } else {
                bulletTimer -= delta;
            }
        } else {
            if (!sb.animationState.getCurrent(1).getAnimation().getName().equals("hide-guns")) sb.animationState.setAnimation(1, "hide-guns", false);
        }

        bulletAmmo += AMMO_REGEN * delta;
        bulletAmmo = Math.min(bulletAmmo, AMMO_MAX);

        if (Controls.slickPressed) {
            if (bulletTimer <= 0 && slickAmmo >= 1) {
                var bone = sb.skeleton.findBone("bone");
                var bullet = new GameObject(unBox);
                new OilSlickBehaviour(bullet, bone.getWorldX(), bone.getWorldY(), go.body.getAngle() + 180 * degRad);
                bulletTimer = BULLET_INTERVAL;
                slickAmmo--;
                sndDrip.play(.5f);
            } else {
                bulletTimer -= delta;
            }
        }

        slickAmmo += AMMO_REGEN * delta;
        slickAmmo = Math.min(slickAmmo, AMMO_MAX);

        if (stunned > 0) stunned -= delta;

        if (thrustTimer > 0 && speed >= MINIMUM_THRUST_SPEED || ignition && speed < MIN_SPEED) {
            thrustTimer -= delta;
            if (thrustTimer < 0) thrustTimer = 0;

            for (var emitter : thrusterLeftEffect.getEmitters()) {
                emitter.setContinuous(true);
                emitter.getAngle().setHigh(MathUtils.radDeg * go.body.getAngle() + 180);
            }

            for (var emitter : thrusterRightEffect.getEmitters()) {
                emitter.setContinuous(true);
                emitter.getAngle().setHigh(MathUtils.radDeg * go.body.getAngle() + 180);
            }
        } else {
            for (var emitter : thrusterLeftEffect.getEmitters()) {
                emitter.setContinuous(false);
            }

            for (var emitter : thrusterRightEffect.getEmitters()) {
                emitter.setContinuous(false);
            }
        }

        thrusterLeftEffect.update(delta);
        var bone = go.skeleton.findBone("thruster-left-bone");
        thrusterLeftEffect.setPosition(bone.getWorldX() + go.body.getLinearVelocity().x * unBox.getOptions().getTimeStep(), bone.getWorldY() + go.body.getLinearVelocity().y * unBox.getOptions().getTimeStep());

        thrusterRightEffect.update(delta);
        bone = go.skeleton.findBone("thruster-right-bone");
        thrusterRightEffect.setPosition(bone.getWorldX() + go.body.getLinearVelocity().x * unBox.getOptions().getTimeStep(), bone.getWorldY() + go.body.getLinearVelocity().y * unBox.getOptions().getTimeStep());

        if (skidTimer > 0) {
            skidTimer -= delta;
            if (skidTimer < 0) skidTimer = 0;
        }

        if ((Math.abs(Utils.degDifference(MathUtils.radDeg * go.body.getAngle(), 90)) > 40 && speed >= MIN_SPEED || thrustTimer > 0 || skidTimer > 0) && createDustFixtures.size == 0) {
            if (!playedTurnSound && thrustTimer <= 0) {
                sndSkid.play(.75f);
                playedTurnSound = true;
            }

            for (var emitter : skidLeftEffect.getEmitters()) {
                emitter.setContinuous(true);
                var angle = radDeg * go.body.getAngle();
                emitter.getAngle().setHigh(angle + 180);
                emitter.getGravity().setHigh(-speed / 10);
            }

            for (var emitter : skidRightEffect.getEmitters()) {
                emitter.setContinuous(true);
                var angle = radDeg * go.body.getAngle();
                emitter.getAngle().setHigh(angle + 180);
                emitter.getGravity().setHigh(-speed / 10);
            }
        } else {
            playedTurnSound = false;
            for (var emitter : skidLeftEffect.getEmitters()) {
                emitter.setContinuous(false);
            }

            for (var emitter : skidRightEffect.getEmitters()) {
                emitter.setContinuous(false);
            }
        }

        skidLeftEffect.update(delta);
        bone = go.skeleton.findBone("thruster-left-bone");
        skidLeftEffect.setPosition(bone.getWorldX() + go.body.getLinearVelocity().x * unBox.getOptions().getTimeStep(), bone.getWorldY() + go.body.getLinearVelocity().y * unBox.getOptions().getTimeStep());

        skidRightEffect.update(delta);
        bone = go.skeleton.findBone("thruster-right-bone");
        skidRightEffect.setPosition(bone.getWorldX() + go.body.getLinearVelocity().x * unBox.getOptions().getTimeStep(), bone.getWorldY() + go.body.getLinearVelocity().y * unBox.getOptions().getTimeStep());

        if (speed >= MIN_SPEED && createDustFixtures.size > 0) {
            for (var emitter : dustEffect.getEmitters()) {
                emitter.setContinuous(true);
                emitter.getVelocity().setHigh(speed / 10);
            }
        } else {
            for (var emitter : dustEffect.getEmitters()) {
                emitter.setContinuous(false);
            }
        }

        dustEffect.update(delta);
        bone = go.skeleton.findBone("dust-bone");
        dustEffect.setPosition(bone.getWorldX(), bone.getWorldY());
    }

    @Override
    public void render(Batch batch) {
        skidLeftEffect.draw(batch);
        skidRightEffect.draw(batch);
        thrusterLeftEffect.draw(batch);
        thrusterRightEffect.draw(batch);
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
            sndCrash.play();
            if (contact.getWorldManifold().getNormal().y > .8) {
                civilianBehaviour.die();
                die();
            }
            else {
                go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
                civilianBehaviour.stunned = CivillianBehaviour.STUN_INTERVAL;

                civilianBehaviour.go.body.setLinearVelocity(
                    CivillianBehaviour.STUN_VELOCITY * Math.signum(contact.getWorldManifold().getNormal().x), civilianBehaviour.go.body.getLinearVelocity().y);
            }
        }

        var enemyCarBehaviour = other.go.getBehaviour(EnemyCarBehaviour.class);
        if (enemyCarBehaviour != null) {
            sndCrash.play();
            if (contact.getWorldManifold().getNormal().y > .8) {
//                enemyCarBehaviour.die();
//                die();
            }
            else {
                if (enemyCarBehaviour.enableSpikes && contact.getWorldManifold().getNormal().y >= -.8) {
                    die();
                } else if (enemyCarBehaviour.ramming <= 0) {
                    enemyCarBehaviour.stunned = EnemyCarBehaviour.STUN_INTERVAL;
                    enemyCarBehaviour.go.body.setLinearVelocity(EnemyCarBehaviour.STUN_VELOCITY * Math.signum(contact.getWorldManifold().getNormal().x), enemyCarBehaviour.go.body.getLinearVelocity().y);
                    go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
                } else {
                    stunned = .1f;
                    go.body.setLinearVelocity(3 * -Math.signum(contact.getWorldManifold().getNormal().x), go.body.getLinearVelocity().y);
                }
            }
        }

        var enemyTankBehaviour = other.go.getBehaviour(EnemyTankBehaviour.class);
        if (enemyTankBehaviour != null) {
            sndCrash.play();
            if (contact.getWorldManifold().getNormal().y > .8) {
//                enemyTankBehaviour.die();
//                die();
            }
            else {
                if (enemyTankBehaviour.ramming <= 0) {
                    enemyTankBehaviour.stunned = EnemyTankBehaviour.STUN_INTERVAL;
                    enemyTankBehaviour.go.body.setLinearVelocity(EnemyTankBehaviour.STUN_VELOCITY * Math.signum(contact.getWorldManifold().getNormal().x), enemyTankBehaviour.go.body.getLinearVelocity().y);
                    go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
                } else {
                    stunned = .1f;
                    go.body.setLinearVelocity(3 * -Math.signum(contact.getWorldManifold().getNormal().x), go.body.getLinearVelocity().y);
                }
            }
        }

        var enemyBikeBehaviour = other.go.getBehaviour(EnemyBikeBehaviour.class);
        if (enemyBikeBehaviour != null) {
            sndCrash.play();
            if (contact.getWorldManifold().getNormal().y > .8) {
//                enemyBikeBehaviour.die();
//                die();
            }
            else {
                enemyBikeBehaviour.stunned = EnemyBikeBehaviour.STUN_INTERVAL;
                enemyBikeBehaviour.go.body.setLinearVelocity(EnemyBikeBehaviour.STUN_VELOCITY * Math.signum(contact.getWorldManifold().getNormal().x), enemyBikeBehaviour.go.body.getLinearVelocity().y);
                go.body.setLinearVelocity(0, go.body.getLinearVelocity().y);
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
        if (go.getState() == GameObjectState.DESTROYED) return;
        var explosion = new GameObject(unBox);
        var eb = new ExplosionBehaviour(explosion);
        eb.targetX = go.body.getPosition().x;
        eb.targetY = go.body.getPosition().y;

        music.stop();

        go.destroy();
        speed = 0;

        GameScreen.gameScreen.restartGame();
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
