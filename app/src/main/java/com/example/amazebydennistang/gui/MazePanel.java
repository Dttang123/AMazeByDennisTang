package com.example.amazebydennistang.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class MazePanel extends View implements P7PanelS23{
    private Canvas canvasNoteTaker;
    private Bitmap bitmapNoteTaker;
    private Paint paint;
    private int sideLen;

    private void init (@Nullable AttributeSet set){

    }
    private void init(){
        sideLen = 1350;
        bitmapNoteTaker = Bitmap.createBitmap(sideLen, sideLen, Bitmap.Config. RGB_565 );
        canvasNoteTaker = new Canvas(bitmapNoteTaker);
        paint = new Paint();
        paint.setStrokeWidth(5);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the bitmap on the canvas
        canvas.drawBitmap(bitmapNoteTaker,0,0, paint);
    }
    public MazePanel(Context context) {
        super(context);
        init(null);
        init();
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        init();
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        init();
    }

    public MazePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
        init();
    }

    @Override
    public void commit() {
        invalidate();
    }

    @Override
    public boolean isOperational() {
        if(this.canvasNoteTaker != null){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void setColor(int argb) {

        this.paint.setColor(argb);
    }

    /**
     * Method used in Wall class to convert from the red, green,
     * and blue components into a single integer representation
     */
    public static int getRGB(int red, int green, int blue) {

        Paint paintTemp = new Paint();
        paintTemp.setARGB(255, red, green, blue);
        return paintTemp.getColor();
    }

    @Override
    public int getColor() {
        return this.paint.getColor();
    }

    @Override
    public void addBackground(float percentToExit) {
        int viewWidth = Constants.VIEW_WIDTH;
        int viewHeight = Constants.VIEW_HEIGHT;


        setColor(ColorTheme.getColor(ColorTheme.MazeColors.BACKGROUND_TOP,percentToExit).toArgb());
        //setColor(Color.RED);
        addFilledRectangle(0, 0, viewWidth, viewHeight/2);
        //addFilledRectangle(0, 0, viewWidth, viewHeight/2);
        setColor(ColorTheme.getColor(ColorTheme.MazeColors.BACKGROUND_BOTTOM,percentToExit).toArgb());
        //setColor(Color.YELLOW);
        addFilledRectangle(0, 550, viewWidth, 1200);

    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        Log.v("addFilledRectangle Reached: ", "rect filled");
        Rect rectangle = new Rect(x, y, width, height);
        canvasNoteTaker.drawRect(rectangle,this.paint);
    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Path wallpath = new Path();
        wallpath.reset();
        //Initial point

        wallpath.moveTo(xPoints[0], yPoints[0]);

        for (int i = 1; i <= nPoints; i++)
        {
            //Connects final point to initial point
            if (i == nPoints)
            {
                i = 0;
                wallpath.lineTo(xPoints[i], yPoints[i]);
                break;
            }
            wallpath.lineTo(xPoints[i], yPoints[i]);
        }
        canvasNoteTaker.drawPath(wallpath, this.paint);
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Path wallpath = new Path();
        wallpath.reset();
        //Initial point
        wallpath.moveTo(xPoints[0], yPoints[0]);

        for (int i = 1; i <= nPoints; i++)
        {
            //Connects final point to initial point
            if (i == nPoints)
            {
                i = 0;
                wallpath.lineTo(xPoints[i], yPoints[i]);
                break;
            }
            wallpath.lineTo(xPoints[i], yPoints[i]);
        }
        canvasNoteTaker.drawPath(wallpath, this.paint);
    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        canvasNoteTaker.drawLine(startX, startY, endX, endY, paint);

    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        canvasNoteTaker.drawOval(x, y, x + width, y + height, paint);
    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        canvasNoteTaker.drawArc(x, y, x + width, y + height, startAngle, arcAngle, true, paint);
    }

    @Override
    public void addMarker(float x, float y, String str) {
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvasNoteTaker.drawText(str, x, y, paint);
    }

    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue) {
        switch (hintKey) {
            case KEY_RENDERING:
                paint.setFilterBitmap(hintValue == P7RenderingHints.VALUE_RENDER_QUALITY);
                break;
            case KEY_ANTIALIASING:
                paint.setAntiAlias(hintValue == P7RenderingHints.VALUE_ANTIALIAS_ON);
                break;
            case KEY_INTERPOLATION:
                paint.setFilterBitmap(hintValue == P7RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                break;
        }
    }

}
