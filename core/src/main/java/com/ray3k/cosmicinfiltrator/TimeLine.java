package com.ray3k.cosmicinfiltrator;

import com.badlogic.gdx.math.MathUtils;
import com.ray3k.cosmicinfiltrator.behaviours.*;
import com.ray3k.cosmicinfiltrator.screens.CarScreen;
import com.ray3k.cosmicinfiltrator.screens.CreditsScreen;
import com.ray3k.cosmicinfiltrator.screens.GameScreen;
import dev.lyze.gdxUnBox2d.GameObject;

import static com.ray3k.cosmicinfiltrator.Core.*;
import static com.ray3k.cosmicinfiltrator.Resources.*;

public class TimeLine {
    public static float time;
    public static float nextStreetTypeTime;
    public static int streetIndex;
    public static float civilianIntervalMin;
    public static float civilianIntervalMax;
    public static float civilianTimer;
    public static float enemyCarIntervalMin;
    public static float enemyCarIntervalMax;
    public static float enemySpikeCarIntervalMin;
    public static float enemySpikeCarIntervalMax;
    public static float enemyCarTimer;
    public static float enemySpikeCarTimer;
    public static float spawnX;
    public static float spawnWidth;

    public static void reset() {
        time = 0;
        streetIndex = 0;
        nextStreetTypeTime = 10;
    }

    public static void update(float delta) {
        time += delta;

        civilianTimer -= delta;
        if (civilianTimer < 0) {
            civilianTimer = MathUtils.random(civilianIntervalMin, civilianIntervalMax);
            var civilian = new GameObject(unBox);
            new CivillianBehaviour(civilian, -1f, spawnX, spawnWidth);
        }

        enemyCarTimer -= delta;
        if (enemyCarTimer < 0) {
            enemyCarTimer = MathUtils.random(enemyCarIntervalMin, enemyCarIntervalMax);
            var enemyCar = new GameObject(unBox);
            if (streetIndex < 3 || MathUtils.randomBoolean(.5f)) {
                var ecb = new EnemyCarBehaviour(enemyCar, -1f, spawnX, spawnWidth);
            } else if (MathUtils.randomBoolean(.5f)) {
                var ecb = new EnemyTankBehaviour(enemyCar, -1f, spawnX, spawnWidth);
            } else {
                var ecb = new EnemyBikeBehaviour(enemyCar, -1f, spawnX, spawnWidth);
            }
        }

        enemySpikeCarTimer -= delta;
        if (enemySpikeCarTimer < 0 && streetIndex >= 5) {
            enemySpikeCarTimer = MathUtils.random(enemySpikeCarIntervalMin, enemySpikeCarIntervalMax);
            var enemyCar = new GameObject(unBox);
            var ecb = new EnemyCarBehaviour(enemyCar, -1f, spawnX, spawnWidth);
            ecb.enableSpikes = true;
        }
    }

    public static SpineBehaviour getStreet(GameObject go) {
        boolean newType = false;
        if (time > nextStreetTypeTime) {
            streetIndex++;
            newType = true;
        }

        SpineBehaviour returnValue;

        switch (streetIndex) {
            case 0:
                spawnWidth = 6f;
                spawnX = gameViewport.getWorldWidth() / 2f - spawnWidth / 2f;
                civilianIntervalMin = 1f;
                civilianIntervalMax = 2f;
                enemyCarIntervalMin = 6f;
                enemyCarIntervalMax = 8f;
                enemySpikeCarIntervalMin = 8f;
                enemySpikeCarIntervalMax = 12f;

                if (newType) nextStreetTypeTime += 10;
            case 1:
                spawnWidth = 6f;
                spawnX = gameViewport.getWorldWidth() / 2f - spawnWidth / 2f;
                civilianIntervalMin = 1f;
                civilianIntervalMax = 2f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    GameScreen.gameScreen.showSign(streetNarrowSkeletonData, 7f);
                }
                returnValue = new SpineBehaviour(go, streetWideSkeletonData, streetWideAnimationStateData);
                break;
            case 2:
                spawnWidth = 2f;
                spawnX = gameViewport.getWorldWidth() / 2f - spawnWidth / 2f;
                civilianIntervalMin = 1f;
                civilianIntervalMax = 2f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    returnValue = new SpineBehaviour(go, streetWideToNarrowSkeletonData, streetWideToNarrowAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetNarrowSkeletonData, streetNarrowAnimationStateData);
                break;
            case 3:
                spawnWidth = 6f;
                spawnX = gameViewport.getWorldWidth() / 2f - spawnWidth / 2f;
                civilianIntervalMin = 1f;
                civilianIntervalMax = 2f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    GameScreen.gameScreen.showSign(streetRightSkeletonData, 7f);
                    returnValue = new SpineBehaviour(go, streetNarrowToWideSkeletonData, streetNarrowToWideAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetWideSkeletonData, streetWideAnimationStateData);
                break;
            case 4:
                spawnWidth = 2f;
                spawnX = 7.1f - spawnWidth / 2f;
                civilianIntervalMin = 1f;
                civilianIntervalMax = 2f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    returnValue = new SpineBehaviour(go, streetWideToRightSkeletonData, streetWideToRightAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetRightSkeletonData, streetRightAnimationStateData);
                break;
            case 5:
                spawnWidth = 6f;
                spawnX = gameViewport.getWorldWidth() / 2f - spawnWidth / 2f;
                civilianIntervalMin = 1f;
                civilianIntervalMax = 2f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    GameScreen.gameScreen.showSign(streetDoubleSkeletonData, 7f);
                    returnValue = new SpineBehaviour(go, streetRightToWideSkeletonData, streetRightToWideAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetWideSkeletonData, streetWideAnimationStateData);
                break;
            case 6:
                spawnWidth = 6f;
                spawnX = gameViewport.getWorldWidth() / 2f - spawnWidth / 2f;
                civilianIntervalMin = 1f;
                civilianIntervalMax = 2f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    returnValue = new SpineBehaviour(go, streetWideToDoubleSkeletonData, streetWideToDoubleAnimationStateData);

                    var jet = new GameObject(unBox);
                    new EnemyJetBehaviour(jet);
                } else returnValue = new SpineBehaviour(go, streetDoubleSkeletonData, streetDoubleAnimationStateData);
                break;
            case 7:
                spawnWidth = 6f;
                spawnX = gameViewport.getWorldWidth() / 2f - spawnWidth / 2f;
                civilianIntervalMin = .5f;
                civilianIntervalMax = 1f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    GameScreen.gameScreen.showSign(streetLeftSkeletonData, 7f);
                    returnValue = new SpineBehaviour(go, streetDoubleToWideSkeletonData, streetDoubleToWideAnimationStateData);

                    var jet = new GameObject(unBox);
                    new EnemyJetBehaviour(jet);
                } else returnValue = new SpineBehaviour(go, streetWideSkeletonData, streetWideAnimationStateData);
                break;
            case 8:
                spawnWidth = 2f;
                spawnX = 2.8f - spawnWidth / 2f;
                civilianIntervalMin = .5f;
                civilianIntervalMax = 1f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    GameScreen.gameScreen.showSign(streetRightSkeletonData, 7f);
                    returnValue = new SpineBehaviour(go, streetWideToLeftSkeletonData, streetWideToLeftAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetLeftSkeletonData, streetLeftAnimationStateData);
                break;
            case 9:
                spawnWidth = 2f;
                spawnX = 7.1f - spawnWidth / 2f;
                civilianIntervalMin = .5f;
                civilianIntervalMax = 1f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    GameScreen.gameScreen.showSign(streetLeftSkeletonData, 7f);
                    returnValue = new SpineBehaviour(go, streetLeftToRightSkeletonData, streetLeftToRightAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetRightSkeletonData, streetRightAnimationStateData);
                break;
            case 10:
                spawnWidth = 2f;
                spawnX = 2.8f - spawnWidth / 2f;
                civilianIntervalMin = .5f;
                civilianIntervalMax = 1f;

                if (newType) {
                    nextStreetTypeTime += 3;
                    GameScreen.gameScreen.showSign(streetRightSkeletonData, 1f);
                    returnValue = new SpineBehaviour(go, streetRightToLeftSkeletonData, streetRightToLeftAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetLeftSkeletonData, streetNarrowAnimationStateData);
                break;
            case 11:
                spawnWidth = 2f;
                spawnX = 7.1f - spawnWidth / 2f;
                civilianIntervalMin = 2f;
                civilianIntervalMax = 3f;

                if (newType) {
                    nextStreetTypeTime += 3;
                    GameScreen.gameScreen.showSign(streetLeftSkeletonData, 1f);
                    returnValue = new SpineBehaviour(go, streetLeftToRightSkeletonData, streetLeftToRightAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetRightSkeletonData, streetRightAnimationStateData);
                break;
            case 12:
                spawnWidth = 2f;
                spawnX = 2.8f - spawnWidth / 2f;
                civilianIntervalMin = 2f;
                civilianIntervalMax = 3f;

                if (newType) {
                    nextStreetTypeTime += 3;
                    GameScreen.gameScreen.showSign(streetRightSkeletonData, 1f);
                    returnValue = new SpineBehaviour(go, streetRightToLeftSkeletonData, streetRightToLeftAnimationStateData);
                } else returnValue = new SpineBehaviour(go, streetLeftSkeletonData, streetNarrowAnimationStateData);
                break;
            case 13:
                spawnWidth = 2f;
                spawnX = 7.1f - spawnWidth / 2f;
                civilianIntervalMin = 2f;
                civilianIntervalMax = 3f;

                if (newType) {
                    nextStreetTypeTime += 3;
                    returnValue = new SpineBehaviour(go, streetLeftToRightSkeletonData, streetLeftToRightAnimationStateData);
                    var jet = new GameObject(unBox);
                    new EnemyJetBehaviour(jet);
                } else returnValue = new SpineBehaviour(go, streetRightSkeletonData, streetRightAnimationStateData);
                break;
            case 14:
                spawnWidth = 6f;
                spawnX = gameViewport.getWorldWidth() / 2f - spawnWidth / 2f;
                civilianIntervalMin = .5f;
                civilianIntervalMax = 1f;

                if (newType) {
                    nextStreetTypeTime += 10;
                    returnValue = new SpineBehaviour(go, streetRightToWideSkeletonData, streetRightToWideAnimationStateData);

                    var heli = new GameObject(unBox);
                    new EnemyHeliBehaviour(heli);
                } else returnValue = new SpineBehaviour(go, streetWideSkeletonData, streetWideAnimationStateData);
                break;
            case 15:
                if (newType) {
                    var ufo = new GameObject(unBox);
                    new UfoBehaviour(ufo);
                }
                returnValue = new SpineBehaviour(go, streetWideSkeletonData, streetWideAnimationStateData);
                break;
            default:
                returnValue = new SpineBehaviour(go, streetWideSkeletonData, streetWideAnimationStateData);
        }

        return returnValue;
    }
}
