package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.fixtures.CreateFixtureBehaviour;

/**
 * Creates a box fixture in awake with the provided parameters.
 */
public class CreateEdgeShapeFixtureBehaviour extends CreateFixtureBehaviour {
    private final Array<EdgeShape> shapes = new Array<>();

    public CreateEdgeShapeFixtureBehaviour(float[] vertices, GameObject gameObject) {
        this(vertices, Vector2.Zero, gameObject);
    }

    public CreateEdgeShapeFixtureBehaviour(float[] vertices, Vector2 position, GameObject gameObject) {
        this(vertices, position, new FixtureDef(), gameObject);
    }

    public static final Vector2 vertex1 = new Vector2();
    public static final Vector2 vertex2 = new Vector2();

    public CreateEdgeShapeFixtureBehaviour(float[] vertices, Vector2 position, FixtureDef fixtureDef, GameObject gameObject) {
        super(fixtureDef, gameObject);
        for (int i = 0; i < vertices.length; i += 2) {
            vertex1.set(vertices[i], vertices[i+1]);
            if (i+2 < vertices.length) vertex2.set(vertices[i+2], vertices[i+3]);
            else vertex2.set(vertices[0], vertices[1]);

            var shape = new EdgeShape();
            shape.set(vertex1, vertex2);
            shapes.add(shape);
        }
    }

    @Override
    public void awake() {
        for (var shape : shapes) {
            createAndAttachFixture(shape);
        }
    }
}
