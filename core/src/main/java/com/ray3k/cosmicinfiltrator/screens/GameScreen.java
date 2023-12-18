package com.ray3k.cosmicinfiltrator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.spine.SkeletonData;
import com.ray3k.cosmicinfiltrator.Controls;
import com.ray3k.cosmicinfiltrator.Core;
import com.ray3k.cosmicinfiltrator.Resources;
import com.ray3k.cosmicinfiltrator.TimeLine;
import com.ray3k.cosmicinfiltrator.behaviours.PlayerBehaviour;
import com.ray3k.cosmicinfiltrator.behaviours.SpineBehaviour;
import com.ray3k.cosmicinfiltrator.behaviours.StreetBehaviour;
import dev.lyze.gdxUnBox2d.*;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class GameScreen extends ScreenAdapter {
    public static GameScreen gameScreen;
    private Box2DDebugShapeDrawer debugShapeDrawer;
    private Stage stage;
    private Table root;
    public static final Color bgColor = Color.valueOf("0e6d00");

    @Override
    public void show() {
        TimeLine.reset();
        gameScreen = this;

        stage = new Stage(uiViewport, batch);
        root = new Table();
        root.setFillParent(true);

        Gdx.input.setInputProcessor(new InputMultiplexer(controls, stage));

        debugShapeDrawer = new Box2DDebugShapeDrawer(shapeDrawer);
        unBox = new UnBox<>(new Box2dPhysicsWorld(new World(new Vector2(0, 0), true)));

        var player = new GameObject(unBox);
        new PlayerBehaviour(player);

        var street = new GameObject(unBox);
        new StreetBehaviour(street, null);
    }

    @Override
    public void render(float delta) {
        if (PlayerBehaviour.speed >= PlayerBehaviour.MIN_SPEED) TimeLine.update(delta);

        ScreenUtils.clear(bgColor);

        unBox.preRender(delta);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        batch.begin();
        unBox.render(batch);
//        debugShapeDrawer.render(unBox.getPhysicsWorld().getWorld());
        batch.end();

        unBox.postRender();

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        uiViewport.apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    public void showSign(SkeletonData skeletonData, float delay) {
        var drawable = skin.getDrawable("sign-6");
        if (skeletonData == streetLeftSkeletonData) {
            drawable = skin.getDrawable("sign-2");
        } else if (skeletonData == streetRightSkeletonData) {
            drawable = skin.getDrawable("sign-1");
        } else if (skeletonData == streetDoubleSkeletonData) {
            drawable = skin.getDrawable("sign-5");
        }

        var image = new Image(drawable);
        image.setScaling(Scaling.fit);
        image.scaleBy(-.5f);

        stage.addAction(Actions.sequence(
            Actions.delay(delay),
            Actions.run(() -> {
                stage.addActor(image);
                sndWarning.play();
            }),
            Actions.delay(2f),
            Actions.run(image::remove)));
    }

    public void restartGame() {
        stage.addAction(Actions.delay(1.5f, Actions.run(() -> {
            Core.core.setScreen(new GameScreen());
        })));
    }
}
