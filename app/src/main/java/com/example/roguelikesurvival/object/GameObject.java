package com.example.roguelikesurvival.object;

import android.graphics.Canvas;

public abstract class GameObject {
    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;
    //실행시 플레이어가 처음으로 바라보는 방향 오른쪽으로 설정
    protected double directionX = 1;
    protected double directionY;

    GameObject(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();

    //두 object간의 거리 구하기
    protected static double getDistanceBetweenObject(GameObject obj1, GameObject obj2) {
        return Math.sqrt(Math.pow(obj2.getPositionX() - obj1.getPositionX(), 2) +
                Math.pow(obj2.getPositionY() - obj1.getPositionY(), 2));
    }

    protected double getPositionX() {
        return positionX;
    }

    protected double getPositionY() {
        return positionY;
    }

    protected double getDirectionX() {
        return directionX;
    }

    protected double getDirectionY() {
        return directionY;
    }
}
