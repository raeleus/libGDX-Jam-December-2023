package com.ray3k.cosmicinfiltrator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

public class Controls extends InputAdapter implements ControllerListener {
    public static boolean upPressed;
    public static boolean downPressed;
    public static boolean leftPressed;
    public static boolean rightPressed;
    public static boolean firePressed;
    public static boolean slickPressed;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.UP) upPressed = true;
        if (keycode == Keys.DOWN) downPressed = true;
        if (keycode == Keys.LEFT) leftPressed = true;
        if (keycode == Keys.RIGHT) rightPressed = true;
        if (keycode == Keys.Z) firePressed = true;
        if (keycode == Keys.X) slickPressed = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.UP) upPressed = false;
        if (keycode == Keys.DOWN) downPressed = false;
        if (keycode == Keys.LEFT) leftPressed = false;
        if (keycode == Keys.RIGHT) rightPressed = false;
        if (keycode == Keys.Z) firePressed = false;
        if (keycode == Keys.X) slickPressed = false;
        return false;
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == controller.getMapping().buttonDpadUp) upPressed = true;
        if (buttonCode == controller.getMapping().buttonDpadDown) downPressed = true;
        if (buttonCode == controller.getMapping().buttonDpadLeft) leftPressed = true;
        if (buttonCode == controller.getMapping().buttonDpadRight) rightPressed = true;
        if (buttonCode == controller.getMapping().buttonA) firePressed = true;
        if (buttonCode == controller.getMapping().buttonB) slickPressed = true;
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (buttonCode == controller.getMapping().buttonDpadUp) upPressed = false;
        if (buttonCode == controller.getMapping().buttonDpadDown) downPressed = false;
        if (buttonCode == controller.getMapping().buttonDpadLeft) leftPressed = false;
        if (buttonCode == controller.getMapping().buttonDpadRight) rightPressed = false;
        if (buttonCode == controller.getMapping().buttonA) firePressed = false;
        if (buttonCode == controller.getMapping().buttonB) slickPressed = false;
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }
}
