package com.ray3k.cosmicinfiltrator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.ray3k.cosmicinfiltrator.screens.CarScreen;
import com.ray3k.cosmicinfiltrator.SpineImage;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class LogoScreen extends ScreenAdapter {
    private Stage stage;

    @Override
    public void show() {
        stage = new Stage(uiViewport, batch);
        Gdx.input.setInputProcessor(stage);

        var root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        var image = new SpineImage(skeletonRenderer, logoSkeletonData, logoAnimationStateData);
        image.setCrop(0, 0, 1024, 576);
        image.getAnimationState().setAnimation(0, "animation", false);
        root.add(image);

        image.getAnimationState().addListener(new AnimationStateAdapter() {
            @Override
            public void complete(TrackEntry entry) {
                nextScreen();
            }

            @Override
            public void event(TrackEntry entry, Event event) {
                var path = event.getData().getAudioPath();
                if (path == null) return;
                var sound = Gdx.audio.newSound(Gdx.files.internal(path));
                sound.play();
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nextScreen();
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                nextScreen();
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        uiViewport.apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
    }

    public void nextScreen() {
        core.setScreen(new CarScreen());
    }
}
