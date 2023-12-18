package com.ray3k.cosmicinfiltrator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.ray3k.cosmicinfiltrator.Core.*;

public class SplashScreen extends ScreenAdapter {
    private Stage stage;

    @Override
    public void show() {
        stage = new Stage(uiViewport, batch);
        Gdx.input.setInputProcessor(stage);

        var root = new Table();
        root.setFillParent(true);
        root.pad(40);
        stage.addActor(root);

        var image = new Image(skin, "logo");
        image.setScaling(Scaling.fit);
        root.add(image).width(800);

        root.row();
        var label = new Label("CLICK TO INSERT COIN\nCOINS(0)", skin);
        label.setAlignment(Align.center);
        root.add(label);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Buttons.RIGHT) debugScreen();
                else nextScreen();
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
        ScreenUtils.clear(Color.BLACK);

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
        core.setScreen(new com.ray3k.cosmicinfiltrator.screens.LibgdxScreen());
    }

    public void debugScreen() {
        core.setScreen(new GameScreen());
    }
}
