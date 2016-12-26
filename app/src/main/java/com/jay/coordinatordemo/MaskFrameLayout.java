package com.jay.coordinatordemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by Jay on 2016/12/26.
 * mask frame layout
 */

public class MaskFrameLayout extends FrameLayout {
    private Paint mMaskPaint;
    private ValueAnimator mMaskAnimator;
    private int mMaskRadius = 0;
    private boolean mIsStart = false;

    public MaskFrameLayout(Context context) {
        this(context, null);
    }

    public MaskFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth(), getHeight(), mMaskRadius, mMaskPaint);
    }

    public void initAnim() {
        if(!mIsStart) {
            if (mMaskPaint == null) {
                mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mMaskPaint.setColor(0xCCFFFFFF);
            }

            if (mMaskAnimator == null) {
                mMaskAnimator = ValueAnimator.ofInt(0, (int) Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()));
                mMaskAnimator.setDuration(270);
                mMaskAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mMaskRadius = (int) animation.getAnimatedValue();
                        invalidate();
                    }
                });
            }
            mMaskAnimator.start();
            mIsStart = true;
        } else {
            Log.i("JAYTEST", "already init!");
        }
    }

    public boolean isStarted() {
        return mIsStart;
    }

    public void hideMask() {
        mIsStart = false;
        this.setVisibility(GONE);
    }

}
