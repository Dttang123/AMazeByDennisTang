package com.example.amazebydennistang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
public class MazePanel extends View{

    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;


    public MazePanel(Context context, AttributeSet attrs){
        super(context, attrs);

        // Initialize the instance variables
        paint = new Paint();
        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        // Draw the graphics for the custom view
        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, 100, 100, paint);

        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 75, 100, 100, paint);

        //Red Circle
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 20, paint);

        //Green Polygon
        paint.setColor(Color.GREEN);
        Path path = new Path();
        path.moveTo(10, 10);
        path.lineTo(10, 40);
        path.lineTo(40, 30);
        path.lineTo(40, 15);
        path.close();
        canvas.drawPath(path, paint);

        //Yellow Polygon
        paint.setColor(Color.YELLOW);
        Path path2 = new Path();
        path2.moveTo(90, 10);
        path2.lineTo(90, 40);
        path2.lineTo(60, 30);
        path2.lineTo(60, 15);
        path2.close();
        canvas.drawPath(path2, paint);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the bitmap on the canvas
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, getWidth(), getHeight()), null);
    }
}


