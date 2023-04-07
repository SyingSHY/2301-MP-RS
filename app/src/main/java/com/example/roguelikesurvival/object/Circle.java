package com.example.roguelikesurvival.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.roguelikesurvival.Camera;

public abstract class Circle extends GameObject {

    protected double radius;
    protected Paint paint;

    Circle(Context context, int color, double positionX, double positionY, double radius) {
        super(positionX, positionY);

        this.radius = radius;
        paint = new Paint();
        paint.setColor(color);
    }

    //두 object가 충돌하면 true를 return
    public static boolean isColliding(Circle obj1, Circle obj2) {
        double distance = getDistanceBetweenObject(obj1, obj2);
        double distanceToCollision = obj1.getRadius() + obj2.getRadius();
        if (distance < distanceToCollision)
            return true;
        else
            return false;
    }

    private double getRadius() {
        return radius;
    }

    public void draw(Canvas canvas, Camera camera) {
        canvas.drawCircle(
                (float) camera.gameToScreenCoordinateX(positionX),
                (float) camera.gameToScreenCoordinateY(positionY),
                (float) radius,
                paint);
    }
}
