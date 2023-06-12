package com.example.roguelikesurvival;

public class Utils {
    public static double getDistanceBetweenPoints(double p1x, double p1y, double p2x, double p2y) {
        return Math.sqrt(Math.pow(p1x - p2x, 2)) +
                Math.sqrt(Math.pow(p1y - p2y, 2));
    }

    public static int getRelativeDisplayWidth(int width) {
        return (int)((width / (float) 1920) * MainActivity.displayWidth);
    }

    public static int getRelativeDisplayHeight(int height) {
        return (int)((height / (float) 1080) * MainActivity.displayHeight);
    }
}
