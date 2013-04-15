package net.squidmonkey.Alpha;

import android.graphics.Canvas;

public class GameLoopThread extends Thread {
    private final long FPS = 20;

    private InGame view;
    private boolean running = false;

    public GameLoopThread(InGame view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 10)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {
                System.out.println("Sleep time error.");
            }
        }
    }
}