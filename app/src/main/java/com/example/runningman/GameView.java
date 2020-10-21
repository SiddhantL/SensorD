package com.example.runningman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private SurfaceHolder ourHolder;
    public int scoregame=0;
    private volatile boolean playing;
    private Canvas canvas;
    private Bitmap bitmapRunningMan, bitmapRunningEnemy1, bitmapRunningEnemy2, bitmapRunningEnemy3,bmap1,bmap2,bmap3;
    private boolean isMoving;
    private float runSpeedPerSecond = 500;
    private float manXPos = 10, manYPos = 10;
    private float enemyXPos = 10, enemyYPos = 10;
    private int frameWidth = 140, frameHeight = 274;
    private int frameCount = 1;
    private int currentFrame = 0;
    public Boolean lcar=false,ccar=false,rcar=false,allocate1=false,allocate2=false,allocate3=false,control=true;
    public static Boolean crash=false;
    private float x = 0, enemyyl = -1,enemmyc=-1,enemmyr=-1,pav=0;
    private int speeda=14,speedb=16,speedc=18;
    public String message="";
    private long fps;
    private long timeThisFrame;
    private long lastFrameChangeTime = 0;
    private static MediaPlayer mp;
    float time=0;
    Context contexts;
    private int frameLengthInMillisecond = 50;
    private Rect frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
    private RectF whereToDraw = new RectF(manXPos, manYPos, manXPos + frameWidth, frameHeight);
    private RectF whereToDrawEnemyleft = new RectF(enemyXPos, manYPos, enemyYPos + frameWidth, frameHeight);
    private RectF whereToDrawEnemyCenter = new RectF(enemyXPos, manYPos, enemyYPos + frameWidth, frameHeight);
    private RectF whereToDrawEnemyRight = new RectF(enemyXPos, manYPos, enemyYPos + frameWidth, frameHeight);
    public GameView(Context context) {
        super(context);
        contexts=context;
        mp=MediaPlayer.create(getContext(),R.raw.bgm);
        mp.start();
        mp.setLooping(true);
        ourHolder = getHolder();
        bitmapRunningMan = BitmapFactory.decodeResource(getResources(), R.drawable.running_car_2);
        bitmapRunningMan = Bitmap.createScaledBitmap(bitmapRunningMan, frameWidth * frameCount, frameHeight, false);
        bitmapRunningEnemy1 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy_3);
        bitmapRunningEnemy1 = Bitmap.createScaledBitmap(bitmapRunningEnemy1, frameWidth * frameCount, frameHeight, false);
        bitmapRunningEnemy2 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy_2);
        bitmapRunningEnemy2 = Bitmap.createScaledBitmap(bitmapRunningEnemy2, frameWidth * frameCount, frameHeight, false);
        bitmapRunningEnemy3 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy);
        bitmapRunningEnemy3 = Bitmap.createScaledBitmap(bitmapRunningEnemy3, frameWidth * frameCount, frameHeight, false);

    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void update() {
        message="Score: "+Integer.toString(scoregame);
        if (lcar) {
            if (manXPos >= getWidth() / 6 - frameWidth - return_int_ratiow(70) && manXPos <= getWidth() / 6 + frameWidth / 2 - return_int_ratiow(70) && enemyyl >= getHeight() - 2 * frameHeight - return_int_ratioh(50) && enemyyl <= getHeight() - return_int_ratioh(50)) {
                message = "CRASH";
                MediaPlayer mp2=MediaPlayer.create(getContext(),R.raw.crash);
                mp2.start();
                crash();
                pause();
                mp.pause();
                crash=true;
                scoregame = 0;
            }
        }
        if (ccar) {
            if (manXPos >= getWidth() / 2 - 2 * frameWidth + return_int_ratiow(15) && manXPos <= getWidth() / 2 + return_int_ratiow(15) && enemmyc >= getHeight() - 2 * frameHeight - return_int_ratioh(50) && enemmyc <= getHeight() - return_int_ratioh(50)) {
                message = "CRASH";
                MediaPlayer mp2=MediaPlayer.create(getContext(),R.raw.crash);
                mp2.start();
                crash();
                pause();
                mp.pause();
                crash=true;
                scoregame = 0;

            }
        }if (rcar) {
            if (manXPos >= (int) 5 * getWidth() / 6 - 2 * frameWidth && manXPos <= (int) 5 * getWidth() / 6 && enemmyr >= getHeight() - 2 * frameHeight - return_int_ratioh(50) && enemmyr <= getHeight() - return_int_ratioh(50)) {
                message = "CRASH";
                MediaPlayer mp2=MediaPlayer.create(getContext(),R.raw.crash);
                mp2.start();
                crash();
                pause();
                mp.pause();
                crash=true;
                scoregame = 0;
            }
        }
        x = x + return_int_ratioh(10);
        pav=pav+return_int_ratioh(10);
        enemyYPos = enemyYPos + return_int_ratioh(2);
        if (scoregame>2000)
            time=1;
        else if (scoregame>3000)
            time=2;
        else if (scoregame>4000)
            time=3;
        if (enemyyl>-1)
            if (lcar)
                enemyyl = enemyyl + speeda+time;
        if (enemmyc>-1)
            if (ccar)
                enemmyc = enemmyc + speedb+time;
        if (enemmyr>-1)
            if (rcar)
                enemmyr = enemmyr + speedc+time;
        if (pav>=getHeight()/2){
            pav=0;
        }
        if (x >= getHeight() / 4) {
            x = x - (getHeight() / 4);
        }
        if (enemyyl>getHeight()) {
            enemyyl = 0;
            lcar=false;
            allocate1=false;
        }
        if (enemmyc>getHeight()) {
            enemmyc = 0;
            ccar=false;
            allocate2=false;
        }
        if (enemmyr>getHeight()) {
            enemmyr = 0;
            rcar=false;
            allocate3=false;
        }
        if (isMoving) {
            if (manXPos > getWidth() / 2) ;
            manXPos = manXPos + runSpeedPerSecond / fps +time;
            if (manXPos > getWidth() - frameWidth - return_int_ratiow(140)) {
                //Go to next line on end
                manXPos = getWidth() - frameWidth - return_int_ratiow(140);
            }
            if (manYPos + frameHeight > getHeight()) {
            }
        } else {
            {
                if (manXPos < getWidth() / 2) ;
                manXPos = manXPos - runSpeedPerSecond / fps -time;
                if (manXPos <= 0) {
                    //Go to next line on end
                    //    manXPos = getWidth();
                    manXPos = 0;
                }
                if (manYPos + frameHeight > getHeight()) {
                    manYPos = return_int_ratioh(10);
                }
            }

        }

    }

    public void manageCurrentFrame() {
        long time = System.currentTimeMillis();
        if (isMoving) {
            if (time > lastFrameChangeTime + frameLengthInMillisecond) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {
                    currentFrame = 0;
                } else if (currentFrame <= 0) {
                    currentFrame = frameCount;
                }
            }
        } else {
            if (time > lastFrameChangeTime + frameLengthInMillisecond) {
                lastFrameChangeTime = time;
                currentFrame--;
                if (currentFrame <= 0) {
                    currentFrame = frameCount - 1;
                } else if (currentFrame >= frameCount - 1) {
                    currentFrame = 0;
                }
            }
        }
        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;
    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            Paint p2 = new Paint();
            p2.setColor(Color.YELLOW);
            canvas.drawRect(
                    0,
                    -2*getHeight()/4+pav,
                    getWidth(),
                    -getHeight()/4 + pav, p2);
            canvas.drawRect(
                    0,
                    -getHeight()/4+pav,
                    getWidth(),
                    0 + pav, p);
            canvas.drawRect(
                    0,
                    pav,
                    getWidth(),
                    getHeight()/4 + pav, p2);
            canvas.drawRect(
                    0,
                    getHeight()/4+pav,
                    getWidth(),
                    getHeight()/2 + pav, p);
            canvas.drawRect(
                    0,
                    getHeight()/2+pav,
                    getWidth(),
                    3*getHeight()/4 + pav, p2);
            canvas.drawRect(
                    0,
                    3*getHeight()/4+pav,
                    getWidth(),
                    getHeight() + pav, p);

            Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            Paint paint2 = new Paint();
            paint2.setColor(Color.WHITE);
            canvas.drawRect(
                    return_int_ratiow(70),
                    0,
                    getWidth() - return_int_ratiow(70),
                    getHeight(), paint);
            paint.setColor(Color.GREEN);

            canvas.drawRect(
                    (getWidth() / 3),
                    -(getHeight() / 4) + x+return_int_ratioh(50),
                    return_int_ratiow(30) + (getWidth() / 3),
                    0 + x, paint2);
            canvas.drawRect(
                    (getWidth() / 3),
                    return_int_ratioh(50) + x,
                    return_int_ratiow(30) + (getWidth() / 3),
                    (getHeight() / 4) + x, paint2);
            canvas.drawRect(
                    (getWidth() / 3),
                    x + return_int_ratioh(50) + (getHeight() / 4),
                    return_int_ratiow(30) + (getWidth() / 3),
                    x + (getHeight() / 4) * 2, paint2);
            canvas.drawRect(
                    (getWidth() / 3),
                    x + return_int_ratioh(50) + (getHeight() / 4) * 2,
                    return_int_ratiow(30) + (getWidth() / 3),
                    x + (getHeight() / 4) * 3, paint2);
            canvas.drawRect(
                    (getWidth() / 3),
                    x + return_int_ratioh(50) + (getHeight() / 4) * 3,
                    return_int_ratiow(30) + (getWidth() / 3),
                    x + return_int_ratioh(50) + (getHeight() / 4) * 4, paint2);


            canvas.drawRect(
                    (2 * getWidth() / 3),
                    -(getHeight() / 4) + x+return_int_ratioh(50),
                    return_int_ratiow(30) + (2 * getWidth() / 3),
                    0 + x, paint2);
            canvas.drawRect(
                    (2 * getWidth() / 3),
                    return_int_ratioh(50) + x,
                    return_int_ratiow(30) + (2 * getWidth() / 3),
                    (getHeight() / 4) + x, paint2);
            canvas.drawRect(
                    (2 * getWidth() / 3),
                    x + return_int_ratioh(50) + (getHeight() / 4),
                    return_int_ratiow(30) + (2 * getWidth() / 3),
                    x + (getHeight() / 4) * 2, paint2);
            canvas.drawRect(
                    (2 * getWidth() / 3),
                    x + return_int_ratioh(50) + (getHeight() / 4) * 2,
                    return_int_ratiow(30) + (2 * getWidth() / 3),
                    x + (getHeight() / 4) * 3, paint2);
            canvas.drawRect(
                    (2 * getWidth() / 3),
                    x + return_int_ratioh(50) + (getHeight() / 4) * 3,
                    return_int_ratiow(30) + (2 * getWidth() / 3),
                    x + return_int_ratioh(50) + (getHeight() / 4) * 4, paint2);

            enemyXPos = ((getWidth()) / 6) - frameWidth / 2 +return_int_ratioh(35);
            Paint painter = new Paint();
            painter.setColor(Color.WHITE);
            //canvas.drawPaint(painter);
            canvas.drawRect(return_float_ratiow(20),return_float_ratioh(5),return_float_ratiow(350),return_float_ratioh(80),painter);
            painter.setColor(Color.BLACK);
            painter.setTextSize(return_int_ratiow(50));
            scoregame=scoregame+1;
            canvas.drawText(/*"Score: "+Integer.toString(scoregame)*/message, return_int_ratiow(45), return_int_ratioh(60), painter);
            if (enemyyl==-1&&enemmyc==-1&&enemmyr==-1) {
                manXPos = getWidth() / 2;
                frameHeight=return_int_ratioh(274);
                frameWidth=return_int_ratiow(140);
                runSpeedPerSecond = return_int_ratioh(500);
                manXPos = return_int_ratioh(10); manYPos = return_int_ratioh(10);
                enemyXPos = return_int_ratioh(10); enemyYPos = return_int_ratioh(10);
                speeda=return_int_ratioh(14);speedb=return_int_ratioh(16);speedc=return_int_ratioh(18);}
            if (enemyyl==-1)
                enemyyl=0;
            if (enemmyc==-1)
                enemmyc=0;
            if (enemmyr==-1)
                enemmyr=0;
            final int min = 1;
            final int max = 3;
            final int random = new Random().nextInt((max - min) + 1) + min;
            final int min3 = 0;
            final int max3 = 10;
            final int random3 = new Random().nextInt((max3 - min3) + 1) + min3;
            if (scoregame<1000) {
                switch (random) {
                    case 1:
                        if (!(ccar && rcar))
                            lcar = true;
                        break;
                    case 2:
                        if (!(lcar && rcar))
                            ccar = true;
                        break;
                    case 3:
                        if (!(lcar && ccar))
                            rcar = true;
                        break;
                }
            }else if (random3==8 && scoregame>1500 && ((enemmyc>enemmyr+2*frameHeight) ||(enemmyc>enemyyl+2*frameHeight))){
                lcar=true;
                ccar=true;
                rcar=true;
            }else{
                switch (random) {
                    case 1:
                        if (!(ccar && rcar))
                            lcar = true;
                        break;
                    case 2:
                        if (!(lcar && rcar))
                            ccar = true;
                        break;
                    case 3:
                        if (!(lcar && ccar))
                            rcar = true;
                        break;
                }
            }
            spawn();
            whereToDraw.set((int) manXPos+return_int_ratiow(70), (int) getHeight() - frameHeight - return_int_ratioh(50), (int) manXPos + frameWidth+return_int_ratiow(70), (int) getHeight() - return_int_ratioh(50));
            //   manageCurrentFrame();
            canvas.drawBitmap(bitmapRunningMan, frameToDraw, whereToDraw, null);
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }
    public void spawn() {
        if (lcar) {
            if (!allocate1) {
                final int min = 1;
                final int max = 3;
                final int random = new Random().nextInt((max - min) + 1) + min;
                switch (random){
                    case 1:
                        bmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy);
                        bmap1 = Bitmap.createScaledBitmap(bmap1, 140 * frameCount, 274, false);
                        speeda=return_int_ratioh(15);
                        break;
                    case 2:
                        bmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy_2);
                        bmap1 = Bitmap.createScaledBitmap(bmap1, 140 * frameCount, 274, false);
                        speeda=return_int_ratioh(17);
                        break;
                    case 3:
                        bmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy_3);
                        bmap1 = Bitmap.createScaledBitmap(bmap1, 140 * frameCount, 274, false);
                        speeda=return_int_ratioh(13);
                        break;
                }
                allocate1=true;
            }
            whereToDrawEnemyleft.set((int)((getWidth()) / 6)+return_int_ratiow(35) - frameWidth / 2 , (int) enemyyl, (int) ((getWidth()) / 6)+return_int_ratiow(35)+ frameWidth/2, (int) enemyyl + frameHeight);
            canvas.drawBitmap(bmap1, frameToDraw, whereToDrawEnemyleft, null);
        }
        if (ccar) {
            whereToDrawEnemyCenter.set((int) getWidth() / 2+ return_int_ratiow(15) - frameWidth / 2 , (int) enemmyc, (int)getWidth() / 2+ return_int_ratiow(15)+ frameWidth / 2, (int) enemmyc + frameHeight);
            if (!allocate2) {
                final int min = 1;
                final int max = 3;
                final int random = new Random().nextInt((max - min) + 1) + min;
                switch (random){
                    case 1:
                        bmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy);
                        bmap2 = Bitmap.createScaledBitmap(bmap2, 140 * frameCount, 274, false);
                        speedb=return_int_ratioh(15);
                        break;
                    case 2:
                        bmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy_2);
                        bmap2 = Bitmap.createScaledBitmap(bmap2, 140 * frameCount, 274, false);
                        speedb=return_int_ratioh(17);
                        break;
                    case 3:
                        bmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy_3);
                        bmap2 = Bitmap.createScaledBitmap(bmap2, 140 * frameCount, 274, false);
                        speedb=return_int_ratioh(13);
                        break;
                }
                allocate2=true;
            }
            canvas.drawBitmap(bmap2, frameToDraw, whereToDrawEnemyCenter, null);
        }
        if (rcar) {
            whereToDrawEnemyRight.set((int)5 * getWidth() / 6 - frameWidth / 2, (int) enemmyr, (int) 5 * getWidth() / 6 + frameWidth / 2, (int) enemmyr + frameHeight);
            if (!allocate3) {
                final int min = 1;
                final int max = 3;
                final int random = new Random().nextInt((max - min) + 1) + min;
                switch (random){
                    case 1:
                        bmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy);
                        bmap3 = Bitmap.createScaledBitmap(bmap3, 140 * frameCount, 274, false);
                        speedc=return_int_ratioh(15);
                        break;
                    case 2:
                        bmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy_2);
                        bmap3 = Bitmap.createScaledBitmap(bmap3, 140 * frameCount, 274, false);
                        speedc=return_int_ratioh(17);
                        break;
                    case 3:
                        bmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.running_enemy_3);
                        bmap3 = Bitmap.createScaledBitmap(bmap3, 140 * frameCount, 274, false);
                        speedc=return_int_ratioh(13);
                        break;
                }
                allocate3=true;
            }
          canvas.drawBitmap(bmap3, frameToDraw, whereToDrawEnemyRight, null);
        }
    }
    public void crash(){
        synchronized (ourHolder) {
            gameThread.interrupt();
            Intent gameover=new Intent(contexts,ShowScore.class);
            gameover.putExtra("score",Integer.toString(scoregame));
            contexts.startActivity(gameover);
            ((Activity) contexts).finish();
        }
    }
    public void pause() {
        playing = false;
        mp.pause();
        mp.stop();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("ERR", "Joining Thread");
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (playing==false){
            mp.start();
            enemyyl=-1;
            enemmyc=-1;
            enemmyr=-1;
            scoregame=0;
            lcar=false;ccar=false;rcar=false;
            allocate1=false;allocate2=false;allocate3=false;
            manXPos=getWidth()/2;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isMoving = !isMoving;
                break;
        }
        return true;
    }

    public void go() {
        isMoving = false;
    }

    public void nogo() {
        isMoving = true;
    }
int ratioh,ratiow;
    float fratioh,fratiow;
    int return_int_ratioh(int x){
        if (getHeight()==0){return x;}
 ratioh=Math.round(x/(2030f/getHeight()));
 return ratioh;
}
float return_float_ratioh(float x){
    if (getHeight()==0){return x;}
    fratioh=Math.round(x/(2030f/getHeight()));
    return fratioh;
    }
    int return_int_ratiow(int x){
        if (getWidth()==0){return x;}
        ratiow=Math.round(x/(1080f/getWidth()));
return ratiow;
    }
    float return_float_ratiow(float x){
        if (getWidth()==0){return x;}
        fratiow=Math.round(x/(1080f/getWidth()));
        return fratiow;
    }
}
