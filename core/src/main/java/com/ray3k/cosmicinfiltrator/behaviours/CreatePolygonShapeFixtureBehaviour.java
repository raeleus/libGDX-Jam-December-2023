package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.fixtures.CreateFixtureBehaviour;

/**
 * Creates a box fixture in awake with the provided parameters.
 */
public class CreatePolygonShapeFixtureBehaviour extends CreateFixtureBehaviour {
    private final PolygonShape shape;

    public CreatePolygonShapeFixtureBehaviour(float[] vertices, GameObject gameObject) {
        this(vertices, Vector2.Zero, gameObject);
    }

    public CreatePolygonShapeFixtureBehaviour(float[] vertices, Vector2 position, GameObject gameObject) {
        this(vertices, position, new FixtureDef(), gameObject);
    }

    public CreatePolygonShapeFixtureBehaviour(float[] vertices, Vector2 position, FixtureDef fixtureDef, GameObject gameObject) {
        super(fixtureDef, gameObject);
        shape = new PolygonShape();
        shape.set(vertices);
    }

    @Override
    public void awake() {
        createAndAttachFixture(shape);
    }
}
