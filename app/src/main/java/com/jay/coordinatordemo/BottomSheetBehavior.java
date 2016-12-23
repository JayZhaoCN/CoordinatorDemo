package com.jay.coordinatordemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by Jay on 2016/12/23.
 * custom bottom sheet behavior
 */

public class BottomSheetBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private ObjectAnimator mAnimator;
    private boolean isHided = false;

    private float animatorHeight;

    public BottomSheetBehavior() {
    }

    public BottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        //dyConsumed > 0表示上滑，反之下滑
        if(dyConsumed > 0) {
            //up disappear
            if(!isHided) {
                animatorHeight = coordinatorLayout.getHeight() - child.getY();
                mAnimator = ObjectAnimator.ofFloat(child, "translationY", 0, animatorHeight);
                mAnimator.setDuration(500);
                mAnimator.setInterpolator(new DecelerateInterpolator(3));
                mAnimator.start();
                isHided = true;
            }
        } else {
            //down appear
            if(isHided) {
                mAnimator = ObjectAnimator.ofFloat(child, "translationY", animatorHeight, 0);
                mAnimator.setInterpolator(new DecelerateInterpolator(3));
                mAnimator.setDuration(500);
                mAnimator.start();
                isHided = false;
            }
        }
    }
}
