package com.it_intensive.eli3;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;


public class PathView extends View {
    Paint paintDraw;
    Path pathDraw;

    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paintDraw = new Paint();
        paintDraw.setColor(0xFF303F9F);
        paintDraw.setStrokeWidth(15);
        paintDraw.setStyle(Paint.Style.STROKE);

        float radius = 20.0f;
        CornerPathEffect cornerPathEffect =
                new CornerPathEffect(radius);
        paintDraw.setPathEffect(cornerPathEffect);

        pathDraw = new Path();
    }

    public void drawLocationPath(Point[] locPath) {
        pathDraw.reset();
        pathDraw.moveTo(locPath[0].x, locPath[0].y);
        for(int i = 1; i < locPath.length; i++)
            if(locPath[i].x != locPath[i - 1].x || locPath[i].y != locPath[i - 1].y)
                pathDraw.lineTo(locPath[i].x, locPath[i].y);
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(pathDraw, paintDraw);
    }
}
