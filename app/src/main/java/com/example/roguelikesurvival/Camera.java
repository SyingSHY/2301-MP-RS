package com.example.roguelikesurvival;

import android.graphics.Rect;

import com.example.roguelikesurvival.object.GameObject;

public class Camera {
    public final int widthPixels;
    public final int heightPixels;
    private double gameToScreenCoordinateOffsetX;
    private double gameToScreenCoordinateOffsetY;
    private double screenCenterX;
    private double screenCenterY;
    private double gameCenterX;
    private double gameCenterY;
    private GameObject centerObject;

    public Camera(int widthPixels, int heightPixels, GameObject centerObject) {
        this.centerObject = centerObject;

        screenCenterX = widthPixels / 2.0;
        screenCenterY = heightPixels / 2.0;

        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    public void update() {
        gameCenterX = centerObject.getPositionX();
        gameCenterY = centerObject.getPositionY();

        gameToScreenCoordinateOffsetX = screenCenterX - gameCenterX;
        gameToScreenCoordinateOffsetY = screenCenterY - gameCenterY;
    }

    public double gameToScreenCoordinateX(double x) {
        return x + gameToScreenCoordinateOffsetX;
    }

    public double gameToScreenCoordinateY(double y) {
        return y + gameToScreenCoordinateOffsetY;
    }
}
