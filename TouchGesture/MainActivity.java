package com.example.android.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private float mPreviousX=0;
    private float mPreviousY=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawingView(this));
    }

    class DrawingView extends SurfaceView {

        private final SurfaceHolder surfaceHolder;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        public DrawingView(Context context) {
            super(context);
            surfaceHolder = getHolder();
            paint.setColor(Color.RED);

        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            // MotionEvent reports input details from the touch screen
            // and other input controls. In this case, you are only
            // interested in events where the touch position changed.

            float x = e.getX();
            float y = e.getY();
            if (e.getAction() == MotionEvent.ACTION_MOVE) {
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                if (surfaceHolder.getSurface().isValid()) {
                    Canvas canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.BLACK);
                    canvas.drawCircle(dx, dy, 50, paint);
                    paint.setTextSize(25);
                    canvas.drawText("Current X: " + dx + "  Current Y: " + dy + " Pointer count " + e.getPointerId(0), 40, 50, paint);
                    surfaceHolder.unlockCanvasAndPost(canvas);

                }

            }
            return true;
        }
    }
}
