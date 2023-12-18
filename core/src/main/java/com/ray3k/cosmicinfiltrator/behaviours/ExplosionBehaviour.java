package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;

public class ExplosionBehaviour extends BehaviourAdapter {
    private ParticleEffect particleEffect = new ParticleEffect();
    public float targetX, targetY;

    public ExplosionBehaviour(GameObject gameObject) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        setRenderOrder(DEPTH_PARTICLES);
    }

    @Override
    public void start() {
        particleEffect.load(Gdx.files.internal("particles/explosion.p"), Gdx.files.internal("particles"));
        particleEffect.scaleEffect(1 / PPM);
        particleEffect.start();
        go.body.setTransform(targetX, targetY, 0);
    }

    @Override
    public void fixedUpdate() {
        go.body.setLinearVelocity(0, -PlayerBehaviour.speed);
        particleEffect.setPosition(go.body.getPosition().x, go.body.getPosition().y);
    }

    @Override
    public void update(float delta) {
        particleEffect.update(delta);
    }

    @Override
    public void render(Batch batch) {
        particleEffect.draw(batch);
    }
}
