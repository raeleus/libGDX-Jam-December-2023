package com.ray3k.cosmicinfiltrator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;

import static com.ray3k.cosmicinfiltrator.Core.*;

public class Resources {
    public static Sound sndExplosion;
    public static Sound sndLaunch;
    public static Sound sndGun;
    public static Sound sndScreech;
    public static Sound sndSmallExplosion;
    public static Sound sndDrip;
    public static Sound sndClick;
    public static Sound sndBeam;
    public static Sound sndCrash;
    public static Sound sndBoost;
    public static Sound sndWarning;
    public static Sound sndSkid;
    public static SkeletonData truckSkeletonData;
    public static AnimationStateData truckAnimationStateData;
    public static SkeletonData shadowSkeletonData;
    public static AnimationStateData shadowAnimationStateData;
    public static SkeletonData scorchSkeletonData;
    public static AnimationStateData scorchAnimationStateData;
    public static SkeletonData plasmaSkeletonData;
    public static AnimationStateData plasmaAnimationStateData;
    public static SkeletonData enemyMothershipSkeletonData;
    public static AnimationStateData enemyMothershipAnimationStateData;
    public static SkeletonData missileSkeletonData;
    public static AnimationStateData missileAnimationStateData;
    public static SkeletonData crosshairSkeletonData;
    public static AnimationStateData crosshairAnimationStateData;
    public static SkeletonData bulletSkeletonData;
    public static AnimationStateData bulletAnimationStateData;
    public static SkeletonData oilSlickSkeletonData;
    public static AnimationStateData oilSlickAnimationStateData;
    public static SkeletonData civilianSkeletonData;
    public static AnimationStateData civilianAnimationStateData;
    public static SkeletonData enemyHeliSkeletonData;
    public static AnimationStateData enemyHeliAnimationStateData;
    public static SkeletonData enemyJetSkeletonData;
    public static AnimationStateData enemyJetAnimationStateData;
    public static SkeletonData enemyBikeSkeletonData;
    public static AnimationStateData enemyBikeAnimationStateData;
    public static SkeletonData enemyTankSkeletonData;
    public static AnimationStateData enemyTankAnimationStateData;
    public static SkeletonData enemyCarSkeletonData;
    public static AnimationStateData enemyCarAnimationStateData;
    public static SkeletonData playerSkeletonData;
    public static AnimationStateData playerAnimationStateData;
    public static SkeletonData streetLeftToRightSkeletonData;
    public static AnimationStateData streetLeftToRightAnimationStateData;
    public static SkeletonData streetRightToLeftSkeletonData;
    public static AnimationStateData streetRightToLeftAnimationStateData;
    public static SkeletonData streetWideToDoubleSkeletonData;
    public static AnimationStateData streetWideToDoubleAnimationStateData;
    public static SkeletonData streetDoubleToWideSkeletonData;
    public static AnimationStateData streetDoubleToWideAnimationStateData;
    public static SkeletonData streetWideToLeftSkeletonData;
    public static AnimationStateData streetWideToLeftAnimationStateData;
    public static SkeletonData streetLeftToWideSkeletonData;
    public static AnimationStateData streetLeftToWideAnimationStateData;
    public static SkeletonData streetWideToRightSkeletonData;
    public static AnimationStateData streetWideToRightAnimationStateData;
    public static SkeletonData streetRightToWideSkeletonData;
    public static AnimationStateData streetRightToWideAnimationStateData;
    public static SkeletonData streetWideToNarrowSkeletonData;
    public static AnimationStateData streetWideToNarrowAnimationStateData;
    public static SkeletonData streetNarrowToWideSkeletonData;
    public static AnimationStateData streetNarrowToWideAnimationStateData;
    public static SkeletonData streetDoubleSkeletonData;
    public static AnimationStateData streetDoubleAnimationStateData;
    public static SkeletonData streetRightSkeletonData;
    public static AnimationStateData streetRightAnimationStateData;
    public static SkeletonData streetLeftSkeletonData;
    public static AnimationStateData streetLeftAnimationStateData;
    public static SkeletonData streetNarrowSkeletonData;
    public static AnimationStateData streetNarrowAnimationStateData;
    public static SkeletonData streetWideSkeletonData;
    public static AnimationStateData streetWideAnimationStateData;
    public static SkeletonData libgdxSkeletonData;
    public static AnimationStateData libgdxAnimationStateData;
    public static SkeletonData logoSkeletonData;
    public static AnimationStateData logoAnimationStateData;
    public static SkeletonData infiltratorSkeletonData;
    public static AnimationStateData infiltratorAnimationStateData;

    public static void loadResources() {
        sndScreech = Gdx.audio.newSound(Gdx.files.internal("sfx/screech.mp3"));
        sndLaunch = Gdx.audio.newSound(Gdx.files.internal("sfx/launch.mp3"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("sfx/explosion.mp3"));
        sndGun = Gdx.audio.newSound(Gdx.files.internal("sfx/machine-gun.mp3"));
        sndSmallExplosion = Gdx.audio.newSound(Gdx.files.internal("sfx/small-explosion.mp3"));
        sndDrip = Gdx.audio.newSound(Gdx.files.internal("sfx/drip.mp3"));
        sndClick = Gdx.audio.newSound(Gdx.files.internal("sfx/click.mp3"));
        sndBeam = Gdx.audio.newSound(Gdx.files.internal("sfx/beam.mp3"));
        sndCrash = Gdx.audio.newSound(Gdx.files.internal("sfx/crash.mp3"));
        sndBoost = Gdx.audio.newSound(Gdx.files.internal("sfx/boost.mp3"));
        sndWarning = Gdx.audio.newSound(Gdx.files.internal("sfx/warning.mp3"));
        sndSkid = Gdx.audio.newSound(Gdx.files.internal("sfx/skid.mp3"));

        skeletonJson.setScale(1);

        libgdxSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/libgdx.json"));
        libgdxAnimationStateData = new AnimationStateData(libgdxSkeletonData);

        logoSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/ray3k.json"));
        logoAnimationStateData = new AnimationStateData(logoSkeletonData);

        infiltratorSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/infiltrator.json"));
        infiltratorAnimationStateData = new AnimationStateData(infiltratorSkeletonData);

        skeletonJson.setScale(1 / PPM);

        truckSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/truck.json"));
        truckAnimationStateData = new AnimationStateData(truckSkeletonData);

        shadowSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/shadow.json"));
        shadowAnimationStateData = new AnimationStateData(shadowSkeletonData);

        scorchSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/scorch.json"));
        scorchAnimationStateData = new AnimationStateData(scorchSkeletonData);

        plasmaSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/plasma.json"));
        plasmaAnimationStateData = new AnimationStateData(plasmaSkeletonData);

        enemyMothershipSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/enemy-mothership.json"));
        enemyMothershipAnimationStateData = new AnimationStateData(enemyMothershipSkeletonData);

        missileSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/missile.json"));
        missileAnimationStateData = new AnimationStateData(missileSkeletonData);

        crosshairSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/crosshair.json"));
        crosshairAnimationStateData = new AnimationStateData(crosshairSkeletonData);

        bulletSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/bullet.json"));
        bulletAnimationStateData = new AnimationStateData(bulletSkeletonData);

        oilSlickSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/oil-slick.json"));
        oilSlickAnimationStateData = new AnimationStateData(oilSlickSkeletonData);

        civilianSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/civillian.json"));
        civilianAnimationStateData = new AnimationStateData(civilianSkeletonData);

        enemyHeliSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/enemy-heli.json"));
        enemyHeliAnimationStateData = new AnimationStateData(enemyHeliSkeletonData);

        enemyJetSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/enemy-jet.json"));
        enemyJetAnimationStateData = new AnimationStateData(enemyJetSkeletonData);

        enemyBikeSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/enemy-bike.json"));
        enemyBikeAnimationStateData = new AnimationStateData(enemyBikeSkeletonData);

        enemyTankSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/enemy-tank.json"));
        enemyTankAnimationStateData = new AnimationStateData(enemyTankSkeletonData);

        enemyCarSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/enemy-car.json"));
        enemyCarAnimationStateData = new AnimationStateData(enemyCarSkeletonData);

        playerSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/player.json"));
        playerAnimationStateData = new AnimationStateData(playerSkeletonData);

        streetLeftToRightSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-left-to-right.json"));
        streetLeftToRightAnimationStateData = new AnimationStateData(streetLeftToRightSkeletonData);

        streetRightToLeftSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-right-to-left.json"));
        streetRightToLeftAnimationStateData = new AnimationStateData(streetRightToLeftSkeletonData);

        streetWideToDoubleSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-wide-to-double.json"));
        streetWideToDoubleAnimationStateData = new AnimationStateData(streetWideToDoubleSkeletonData);

        streetDoubleToWideSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-double-to-wide.json"));
        streetDoubleToWideAnimationStateData = new AnimationStateData(streetDoubleToWideSkeletonData);

        streetWideToLeftSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-wide-to-left.json"));
        streetWideToLeftAnimationStateData = new AnimationStateData(streetWideToLeftSkeletonData);

        streetLeftToWideSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-left-to-wide.json"));
        streetLeftToWideAnimationStateData = new AnimationStateData(streetLeftToWideSkeletonData);

        streetWideToRightSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-wide-to-right.json"));
        streetWideToRightAnimationStateData = new AnimationStateData(streetWideToRightSkeletonData);

        streetRightToWideSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-right-to-wide.json"));
        streetRightToWideAnimationStateData = new AnimationStateData(streetRightToWideSkeletonData);

        streetWideToNarrowSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-wide-to-narrow.json"));
        streetWideToNarrowAnimationStateData = new AnimationStateData(streetWideToNarrowSkeletonData);

        streetNarrowToWideSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-narrow-to-wide.json"));
        streetNarrowToWideAnimationStateData = new AnimationStateData(streetNarrowToWideSkeletonData);

        streetDoubleSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-double.json"));
        streetDoubleAnimationStateData = new AnimationStateData(streetDoubleSkeletonData);

        streetRightSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-right.json"));
        streetRightAnimationStateData = new AnimationStateData(streetRightSkeletonData);

        streetLeftSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-left.json"));
        streetLeftAnimationStateData = new AnimationStateData(streetLeftSkeletonData);

        streetNarrowSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-narrow.json"));
        streetNarrowAnimationStateData = new AnimationStateData(streetNarrowSkeletonData);

        streetWideSkeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("spine/street-wide.json"));
        streetWideAnimationStateData = new AnimationStateData(streetWideSkeletonData);
    }
}
