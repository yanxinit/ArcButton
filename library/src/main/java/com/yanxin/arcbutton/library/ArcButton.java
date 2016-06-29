package com.yanxin.arcbutton.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by YanXin on 2016/6/29.
 */
public class ArcButton extends Button {

    private static final String MASK_COLOR = "#33000000";

    private float mLeftTopRadius;
    private float mRightTopRadius;
    private float mLeftBottomRadius;
    private float mRightBottomRadius;
    private int mBackgroundColor;

    private Paint mButtonPaint = new Paint();

    private boolean mIsTouch;

    public ArcButton(Context context) {
        this(context, null);
    }

    public ArcButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public ArcButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArcButton, defStyleAttr, 0);
        mLeftTopRadius = array.getDimension(R.styleable.ArcButton_leftTopRadius, 0);
        mLeftBottomRadius = array.getDimension(R.styleable.ArcButton_leftBottomRadius, 0);
        mRightTopRadius = array.getDimension(R.styleable.ArcButton_rightTopRadius, 0);
        mRightBottomRadius = array.getDimension(R.styleable.ArcButton_rightBottomRadius, 0);
        mBackgroundColor = array.getColor(R.styleable.ArcButton_backgroundColor, Color.GREEN);
        array.recycle();

        setupOnTouchListener();
    }

    private void setupOnTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsTouch = true;
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mIsTouch = false;
                        invalidate();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mButtonPaint.setAntiAlias(true);
        mButtonPaint.setColor(mBackgroundColor);
        Path path = getCornerRectPath(mLeftTopRadius, mRightTopRadius, mLeftBottomRadius, mRightBottomRadius);
        canvas.drawPath(path, mButtonPaint);
        if (mIsTouch) {
            mButtonPaint.setColor(Color.parseColor(MASK_COLOR));
            canvas.drawPath(path, mButtonPaint);
        }
        super.onDraw(canvas);
    }

    private Path getCornerRectPath(float leftTopRadius, float rightTopRadius,
                                   float leftBottomRadius, float rightBottomRadius) {
        float width = getRight() - getLeft();
        float height = getBottom() - getTop();

        float maxRadius = Math.min(width, height) / 2;
        leftTopRadius = formatRadius(0F, maxRadius, leftTopRadius);
        rightTopRadius = formatRadius(0F, maxRadius, rightTopRadius);
        leftBottomRadius = formatRadius(0F, maxRadius, leftBottomRadius);
        rightBottomRadius = formatRadius(0F, maxRadius, rightBottomRadius);

        Path path = new Path();
        path.moveTo(leftTopRadius, 0);
        path.lineTo(getWidth() - rightTopRadius, 0);

//        path.rQuadTo(rightTopRadius, 0, rightTopRadius, rightTopRadius);
        path.arcTo(new RectF(getWidth() - rightTopRadius * 2, 0, getWidth(), rightTopRadius * 2), -90, 90);
        path.lineTo(getWidth(), getHeight() - rightBottomRadius);
//        path.rQuadTo(0, rightBottomRadius, -rightBottomRadius, rightBottomRadius);
        path.arcTo(new RectF(getWidth() - rightBottomRadius * 2, getHeight() - rightBottomRadius * 2, getWidth(), getHeight()), 0, 90);
        path.lineTo(leftBottomRadius, getHeight());
//        path.rQuadTo(-leftBottomRadius, 0, -leftBottomRadius, -leftBottomRadius);
        path.arcTo(new RectF(0, getHeight() - leftBottomRadius * 2, leftBottomRadius * 2, getHeight()), 90, 90);
        path.lineTo(0, leftTopRadius);
//        path.rQuadTo(0, -leftTopRadius, leftTopRadius, -leftTopRadius);
        path.arcTo(new RectF(0, 0, leftTopRadius * 2, leftTopRadius * 2), 180, 90);

        path.close();

        return path;
    }

    private float formatRadius(float minRadius, float maxRadius, float radius) {
        if (radius < minRadius)
            radius = minRadius;
        if (radius > maxRadius)
            radius = maxRadius;
        return radius;
    }

}
