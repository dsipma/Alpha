package net.squidmonkey.Alpha;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.util.Random;

public class Letter {
    private final int TOP_SIZE = 10;  //Percentage of screen for apples and shit over the top
    private final int GAME_SIZE = 75;  //  percetage of screen for drawing letters

    private final int MAX_TWIST_ANGLE = 15;
    private final int MAX_WIGGLE_SPEED = 3;
    private final int MAX_SPEED = 1;
    boolean wiggle = true;  //true means wiggle to the right
    private InGame inGame;
    private Bitmap bmp; //letter image
    private int wiggleSpeed, twist; // speed of wiggle, amount of twist
    private int x, y, xSpeed, ySpeed, width, height; //position coords, and measurements

    public Letter(InGame inGame, Bitmap bmp) {
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
        this.bmp = bmp;
        this.inGame = inGame;

        Random rnd = new Random();
        x = rnd.nextInt(inGame.getWidth() - width);
        y = rnd.nextInt(inGame.getHeight() * GAME_SIZE / 100 - height) + inGame.getHeight() * TOP_SIZE / 100;
        twist = rnd.nextInt(MAX_TWIST_ANGLE * 2) - MAX_TWIST_ANGLE;
        wiggleSpeed = rnd.nextInt(MAX_WIGGLE_SPEED - 1) + 1;
        xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED + 1;
        ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED + 1;
    }

    private void update() {

        //if rotating to right
        if(wiggle) {
            twist += wiggleSpeed;
            if(twist > MAX_TWIST_ANGLE)
                wiggle=false;
        }

        else { //if twisting to left
            twist-= wiggleSpeed;
            if(twist < -MAX_TWIST_ANGLE)
                wiggle=true;
        }

        if (x >= inGame.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y >= inGame.getHeight() * GAME_SIZE / 100 - height - ySpeed || y + ySpeed <= inGame.getHeight() * TOP_SIZE / 100) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;

    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.save();
        canvas.rotate(twist, x + (width / 2), y + (height / 2));
        canvas.drawBitmap(bmp, x, y , null);
        canvas.restore();
    }

    public boolean isCollision(float x2, float y2) {
        return (x2 > x) && (x2 < (x + width)) && (y2 > y) && (y2 < (y + height));
    }

    public Bitmap getBmp() {
        return bmp;
    }

}