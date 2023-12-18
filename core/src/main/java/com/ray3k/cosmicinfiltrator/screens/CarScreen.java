package com.ray3k.cosmicinfiltrator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.Skin;
import com.ray3k.cosmicinfiltrator.SpineImage;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class CarScreen extends ScreenAdapter {
    private Stage stage;
    public static int carIndex;

    @Override
    public void show() {
        stage = new Stage(uiViewport, batch);
        Gdx.input.setInputProcessor(stage);

        var root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("car-select-background"));
        stage.addActor(root);

        var stack = new Stack();
        root.add(stack).grow();

        var image = new SpineImage(skeletonRenderer, infiltratorSkeletonData, infiltratorAnimationStateData);
        image.setScaling(Scaling.fill);
        image.setCrop(0, 0, 1024, 576);
        image.getSkeleton().setSkin(infiltratorSkeletonData.findSkin("infiltrator"));
        image.getAnimationState().setAnimation(0, "show-right", false);
        stack.add(image);

        var skinArray = new Array<Skin>();
        skinArray.add(infiltratorSkeletonData.findSkin("infiltrator"));
        skinArray.add(infiltratorSkeletonData.findSkin("muscle"));
        skinArray.add(infiltratorSkeletonData.findSkin("pursuit"));
        skinArray.add(infiltratorSkeletonData.findSkin("owl"));

        var table = new Table();
        table.pad(40);
        stack.add(table);

        var imageButton = new ImageButton(skin, "left");
        table.add(imageButton);
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                image.getAnimationState().setAnimation(0, "hide-left", false);
                image.getAnimationState().addAnimation(0, "show-left", false, 0);
            }
        });

        table.add().grow();

        imageButton = new ImageButton(skin, "right");
        table.add(imageButton);
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                image.getAnimationState().setAnimation(0, "hide-right", false);
                image.getAnimationState().addAnimation(0, "show-right", false, 0);
            }
        });

        table.row();
        var textButton = new TextButton("ENGAGE", skin);
        table.add(textButton).colspan(3);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (carIndex != 3) nextScreen();
            }
        });

        image.getAnimationState().addListener(new AnimationStateAdapter() {
            @Override
            public void complete(TrackEntry entry) {
                if (entry.getAnimation().getName().equals("hide-left")) {
                    carIndex--;
                    if (carIndex < 0) carIndex = skinArray.size - 1;
                } else if (entry.getAnimation().getName().equals("hide-right")) {
                    carIndex++;
                    if (carIndex > skinArray.size - 1) carIndex = 0;
                } else return;

                image.getSkeleton().setSkin(skinArray.get(carIndex));
                image.getSkeleton().setSlotsToSetupPose();
                if (carIndex == 3) textButton.setText("HUNGRY DLC ONLY");
                else textButton.setText("ENGAGE");
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
        core.setScreen(new GameScreen());
    }
}
