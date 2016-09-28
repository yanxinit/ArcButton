package com.yanxin.arcbutton.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

public class ArcButton extends Button {

    private static final int ACTIVE_COLOR_ALPHA = 128;
    private static final String DISABLED_COLOR = "#EEEEEE";

    private float mLeftTopRadius;
    private float mRightTopRadius;
    private float mLeftBottomRadius;
    private float mRightBottomRadius;
    private int mBackgroundColor;
    private float mStrokeWidth;
    private int mStrokeColor;

    public ArcButton(Context context) {
        this(context, null);
    }

    public ArcButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    @SuppressWarnings("deprecation")
    public ArcButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArcButton, defStyleAttr, 0);
        mLeftTopRadius = array.getDimension(R.styleable.ArcButton_leftTopRadius, 0);
        mLeftBottomRadius = array.getDimension(R.styleable.ArcButton_leftBottomRadius, 0);
        mRightTopRadius = array.getDimension(R.styleable.ArcButton_rightTopRadius, 0);
        mRightBottomRadius = array.getDimension(R.styleable.ArcButton_rightBottomRadius, 0);
        mBackgroundColor = array.getColor(R.styleable.ArcButton_backgroundColor, Color.GREEN);
        mStrokeColor = array.getColor(R.styleable.ArcButton_strokeColor, Color.GREEN);
        mStrokeWidth = array.getDimension(R.styleable.ArcButton_strokeWidth, 0);
        array.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                setStateListAnimator(null);
            setBackground(generateBackground());
        } else {
            setBackgroundDrawable(generateBackground());
        }
    }

    private Drawable generateBackground() {
        GradientDrawable defaultGd = new GradientDrawable();
        GradientDrawable activeGd = new GradientDrawable();
        GradientDrawable disabledGd = new GradientDrawable();

        defaultGd.setColor(mBackgroundColor);
        activeGd.setColor(getActiveColor(mBackgroundColor));
        disabledGd.setColor(Color.parseColor(DISABLED_COLOR));

        defaultGd.setStroke((int) mStrokeWidth, mStrokeColor);
        activeGd.setStroke((int) mStrokeWidth, mStrokeColor);
        disabledGd.setStroke((int) mStrokeWidth, mStrokeColor);

        setupDrawableCorners(defaultGd, activeGd, disabledGd);
        return setupStateDrawable(defaultGd, activeGd, disabledGd);
    }

    private int getActiveColor(int color) {
        return increaseOpacityFromInt(color, ACTIVE_COLOR_ALPHA);
    }

    private int increaseOpacityFromInt(int color, int
            alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    private void setupDrawableCorners(GradientDrawable defaultGd, GradientDrawable activeGd,
                                      GradientDrawable disabledGd) {
        float[] radii = new float[]{
                mLeftTopRadius, mLeftTopRadius,
                mRightTopRadius, mRightTopRadius,
                mRightBottomRadius, mRightBottomRadius,
                mLeftBottomRadius, mLeftBottomRadius
        };

        defaultGd.setCornerRadii(radii);
        activeGd.setCornerRadii(radii);
        disabledGd.setCornerRadii(radii);
    }

    private StateListDrawable setupStateDrawable(GradientDrawable defaultGd, GradientDrawable activeGd,
                                                 GradientDrawable disabledGd) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        LayerDrawable defaultLayer = new LayerDrawable(new Drawable[]{defaultGd});
        LayerDrawable activeLayer = new LayerDrawable(new Drawable[]{activeGd});
        LayerDrawable disabledLayer = new LayerDrawable(new Drawable[]{disabledGd});

        if (Build.VERSION.SDK_INT >= 14) {
            stateListDrawable.addState(new int[]{android.R.attr.state_hovered}, activeLayer);
        }

        stateListDrawable.addState(new int[]{android.R.attr.state_activated}, activeLayer);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, activeLayer);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, activeLayer);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, activeLayer);
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, disabledLayer);
        stateListDrawable.addState(new int[]{}, defaultLayer);

        return stateListDrawable;
    }

}
