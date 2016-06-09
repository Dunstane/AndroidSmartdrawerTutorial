package stenden.dunstane.androidsmartdrawer;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.graphics.PointF;
import java.util.ArrayList;

/**
 * Created by Dunstane on 4/19/2016.
 */
public class DrawingView extends View
{

    /* Note that below are from a tutorial about drawing to android screens*/
    private Path drawPath;
    private Paint drawPaint, canvasPaint;

    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private ArrayList<PointF> LastLinePoints;
    private ArrayList<PointF> Line;
    private ArrayList<ArrayList<PointF>> AllLines;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
        AllLines=new ArrayList<>();
        LastLinePoints=new ArrayList<PointF>();
    }
    private void setupDrawing(){
        //sets up path and paint objects
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(0xFF000000);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(15);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
       // LastLinePoints.clear();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {

        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

//android touch drawing code for onTouchevents.
    //uses methods in drawpath to create a path and later adds the path to the canvas
        @Override
        public boolean onTouchEvent(MotionEvent event)
        {

            float touchX = event.getX();
            float touchY = event.getY();

            LastLinePoints.add(new PointF(touchX,touchY));
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    //setting up points log
                    LastLinePoints.add(new PointF(touchX,touchY));
                    break;
                case MotionEvent.ACTION_MOVE:
                    int size = event.getHistorySize();
                    if(size >= 5)

                    {
                        LastLinePoints.add(new PointF(event.getHistoricalX((size / 5) - 1),event.getHistoricalY((size / 5) - 1)));
                        drawPath.lineTo(event.getHistoricalX((size / 5) - 1), event.getHistoricalY((size / 5) - 1));

                        LastLinePoints.add(new PointF(event.getHistoricalX(((size / 5) * 2) - 1), event.getHistoricalY((size / 5) * 2) - 1));
                        //drawPath.lineTo(event.getHistoricalX(((size / 5) * 2) - 1), event.getHistoricalY((size / 5) * 2) - 1);

                        LastLinePoints.add(new PointF(event.getHistoricalX(((size / 5) * 3) - 1), event.getHistoricalY((size / 5) * 3) - 1));
                        //drawPath.lineTo(event.getHistoricalX(((size / 5) * 3) - 1), event.getHistoricalY((size / 5) * 3) - 1);

                        LastLinePoints.add(new PointF(event.getHistoricalX(((size / 5) * 4) - 1), event.getHistoricalY((size / 5) * 4) - 1));
                       // drawPath.lineTo(event.getHistoricalX(((size / 5) * 4) - 1), event.getHistoricalY((size / 5) * 4) - 1);


                        LastLinePoints.add(new PointF(event.getHistoricalX(size - 1), event.getHistoricalY(size - 1)));
                        drawPath.lineTo(event.getHistoricalX(size - 1), event.getHistoricalY(size - 1));

                    }
                    else if (size >= 2)
                    {
                        LastLinePoints.add(new PointF(event.getHistoricalX((size / 2) - 1), event.getHistoricalY((size / 2) - 1)));
                        drawPath.lineTo(event.getHistoricalX((size / 2) - 1), event.getHistoricalY((size / 2) - 1));

                        LastLinePoints.add(new PointF(event.getHistoricalX(size - 1), event.getHistoricalY(size - 1)));
                        //drawPath.lineTo(event.getHistoricalX(size - 1), event.getHistoricalY(size - 1));

                    }
                    else
                    {
                        LastLinePoints.add(new PointF(touchX,touchY));
                        drawPath.lineTo(touchX, touchY);

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    LastLinePoints.add(new PointF(touchX,touchY));
                    AllLines.add(LastLinePoints);

                    drawCanvas.drawPath(drawPath, drawPaint);

                 //   Line =new ArrayList<>();
                   // for(PointF x: LastLinePoints)
                   // {
                    //    Line.add(x);
                  //  }
                   // LastLinePoints.clear();
                    drawPath.reset();
                    break;
                default:
                    return false;
            }
            invalidate();
            return true;
        }

    public ArrayList<ArrayList<PointF>> giveAllLines()
    {
        return this.AllLines;
    }
    public void startNew(){
        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        invalidate();
    }
}