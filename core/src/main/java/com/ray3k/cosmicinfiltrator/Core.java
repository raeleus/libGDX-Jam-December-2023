package com.ray3k.cosmicinfiltrator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import com.ray3k.cosmicinfiltrator.screens.SplashScreen;
import dev.lyze.gdxUnBox2d.Box2dPhysicsWorld;
import dev.lyze.gdxUnBox2d.UnBox;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.ray3k.cosmicinfiltrator.Resources.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    public static Core core;
    public static TwoColorPolygonBatch batch;
    public static TextureAtlas textureAtlas;
    public static Skin skin;
    public static OrthographicCamera gameCamera;
    public static FitViewport gameViewport;
    public static FitViewport uiViewport;
    public static SkeletonRenderer skeletonRenderer;
    public static SkeletonJson skeletonJson;
    public static ShapeDrawer shapeDrawer;
    public static Controls controls;
    public static final float PPM = 100;
    public static UnBox<Box2dPhysicsWorld> unBox;

    public static final float DEPTH_STREET = 0;
    public static final float DEPTH_PARTICLES_UNDER = 5;
    public static final float DEPTH_ENEMY = 8;
    public static final float DEPTH_BULLET = 9;
    public static final float DEPTH_PLAYER = 10;
    public static final float DEPTH_PARTICLES = 15;
    public static final float DEPTH_ENEMY_SKY = 20;

    public Core() {
        core = this;
    }

    @Override
    public void create() {
        batch = new TwoColorPolygonBatch(32767);
        textureAtlas = new TextureAtlas(Gdx.files.internal("textures/textures.atlas"));
        skin = new Skin();
        skin.addRegions(textureAtlas);
        skin.load(Gdx.files.internal("skin/skin.json"));
        skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);
        skeletonJson = new SkeletonJson(textureAtlas);
        shapeDrawer = new ShapeDrawer(batch, textureAtlas.findRegion("game/white"));
        shapeDrawer.setDefaultLineWidth(1 / PPM);
        controls = new Controls();
        Controllers.addListener(controls);

        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(1024 / PPM, 576 / PPM, gameCamera);
        uiViewport = new FitViewport(1024, 576);

        loadResources();

        setScreen(new SplashScreen());
    }
}
