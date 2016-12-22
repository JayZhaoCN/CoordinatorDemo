package com.jay.coordinatordemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Jay on 2016/12/20.
 * my behavior
 */

public class MyBehavior extends  CoordinatorLayout.Behavior<FloatingActionButton> {
    private ObjectAnimator mAnimator;
    private boolean isHided = false;

    private float animatorWidth;

    public MyBehavior() {
    }

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        //true if the Behavior wishes to accept this nested scroll
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if(dyConsumed > 0) {
            //up disappear
            if(!isHided) {
                animatorWidth = coordinatorLayout.getWidth() - child.getX();
                mAnimator = ObjectAnimator.ofFloat(child, "translationX", 0, animatorWidth);
                mAnimator.setDuration(500);
                mAnimator.setInterpolator(new DecelerateInterpolator(3));
                mAnimator.start();
                isHided = true;
            }
        } else {
            //down appear
            if(isHided) {
                mAnimator = ObjectAnimator.ofFloat(child, "translationX", animatorWidth, 0);
                mAnimator.setInterpolator(new DecelerateInterpolator(3));
                mAnimator.setDuration(500);
                mAnimator.start();
                isHided = false;
            }
        }
    }
}
