package com.jay.coordinatordemo.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jay.coordinatordemo.R;

import java.util.ArrayList;

/**
 * Created by Jay on 2016/12/26.
 * floating action menu
 */

//TODO 添加文字的功能待实现
//TODO 需要自定义的ChildView
public class FloatingActionMenu extends LinearLayout {
    private static final String TAG = "FloatingActionMenu";

    private ValueAnimator mTranslateAnimator;
    private ValueAnimator mScaleAnimator;
    private ValueAnimator mAlphaAnimator;

    private boolean mIsShowing = false;

    private Context mContext;
    private FloatingActionButton mBottomButton;

    private int mBottomMargin = 60;
    private int mLeftMargin = 60;
    private int mRightMargin = 60;
    private int mTopMargin = 60;

    private static final int DEFAULT_DURATION = 350;
    private int mAnimationDuration = DEFAULT_DURATION;

    private int mAnimationType = TYPE_TRANSLATE;

    private int mBottomDrawableRes = -1;

    private OnItemClickListener mOnItemClickListener;

    private static final int TYPE_TRANSLATE = 0;
    private static final int TYPE_SCALE = 1;
    private static final int TYPE_ALPHA = 2;

    public FloatingActionMenu(Context context) {
        this(context, null);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);

        obtainAttrs(attrs, defStyleAttr);
    }

    private void obtainAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, 0, defStyleAttr);

        //margins
        mTopMargin = ta.getDimensionPixelOffset(R.styleable.FloatingActionMenu_marginTop, (int) dp2px(mContext, 20f));
        mBottomMargin = ta.getDimensionPixelOffset(R.styleable.FloatingActionMenu_marginBottom, (int) dp2px(mContext, 20f));
        mLeftMargin = ta.getDimensionPixelOffset(R.styleable.FloatingActionMenu_marginLeft, (int) dp2px(mContext, 20f));
        mRightMargin = ta.getDimensionPixelOffset(R.styleable.FloatingActionMenu_marginRight, (int) dp2px(mContext, 20f));
        //animation type
        mAnimationType = ta.getInt(R.styleable.FloatingActionMenu_animationType, TYPE_TRANSLATE);
        //animation duration
        mAnimationDuration = ta.getInt(R.styleable.FloatingActionMenu_animationDuration, DEFAULT_DURATION);
        mBottomDrawableRes = ta.getResourceId(R.styleable.FloatingActionMenu_bottomDrawable, -1);

        ta.recycle();
    }

    private ArrayList<Integer> translateHeights;
    private ArrayList<View> children;


    private void initAnimator(final boolean isExpand) {
        //this.setElevation(10);
        final int childCount = getChildCount();
        children = new ArrayList<>();
        for(int i=0; i<childCount; i++) {
            children.add(getChildAt(i));
        }
        switch (mAnimationType) {
            case TYPE_TRANSLATE:
                if(translateHeights == null) {
                    translateHeights = new ArrayList<>();
                    for(int i=0; i<getChildCount(); i++) {
                        translateHeights.add((int) (mBottomButton.getY() - getChildAt(i).getY()));
                    }
                }
                //这一步很重要
                if(mTranslateAnimator != null)
                    mTranslateAnimator.removeAllListeners();

                mTranslateAnimator = isExpand ? ValueAnimator.ofFloat(1, 0) : ValueAnimator.ofFloat(0, 1);
                mTranslateAnimator.setDuration(mAnimationDuration);
                if(isExpand) {
                    mTranslateAnimator.setInterpolator(new OvershootInterpolator());
                } else {
                    mTranslateAnimator.setInterpolator(new DecelerateInterpolator(3));
                }

                mTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        for(int i=0; i<childCount; i++) {
                            if(isExpand) {
                                children.get(i).setAlpha(1);
                            } else if(i!= getChildCount() - 1) {
                                children.get(i).setAlpha(1 - (float)animation.getAnimatedValue());
                            }
                            children.get(i).setTranslationY(translateHeights.get(i) * (float)animation.getAnimatedValue());
                            invalidate();
                        }
                    }
                });
                if(!isExpand) {
                    mTranslateAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            for (int i = 0; i < getChildCount() - 1; i++) {
                                getChildAt(i).setVisibility(INVISIBLE);
                            }
                        }
                    });
                }
                break;
            case TYPE_SCALE:
                break;
            case TYPE_ALPHA:
                break;
            default:
                break;

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        initAnimator(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        adjustChild();

        mBottomButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsShowing) {
                    expand();
                } else {
                    collapseWithAnimation();

                }
            }
        });
    }

    /**
     * 展开都是带动画效果的
     */
    public void expand() {
        mOnCollpaseListener.onExpanding();
        initAnimator(true);
        if (mAnimationType == TYPE_TRANSLATE) {
            mTranslateAnimator.start();
        } else if (mAnimationType == TYPE_SCALE) {
            mScaleAnimator.start();
        } else if (mAnimationType == TYPE_ALPHA) {
            mAlphaAnimator.start();
        }

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(VISIBLE);
        }
        mIsShowing = true;
    }


    /**
     * 不带动画效果的关闭
     */
    public void collapse() {
        mOnCollpaseListener.onCollapsing();
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<getChildCount() - 1; i++) {
                    getChildAt(i).setTranslationY(translateHeights.get(i));
                    getChildAt(i).setVisibility(INVISIBLE);
                }
                mIsShowing = false;
            }
        }, 300);
    }

    /**
     * 带动画效果的关闭
     */
    public void collapseWithAnimation() {
        mOnCollpaseListener.onCollapsing();
        initAnimator(false);
        if (mAnimationType == TYPE_TRANSLATE) {
            mTranslateAnimator.start();
        } else if (mAnimationType == TYPE_SCALE) {
            mScaleAnimator.start();
        } else if (mAnimationType == TYPE_ALPHA) {
            mAlphaAnimator.start();
        }
        mIsShowing = false;
    }

    private void adjustChild() {
        mBottomButton = new FloatingActionButton(mContext);
        if(mBottomDrawableRes != -1)
            mBottomButton.setImageResource(mBottomDrawableRes);
        addView(mBottomButton);

        for(int i=0; i<getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams childParams = (LayoutParams) child.getLayoutParams();
            childParams.bottomMargin = mBottomMargin;
            childParams.leftMargin = mLeftMargin;
            childParams.rightMargin = mRightMargin;
            if(i == 0) {
                childParams.topMargin = mTopMargin * getChildCount();
            }
            child.setLayoutParams(childParams);
            //最开始，除了bottom button，都是不可见的
            if(i != getChildCount() - 1) {
                child.setVisibility(INVISIBLE);
            }
        }
    }

    public static float dp2px(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;

        for(int i=0; i<getChildCount() - 1; i++) {
            final int finalI = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(finalI);
                }
            });
        }
    }

    private OnCollapseListener mOnCollpaseListener;

    public void setOnCollpaseListener(OnCollapseListener listener) {
        this.mOnCollpaseListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnCollapseListener {
        void onCollapsing();
        void onExpanding();
    }

}
