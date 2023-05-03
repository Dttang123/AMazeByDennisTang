package com.example.amazebydennistang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
public class MazePanel extends View implements P7PanelS23 {

    private Canvas canvasNoteTaker;
    private Bitmap bitmapNoteTaker;
    private Paint paint;


    public MazePanel(Context context, AttributeSet attrs){
        super(context, attrs);

        // Initialize the instance variables
        paint = new Paint();
        bitmapNoteTaker = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        canvasNoteTaker = new Canvas(bitmapNoteTaker);

        myTestImage(canvasNoteTaker);

        //For testing purposes
        myTestImage(canvasNoteTaker);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the bitmap on the canvas
        canvas.drawBitmap(bitmapNoteTaker, null, new Rect(0, 0, getWidth(), getHeight()), null);
    }

    @Override
    public void commit() {
        invalidate();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isOperational() {
        if(this.canvasNoteTaker != null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Sets new color based on parameters
     * @param argb gives the alpha, red, green, and blue encoded value of the color
     */
    @Override
    public void setColor(int argb) {
        this.paint.setColor(argb);
    }

    /**
     * Returns color value
     */
    @Override
    public int getColor() {
        return this.paint.getColor();
    }

    /**
     *
     * @param percentToExit gives the distance to exit
     */
    @Override
    public void addBackground(float percentToExit) {
        //Black to gold -> Gray to Green
        //Am i approaching this correctly?
        //Can I add 2 rectangles that are half of my screen to cover the entire background?

        //Black
        if(percentToExit > 75){
            setColor(Color.BLACK);
            addFilledRectangle(0, 0, 50, 100);
            addFilledRectangle(50, 0, 50, 100);

        }
        //Gold
        else if (75 >= percentToExit && percentToExit > 50)
        {
            setColor(Color.YELLOW);
            addFilledRectangle(0, 0, 50, 100);
            addFilledRectangle(50, 0, 50, 100);
        }
        //Gray
        else if (50 >= percentToExit && percentToExit > 25)
        {
            setColor(Color.GRAY);
            addFilledRectangle(0, 0, 50, 100);
            addFilledRectangle(50, 0, 50, 100);
        }
        //Green
        else if (25 >= percentToExit)
        {
            setColor(Color.GREEN);
            addFilledRectangle(0, 0, 50, 100);
            addFilledRectangle(50, 0, 50, 100);
        }

    }

    /**
     * Fills in a rectangle with the desired color
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the rectangle
     * @param height is the height of the rectangle
     */
    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        Rect rectangle = new Rect(x,y,width,height);
        canvasNoteTaker.drawRect(rectangle,this.paint);
    }

    /**
     * Fills in the polygon with the desired color
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {

        //Creates new path and starting point
        Path path = new Path();
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);

        //Connects all points
        for(int i = 1; i <= nPoints; i++){
            //Maybe try path.close()?
            if (i == nPoints) {
                i = 0;
                path.lineTo(xPoints[i],yPoints[i]);
                break;
            }
            path.lineTo(xPoints[i], yPoints[i]);
        }
        canvasNoteTaker.drawPath(path, this.paint);
    }

    /**
     *
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {

        //Creates new path and starting point and sets strokewidth to thin
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);

        //Connects all points
        for(int i = 1; i <= nPoints; i++){
            //Maybe try path.close()?
            if (i == nPoints) {
                i = 0;
                path.lineTo(xPoints[i],yPoints[i]);
                break;
            }
            path.lineTo(xPoints[i], yPoints[i]);
        }
        canvasNoteTaker.drawPath(path, this.paint);

    }

    /**
     *
     * @param startX is the x-coordinate of the starting point
     * @param startY is the y-coordinate of the starting point
     * @param endX is the x-coordinate of the end point
     * @param endY is the y-coordinate of the end point
     */
    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        canvasNoteTaker.drawLine(startX,startY,endX,endY,this.paint);
    }

    /**
     *
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top right corner
     * @param width is the width of the oval
     * @param height is the height of the oval
     */
    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        canvasNoteTaker.drawOval(new RectF(x,y,width,height), this.paint);

    }

    /**
     *
     * @param x the x coordinate of the upper-left corner of the arc to be drawn.
     * @param y the y coordinate of the upper-right corner of the arc to be drawn.
     * @param width the width of the arc to be drawn.
     * @param height the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle the angular extent of the arc, relative to the start angle.
     */
    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        canvasNoteTaker.drawArc(x,y,width,height,startAngle,arcAngle,true,this.paint);

    }

    /**
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param str the string
     */
    @Override
    public void addMarker(float x, float y, String str) {
        canvasNoteTaker.drawText(str,x,y,this.paint);

    }

    /**
     * @param hintKey the key of the hint to be set.
     * @param hintValue the value indicating preferences for the specified hint category.
     */
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
        }

    }


    private void myTestImage(Canvas c){
        //For Polygons
        int[] xPoints = {30,20,30,40};
        int[] yPoints = {10,40,30,40};
        int nPoints = 4;
        String marker = "Marker";

        setColor(Color.BLUE);
        //addBackground(10);
        //addFilledRectangle(5, 20, 100, 100);
        //addFilledPolygon(xPoints, yPoints, nPoints);
        //addFilledOval(50,40,20,50); check again, prints out oval
        //addPolygon(xPoints, yPoints, nPoints);
        //addArc(0, 10, 10, 20, 10, 70); //check again, prints out semi circle
        //addLine(10, 10, 100, 40);
        //addMarker(10, 25, marker); // Works, text is too big

        //test getColor
        if (paint.getColor() == Color.BLUE){
            //addFilledRectangle(5, 20, 100, 100);
         }

        //test isOperational
        if (isOperational() == true){
            addFilledRectangle(5, 20, 100, 100);
        }

        //test commit() and isOperational()
    }


}


