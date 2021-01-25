package com.quibbler.jetpack.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class CircleClock extends View {
    private static final String TAG = "TAG_Clock";

    private int width;
    private int height;

    private float centerX;
    private float centerY;

    private LocalDateTime localDateTime;
    private int hour;
    private int minute;
    private int second;

    private Path hourPath;
    private Path minutePath;
    private Path secondPath;

    private RectF hourRectF = new RectF();
    private RectF minuteRectF = new RectF();
    private RectF secondRectF = new RectF();

    private Paint mHourPaint;
    private Paint mPaint;
    private Paint mTextPaint;

    public CircleClock(Context context) {
        this(context, null);
    }

    public CircleClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //three path
        hourPath = new Path();
        minutePath = new Path();
        secondPath = new Path();
        //Circle Paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#444444"));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //Hour Circle Paint
        mHourPaint = new Paint();
        mHourPaint.setAntiAlias(true);
        mHourPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mHourPaint.setColor(Color.parseColor("#444444"));
        //
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(40);
    }

    public CircleClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get width and height of Clock View
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //get center of this view and min length
        float length = Math.min(width, height);
        centerX = getX() + length / 2f;
        centerY = getY() + length / 2f;
        //calculate three RectF used in onDraw,to draw arc
        float gap = length / 12;
        hourRectF = new RectF(centerX - 1 * gap, centerY - 1 * gap, centerX + 1 * gap, centerY + 1 * gap);
        minuteRectF = new RectF(centerX - 3 * gap, centerY - 3 * gap, centerX + 3 * gap, centerY + 3 * gap);
        secondRectF = new RectF(centerX - 5 * gap, centerY - 5 * gap, centerX + 5 * gap, centerY + 5 * gap);
        //
        mPaint.setStrokeWidth(length / 12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        localDateTime = LocalDateTime.now();

        hour = localDateTime.getHour();
        minute = localDateTime.getMinute();
        second = localDateTime.getSecond();

        hourPath.reset();
        minutePath.reset();
        secondPath.reset();

        hourPath.addOval(hourRectF, Path.Direction.CW);
        minutePath.addArc(minuteRectF, minute / 60f * 360, 340);
        secondPath.addArc(secondRectF, second / 60f * 360, 350);

        canvas.drawPath(hourPath, mHourPaint);
        canvas.drawPath(minutePath, mPaint);
        canvas.drawPath(secondPath, mPaint);

        canvas.drawText(String.valueOf(hour), centerX, centerY, mTextPaint);
        invalidate();
    }

}
