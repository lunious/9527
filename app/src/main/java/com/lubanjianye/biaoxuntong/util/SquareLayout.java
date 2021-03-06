package com.lubanjianye.biaoxuntong.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by 11645 on 2018/1/26.
 * 正方形控，该控件可以实现正方形布局限制
 * 可通过：oscAccordTo 属性设置依赖的方向，
 * 默认情况下下以较短方向为目标
 */

public class SquareLayout extends FrameLayout {
    private int mBaseDirection;

    public SquareLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout, defStyleAttr, defStyleRes);
//        mBaseDirection = array.getInt(R.styleable.SquareLayout_oscAccordTo, 3);
//        array.recycle();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mBaseDirection == 1) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else if (mBaseDirection == 2) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            if (heightSize == 0) {
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
                return;
            }

            if (widthSize == 0) {
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
                return;
            }

            if (widthSize > heightSize){
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            } else{
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            }
        }
    }
}
