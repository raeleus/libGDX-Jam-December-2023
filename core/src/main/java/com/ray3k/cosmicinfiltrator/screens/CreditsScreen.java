package com.ray3k.cosmicinfiltrator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.Skin;
import com.ray3k.cosmicinfiltrator.SpineImage;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.infiltratorAnimationStateData;
import static com.ray3k.cosmicinfiltrator.Resources.infiltratorSkeletonData;

public class CreditsScreen extends ScreenAdapter {
    private Stage stage;

    @Override
    public void show() {
        stage = new Stage(uiViewport, batch);
        Gdx.input.setInputProcessor(stage);

        var root = new Table();
        root.setFillParent(true);
        root.pad(40);
        stage.addActor(root);

        root.defaults().space(10);
        var label = new Label("The alien has escaped successfully!", skin);
        root.add(label);

        root.row();
        var image = new Image(skin, "credits");
        image.setScaling(Scaling.fit);
        root.add(image).grow();

        root.row();
        label = new Label("A game by Raeleus and Lolomg.\nSpecial thanks to DragonQueen and Lyze!", skin);
        label.setAlignment(Align.center);
        root.add(label);

        root.row();
        var textButton = new TextButton("Return", skin);
        root.add(textButton);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nextScreen();
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
        core.setScreen(new CarScreen());
    }
}
