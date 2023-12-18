package com.ray3k.cosmicinfiltrator.behaviours;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class CrossHairBehaviour extends BehaviourAdapter {
    private SpineBehaviour sb;

    public CrossHairBehaviour(GameObject gameObject) {
        super(gameObject);
        new Box2dBehaviour(BodyDefType.DynamicBody, go);
        sb = new SpineBehaviour(go, crosshairSkeletonData, crosshairAnimationStateData);
        sb.setRenderOrder(DEPTH_PARTICLES);
    }

    @Override
    public void start() {
        sb.animationState.setAnimation(0, "animation", false);
        sb.animationState.apply(sb.skeleton);
        sb.animationState.addListener(new AnimationStateAdapter() {
            @Override
            public void complete(TrackEntry entry) {
                go.destroy();
            }
        });
    }

    @Override
    public void fixedUpdate() {

    }
}
