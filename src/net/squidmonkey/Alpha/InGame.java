package net.squidmonkey.Alpha;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class InGame extends SurfaceView {

    private final int NUMBER_OF_EACH_LETTER = 5; //number of each letter to show in game box

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private List<Letter> letters = new ArrayList<Letter>();
    private Letter activeLetter;
    private int numActiveLetters = NUMBER_OF_EACH_LETTER;
    private List<Letter> searching = new ArrayList<Letter>();
    private Bitmap background;

    public InGame(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        System.out.println("Stupid gameLoopThread.join error");
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createLetters();

                //scale and set background to sand image
                Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.sand);
                float scale = (float)bg.getHeight()/(float)getHeight();
                int newWidth = Math.round(bg.getWidth()/scale);
                int newHeight = Math.round(bg.getHeight()/scale);
                background = Bitmap.createScaledBitmap(bg, newWidth, newHeight, true);

                //start the game loop
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    private void createLetters() {

        //add letters to game area
        for(int i = 0; i < NUMBER_OF_EACH_LETTER; i++) {
            letters.add(createLetter(R.drawable.d));
            letters.add(createLetter(R.drawable.c));
            letters.add(createLetter(R.drawable.b));
            letters.add(createLetter(R.drawable.a));
        }

        //build searching list and set current item
        searching.add(createLetter(R.drawable.a));
        searching.add(createLetter(R.drawable.b));
        searching.add(createLetter(R.drawable.c));
        searching.add(createLetter(R.drawable.d));
        activeLetter = searching.get(0);

    }

    //on screen touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       synchronized (getHolder()) {

           //check each letter for potential collision

           //for (int i = letters.size() - 1; i >= 0; i--) {
           //    Letter letter = letters.get(i);
           for(Letter letter : letters) {
                if (letter.isCollision(event.getX(), event.getY())) {

                    //if collision, check and see if current letter
                    //if current, remove it and decrement number of active letters

                    //TODO Fix, not comparing correctly
                    if(activeLetter.getBmp().sameAs(letter.getBmp()))
                        Log.i("Alpha", "active letters:" + numActiveLetters);
                        letters.remove(letter);
                        numActiveLetters--;

                        //if last active number, move on to next searching letter and remove extinguished letter
                        if(numActiveLetters == 0) {

                            numActiveLetters = NUMBER_OF_EACH_LETTER;
                            for (Letter search : searching) {
                                if(search.getBmp().sameAs(activeLetter.getBmp())) {
                                    searching.remove(search);
                                    activeLetter = searching.get(0);

                                }
                            }
                        }
                break;
                }
            }
       }
        return true;
    }

    private Letter createLetter(int resource) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Letter(this,bmp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, null);

        for (Letter letter : letters) {
            letter.onDraw(canvas);
        }
    }
}