package com.filepreview.application.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * Round Image
 *
 * @author chenjiayou
 * @version 1.0.0
 * @since 2022/10/14
 */
public class RoundImageView extends AppCompatImageView {
    //Fillet radius
    private int radius = 18;
    //Fillet diameter
    private int diameter = radius << 1;
    private Paint paint;
    private Path path;

    public RoundImageView(Context context) {
        super(context);
        initPaint();
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawLeftTop(canvas);
        drawRightTop(canvas);
        drawLeftBottom(canvas);
        drawRightBottom(canvas);
    }

    private void drawLeftTop(Canvas canvas) {
        path.moveTo(0, radius);
        path.lineTo(0, 0);
        path.lineTo(radius, 0);
        path.arcTo(new RectF(0, 0, diameter, diameter), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightTop(Canvas canvas) {
        path.moveTo(getWidth(), radius);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - radius, 0);
        path.arcTo(new RectF(getWidth() - diameter, 0, getWidth(), diameter), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLeftBottom(Canvas canvas) {
        path.moveTo(0, getHeight() - radius);
        path.lineTo(0, getHeight());
        path.lineTo(radius, getHeight());
        path.arcTo(new RectF(0,getHeight() - diameter, diameter, getHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightBottom(Canvas canvas) {
        path.moveTo(getWidth() - radius, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - radius);
        RectF oval = new RectF(getWidth() - diameter, getHeight() - diameter, getWidth(), getHeight());
        path.arcTo(oval, 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }
}
