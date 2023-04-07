package com.example.roguelikesurvival.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.roguelikesurvival.Utils;

public class Joystick {
    private int outerCenterPosX;
    private int outerCenterPosY;
    private int innerCenterPosX;
    private int innerCenterPosY;
    private int outerRadius;
    private int innerRadius;
    private Paint outerPaint;
    private Paint innerPaint;
    private double touchDistance;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;


    public Joystick(int centerPosX, int centerPosY, int outerRadius, int innerRadius) {
        // 조이스틱 위치 설정
        outerCenterPosX = centerPosX;
        outerCenterPosY = centerPosY;
        innerCenterPosX = centerPosX;
        innerCenterPosY = centerPosY;

        // 조이스틱 원의 반지름 설정
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;

        //조이스틱 outter paint 설정
        outerPaint = new Paint();
        outerPaint.setColor(Color.GRAY);
        outerPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //조이스틱 innter paint 설정
        innerPaint = new Paint();
        innerPaint.setColor(Color.WHITE);
        innerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(outerCenterPosX, outerCenterPosY, outerRadius, outerPaint);
        canvas.drawCircle(innerCenterPosX, innerCenterPosY, innerRadius, innerPaint);
    }

    public void update() {
        updateInnerPos();
    }

    private void updateInnerPos() {
        innerCenterPosX = (int) (outerCenterPosX + actuatorX * outerRadius);
        innerCenterPosY = (int) (outerCenterPosY + actuatorY * outerRadius);
    }

    public boolean isPressed(double touchPosX, double touchPosY) {
        touchDistance = Utils.getDistanceBetweenPoints(outerCenterPosX, outerCenterPosY, touchPosX, touchPosY);
        return touchDistance < outerRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double touchposX, double touchPosY) {
        double deltaX = touchposX - outerCenterPosX;
        double deltaY = touchPosY - outerCenterPosY;
        double deltaDistance = Utils.getDistanceBetweenPoints(deltaX, deltaY, 0, 0);

        if (deltaDistance < outerRadius) {
            actuatorX = deltaX / outerRadius;
            actuatorY = deltaY / outerRadius;
        } else {
            actuatorX = deltaX / deltaDistance;
            actuatorY = deltaY / deltaDistance;
        }
    }

    public void resetActuator() {
        actuatorX = actuatorY = 0.0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
