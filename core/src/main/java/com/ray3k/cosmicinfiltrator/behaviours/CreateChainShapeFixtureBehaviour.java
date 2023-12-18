package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.fixtures.CreateFixtureBehaviour;

/**
 * Creates a box fixture in awake with the provided parameters.
 */
public class CreateChainShapeFixtureBehaviour extends CreateFixtureBehaviour {
    private final ChainShape shape;

    public CreateChainShapeFixtureBehaviour(float[] vertices, GameObject gameObject) {
        this(vertices, Vector2.Zero, gameObject);
    }

    public CreateChainShapeFixtureBehaviour(float[] vertices, Vector2 position, GameObject gameObject) {
        this(vertices, position, new FixtureDef(), gameObject);
    }

    public CreateChainShapeFixtureBehaviour(float[] vertices, Vector2 position, FixtureDef fixtureDef, GameObject gameObject) {
        super(fixtureDef, gameObject);
        shape = new ChainShape();
        shape.createLoop(vertices);
    }

    @Override
    public void awake() {
        createAndAttachFixture(shape);
    }
}
