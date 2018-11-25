package com.example.android.assignment4touch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class WriteOnScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TouchEventView(this, null));
    }

    public class TouchEventView extends View {
        private Paint paint = new Paint();
        private Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Path path = new Path();
        Context context;
        float x,y;
        GestureDetector gestureDetector;

        public TouchEventView(Context context, AttributeSet attrs) {
            super(context, attrs);
            gestureDetector = new GestureDetector(context, new GestureListener());
            this.context = context;

            paint.setAntiAlias(true);
            paint.setStrokeWidth(6f);
            paint.setColor(Color.BLACK);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint1.setColor(Color.RED);
        }

        public void setColor(int r, int g, int b) {
            int rgb = Color.rgb(r, g, b);
            paint.setColor(rgb);
        }

        private class GestureListener extends GestureDetector.SimpleOnGestureListener {
            // event when double tap occurs
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();

                // clean drawing area on double tap
                path.reset();
                Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

                return true;
            }

        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
            canvas.drawText("X: "+x+" Y: "+y+" PointerID 0",40,50,paint1);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            x=eventX;
            y=eventY;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    return true;
                case MotionEvent.ACTION_MOVE:

                    path.lineTo(eventX, eventY);
                    break;
                case MotionEvent.ACTION_UP:

                    break;
                default:
                    return false;
            }

            // for demostraction purposes
            gestureDetector.onTouchEvent(event);
            // Schedules a repaint.
            invalidate();
            return true;
        }
    }
}
